package com.lias.gestionlabo.repositories;

import com.lias.gestionlabo.models.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {
    // Recheche des publications par membre
    List<Publication> findByMembreIdMembre(Long idMembre);
}