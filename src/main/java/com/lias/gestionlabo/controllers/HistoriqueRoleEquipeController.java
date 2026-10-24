package com.lias.gestionlabo.controllers;

import com.lias.gestionlabo.models.HistoriqueRoleEquipe;
import com.lias.gestionlabo.services.HistoriqueRoleEquipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles-historique")
@CrossOrigin(origins = "*")
public class HistoriqueRoleEquipeController {

    @Autowired
    private HistoriqueRoleEquipeService historiqueService;

    @PostMapping
    public HistoriqueRoleEquipe assignerRole(@RequestBody HistoriqueRoleEquipe historique) {
        return historiqueService.assignerRole(
            historique.getMembre(),
            historique.getRole(),
            historique.getDateDebut()
        );
    }

    @GetMapping
    public List<HistoriqueRoleEquipe> getHistorique() {
        return historiqueService.getAllHistoriqueRoles();
    }
}