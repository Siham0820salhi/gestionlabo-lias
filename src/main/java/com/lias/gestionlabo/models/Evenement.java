package com.lias.gestionlabo.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "evenements")
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @Enumerated(EnumType.STRING)
    private TypeEvenement type; // CONFERENCE, SEMINAIRE, WORKSHOP

    private LocalDate date;
    private String lieu;
    private int edition;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Organisateur — membre li khla9 l événement
    @ManyToOne
    @JoinColumn(name = "organisateur_id")
    private Membre organisateur;

    public Evenement() {}

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public TypeEvenement getType() { return type; }
    public void setType(TypeEvenement type) { this.type = type; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public int getEdition() { return edition; }
    public void setEdition(int edition) { this.edition = edition; }

    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description; 
    }

    public Membre getOrganisateur() { return organisateur; }
    public void setOrganisateur(Membre organisateur) { 
        this.organisateur = organisateur; 
    }
}