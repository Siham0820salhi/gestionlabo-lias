package com.lias.gestionlabo.models;

import jakarta.persistence.*;

@Entity
@Table(name = "partenaires")
public class Partenaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String type; // Université, Entreprise, Organisme, etc.
    private String pays;
    private String email;
    private String siteWeb;

    @Column(columnDefinition = "TEXT")
    private String description;

    public Partenaire() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSiteWeb() { return siteWeb; }
    public void setSiteWeb(String siteWeb) { this.siteWeb = siteWeb; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
