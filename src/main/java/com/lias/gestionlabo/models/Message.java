package com.lias.gestionlabo.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "expediteur_id", nullable = false)
    private Membre expediteur;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenu;

    // Optionnel : message lié à un événement
    @ManyToOne
    @JoinColumn(name = "evenement_id")
    private Evenement evenement;

    // Optionnel : message d'équipe (nom de l'équipe)
    private String equipe;

    private LocalDateTime dateEnvoi = LocalDateTime.now();

    public Message() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Membre getExpediteur() { return expediteur; }
    public void setExpediteur(Membre expediteur) { this.expediteur = expediteur; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public Evenement getEvenement() { return evenement; }
    public void setEvenement(Evenement evenement) { this.evenement = evenement; }

    public String getEquipe() { return equipe; }
    public void setEquipe(String equipe) { this.equipe = equipe; }

    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }
}
