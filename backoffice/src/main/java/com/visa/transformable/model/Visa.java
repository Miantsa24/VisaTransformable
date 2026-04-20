package com.visa.transformable.model;

import jakarta.persistence.*;

@Entity
@Table(name = "visa")
public class Visa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference", nullable = false, unique = true, length = 100)
    private String reference;

    @Column(name = "date_debut")
    private java.sql.Date dateDebut;

    @Column(name = "date_fin")
    private java.sql.Date dateFin;

    @ManyToOne
    @JoinColumn(name = "id_type_visa")
    private TypeVisa typeVisa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_passeport")
    private Passeport passeport;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public java.sql.Date getDateDebut() { return dateDebut; }
    public void setDateDebut(java.sql.Date dateDebut) { this.dateDebut = dateDebut; }

    public java.sql.Date getDateFin() { return dateFin; }
    public void setDateFin(java.sql.Date dateFin) { this.dateFin = dateFin; }

    public TypeVisa getTypeVisa() { return typeVisa; }
    public void setTypeVisa(TypeVisa typeVisa) { this.typeVisa = typeVisa; }

    public Passeport getPasseport() { return passeport; }
    public void setPasseport(Passeport passeport) { this.passeport = passeport; }
}
