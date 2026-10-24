package com.lias.gestionlabo.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Enumerated(EnumType.STRING)
    private TypeDocument type;

    private String cheminFichier;

    private LocalDateTime dateUpload = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "evenement_id")
    private Evenement evenement;

    @ManyToOne
    @JoinColumn(name = "uploader_id")
    private Membre uploader;

    @Column(columnDefinition = "TEXT")
    private String description;

    public Document() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public TypeDocument getType() { return type; }
    public void setType(TypeDocument type) { this.type = type; }

    public String getCheminFichier() { return cheminFichier; }
    public void setCheminFichier(String cheminFichier) { this.cheminFichier = cheminFichier; }

    public LocalDateTime getDateUpload() { return dateUpload; }
    public void setDateUpload(LocalDateTime dateUpload) { this.dateUpload = dateUpload; }

    public Evenement getEvenement() { return evenement; }
    public void setEvenement(Evenement evenement) { this.evenement = evenement; }

    public Membre getUploader() { return uploader; }
    public void setUploader(Membre uploader) { this.uploader = uploader; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
