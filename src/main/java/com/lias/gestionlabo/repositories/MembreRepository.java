package com.lias.gestionlabo.repositories;

import com.lias.gestionlabo.models.Membre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MembreRepository extends JpaRepository<Membre, Long> {
    
    // Méthode pour chercher un membre par son email lors du Login
    Optional<Membre> findByEmail(String email);
}