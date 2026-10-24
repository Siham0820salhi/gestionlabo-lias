package com.lias.gestionlabo.controllers;

import com.lias.gestionlabo.models.Membre;
import com.lias.gestionlabo.services.MembreService;
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

@Controller
@RequestMapping("/membres")
public class MembreController {

	@Autowired
	private MembreService membreService;

	// 📁 Dossier de stockage des photos de profil
	public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/images";

	// Méthode pour injecter l'utilisateur connecté dans la vue (évite les bugs
	// d'affichage de la sidebar)
	private void ajouterAttributsUtilisateur(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
			model.addAttribute("username", auth.getName());
		}
	}

	// 1. Afficher la liste de tous les membres
	@GetMapping
	public String getAllMembres(Model model) {
	    // Injecter l'utilisateur connecté pour éviter les bugs d'affichage de la sidebar
	    org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
	        model.addAttribute("username", auth.getName());
	    }
	    
	    // Envoi de la liste des membres au modèle
	    java.util.List<com.lias.gestionlabo.models.Membre> listMembres = membreService.getAllMembres();
	    model.addAttribute("membresList", listMembres); 
	    
	    return "membres"; // Ouvre la page templates/membres.html
	}

	// 🆕 Méthode corrigée pour l'appel AJAX (Safe handling dial Optional)
	@GetMapping("/api/membres/{id}")
	@ResponseBody
	public Membre getMembreJson(@PathVariable Long id) {
	    // .orElse(null) kat-7ll l-mouchkil dial 'Optional'
	    return membreService.getMembreById(id).orElse(null);
	}

	// 2. Afficher le formulaire d'ajout
	@GetMapping("/nouveau")
	public String showFormNouveauMembre(Model model) {
		ajouterAttributsUtilisateur(model);
		model.addAttribute("membre", new Membre());
		return "form-membre";
	}

	// 3. Traiter l'enregistrement (Sauvegarde de l'entité + Image)
	@PostMapping("/sauvegarder")
	public String sauvegarderMembre(@ModelAttribute("membre") Membre membre,
			@RequestParam("photoFile") MultipartFile photoFile) {

		if (!photoFile.isEmpty()) {
			try {
				String fileName = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();
				Path path = Paths.get(UPLOAD_DIRECTORY, fileName);

				Files.createDirectories(path.getParent());
				Files.write(path, photoFile.getBytes());

				membre.setPhoto(fileName);

			} catch (IOException e) {
				e.printStackTrace();
				membre.setPhoto("default-avatar.png");
			}
		} else if (membre.getPhoto() == null || membre.getPhoto().isEmpty()) {
			membre.setPhoto("default-avatar.png");
		}

		membreService.saveMembre(membre);
		return "redirect:/membres";
	}

	// 4. Supprimer un membre
	@GetMapping("/supprimer/{id}")
	public String deleteMembre(@PathVariable Long id) {
		membreService.deleteMembre(id);
		return "redirect:/membres";
	}

	@InitBinder
	public void initBinder(org.springframework.web.bind.WebDataBinder binder) {
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(java.util.Date.class,
				new org.springframework.beans.propertyeditors.CustomDateEditor(dateFormat, true));
	}
}