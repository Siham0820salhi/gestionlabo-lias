package com.lias.gestionlabo.repositories;

import com.lias.gestionlabo.models.Convention;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConventionRepository extends JpaRepository<Convention, Long> {
    List<Convention> findByActive(boolean active);
    List<Convention> findByPartenaireId(Long partenaireId);
}
