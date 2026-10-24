package com.lias.gestionlabo.repositories;

import com.lias.gestionlabo.models.DemandeMateriel;
import com.lias.gestionlabo.models.Membre;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DemandeMaterielRepository extends JpaRepository<DemandeMateriel, Long> {
    List<DemandeMateriel> findByMembre(Membre membre);
}