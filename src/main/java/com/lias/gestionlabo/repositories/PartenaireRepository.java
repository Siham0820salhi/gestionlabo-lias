package com.lias.gestionlabo.repositories;

import com.lias.gestionlabo.models.Partenaire;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PartenaireRepository extends JpaRepository<Partenaire, Long> {
    List<Partenaire> findByNomContainingIgnoreCase(String nom);
}
