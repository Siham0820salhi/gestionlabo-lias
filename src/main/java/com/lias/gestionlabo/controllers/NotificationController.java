package com.lias.gestionlabo.controllers;

import com.lias.gestionlabo.services.NotificationService;
import com.lias.gestionlabo.services.MembreService;
import com.lias.gestionlabo.models.Membre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired private NotificationService notifService;
    @Autowired private MembreService membreService;

    @GetMapping
    public String liste(Model model, Principal principal) {
        Membre m = membreService.findByEmail(principal.getName());
        model.addAttribute("notifications", notifService.getMesNotifications(m.getIdMembre()));
        notifService.marquerToutesLues(m.getIdMembre());
        return "notifications";
    }

    @PostMapping("/lire/{id}")
    public String marquerLue(@PathVariable Long id) {
        notifService.marquerLue(id);
        return "redirect:/notifications";
    }
}
