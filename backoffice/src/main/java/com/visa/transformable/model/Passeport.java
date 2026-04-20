package com.visa.transformable.model;

import jakarta.persistence.*;

@Entity
@Table(name = "passeport")
public class Passeport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_demandeur")
    private Demandeur demandeur;

    @Column(name = "numero_passeport", nullable = false, unique = true, length = 50)
    private String numeroPasseport;

    @Column(name = "date_delivrance")
    private java.sql.Date dateDelivrance;

    @Column(name = "date_expiration")
    private java.sql.Date dateExpiration;

    @Column(name = "pays_delivrance", length = 100)
    private String paysDelivrance;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Demandeur getDemandeur() { return demandeur; }
    public void setDemandeur(Demandeur demandeur) { this.demandeur = demandeur; }

    public String getNumeroPasseport() { return numeroPasseport; }
    public void setNumeroPasseport(String numeroPasseport) { this.numeroPasseport = numeroPasseport; }

    public java.sql.Date getDateDelivrance() { return dateDelivrance; }
    public void setDateDelivrance(java.sql.Date dateDelivrance) { this.dateDelivrance = dateDelivrance; }

    public java.sql.Date getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(java.sql.Date dateExpiration) { this.dateExpiration = dateExpiration; }

    public String getPaysDelivrance() { return paysDelivrance; }
    public void setPaysDelivrance(String paysDelivrance) { this.paysDelivrance = paysDelivrance; }
}
