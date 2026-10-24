package com.lias.gestionlabo.repositories;

import com.lias.gestionlabo.models.Mandat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MandatRepository extends JpaRepository<Mandat, Long> {
    // Bch n-l9ao ghir les mandats li khdamin daba (actifs)
    List<Mandat> findByEstActifTrue();
}