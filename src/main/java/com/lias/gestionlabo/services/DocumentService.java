package com.lias.gestionlabo.services;

import com.lias.gestionlabo.models.Document;
import com.lias.gestionlabo.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepo;

    public Document sauvegarder(Document document) {
        return documentRepo.save(document);
    }

    public List<Document> getTousLesDocuments() {
        return documentRepo.findAll();
    }

    public List<Document> getDocumentsParEvenement(Long evenementId) {
        return documentRepo.findByEvenementIdOrderByDateUploadDesc(evenementId);
    }

    public List<Document> rechercherParNom(String nom) {
        return documentRepo.findByNomContainingIgnoreCase(nom);
    }

    public Optional<Document> findById(Long id) {
        return documentRepo.findById(id);
    }

    public void supprimer(Long id) {
        documentRepo.deleteById(id);
    }
}
