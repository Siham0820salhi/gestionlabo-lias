package com.lias.gestionlabo.services;

import com.lias.gestionlabo.models.Publication;
import com.lias.gestionlabo.repositories.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PublicationService {

    @Autowired
    private PublicationRepository publicationRepository;

    public Publication savePublication(Publication pub) {
        return publicationRepository.save(pub);
    }

    public List<Publication> getAllPublications() {
        return publicationRepository.findAll();
    }

    public List<Publication> getPublicationsByMembre(Long idMembre) {
        return publicationRepository.findByMembreIdMembre(idMembre);
    }
}