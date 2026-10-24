package com.lias.gestionlabo.services;

import com.lias.gestionlabo.models.*;
import com.lias.gestionlabo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RechercheService {

    @Autowired private MembreRepository membreRepo;
    @Autowired private PublicationRepository publicationRepo;
    @Autowired private EvenementRepository evenementRepo;
    @Autowired private DocumentRepository documentRepo;

    public List<Membre> rechercherMembres(String q) {
        return membreRepo.findAll().stream()
            .filter(m -> contains(m.getNom(), q) || contains(m.getPrenom(), q)
                      || contains(m.getEmail(), q))
            .toList();
    }

    public List<Publication> rechercherPublications(String q) {
        return publicationRepo.findAll().stream()
            .filter(p -> contains(p.getTitre(), q) || contains(p.getAuteurs(), q)
                      || contains(p.getEquipe(), q))
            .toList();
    }

    public List<Evenement> rechercherEvenements(String q) {
        return evenementRepo.findAll().stream()
            .filter(e -> contains(e.getTitre(), q) || contains(e.getLieu(), q)
                      || contains(e.getDescription(), q))
            .toList();
    }

    public List<Document> rechercherDocuments(String q) {
        return documentRepo.findByNomContainingIgnoreCase(q);
    }

    private boolean contains(String field, String q) {
        return field != null && field.toLowerCase().contains(q.toLowerCase());
    }
}
