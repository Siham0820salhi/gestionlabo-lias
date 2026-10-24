package com.lias.gestionlabo.repositories;

import com.lias.gestionlabo.models.DemandeAdhesion;
import com.lias.gestionlabo.models.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DemandeAdhesionRepository extends JpaRepository<DemandeAdhesion, Long> {
    // Filtrer les demandes par statut (ex: EN_ATTENTE pour le Directeur)
    List<DemandeAdhesion> findByStatut(StatutDemande statut);
}