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
    //Donnees du demandeur
    private String nom;
    private String prenoms;
    private Date dateNaissance;
    private String lieuNaissance;
    private String telephone;
    private String email;
    private String adresse;
    private Long idNationalite;
    private Long idSituationFamiliale;
    //Donnees du passeport
    private String NumeroPasseport;
    private Date dateDelivrance;
    private Date dateExpiration;
    private String paysDelivrance;
    //Donnees du visa
    private String referenceVisa;
    private Date dateDebutVisa;
    private Date dateFinVisa;



    public DemandeDTO() {}

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }  

    public String getPrenoms() { return prenoms; }
    public void setPrenoms(String prenoms) { this.prenoms = prenoms; }

    public Date getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(Date dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getLieuNaissance() { return lieuNaissance; }
    public void setLieuNaissance(String lieuNaissance) { this.lieuNaissance = lieuNaissance; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

        public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public Long getIdNationalite() { return idNationalite; }
    public void setIdNationalite(Long idNationalite) { this.idNationalite = idNationalite; }

    public Long getIdSituationFamiliale() { return idSituationFamiliale; }
    public void setIdSituationFamiliale(Long idSituationFamiliale) { this.idSituationFamiliale = idSituationFamiliale; }

    public String getNumeroPasseport() { return NumeroPasseport; }
    public void setNumeroPasseport(String numeroPasseport) { NumeroPasseport = numeroPasseport; }
    public Date getDateDelivrancePasseport() { return dateDelivrance; }
    public void setDateDelivrancePasseport(Date dateDelivrance) { this.dateDelivrance = dateDelivrance; }
    public Date getDateExpirationPasseport() { return dateExpiration; }
    public void setDateExpirationPasseport(Date dateExpiration) { this.dateExpiration = dateExpiration; }
    public String getPaysDelivrancePasseport() { return paysDelivrance; }
    public void setPaysDelivrancePasseport(String paysDelivrance) { this.paysDelivrance = paysDelivrance; }

    public String getReferenceVisa() { return referenceVisa; }
    public void setReferenceVisa(String referenceVisa) { this.referenceVisa = referenceVisa; }  
    public Date getDateDebutVisa() { return dateDebutVisa; }
    public void setDateDebutVisa(Date dateDebutVisa) { this.dateDebutVisa = dateDebutVisa; }
    public Date getDateFinVisa() { return dateFinVisa; }
    public void setDateFinVisa(Date dateFinVisa) { this.dateFinVisa = dateFinVisa; }

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