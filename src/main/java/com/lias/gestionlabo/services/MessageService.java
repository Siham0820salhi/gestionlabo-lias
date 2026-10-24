package com.lias.gestionlabo.services;

import com.lias.gestionlabo.models.Message;
import com.lias.gestionlabo.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepo;

    public Message sauvegarder(Message message) {
        return messageRepo.save(message);
    }

    public List<Message> getMessagesGeneraux() {
        return messageRepo.findByEquipeIsNullAndEvenementIsNullOrderByDateEnvoiDesc();
    }

    public List<Message> getMessagesParEquipe(String equipe) {
        return messageRepo.findByEquipeOrderByDateEnvoiAsc(equipe);
    }

    public List<Message> getMessagesParEvenement(Long evenementId) {
        return messageRepo.findByEvenementIdOrderByDateEnvoiAsc(evenementId);
    }

    public void supprimer(Long id) {
        messageRepo.deleteById(id);
    }
}
