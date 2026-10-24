package com.lias.gestionlabo.services;

import com.lias.gestionlabo.models.*;
import com.lias.gestionlabo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;

@Service
public class DemandeAdhesionService {

    @Autowired
    private DemandeAdhesionRepository demandeRepository;
    
    @Autowired
    private MembreRepository membreRepository;
    
    @Autowired
    private HistoriqueAffiliationRepository historiqueRepository;

    @Autowired
    private JavaMailSender mailSender;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DemandeAdhesion save(DemandeAdhesion demande) {
        return demandeRepository.save(demande);
    }

    public List<DemandeAdhesion> getDemandesEnAttente() {
        return demandeRepository.findByStatut(StatutDemande.EN_ATTENTE);
    }

    @Transactional
    public void accepterDemande(Long id, String role) {
        // 1. Récupération de la demande
        DemandeAdhesion demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
        
        // 2. Génération d'un mot de passe aléatoire
        String rawPassword = generateRandomPassword();
        
        // 3. Création automatique du membre
        Membre membre = new Membre();
        membre.setNom(demande.getNom());
        membre.setPrenom(demande.getPrenom());
        membre.setEmail(demande.getEmail());
        
        // Attribution du rôle et du statut
        membre.setRole(role); 
        // تحويل النص (role) إلى Enum (StatutMembre)
        try {
            membre.setStatutActuel(StatutMembre.valueOf(role));
        } catch (IllegalArgumentException e) {
            // في حالة كان الـ role لا يطابق قيم الـ Enum، نضع قيمة افتراضية
            membre.setStatutActuel(StatutMembre.ASSOCIE); 
        }
        
        // Hachage du mot de passe
        membre.setPassword(passwordEncoder.encode(rawPassword));
        
        // Transfert des données complémentaires pour le profil
        membre.setDernierDiplome(demande.getDernierDiplome());
        membre.setEtablissement(demande.getEtablissement());
        
        // إضافة الحقول الجديدة التي كانت تظهر فارغة
        // ملاحظة: تأكدي أن هذه الحقول موجودة في كلاس DemandeAdhesion
        membre.setBiographie("Membre nouvellement inscrit via demande d'adhésion.");
        membre.setCentresDinteret("Non spécifié");
        
        // Date d'affiliation = aujourd'hui
        membre.setDateAffiliation(java.sql.Date.valueOf(LocalDate.now()));
        membreRepository.save(membre);

        // 4. Enregistrement dans l'historique d'affiliation (période ouverte)
        HistoriqueAffiliation historique = new HistoriqueAffiliation();
        historique.setMembre(membre);
        historique.setDateDebut(LocalDate.now());
        historique.setStatut(membre.getStatutActuel().name()); // valeur réelle de l'enum
        // dateFin = null → période en cours
        historiqueRepository.save(historique);

        // 5. Mise à jour du statut de la demande
        demande.setStatut(StatutDemande.ACCEPTEE);
        demandeRepository.save(demande);

        // 6. Envoi de l'email
        String loginUrl = "http://localhost:8080/login";
        String emailBody = "Félicitations " + demande.getPrenom() + " !\n\n" +
                           "Votre demande d'adhésion au laboratoire LIAS a été acceptée.\n" +
                           "Votre rôle assigné est : " + role + "\n" +
                           "Votre mot de passe provisoire est : " + rawPassword + "\n\n" +
                           "Vous pouvez vous connecter via le lien suivant : " + loginUrl;

        sendEmail(demande.getEmail(), "Validation de votre adhésion - Laboratoire LIAS", emailBody);
    }
    
    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
    
    @Transactional
    public void traiterDemande(Long id, StatutDemande statut) {
        DemandeAdhesion demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        demande.setStatut(statut);
        demandeRepository.save(demande);

        // Envoyer email de refus
        if (statut == StatutDemande.REFUSEE) {
            String emailBody = "Bonjour " + demande.getPrenom() + " " + demande.getNom() + ",\n\n"
                    + "Nous avons bien étudié votre demande d'adhésion au laboratoire LIAS.\n"
                    + "Après examen, nous sommes au regret de vous informer que votre candidature "
                    + "n'a pas été retenue à ce stade.\n\n"
                    + "Nous vous encourageons à soumettre à nouveau votre candidature ultérieurement.\n\n"
                    + "Cordialement,\nLa Direction du Laboratoire LIAS";
            sendEmail(demande.getEmail(),
                    "Résultat de votre demande d'adhésion - Laboratoire LIAS", emailBody);
        }
    }
}