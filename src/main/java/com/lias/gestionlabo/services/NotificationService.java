package com.lias.gestionlabo.services;

import com.lias.gestionlabo.models.Membre;
import com.lias.gestionlabo.models.Notification;
import com.lias.gestionlabo.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notifRepo;

    public void envoyer(Membre destinataire, String message, String lien) {
        notifRepo.save(new Notification(destinataire, message, lien));
    }

    public List<Notification> getMesNotifications(Long membreId) {
        return notifRepo.findByDestinataireIdMembreOrderByDateCreationDesc(membreId);
    }

    public long countNonLues(Long membreId) {
        return notifRepo.countByDestinataireIdMembreAndLueFalse(membreId);
    }

    public void marquerLue(Long notifId) {
        notifRepo.findById(notifId).ifPresent(n -> {
            n.setLue(true);
            notifRepo.save(n);
        });
    }

    public void marquerToutesLues(Long membreId) {
        List<Notification> nonLues = notifRepo.findByDestinataireIdMembreAndLueFalse(membreId);
        nonLues.forEach(n -> n.setLue(true));
        notifRepo.saveAll(nonLues);
    }
}
