package com.lias.gestionlabo.services;

import com.lias.gestionlabo.models.Evenement;
import com.lias.gestionlabo.models.Membre;
import com.lias.gestionlabo.repositories.EvenementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EvenementService {

    @Autowired
    private EvenementRepository evenementRepo;

    // Jib tous les événements
    public List<Evenement> getAllEvenements() {
        return evenementRepo.findAll();
    }

    // Jib un événement par id
    public Evenement getById(Long id) {
        return evenementRepo.findById(id)
            .orElseThrow(() -> 
                new RuntimeException("Événement introuvable"));
    }

    // Sauvegarder événement
    public Evenement sauvegarder(Evenement evenement, 
                                  Membre organisateur) {
        evenement.setOrganisateur(organisateur);
        return evenementRepo.save(evenement);
    }

    // Supprimer événement
    public void supprimer(Long id) {
        evenementRepo.deleteById(id);
    }
}