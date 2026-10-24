package com.lias.gestionlabo.repositories;

import com.lias.gestionlabo.models.Laboratoire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaboratoireRepository extends JpaRepository<Laboratoire, Long> {
    // Hna ghadi t-t-khwa l-khedma dial "CRUD" otomatiki mn Spring
}