package com.lias.gestionlabo.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "materiel")
public class Materiel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String reference;   // numéro de série / code interne

    private LocalDate dateAcquisition;

    // Getters / Setters
    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }

    public String getNom()                     { return nom; }
    public void setNom(String nom)             { this.nom = nom; }

    public String getDescription()             { return description; }
    public void setDescription(String desc)    { this.description = desc; }

    public String getReference()               { return reference; }
    public void setReference(String ref)       { this.reference = ref; }

    public LocalDate getDateAcquisition()                  { return dateAcquisition; }
    public void setDateAcquisition(LocalDate d)            { this.dateAcquisition = d; }
}
