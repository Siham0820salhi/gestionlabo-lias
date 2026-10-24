package com.lias.gestionlabo.controllers;

import com.lias.gestionlabo.models.Membre;
import com.lias.gestionlabo.models.RoleMembre;
import com.lias.gestionlabo.repositories.MembreRepository;
import com.lias.gestionlabo.services.HistoriqueRoleEquipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/roles")
@PreAuthorize("hasAnyAuthority('DIRECTEUR','ROLE_DIRECTEUR','ADMIN','ROLE_ADMIN')")
public class RoleEquipeController {

    @Autowired private HistoriqueRoleEquipeService roleService;
    @Autowired private MembreRepository membreRepo;

    @GetMapping("/historique")
    public String historique(Model model) {
        model.addAttribute("historiques", roleService.getAllHistoriqueRoles());
        model.addAttribute("rolesActifs", roleService.getRolesActifs());
        model.addAttribute("membres", membreRepo.findAll());
        model.addAttribute("rolesMembre", RoleMembre.values());
        return "historique-roles";
    }

    @PostMapping("/assigner")
    public String assigner(@RequestParam Long membreId,
                           @RequestParam String role,
                           @RequestParam String dateDebut) {
        Membre m = membreRepo.findById(membreId)
            .orElseThrow(() -> new RuntimeException("Membre introuvable"));
        RoleMembre rm = RoleMembre.valueOf(role);

        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateDebut);
        } catch (ParseException e) {
            date = new Date();
        }

        roleService.assignerRole(m, rm, date);
        return "redirect:/roles/historique?success=1";
    }

    @GetMapping("/cloturer/{id}")
    public String cloturer(@PathVariable Long id) {
        roleService.cloturerRole(id);
        return "redirect:/roles/historique?cloture=1";
    }
}
