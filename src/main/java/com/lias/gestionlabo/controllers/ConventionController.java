package com.lias.gestionlabo.controllers;

import com.lias.gestionlabo.models.Convention;
import com.lias.gestionlabo.models.Partenaire;
import com.lias.gestionlabo.services.ConventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;

@Controller
@RequestMapping("/conventions")
public class ConventionController {

    @Autowired private ConventionService conventionService;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/conventions/";

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("conventions", conventionService.getToutesLesConventions());
        model.addAttribute("partenaires", conventionService.getTousLesPartenaires());
        model.addAttribute("nouvelleConvention", new Convention());
        model.addAttribute("nouveauPartenaire", new Partenaire());
        return "conventions";
    }

    @PostMapping("/partenaire/sauvegarder")
    public String sauvegarderPartenaire(@ModelAttribute Partenaire partenaire) {
        conventionService.sauvegarderPartenaire(partenaire);
        return "redirect:/conventions";
    }

    @PostMapping("/sauvegarder")
    public String sauvegarderConvention(@ModelAttribute Convention convention,
                                        @RequestParam(required = false) Long partenaireId,
                                        @RequestParam("document") MultipartFile document) throws IOException {
        if (partenaireId != null) {
            conventionService.findPartenaireById(partenaireId)
                    .ifPresent(convention::setPartenaire);
        }
        if (!document.isEmpty()) {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            String fileName = System.currentTimeMillis() + "_" + document.getOriginalFilename();
            Files.write(Paths.get(UPLOAD_DIR + fileName), document.getBytes());
            convention.setDocumentPath(fileName);
        }
        conventionService.sauvegarderConvention(convention);
        return "redirect:/conventions";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        conventionService.supprimerConvention(id);
        return "redirect:/conventions";
    }
}
