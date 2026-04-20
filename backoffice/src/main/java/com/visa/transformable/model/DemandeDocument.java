package com.visa.transformable.model;

import jakarta.persistence.*;

@Entity
@Table(name = "demande_document")
public class DemandeDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_demande")
    private Demande demande;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_document")
    private Document document;

    @Column(nullable = false)
    private Boolean fourni = false;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Demande getDemande() { return demande; }
    public void setDemande(Demande demande) { this.demande = demande; }

    public Document getDocument() { return document; }
    public void setDocument(Document document) { this.document = document; }

    public Boolean getFourni() { return fourni; }
    public void setFourni(Boolean fourni) { this.fourni = fourni; }
}
