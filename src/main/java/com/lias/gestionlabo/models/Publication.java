package com.lias.gestionlabo.models;

import jakarta.persistence.*;

@Entity
@Table(name = "publications")
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPublication;

    private String titre;
    private String auteurs;
    private int annee;
    private String journalOuConference;

    private String equipe;

    private String lien;        // URL externe de la publication
    private String fichierPdf;  // nom du fichier PDF uploadé (dans uploads/publications/)

    @ManyToOne
    @JoinColumn(name = "id_membre")
    private Membre membre;

    // Getters et Setters
    public Long getIdPublication() { return idPublication; }
    public void setIdPublication(Long idPublication) { this.idPublication = idPublication; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getAuteurs() { return auteurs; }
    public void setAuteurs(String auteurs) { this.auteurs = auteurs; }
    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }
    public String getJournalOuConference() { return journalOuConference; }
    public void setJournalOuConference(String j) { this.journalOuConference = j; }
    public String getEquipe() { return equipe; }
    public void setEquipe(String equipe) { this.equipe = equipe; }
    public String getLien() { return lien; }
    public void setLien(String lien) { this.lien = lien; }
    public String getFichierPdf() { return fichierPdf; }
    public void setFichierPdf(String fichierPdf) { this.fichierPdf = fichierPdf; }
    public Membre getMembre() { return membre; }
    public void setMembre(Membre membre) { this.membre = membre; }
}