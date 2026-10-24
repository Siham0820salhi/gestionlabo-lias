package com.lias.gestionlabo.controllers;

import com.lias.gestionlabo.models.Message;
import com.lias.gestionlabo.services.MessageService;
import com.lias.gestionlabo.services.MembreService;
import com.lias.gestionlabo.repositories.EvenementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired private MessageService messageService;
    @Autowired private MembreService membreService;
    @Autowired private EvenementRepository evenementRepo;

    @GetMapping
    public String listeGenerale(Model model) {
        model.addAttribute("messages", messageService.getMessagesGeneraux());
        model.addAttribute("contexte", "general");
        return "messages";
    }

    @GetMapping("/equipe/{equipe}")
    public String parEquipe(@PathVariable String equipe, Model model) {
        model.addAttribute("messages", messageService.getMessagesParEquipe(equipe));
        model.addAttribute("contexte", equipe);
        model.addAttribute("equipe", equipe);
        return "messages";
    }

    @GetMapping("/evenement/{id}")
    public String parEvenement(@PathVariable Long id, Model model) {
        model.addAttribute("messages", messageService.getMessagesParEvenement(id));
        model.addAttribute("contexte", "evenement");
        model.addAttribute("evenementId", id);
        evenementRepo.findById(id).ifPresent(e -> model.addAttribute("evenement", e));
        return "messages";
    }

    @PostMapping("/envoyer")
    public String envoyer(@RequestParam String contenu,
                          @RequestParam(required = false) String equipe,
                          @RequestParam(required = false) Long evenementId,
                          Principal principal) {
        Message msg = new Message();
        msg.setContenu(contenu);
        msg.setExpediteur(membreService.findByEmail(principal.getName()));
        if (equipe != null && !equipe.isBlank()) {
            msg.setEquipe(equipe);
        }
        if (evenementId != null) {
            evenementRepo.findById(evenementId).ifPresent(msg::setEvenement);
        }
        messageService.sauvegarder(msg);

        if (equipe != null && !equipe.isBlank()) return "redirect:/messages/equipe/" + equipe;
        if (evenementId != null) return "redirect:/messages/evenement/" + evenementId;
        return "redirect:/messages";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        messageService.supprimer(id);
        return "redirect:/messages";
    }
}
