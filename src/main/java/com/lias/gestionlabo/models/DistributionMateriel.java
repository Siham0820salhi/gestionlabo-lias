package com.lias.gestionlabo.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "distribution_materiel")
public class DistributionMateriel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "demande_id")
    private DemandeMateriel demande;

    @ManyToOne
    @JoinColumn(name = "id_membre")
    private Membre membre;

    // ── Lien vers l'inventaire (nullable pour l'historique ancien) ──
    @ManyToOne
    @JoinColumn(name = "materiel_id")
    private Materiel materiel;

    // ── Type de prêt ────────────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    private TypePret typePret = TypePret.TEMPORAIRE;

    private LocalDate dateDebut;
    private LocalDate dateFin;   // null = DEFINITIF

    private LocalDateTime dateAcceptation;

    // ── Actif (false quand le matériel est rendu) ───────────────────
    // Boolean wrapper : null traité comme true pour l'historique ancien
    private Boolean actif = Boolean.TRUE;

    // ── Champs copiés pour historique (rapport PDF) ──────────────────
    private String descriptionDemande;
    private String nomMembre;
    private String prenomMembre;
    private String departementMembre;
    private String filiereMembre;

    public DistributionMateriel() {}

    // Getters / Setters
    public Long getId()                                     { return id; }

    public DemandeMateriel getDemande()                    { return demande; }
    public void setDemande(DemandeMateriel d)              { this.demande = d; }

    public Membre getMembre()                              { return membre; }
    public void setMembre(Membre m)                        { this.membre = m; }

    public Materiel getMateriel()                          { return materiel; }
    public void setMateriel(Materiel m)                    { this.materiel = m; }

    public TypePret getTypePret()                          { return typePret; }
    public void setTypePret(TypePret t)                    { this.typePret = t; }

    public LocalDate getDateDebut()                        { return dateDebut; }
    public void setDateDebut(LocalDate d)                  { this.dateDebut = d; }

    public LocalDate getDateFin()                          { return dateFin; }
    public void setDateFin(LocalDate d)                    { this.dateFin = d; }

    public LocalDateTime getDateAcceptation()              { return dateAcceptation; }
    public void setDateAcceptation(LocalDateTime d)        { this.dateAcceptation = d; }

    public boolean isActif()                               { return actif == null || actif; }
    public void setActif(boolean a)                        { this.actif = a; }

    public String getDescriptionDemande()                  { return descriptionDemande; }
    public void setDescriptionDemande(String s)            { this.descriptionDemande = s; }

    public String getNomMembre()                           { return nomMembre; }
    public void setNomMembre(String s)                     { this.nomMembre = s; }

    public String getPrenomMembre()                        { return prenomMembre; }
    public void setPrenomMembre(String s)                  { this.prenomMembre = s; }

    public String getDepartementMembre()                   { return departementMembre; }
    public void setDepartementMembre(String s)             { this.departementMembre = s; }

    public String getFiliereMembre()                       { return filiereMembre; }
    public void setFiliereMembre(String s)                 { this.filiereMembre = s; }
}
