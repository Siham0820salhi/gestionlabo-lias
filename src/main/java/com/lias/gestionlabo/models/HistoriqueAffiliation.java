package com.lias.gestionlabo.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class HistoriqueAffiliation {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "membre_id")
    private Membre membre;

    private LocalDate dateDebut; // B-d-l-n-aha l-LocalDate
    private LocalDate dateFin;   // B-d-l-n-aha l-LocalDate

    private String statut; 

    public HistoriqueAffiliation() {}

    public HistoriqueAffiliation(Membre membre, String statut, LocalDate dateDebut) {
        this.membre = membre;
        this.statut = statut;
        this.dateDebut = dateDebut;
    }

    // Getters & Setters m-q-a-d-i-n
    public Long getId() { return id; }
    public Membre getMembre() { return membre; }
    public void setMembre(Membre membre) { this.membre = membre; }
    
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}