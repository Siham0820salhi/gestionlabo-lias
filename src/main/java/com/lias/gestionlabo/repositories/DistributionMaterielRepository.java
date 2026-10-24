package com.lias.gestionlabo.repositories;

import com.lias.gestionlabo.models.DistributionMateriel;
import com.lias.gestionlabo.models.Membre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface DistributionMaterielRepository extends JpaRepository<DistributionMateriel, Long> {

    List<DistributionMateriel> findAllByOrderByDateAcceptationDesc();

    List<DistributionMateriel> findByMembre(Membre membre);

    // IDs des matériels occupés à une date donnée
    @Query("""
        SELECT d.materiel.id FROM DistributionMateriel d
        WHERE d.materiel IS NOT NULL
          AND (d.actif IS NULL OR d.actif = TRUE)
          AND (
                d.typePret = 'DEFINITIF'
                OR (d.typePret = 'TEMPORAIRE'
                    AND d.dateDebut <= :date
                    AND (d.dateFin IS NULL OR d.dateFin >= :date))
              )
        """)
    Set<Long> findOccupiedMaterielIdsOnDate(@Param("date") LocalDate date);
}
