package com.lias.gestionlabo.repositories;

import com.lias.gestionlabo.models.Document;
import com.lias.gestionlabo.models.TypeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByEvenementIdOrderByDateUploadDesc(Long evenementId);
    List<Document> findByType(TypeDocument type);
    List<Document> findByNomContainingIgnoreCase(String nom);
}
