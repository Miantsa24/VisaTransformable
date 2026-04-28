package com.visa.transformable.model;

import jakarta.persistence.*;

@Entity
@Table(name = "carte_resident")
public class CarteResident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_demandeur")
    private Demandeur demandeur;

    @Column(name = "numero_carte_resident", nullable = false, unique = true, length = 50)
    private String numeroCarteResident;

    @Column(name = "date_delivrance")
    private java.sql.Date dateDelivrance;

    @Column(name = "date_expiration")
    private java.sql.Date dateExpiration;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Demandeur getDemandeur() { return demandeur; }
    public void setDemandeur(Demandeur demandeur) { this.demandeur = demandeur; }

    public String getNumeroCarteResident() { return numeroCarteResident; }
    public void setNumeroCarteResident(String numeroCarteResident) { this.numeroCarteResident = numeroCarteResident; }

    public java.sql.Date getDateDelivrance() { return dateDelivrance; }
    public void setDateDelivrance(java.sql.Date dateDelivrance) { this.dateDelivrance = dateDelivrance; }

    public java.sql.Date getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(java.sql.Date dateExpiration) { this.dateExpiration = dateExpiration; }
}
