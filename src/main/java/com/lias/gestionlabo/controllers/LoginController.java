package com.lias.gestionlabo.controllers;

import com.lias.gestionlabo.models.DemandeAdhesion;
import com.lias.gestionlabo.models.Publication;
import com.lias.gestionlabo.models.StatutDemande;
import com.lias.gestionlabo.services.*; // قمنا باستيراد الكل لتشمل MandatService

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class LoginController {

    @Autowired
    private MembreService membreService;
    @Autowired
    private PublicationService publicationService;
    @Autowired
    private DemandeAdhesionService demandeService;
    @Autowired
    private LaboratoireService laboService;
    
    // تم إضافة الخدمة الجديدة هنا
    @Autowired
    private MandatService mandatService;
    @Autowired
    private EvenementService evenementService;

    private void ajouterAttributsUtilisateur(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            model.addAttribute("username", auth.getName());
            // إضافة خاصية المدير النشط لكل الصفحات لتسهيل إخفاء الروابط في HTML
            model.addAttribute("isDirecteurActif", mandatService.isUserDirecteurActif(auth.getName()));
        }
    }

    @GetMapping("/adhesion/nouveau")
    public String formAdhesion(Model model) {
        model.addAttribute("demande", new DemandeAdhesion());
        return "demander-adhesion"; 
    }

    @PostMapping("/adhesion/sauvegarder")
    public String saveAdhesion(@ModelAttribute DemandeAdhesion demande, 
                               @RequestParam("fileCV") MultipartFile fileCV) {
        if (!fileCV.isEmpty()) {
            try {
                String uploadDir = "uploads/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                String fileName = UUID.randomUUID().toString() + "_" + fileCV.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(fileCV.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                demande.setCvPath(fileName); 
            } catch (Exception e) { e.printStackTrace(); }
        }
        demande.setStatut(StatutDemande.EN_ATTENTE);
        demandeService.save(demande); 
        return "redirect:/"; 
    }

    @PostMapping("/demandes/accepter/{id}")
    public String accepterDemande(@PathVariable Long id, @RequestParam("role") String role) {
        try {
            demandeService.accepterDemande(id, role);
            return "redirect:/demandes?success=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/demandes?error=true";
        }
    }
    
    @GetMapping("/demandes/refuser/{id}")
    public String refuserDemande(@PathVariable Long id) {
        demandeService.traiterDemande(id, StatutDemande.REFUSEE);
        return "redirect:/demandes";
    }

    @GetMapping("/login")
    public String login() { return "login"; }
    
    @GetMapping("/")
    public String indexPublic(Model model) {
        // Publications récentes (3 dernières)
        java.util.List<Publication> allPubs = publicationService.getAllPublications();
        int count = Math.min(3, allPubs.size());
        model.addAttribute("recentPublications", allPubs.subList(allPubs.size() - count, allPubs.size()));
        // Événements récents (3 derniers)
        java.util.List<com.lias.gestionlabo.models.Evenement> allEvents = evenementService.getAllEvenements();
        int evCount = Math.min(3, allEvents.size());
        model.addAttribute("recentEvenements", allEvents.subList(allEvents.size() - evCount, allEvents.size()));
        // Stats
        model.addAttribute("totalMembres", membreService.getAllMembres().size());
        model.addAttribute("totalPublications", allPubs.size());
        return "index";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        ajouterAttributsUtilisateur(model);
        model.addAttribute("labo", laboService.getLaboratoireInfo());
        model.addAttribute("totalMembres", membreService.getAllMembres().size());
        model.addAttribute("totalPublications", publicationService.getAllPublications().size());
        model.addAttribute("totalEvenements", evenementService.getAllEvenements().size());
        long mandatsActifs = mandatService.getAllMandats().stream()
            .filter(m -> m.isEstActif()).count();
        model.addAttribute("mandatsActifs", mandatsActifs);
        return "dashboard";
    }

    @GetMapping("/publications")
    public String publicationsPage(Model model) {
        ajouterAttributsUtilisateur(model);
        model.addAttribute("publicationsList", publicationService.getAllPublications());
        return "publications";
    }

    @GetMapping("/demandes")
    public String demandesPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // حماية إضافية للرابط
        if (!mandatService.isUserDirecteurActif(auth.getName())) {
            return "redirect:/dashboard";
        }
        ajouterAttributsUtilisateur(model);
        model.addAttribute("demandesList", demandeService.getDemandesEnAttente());
        return "demandes";
    }

    @GetMapping("/publications/nouveau")
    public String nouvellePublicationForm(Model model) {
        ajouterAttributsUtilisateur(model);
        model.addAttribute("publication", new Publication());
        model.addAttribute("membresList", membreService.getAllMembres());
        return "form-publication";
    }

    @PostMapping("/publications/sauvegarder")
    public String sauvegarderPublication(
            @ModelAttribute("publication") Publication publication,
            @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile) {

        if (pdfFile != null && !pdfFile.isEmpty()) {
            try {
                String filename = UUID.randomUUID() + ".pdf";
                Path dir = Paths.get("uploads/publications");
                Files.createDirectories(dir);
                Files.copy(pdfFile.getInputStream(), dir.resolve(filename),
                           StandardCopyOption.REPLACE_EXISTING);
                publication.setFichierPdf(filename);
            } catch (IOException e) {
                // continuer sans PDF si upload échoue
            }
        }

        publicationService.savePublication(publication);
        return "redirect:/publications";
    }

    @GetMapping("/public/publications")
    public String publicPublicationsPage(Model model) {
        model.addAttribute("publicationsList", publicationService.getAllPublications());
        return "public-publications"; 
    }
    
    @GetMapping("/public/equipes")
    public String publicEquipesPage(Model model) {
        model.addAttribute("membres", membreService.getAllMembres());
        return "public-equipes";
    }

    @GetMapping("/public/activites")
    public String publicActivitesPage(Model model) {
        model.addAttribute("evenements", evenementService.getAllEvenements());
        return "public-activites";
    }
}