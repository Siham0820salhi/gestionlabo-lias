package com.lias.gestionlabo.repositories;

import com.lias.gestionlabo.models.RapportAnnuel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RapportAnnuelRepository extends JpaRepository<RapportAnnuel, Long> {
    Optional<RapportAnnuel> findTopByAnneeOrderByDateGenerationDesc(int annee);
}