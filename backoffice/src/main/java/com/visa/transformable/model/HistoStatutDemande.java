package com.visa.transformable.model;

import jakarta.persistence.*;

@Entity
@Table(name = "histo_statut_demande")
public class HistoStatutDemande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_demande")
    private Demande demande;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_statut_demande")
    private StatutDemande statutDemande;

    @Column(name = "date_changement")
    private java.sql.Timestamp dateChangement;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Demande getDemande() { return demande; }
    public void setDemande(Demande demande) { this.demande = demande; }

    public StatutDemande getStatutDemande() { return statutDemande; }
    public void setStatutDemande(StatutDemande statutDemande) { this.statutDemande = statutDemande; }

    public java.sql.Timestamp getDateChangement() { return dateChangement; }
    public void setDateChangement(java.sql.Timestamp dateChangement) { this.dateChangement = dateChangement; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
