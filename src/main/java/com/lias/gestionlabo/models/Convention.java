package com.lias.gestionlabo.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "conventions")
public class Convention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @ManyToOne
    @JoinColumn(name = "partenaire_id")
    private Partenaire partenaire;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String documentPath;

    private boolean active = true;

    public Convention() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public Partenaire getPartenaire() { return partenaire; }
    public void setPartenaire(Partenaire partenaire) { this.partenaire = partenaire; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDocumentPath() { return documentPath; }
    public void setDocumentPath(String documentPath) { this.documentPath = documentPath; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
