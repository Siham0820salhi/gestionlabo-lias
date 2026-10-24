package com.lias.gestionlabo.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reunions")
public class Reunion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private LocalDateTime dateReunion;
    private String lieu;

    @Column(columnDefinition = "TEXT")
    private String ordreJour;

    private String pvPath; // Chemin vers le fichier PV uploadé

    @ManyToOne
    @JoinColumn(name = "organisateur_id")
    private Membre organisateur;

    public Reunion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public LocalDateTime getDateReunion() { return dateReunion; }
    public void setDateReunion(LocalDateTime dateReunion) { this.dateReunion = dateReunion; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public String getOrdreJour() { return ordreJour; }
    public void setOrdreJour(String ordreJour) { this.ordreJour = ordreJour; }

    public String getPvPath() { return pvPath; }
    public void setPvPath(String pvPath) { this.pvPath = pvPath; }

    public Membre getOrganisateur() { return organisateur; }
    public void setOrganisateur(Membre organisateur) { this.organisateur = organisateur; }
}
