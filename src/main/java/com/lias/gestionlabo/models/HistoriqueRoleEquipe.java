package com.lias.gestionlabo.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "historique_roles_equipes")
public class HistoriqueRoleEquipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistoriqueRole;

    @ManyToOne
    @JoinColumn(name = "id_membre", nullable = false)
    private Membre membre;

    @Enumerated(EnumType.STRING)
    private RoleMembre role;

    private Date dateDebut;
    private Date dateFin;

    public HistoriqueRoleEquipe() {}

    // Getters and Setters
    public Long getIdHistoriqueRole() { return idHistoriqueRole; }
    public void setIdHistoriqueRole(Long idHistoriqueRole) { this.idHistoriqueRole = idHistoriqueRole; }
    public Membre getMembre() { return membre; }
    public void setMembre(Membre membre) { this.membre = membre; }
    public RoleMembre getRole() { return role; }
    public void setRole(RoleMembre role) { this.role = role; }
    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
}