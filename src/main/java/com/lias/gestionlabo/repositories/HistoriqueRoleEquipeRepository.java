package com.lias.gestionlabo.repositories;

import com.lias.gestionlabo.models.HistoriqueRoleEquipe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface HistoriqueRoleEquipeRepository extends JpaRepository<HistoriqueRoleEquipe, Long> {
    List<HistoriqueRoleEquipe> findByMembreIdMembre(Long idMembre);
    List<HistoriqueRoleEquipe> findByDateFinIsNull();
    List<HistoriqueRoleEquipe> findAllByOrderByDateDebutDesc();
}