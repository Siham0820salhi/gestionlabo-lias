package com.lias.gestionlabo.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "destinataire_id", nullable = false)
    private Membre destinataire;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    private String lien; // URL de redirection optionnelle

    private boolean lue = false;

    private LocalDateTime dateCreation = LocalDateTime.now();

    public Notification() {}

    public Notification(Membre destinataire, String message, String lien) {
        this.destinataire = destinataire;
        this.message = message;
        this.lien = lien;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Membre getDestinataire() { return destinataire; }
    public void setDestinataire(Membre destinataire) { this.destinataire = destinataire; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getLien() { return lien; }
    public void setLien(String lien) { this.lien = lien; }

    public boolean isLue() { return lue; }
    public void setLue(boolean lue) { this.lue = lue; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}
