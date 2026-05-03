package com.visa.transformable.model;

import jakarta.persistence.*;

@Entity
@Table(name = "historique_modification")
public class HistoriqueModification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_demande")
    private Demande demande;

    @Column(name = "section")
    private String section; // Etat civil, Passeport, Visa, Documents

    @Column(name = "champ")
    private String champ; // nom, prenoms, document, etc.

    @Column(name = "ancienne_valeur", columnDefinition = "TEXT")
    private String ancienneValeur;

    @Column(name = "nouvelle_valeur", columnDefinition = "TEXT")
    private String nouvelleValeur;

    @Column(name = "date_modification")
    private java.sql.Timestamp dateModification;

    // ========================
    // GETTERS / SETTERS
    // ========================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Demande getDemande() { return demande; }
    public void setDemande(Demande demande) { this.demande = demande; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public String getChamp() { return champ; }
    public void setChamp(String champ) { this.champ = champ; }

    public String getAncienneValeur() { return ancienneValeur; }
    public void setAncienneValeur(String ancienneValeur) { this.ancienneValeur = ancienneValeur; }

    public String getNouvelleValeur() { return nouvelleValeur; }
    public void setNouvelleValeur(String nouvelleValeur) { this.nouvelleValeur = nouvelleValeur; }

    public java.sql.Timestamp getDateModification() { return dateModification; }
    public void setDateModification(java.sql.Timestamp dateModification) { this.dateModification = dateModification; }
}