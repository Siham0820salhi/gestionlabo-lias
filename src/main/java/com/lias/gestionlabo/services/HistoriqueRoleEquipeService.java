package com.lias.gestionlabo.services;

import com.lias.gestionlabo.models.HistoriqueRoleEquipe;
import com.lias.gestionlabo.models.Membre;
import com.lias.gestionlabo.models.RoleMembre;
import com.lias.gestionlabo.repositories.HistoriqueRoleEquipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class HistoriqueRoleEquipeService {

    @Autowired
    private HistoriqueRoleEquipeRepository historiqueRepository;

    // Assigner un nouveau rôle — clôture automatiquement le rôle précédent du même type
    @Transactional
    public HistoriqueRoleEquipe assignerRole(Membre membre, RoleMembre role, Date dateDebut) {
        // Clôturer l'éventuel rôle actif identique pour ce membre
        historiqueRepository.findByMembreIdMembre(membre.getIdMembre()).stream()
            .filter(h -> h.getRole() == role && h.getDateFin() == null)
            .forEach(h -> { h.setDateFin(new Date()); historiqueRepository.save(h); });

        HistoriqueRoleEquipe h = new HistoriqueRoleEquipe();
        h.setMembre(membre);
        h.setRole(role);
        h.setDateDebut(dateDebut);
        return historiqueRepository.save(h);
    }

    // Clôturer un rôle (dateFin = aujourd'hui)
    @Transactional
    public void cloturerRole(Long id) {
        HistoriqueRoleEquipe h = historiqueRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Rôle introuvable"));
        h.setDateFin(new Date());
        historiqueRepository.save(h);
    }

    // Tout l'historique, du plus récent au plus ancien
    public List<HistoriqueRoleEquipe> getAllHistoriqueRoles() {
        return historiqueRepository.findAllByOrderByDateDebutDesc();
    }

    // Rôles actuellement actifs (dateFin = null)
    public List<HistoriqueRoleEquipe> getRolesActifs() {
        return historiqueRepository.findByDateFinIsNull();
    }
}