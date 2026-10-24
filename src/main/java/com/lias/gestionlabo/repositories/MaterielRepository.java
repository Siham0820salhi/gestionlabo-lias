package com.lias.gestionlabo.repositories;

import com.lias.gestionlabo.models.Materiel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterielRepository extends JpaRepository<Materiel, Long> {
    List<Materiel> findAllByOrderByNomAsc();
}
