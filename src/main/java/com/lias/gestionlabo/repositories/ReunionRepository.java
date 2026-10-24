package com.lias.gestionlabo.repositories;

import com.lias.gestionlabo.models.Reunion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReunionRepository extends JpaRepository<Reunion, Long> {
    List<Reunion> findAllByOrderByDateReunionDesc();
}
