package com.lias.gestionlabo;

import com.lias.gestionlabo.models.Membre;
import com.lias.gestionlabo.models.StatutMembre;
import com.lias.gestionlabo.repositories.MembreRepository;
import com.lias.gestionlabo.services.MembreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Date;

@SpringBootApplication
public class GestionlaboApplication {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(GestionlaboApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(MembreService membreService, MembreRepository membreRepo) {
        return (args) -> {

            // ── 1. Compte DIRECTEUR initial (Siham Salhi) ──────────────────────
            String emailDirecteur = "siham.salhi2@lias.ma";
            if (membreService.getAllMembres().stream().noneMatch(m -> m.getEmail().equals(emailDirecteur))) {
                Membre directeur = new Membre();
                directeur.setNom("Salhi");
                directeur.setPrenom("Siham");
                directeur.setEmail(emailDirecteur);
                directeur.setPassword(passwordEncoder.encode("secure123"));
                directeur.setEtablissementOrigine("Université Hassan II");
                directeur.setLaboratoireOrigine("LIAS");
                directeur.setStatutActuel(StatutMembre.PERMANENT);
                directeur.setDateNaissance(new Date());
                directeur.setRole("DIRECTEUR");
                membreService.saveMembre(directeur);
                System.out.println("Siham Salhi créée avec le rôle DIRECTEUR.");
            }

            // ── 2. Compte ADMIN système ─────────────────────────────────────────
            // L'admin n'est pas un chercheur : pas de StatutMembre, pas de RoleMembre
            Membre admin = membreRepo.findByEmail("admin@lias.ma").orElseGet(Membre::new);
            admin.setNom("Système");
            admin.setPrenom("Admin");
            admin.setEmail("admin@lias.ma");
            admin.setPassword(passwordEncoder.encode("Admin@2026"));
            admin.setRole("ADMIN");
            // statutActuel intentionnellement null — admin n'est pas membre du labo
            membreRepo.save(admin);

            System.out.println("==============================================");
            System.out.println("  ADMIN LIAS");
            System.out.println("  Email    : admin@lias.ma");
            System.out.println("  Password : Admin@2026");
            System.out.println("==============================================");
        };
    }
}
