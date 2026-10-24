package com.lias.gestionlabo.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "demandes_adhesion")
public class DemandeAdhesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDemande;

    private String nom;
    private String prenom;
    private String email;
    private LocalDate dateNaissance;
    private String dernierDiplome;
    private String etablissement;
    
    @Lob @Column(columnDefinition = "TEXT")
    private String motivation;
    
    @Lob @Column(columnDefinition = "TEXT")
    private String lettreMotivation;
    
    private String cvPath; // Hna ghandiro smit l-fichier PDF (b7al "siham_cv.pdf")

    @Enumerated(EnumType.STRING)
    private StatutDemande statut = StatutDemande.EN_ATTENTE;

    private LocalDateTime dateDemande = LocalDateTime.now();

    // Getters et Setters
    public Long getIdDemande() { return idDemande; }
    public void setIdDemande(Long idDemande) { this.idDemande = idDemande; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }
    public String getDernierDiplome() { return dernierDiplome; }
    public void setDernierDiplome(String dernierDiplome) { this.dernierDiplome = dernierDiplome; }
    public String getEtablissement() { return etablissement; }
    public void setEtablissement(String etablissement) { this.etablissement = etablissement; }
    public String getMotivation() { return motivation; }
    public void setMotivation(String motivation) { this.motivation = motivation; }
    public String getLettreMotivation() { return lettreMotivation; }
    public void setLettreMotivation(String lettreMotivation) { this.lettreMotivation = lettreMotivation; }
    public String getCvPath() { return cvPath; }
    public void setCvPath(String cvPath) { this.cvPath = cvPath; }
    public StatutDemande getStatut() { return statut; }
    public void setStatut(StatutDemande statut) { this.statut = statut; }
    public LocalDateTime getDateDemande() { return dateDemande; }
    public void setDateDemande(LocalDateTime dateDemande) { this.dateDemande = dateDemande; }
}