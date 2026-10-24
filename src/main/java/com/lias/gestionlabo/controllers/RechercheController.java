package com.lias.gestionlabo.controllers;

import com.lias.gestionlabo.services.RechercheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/recherche")
public class RechercheController {

    @Autowired private RechercheService rechercheService;

    @GetMapping
    public String rechercher(@RequestParam(required = false) String q, Model model) {
        if (q != null && !q.isBlank()) {
            model.addAttribute("membres",      rechercheService.rechercherMembres(q));
            model.addAttribute("publications", rechercheService.rechercherPublications(q));
            model.addAttribute("evenements",   rechercheService.rechercherEvenements(q));
            model.addAttribute("documents",    rechercheService.rechercherDocuments(q));
            model.addAttribute("q", q);
        }
        return "recherche";
    }
}
