package com.lias.gestionlabo.repositories;

import com.lias.gestionlabo.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByDestinataireIdMembreOrderByDateCreationDesc(Long membreId);
    List<Notification> findByDestinataireIdMembreAndLueFalse(Long membreId);
    long countByDestinataireIdMembreAndLueFalse(Long membreId);
}
