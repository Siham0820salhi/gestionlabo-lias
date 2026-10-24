package com.lias.gestionlabo.repositories;

import com.lias.gestionlabo.models.HistoriqueAffiliation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriqueAffiliationRepository extends JpaRepository<HistoriqueAffiliation, Long> {
    // Smiya khassha t-koun "Membre" (l-class) "IdMembre" (l-field f Membre)
    List<HistoriqueAffiliation> findByMembreIdMembre(Long idMembre);
}