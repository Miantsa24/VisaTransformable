package com.visa.transformable.dto;

import java.sql.Date;
import java.util.List;

public class DemandeDTO {
    // Données du formulaire
    private Long idDemandeur;
    private Long idVisa;
    private Long idTypeDemande;
    private Date dateDemande;
    private String observations;
    private Date dateModification;
    private String typeVisa;
    // Liste des documents cochés (IDs)
    private List<Long> documentsCoches;

    public DemandeDTO() {}

    public Long getIdDemandeur() { return idDemandeur; }
    public void setIdDemandeur(Long idDemandeur) { this.idDemandeur = idDemandeur; }

    public Long getIdVisa() { return idVisa; }
    public void setIdVisa(Long idVisa) { this.idVisa = idVisa; }

    public Long getIdTypeDemande() { return idTypeDemande; }
    public void setIdTypeDemande(Long idTypeDemande) { this.idTypeDemande = idTypeDemande; }

    public Date getDateDemande() { return dateDemande; }
    public void setDateDemande(Date dateDemande) { this.dateDemande = dateDemande; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }

    public List<Long> getDocumentsCoches() { return documentsCoches; }
    public void setDocumentsCoches(List<Long> documentsCoches) { this.documentsCoches = documentsCoches; }

    public Date getDateModification() { return dateModification; }
    public void setDateModification(Date dateModification) { this.dateModification = dateModification; }

    public String getTypeVisa() { return typeVisa; }
    public void setTypeVisa(String typeVisa) { this.typeVisa = typeVisa; }
}