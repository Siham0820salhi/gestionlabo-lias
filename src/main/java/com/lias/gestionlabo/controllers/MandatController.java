package com.lias.gestionlabo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.lias.gestionlabo.services.MandatService;
import com.lias.gestionlabo.models.Mandat;
import java.util.List;

@Controller
@RequestMapping("/mandats")
public class MandatController {
    
    @Autowired 
    private MandatService mandatService;

    // Afficher la liste des mandats
    @GetMapping
    public String showMandats(Model model) {
        List<Mandat> mandats = mandatService.getAllMandats();
        System.out.println("Nombre de mandats récupérés : " + mandats.size());
        model.addAttribute("mandats", mandats);
        return "mandats";
    }

    // Clôturer un mandat spécifique
    @PostMapping("/{id}/cloturer")
    @PreAuthorize("hasAnyAuthority('ADMIN','ROLE_ADMIN')")
    public String cloturer(@PathVariable Long id) {
        mandatService.cloturerMandat(id);
        return "redirect:/mandats";
    }
}