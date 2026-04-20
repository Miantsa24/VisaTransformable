package com.visa.transformable.model;

import jakarta.persistence.*;

@Entity
@Table(name = "demande")
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_demande", nullable = false)
    private java.sql.Date dateDemande;

    @ManyToOne
    @JoinColumn(name = "id_statut_demande")
    private StatutDemande statutDemande;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_demandeur")
    private Demandeur demandeur;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_visa")
    private Visa visa;

    @ManyToOne
    @JoinColumn(name = "id_type_demande")
    private TypeDemande typeDemande;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @Column(name = "date_traitement")
    private java.sql.Date dateTraitement;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public java.sql.Date getDateDemande() { return dateDemande; }
    public void setDateDemande(java.sql.Date dateDemande) { this.dateDemande = dateDemande; }

    public StatutDemande getStatutDemande() { return statutDemande; }
    public void setStatutDemande(StatutDemande statutDemande) { this.statutDemande = statutDemande; }

    public Demandeur getDemandeur() { return demandeur; }
    public void setDemandeur(Demandeur demandeur) { this.demandeur = demandeur; }

    public Visa getVisa() { return visa; }
    public void setVisa(Visa visa) { this.visa = visa; }

    public TypeDemande getTypeDemande() { return typeDemande; }
    public void setTypeDemande(TypeDemande typeDemande) { this.typeDemande = typeDemande; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }

    public java.sql.Date getDateTraitement() { return dateTraitement; }
    public void setDateTraitement(java.sql.Date dateTraitement) { this.dateTraitement = dateTraitement; }
}
