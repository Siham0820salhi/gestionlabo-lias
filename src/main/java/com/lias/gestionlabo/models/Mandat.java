package com.lias.gestionlabo.models;

import jakarta.persistence.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "mandats")
public class Mandat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMandat;

    private String role; // Role (DIRECTEUR, etc)

    @ManyToOne
    @JoinColumn(name = "id_membre", nullable = false)
    @JsonIgnore
    private Membre responsable;

    private Date dateDebut;
    private Date dateFin;
    private boolean estActif;

    public Mandat() {}

    // Getters and Setters
    public Long getIdMandat() { return idMandat; }
    public void setIdMandat(Long idMandat) { this.idMandat = idMandat; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Membre getResponsable() { return responsable; }
    public void setResponsable(Membre responsable) { this.responsable = responsable; }
    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
    public boolean isEstActif() { return estActif; }
    public void setEstActif(boolean estActif) { this.estActif = estActif; }
}