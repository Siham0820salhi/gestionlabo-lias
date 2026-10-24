package com.lias.gestionlabo.controllers;

import com.lias.gestionlabo.models.Evenement;
import com.lias.gestionlabo.models.Membre;
import com.lias.gestionlabo.models.TypeEvenement;
import com.lias.gestionlabo.repositories.MembreRepository;
import com.lias.gestionlabo.services.EvenementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/evenements")
public class EvenementController {

    @Autowired
    private EvenementService evenementService;

    @Autowired
    private MembreRepository membreRepo;

    // Liste tous les événements
    @GetMapping
    public String liste(Model model) {
        model.addAttribute("evenements", 
            evenementService.getAllEvenements());
        return "evenements";
    }

    // Formulaire nouveau événement (PERMANENT + DIRECTEUR seulement)
    @PreAuthorize("hasAnyAuthority('PERMANENT','ROLE_PERMANENT','DIRECTEUR','ROLE_DIRECTEUR')")
    @GetMapping("/nouveau")
    public String formulaire(Model model) {
        model.addAttribute("evenement", new Evenement());
        model.addAttribute("types", TypeEvenement.values());
        return "form-evenement";
    }

    @PreAuthorize("hasAnyAuthority('PERMANENT','ROLE_PERMANENT','DIRECTEUR','ROLE_DIRECTEUR')")
    @PostMapping("/sauvegarder")
    public String sauvegarder(
            @ModelAttribute Evenement evenement,
            Principal principal) {
        Membre organisateur = membreRepo
            .findByEmail(principal.getName())
            .orElse(null);
        evenementService.sauvegarder(evenement, organisateur);
        return "redirect:/evenements";
    }

    @PreAuthorize("hasAnyAuthority('PERMANENT','ROLE_PERMANENT','DIRECTEUR','ROLE_DIRECTEUR')")
    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        evenementService.supprimer(id);
        return "redirect:/evenements";
    }

    // Vue calendrier
    @GetMapping("/calendrier")
    public String calendrier(Model model) {
        model.addAttribute("evenements", evenementService.getAllEvenements());
        return "calendrier";
    }

    // API JSON pour FullCalendar
    @GetMapping("/api/calendrier")
    @ResponseBody
    public List<Map<String, Object>> getEvenementsJson() {
        return evenementService.getAllEvenements().stream()
            .map(e -> Map.<String, Object>of(
                "id",    e.getId(),
                "title", e.getTitre(),
                "start", e.getDate().toString(),
                "color", couleurType(e.getType())
            ))
            .collect(Collectors.toList());
    }

    private String couleurType(TypeEvenement type) {
        return switch (type) {
            case CONFERENCE -> "#6ee7b7";
            case SEMINAIRE  -> "#38bdf8";
            case WORKSHOP   -> "#f472b6";
        };
    }
}