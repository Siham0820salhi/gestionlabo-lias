package com.lias.gestionlabo.services;

import com.lias.gestionlabo.models.Membre;
import com.lias.gestionlabo.models.HistoriqueAffiliation;
import com.lias.gestionlabo.repositories.MembreRepository;
import com.lias.gestionlabo.repositories.HistoriqueAffiliationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate; // Hna l-correction
import java.util.List;
import java.util.Optional;

@Service
public class MembreService {

    @Autowired
    private MembreRepository membreRepository;

    @Autowired
    private HistoriqueAffiliationRepository historiqueRepo; 

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Sauvegarder ou modifier un membre avec cryptage + historisation correcte du statut
    public Membre saveMembre(Membre membre) {
        boolean statutChange = false;

        if (membre.getIdMembre() != null) {
            Optional<Membre> ancienOpt = membreRepository.findById(membre.getIdMembre());
            if (ancienOpt.isPresent()) {
                Membre ancien = ancienOpt.get();
                statutChange = ancien.getStatutActuel() != null
                    && membre.getStatutActuel() != null
                    && !ancien.getStatutActuel().equals(membre.getStatutActuel());

                if (statutChange) {
                    // Clôturer l'enregistrement ouvert (dateFin = null)
                    historiqueRepo.findByMembreIdMembre(membre.getIdMembre()).stream()
                        .filter(h -> h.getDateFin() == null)
                        .forEach(h -> {
                            h.setDateFin(LocalDate.now());
                            historiqueRepo.save(h);
                        });
                }
            }
        }

        if (membre.getPassword() != null && !membre.getPassword().startsWith("$2a$")) {
            membre.setPassword(passwordEncoder.encode(membre.getPassword()));
        }

        Membre saved = membreRepository.save(membre);

        // Créer un nouvel enregistrement pour le nouveau statut
        if (statutChange && saved.getStatutActuel() != null) {
            HistoriqueAffiliation nouv = new HistoriqueAffiliation();
            nouv.setMembre(saved);
            nouv.setStatut(saved.getStatutActuel().name());
            nouv.setDateDebut(LocalDate.now());
            // dateFin = null → période en cours
            historiqueRepo.save(nouv);
        }

        return saved;
    }

    public List<Membre> getAllMembres() {
        return membreRepository.findAll();
    }

    public Optional<Membre> getMembreById(Long id) {
        return membreRepository.findById(id);
    }

    public void deleteMembre(Long id) {
        membreRepository.findById(id).ifPresent(membre -> {
            membre.setStatutActuel(com.lias.gestionlabo.models.StatutMembre.ANCIEN);
            membreRepository.save(membre);
        });
    }

    public Membre findByEmail(String email) {
        return membreRepository.findByEmail(email).orElse(null);
    }
}