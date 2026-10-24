package com.lias.gestionlabo.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "laboratoire")
public class Laboratoire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Temporal(TemporalType.DATE)
    private Date dateCreation; // Hadia hiya l-date li bghiti

    // Constructeurs
    public Laboratoire() {}

    public Laboratoire(String nom, Date dateCreation) {
        this.nom = nom;
        this.dateCreation = dateCreation;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Date getDateCreation() { return dateCreation; }
    public void setDateCreation(Date dateCreation) { this.dateCreation = dateCreation; }
}