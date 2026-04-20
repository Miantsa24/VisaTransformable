package com.visa.transformable.model;

import jakarta.persistence.*;

@Entity
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String libelle;

    @Column(nullable = false)
    private Boolean obligatoire;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_cible", nullable = false)
    private TypeCible typeCible;

    public enum TypeCible {
        commun, investisseur, travailleur
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public Boolean getObligatoire() { return obligatoire; }
    public void setObligatoire(Boolean obligatoire) { this.obligatoire = obligatoire; }

    public TypeCible getTypeCible() { return typeCible; }
    public void setTypeCible(TypeCible typeCible) { this.typeCible = typeCible; }
}
