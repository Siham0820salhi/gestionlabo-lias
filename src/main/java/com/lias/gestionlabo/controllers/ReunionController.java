package com.lias.gestionlabo.controllers;

import com.lias.gestionlabo.models.Reunion;
import com.lias.gestionlabo.services.ReunionService;
import com.lias.gestionlabo.services.MembreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.security.Principal;

@Controller
@RequestMapping("/reunions")
public class ReunionController {

    @Autowired private ReunionService reunionService;
    @Autowired private MembreService membreService;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/pv/";

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("reunions", reunionService.getToutesLesReunions());
        model.addAttribute("nouvelleReunion", new Reunion());
        return "reunions";
    }

    @PostMapping("/sauvegarder")
    public String sauvegarder(@ModelAttribute Reunion reunion,
                              @RequestParam("pvFichier") MultipartFile pvFichier,
                              Principal principal) throws IOException {
        reunion.setOrganisateur(membreService.findByEmail(principal.getName()));
        if (!pvFichier.isEmpty()) {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            String fileName = System.currentTimeMillis() + "_" + pvFichier.getOriginalFilename();
            Files.write(Paths.get(UPLOAD_DIR + fileName), pvFichier.getBytes());
            reunion.setPvPath(fileName);
        }
        reunionService.sauvegarder(reunion);
        return "redirect:/reunions";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        reunionService.supprimer(id);
        return "redirect:/reunions";
    }
}
