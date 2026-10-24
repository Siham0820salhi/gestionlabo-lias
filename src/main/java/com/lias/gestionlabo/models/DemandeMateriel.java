package com.lias.gestionlabo.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class DemandeMateriel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_membre")
    private Membre membre;

    private String description;
    private String departement;
    private String filiere;

    @Enumerated(EnumType.STRING)
    private StatutDemande statut = StatutDemande.EN_ATTENTE;

    private LocalDateTime dateDemande = LocalDateTime.now();

    // ── Nouveau : type de prêt souhaité ──────────────────────
    @Enumerated(EnumType.STRING)
    private TypePret typePret = TypePret.TEMPORAIRE;

    private LocalDate dateDebutSouhaitee;
    private LocalDate dateFinSouhaitee;   // null si DEFINITIF

    public DemandeMateriel() {}

    // Getters / Setters
    public Long getId()                                { return id; }
    public void setId(Long id)                         { this.id = id; }

    public Membre getMembre()                          { return membre; }
    public void setMembre(Membre membre)               { this.membre = membre; }

    public String getDescription()                     { return description; }
    public void setDescription(String d)               { this.description = d; }

    public String getDepartement()                     { return departement; }
    public void setDepartement(String d)               { this.departement = d; }

    public String getFiliere()                         { return filiere; }
    public void setFiliere(String f)                   { this.filiere = f; }

    public StatutDemande getStatut()                   { return statut; }
    public void setStatut(StatutDemande s)             { this.statut = s; }

    public LocalDateTime getDateDemande()              { return dateDemande; }
    public void setDateDemande(LocalDateTime d)        { this.dateDemande = d; }

    public TypePret getTypePret()                      { return typePret; }
    public void setTypePret(TypePret t)                { this.typePret = t; }

    public LocalDate getDateDebutSouhaitee()           { return dateDebutSouhaitee; }
    public void setDateDebutSouhaitee(LocalDate d)     { this.dateDebutSouhaitee = d; }

    public LocalDate getDateFinSouhaitee()             { return dateFinSouhaitee; }
    public void setDateFinSouhaitee(LocalDate d)       { this.dateFinSouhaitee = d; }
}
