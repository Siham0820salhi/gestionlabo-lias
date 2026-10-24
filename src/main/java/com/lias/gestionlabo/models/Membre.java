package com.lias.gestionlabo.models;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "membres")
public class Membre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMembre;
    
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;
 // Zidi had l-champs f Membre.java
    @Temporal(TemporalType.DATE)
    private Date dateEmbauche;

    @Temporal(TemporalType.DATE)
    private Date dateAffiliation; // Hadia hiya "Clé métier"

    private String nom;
    private String prenom;
    
    @Column(unique = true)
    private String email;
    
    private String password;

    @Enumerated(EnumType.STRING)
    private StatutMembre statutActuel; // PERMANENT, ASSOCIE, DOCTORANT, ANCIEN, RETRAITE

    private String etablissementOrigine;
    private String laboratoireOrigine;
    
 // Zidi had l-champs f l-class Membre bach i-t-stok-aw l-info d-l-diplôme
    private String dernierDiplome;
    private String etablissement;
    
    // 🌟 Les champs ajoutés pour le profil complet (Cahier de charges Partie 3)
    @Column(name = "photo") // Hada bach t-3ref DB anna had l-colonne smitha photo
    private String photo;
    
    private String role;
    @Column(columnDefinition = "TEXT")
    private String biographie;
    
    private String centresDinteret; // Ex: IA, Big Data, DevOps
    
    private String equipeActuelle; // Nom de l'équipe (Ex: Équipe Systèmes Intelligents)

    // 🔄 MODIFICATION ICI : Remplacement de "auteurPrincipal" par "membre" pour matcher votre classe Publication
    @OneToMany(mappedBy = "membre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Publication> publications;

    // Constructeurs
    public Membre() {}

    // 🌟 METHODE UTITLITAIRE : Pour afficher la vraie photo dans Thymeleaf sans bug de chemin
    @Transient
    public String getTransientPhotoImagePath() {
        if (photo == null || photo.isEmpty() || photo.equals("default-avatar.png")) {
            return "/images/default-avatar.png"; // Image par défaut si le membre n'a pas mis de photo
        }
        return "/images/" + photo; // Chemin vers l'image uploadée dans static/images/
    }

    // Getters et Setters
    public Long getIdMembre() { return idMembre; }
    public void setIdMembre(Long idMembre) { this.idMembre = idMembre; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public StatutMembre getStatutActuel() { return statutActuel; }
    public void setStatutActuel(StatutMembre statutActuel) { this.statutActuel = statutActuel; }

    public String getEtablissementOrigine() { return etablissementOrigine; }
    public void setEtablissementOrigine(String etablissementOrigine) { this.etablissementOrigine = etablissementOrigine; }

    public String getLaboratoireOrigine() { return laboratoireOrigine; }
    public void setLaboratoireOrigine(String laboratoireOrigine) { this.laboratoireOrigine = laboratoireOrigine; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public String getBiographie() { return biographie; }
    public void setBiographie(String biographie) { this.biographie = biographie; }

    public String getCentresDinteret() { return centresDinteret; }
    public void setCentresDinteret(String centresDinteret) { this.centresDinteret = centresDinteret; }

    public String getEquipeActuelle() { return equipeActuelle; }
    public void setEquipeActuelle(String equipeActuelle) { this.equipeActuelle = equipeActuelle; }

    public Date getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(Date dateNaissance) { this.dateNaissance = dateNaissance; }
    
 // Zidi l-Getters & Setters dyalhom
    public Date getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(Date dateEmbauche) { this.dateEmbauche = dateEmbauche; }

    public Date getDateAffiliation() { return dateAffiliation; }
    public void setDateAffiliation(Date dateAffiliation) { this.dateAffiliation = dateAffiliation; }

    public List<Publication> getPublications() { return publications; }
    public void setPublications(List<Publication> publications) { this.publications = publications; }
    
    public String getDernierDiplome() { return dernierDiplome; }
    public void setDernierDiplome(String dernierDiplome) { this.dernierDiplome = dernierDiplome; }

    public String getEtablissement() { return etablissement; }
    public void setEtablissement(String etablissement) { this.etablissement = etablissement; }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

	
}
