package com.lias.gestionlabo.repositories;

import com.lias.gestionlabo.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByEquipeOrderByDateEnvoiAsc(String equipe);
    List<Message> findByEvenementIdOrderByDateEnvoiAsc(Long evenementId);
    List<Message> findByEquipeIsNullAndEvenementIsNullOrderByDateEnvoiDesc();
}
