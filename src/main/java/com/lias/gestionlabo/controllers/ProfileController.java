package com.lias.gestionlabo.controllers;

import com.lias.gestionlabo.models.Membre;
import com.lias.gestionlabo.repositories.HistoriqueAffiliationRepository;
import com.lias.gestionlabo.services.MembreService;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@RequestMapping("/profil")
public class ProfileController {

    @Autowired
    private MembreService membreService;

    @Autowired
    private HistoriqueAffiliationRepository historiqueRepo;

    @GetMapping
    public String showMyProfile(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Membre m = membreService.findByEmail(email);
        
        // --- تصحيح لمنع Error 500 ---
        if (m == null) {
            // إيلا ما لقاش العضو، نرجعوه لصفحة Login أو Dashboard عوض ما يضرب Error
            return "redirect:/login?error"; 
        }
        
        model.addAttribute("membre", m);
        
        // Zidi l-historique dial had l-membre
        model.addAttribute("historiqueList", historiqueRepo.findByMembreIdMembre(m.getIdMembre()));
        
        return "mon-profil";
    }

    @PostMapping("/modifier")
    public String updateProfile(@ModelAttribute("membre") Membre membreModifie, 
                                @RequestParam(value = "photoFile", required = false) MultipartFile photoFile) {
        
        // 1. Jibi l-membre l-asli mn DB
        if (membreModifie.getIdMembre() == null) {
            return "redirect:/profil?error";
        }

        Membre membreOrigine = membreService.getMembreById(membreModifie.getIdMembre()).orElse(null);
        
        if (membreOrigine != null) {
            // 2. Mappi les nouvelles valeurs
            membreOrigine.setNom(membreModifie.getNom());
            membreOrigine.setPrenom(membreModifie.getPrenom());
            membreOrigine.setBiographie(membreModifie.getBiographie());
            membreOrigine.setCentresDinteret(membreModifie.getCentresDinteret());
            membreOrigine.setEtablissementOrigine(membreModifie.getEtablissementOrigine());
            membreOrigine.setLaboratoireOrigine(membreModifie.getLaboratoireOrigine());
            membreOrigine.setEquipeActuelle(membreModifie.getEquipeActuelle());
            
            // 3. Sauvegarde du fichier photo sur le disque
            if (photoFile != null && !photoFile.isEmpty()) {
                try {
                    String originalFilename = photoFile.getOriginalFilename();
                    String extension = (originalFilename != null && originalFilename.contains("."))
                        ? originalFilename.substring(originalFilename.lastIndexOf("."))
                        : ".jpg";
                    String filename = UUID.randomUUID().toString() + extension;

                    Path uploadDir = Paths.get("uploads");
                    Files.createDirectories(uploadDir);
                    Files.copy(photoFile.getInputStream(), uploadDir.resolve(filename),
                               StandardCopyOption.REPLACE_EXISTING);

                    membreOrigine.setPhoto(filename);
                } catch (IOException e) {
                    // Continuer à sauvegarder les autres champs même si l'upload échoue
                }
            }
            
            // 4. Save
            membreService.saveMembre(membreOrigine);
        }
        return "redirect:/profil";
    }
}