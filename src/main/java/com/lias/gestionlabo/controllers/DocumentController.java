package com.lias.gestionlabo.controllers;

import com.lias.gestionlabo.models.Document;
import com.lias.gestionlabo.models.TypeDocument;
import com.lias.gestionlabo.services.DocumentService;
import com.lias.gestionlabo.services.MembreService;
import com.lias.gestionlabo.repositories.EvenementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.security.Principal;

@Controller
@RequestMapping("/documents")
public class DocumentController {

    @Autowired private DocumentService documentService;
    @Autowired private MembreService membreService;
    @Autowired private EvenementRepository evenementRepo;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/documents/";

    @GetMapping
    public String liste(Model model, @RequestParam(required = false) String q) {
        if (q != null && !q.isBlank()) {
            model.addAttribute("documents", documentService.rechercherParNom(q));
            model.addAttribute("q", q);
        } else {
            model.addAttribute("documents", documentService.getTousLesDocuments());
        }
        model.addAttribute("types", TypeDocument.values());
        model.addAttribute("evenements", evenementRepo.findAll());
        return "documents";
    }

    @PostMapping("/uploader")
    public String uploader(@RequestParam String nom,
                           @RequestParam TypeDocument type,
                           @RequestParam(required = false) String description,
                           @RequestParam(required = false) Long evenementId,
                           @RequestParam("fichier") MultipartFile fichier,
                           Principal principal) throws IOException {
        Document doc = new Document();
        doc.setNom(nom);
        doc.setType(type);
        doc.setDescription(description);
        doc.setUploader(membreService.findByEmail(principal.getName()));

        if (evenementId != null) {
            evenementRepo.findById(evenementId).ifPresent(doc::setEvenement);
        }

        if (!fichier.isEmpty()) {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            String fileName = System.currentTimeMillis() + "_" + fichier.getOriginalFilename();
            Files.write(Paths.get(UPLOAD_DIR + fileName), fichier.getBytes());
            doc.setCheminFichier(fileName);
        }

        documentService.sauvegarder(doc);
        return "redirect:/documents";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        documentService.supprimer(id);
        return "redirect:/documents";
    }
}
