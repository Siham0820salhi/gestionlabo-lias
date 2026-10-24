package com.lias.gestionlabo.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rapport_annuel")
public class RapportAnnuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Infos générales ──────────────────
    private int annee;
    private LocalDateTime dateGeneration;

    // ⭐ Directeur li généra rapport
    @ManyToOne
    @JoinColumn(name = "directeur_id")
    private Membre directeur;

    // ── Section Membres ──────────────────
    private int totalMembresActifs;
    private int nouveauxMembres;

    // ── Section Publications ─────────────
    private int totalPublications;

    // ── Section Événements ───────────────
    private int totalEvenements;

    // ── Section Matériel ─────────────────
    private int totalAccepte;
    private int totalRefuse;

    // ⭐ Liste distributions — bach rapport
    // yb9a fih détails dyal kol wa7d
    @OneToMany
    @JoinColumn(name = "rapport_id")
    private List<DistributionMateriel> distributions;

    // Constructor
    public RapportAnnuel() {
        this.dateGeneration = LocalDateTime.now();
    }

    // ── Getters / Setters ─────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }

    public LocalDateTime getDateGeneration() { return dateGeneration; }
    public void setDateGeneration(LocalDateTime dateGeneration) { 
        this.dateGeneration = dateGeneration; 
    }

    public Membre getDirecteur() { return directeur; }
    public void setDirecteur(Membre directeur) { 
        this.directeur = directeur; 
    }

    public int getTotalMembresActifs() { return totalMembresActifs; }
    public void setTotalMembresActifs(int totalMembresActifs) { 
        this.totalMembresActifs = totalMembresActifs; 
    }

    public int getNouveauxMembres() { return nouveauxMembres; }
    public void setNouveauxMembres(int nouveauxMembres) { 
        this.nouveauxMembres = nouveauxMembres; 
    }

    public int getTotalPublications() { return totalPublications; }
    public void setTotalPublications(int totalPublications) { 
        this.totalPublications = totalPublications; 
    }

    public int getTotalEvenements() { return totalEvenements; }
    public void setTotalEvenements(int totalEvenements) { 
        this.totalEvenements = totalEvenements; 
    }

    public int getTotalAccepte() { return totalAccepte; }
    public void setTotalAccepte(int totalAccepte) { 
        this.totalAccepte = totalAccepte; 
    }

    public int getTotalRefuse() { return totalRefuse; }
    public void setTotalRefuse(int totalRefuse) { 
        this.totalRefuse = totalRefuse; 
    }

    public List<DistributionMateriel> getDistributions() { 
        return distributions; 
    }
    public void setDistributions(List<DistributionMateriel> distributions) { 
        this.distributions = distributions; 
    }
}