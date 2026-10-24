package com.lias.gestionlabo.services;

import com.lias.gestionlabo.models.Reunion;
import com.lias.gestionlabo.repositories.ReunionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReunionService {

    @Autowired
    private ReunionRepository reunionRepo;

    public Reunion sauvegarder(Reunion reunion) {
        return reunionRepo.save(reunion);
    }

    public List<Reunion> getToutesLesReunions() {
        return reunionRepo.findAllByOrderByDateReunionDesc();
    }

    public Optional<Reunion> findById(Long id) {
        return reunionRepo.findById(id);
    }

    public void supprimer(Long id) {
        reunionRepo.deleteById(id);
    }
}
