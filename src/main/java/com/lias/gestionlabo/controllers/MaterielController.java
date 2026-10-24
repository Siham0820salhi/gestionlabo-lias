package com.lias.gestionlabo.controllers;

import com.lias.gestionlabo.models.DemandeMateriel;
import com.lias.gestionlabo.models.Materiel;
import com.lias.gestionlabo.models.Membre;
import com.lias.gestionlabo.models.StatutDemande;
import com.lias.gestionlabo.models.StatutMembre;
import com.lias.gestionlabo.models.TypePret;
import com.lias.gestionlabo.repositories.DemandeMaterielRepository;
import com.lias.gestionlabo.repositories.DistributionMaterielRepository;
import com.lias.gestionlabo.repositories.MaterielRepository;
import com.lias.gestionlabo.repositories.MembreRepository;
import com.lias.gestionlabo.repositories.RapportAnnuelRepository;
import com.lias.gestionlabo.services.MaterielService;
import com.lowagie.text.DocumentException;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MaterielController {

    @Autowired private DemandeMaterielRepository      demandeRepo;
    @Autowired private MembreRepository               membreRepo;
    @Autowired private MaterielRepository             materielRepo;
    @Autowired private DistributionMaterielRepository distRepo;
    @Autowired private RapportAnnuelRepository        rapportRepo;
    @Autowired private MaterielService                materielService;

    // ─────────────────────────────────────────────────────────────
    //  MEMBRE : formulaire de demande
    // ─────────────────────────────────────────────────────────────
    @GetMapping("/demande-materiel")
    public String showForm(Model model, Principal p) {
        Membre m = membreRepo.findByEmail(p.getName()).orElse(null);
        model.addAttribute("membre", m);
        model.addAttribute("demande", new DemandeMateriel());

        // Seuls PERMANENT et DOCTORANT (membres dans le labo) peuvent emprunter
        boolean accesRefuse = true;
        if (m != null) {
            StatutMembre s = m.getStatutActuel();
            accesRefuse = (s == null
                    || s == StatutMembre.ASSOCIE
                    || s == StatutMembre.RETRAITE
                    || s == StatutMembre.ANCIEN);
        }
        model.addAttribute("accesRefuse", accesRefuse);
        return "demande-materiel";
    }

    @PostMapping("/demande-materiel")
    public String saveDemande(@ModelAttribute DemandeMateriel d, Principal p) {
        Membre m = membreRepo.findByEmail(p.getName()).orElse(null);
        if (m == null) return "redirect:/dashboard";

        StatutMembre s = m.getStatutActuel();
        if (s == null || s == StatutMembre.ASSOCIE
                      || s == StatutMembre.RETRAITE
                      || s == StatutMembre.ANCIEN) {
            return "redirect:/demande-materiel?accesRefuse=1";
        }

        d.setMembre(m);
        d.setStatut(StatutDemande.EN_ATTENTE);
        demandeRepo.save(d);
        return "redirect:/mes-demandes";
    }

    // ─────────────────────────────────────────────────────────────
    //  MEMBRE : mes demandes
    // ─────────────────────────────────────────────────────────────
    @GetMapping("/mes-demandes")
    public String showMyDemandes(Model model, Principal p) {
        Membre m = membreRepo.findByEmail(p.getName()).orElse(null);
        if (m != null) {
            model.addAttribute("mesDemandes", demandeRepo.findByMembre(m));
        }
        return "mes-demandes";
    }

    // ─────────────────────────────────────────────────────────────
    //  DIRECTEUR : page de gestion
    // ─────────────────────────────────────────────────────────────
    @GetMapping("/admin/demandes")
    public String listDemandes(Model model,
                               @RequestParam(required = false) String dateCheck) {

        model.addAttribute("demandes",   demandeRepo.findAll());
        model.addAttribute("inventaire", materielRepo.findAllByOrderByNomAsc());
        model.addAttribute("historique", distRepo.findAllByOrderByDateAcceptationDesc());

        // Seuls PERMANENT et DOCTORANT sont éligibles à recevoir du matériel
        List<Membre> eligibles = membreRepo.findAll().stream()
                .filter(m -> m.getStatutActuel() == StatutMembre.PERMANENT
                          || m.getStatutActuel() == StatutMembre.DOCTORANT)
                .collect(Collectors.toList());
        model.addAttribute("membresEligibles", eligibles);

        // Disponibilité à la date vérifiée (défaut = aujourd'hui)
        LocalDate checkDate = (dateCheck != null && !dateCheck.isBlank())
                ? LocalDate.parse(dateCheck) : LocalDate.now();
        model.addAttribute("dateCheck", checkDate);

        Set<Long> occupes = distRepo.findOccupiedMaterielIdsOnDate(checkDate);
        model.addAttribute("occupes", occupes);

        return "admin-materiel";
    }

    @PostMapping("/admin/demandes/accepter/{id}")
    public String accepter(@PathVariable Long id) {
        materielService.accepterDemande(id);
        return "redirect:/admin/demandes";
    }

    @PostMapping("/admin/demandes/refuser/{id}")
    public String refuser(@PathVariable Long id) {
        materielService.refuserDemande(id);
        return "redirect:/admin/demandes";
    }

    // Distribution directe par le Directeur
    @PostMapping("/admin/materiel/distribuer")
    public String distribuer(@RequestParam Long materielId,
                             @RequestParam Long membreId,
                             @RequestParam String typePret,
                             @RequestParam String dateDebut,
                             @RequestParam(required = false) String dateFin) {
        materielService.distribuerDirectement(
                materielId, membreId,
                TypePret.valueOf(typePret),
                LocalDate.parse(dateDebut),
                (dateFin != null && !dateFin.isBlank()) ? LocalDate.parse(dateFin) : null);
        return "redirect:/admin/demandes?success=1";
    }

    // Ajouter un équipement à l'inventaire
    @PostMapping("/admin/materiel/ajouter")
    public String ajouterMateriel(@RequestParam String nom,
                                  @RequestParam(required = false) String description,
                                  @RequestParam(required = false) String reference,
                                  @RequestParam(required = false) String dateAcquisition) {
        Materiel mat = new Materiel();
        mat.setNom(nom);
        mat.setDescription(description);
        mat.setReference(reference);
        if (dateAcquisition != null && !dateAcquisition.isBlank())
            mat.setDateAcquisition(LocalDate.parse(dateAcquisition));
        materielRepo.save(mat);
        return "redirect:/admin/demandes?materielAjoute=1";
    }

    // Marquer retour matériel
    @PostMapping("/admin/materiel/retour/{id}")
    public String retourMateriel(@PathVariable Long id) {
        materielService.marquerRetourné(id);
        return "redirect:/admin/demandes?retour=1";
    }

    // ─────────────────────────────────────────────────────────────
    //  RAPPORT ANNUEL
    // ─────────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    @GetMapping("/admin/rapport")
    public String showRapport(@RequestParam(required = false) Integer annee, Model model) {
        int year = (annee != null) ? annee : java.time.LocalDate.now().getYear();
        model.addAttribute("anneeActuelle", year);
        try {
            model.addAttribute("rapports", rapportRepo.findAll());
            rapportRepo.findTopByAnneeOrderByDateGenerationDesc(year)
                    .ifPresent(r -> model.addAttribute("rapportActuel", r));
        } catch (Exception e) {
            model.addAttribute("rapports", Collections.emptyList());
        }
        return "rapport";
    }

    @PostMapping("/admin/rapport/generer")
    public String genererRapport(@RequestParam int annee) {
        materielService.genererRapportAnnuel(annee);
        return "redirect:/admin/rapport?annee=" + annee + "&success=1";
    }

    @GetMapping("/admin/rapport/download")
    public void downloadRapport(@RequestParam int annee, HttpServletResponse response)
            throws IOException, DocumentException, java.io.IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=Rapport_LIAS_" + annee + ".pdf");
        materielService.genererPdfRapport(annee, response);
    }
}
