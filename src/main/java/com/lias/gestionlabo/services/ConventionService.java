package com.lias.gestionlabo.services;

import com.lias.gestionlabo.models.Convention;
import com.lias.gestionlabo.models.Partenaire;
import com.lias.gestionlabo.repositories.ConventionRepository;
import com.lias.gestionlabo.repositories.PartenaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ConventionService {

    @Autowired
    private ConventionRepository conventionRepo;

    @Autowired
    private PartenaireRepository partenaireRepo;

    public Convention sauvegarderConvention(Convention convention) {
        return conventionRepo.save(convention);
    }

    public Partenaire sauvegarderPartenaire(Partenaire partenaire) {
        return partenaireRepo.save(partenaire);
    }

    public List<Convention> getToutesLesConventions() {
        return conventionRepo.findAll();
    }

    public List<Convention> getConventionsActives() {
        return conventionRepo.findByActive(true);
    }

    public List<Partenaire> getTousLesPartenaires() {
        return partenaireRepo.findAll();
    }

    public Optional<Convention> findConventionById(Long id) {
        return conventionRepo.findById(id);
    }

    public Optional<Partenaire> findPartenaireById(Long id) {
        return partenaireRepo.findById(id);
    }

    public void supprimerConvention(Long id) {
        conventionRepo.deleteById(id);
    }

    public void supprimerPartenaire(Long id) {
        partenaireRepo.deleteById(id);
    }
}
