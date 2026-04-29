package com.visa.transformable.dto;

import java.sql.Date;
import java.util.List;

public class DuplicataDTO {
    private String typePerte;
    private Long idDemandeur;
    private Long idVisa;
    private Long idVisaOrigine;
    private Long idCarteResident;
    private String numeroNouveauPasseport;
    private Long idTypeDemande;
    private Date dateDemande;
    private String observations;
    private List<Long> documentsCoches;
    private String typeVisa;
    private Date dateDelivranceNouveauPasseport;
private Date dateExpirationNouveauPasseport;
private String paysDelivranceNouveauPasseport;

    private String nom;
    private String prenoms;
    private Date dateNaissance;
    private String lieuNaissance;
    private String telephone;
    private String email;
    private String adresse;
    private Long idSituationFamiliale;
    private Long idNationalite;

    private String numeroPasseport;
    private Date dateDelivrancePasseport;
    private Date dateExpirationPasseport;
    private String paysDelivrancePasseport;

    private String referenceVisa;
    private Date dateDebutVisa;
    private Date dateFinVisa;


    public Date getDateDelivranceNouveauPasseport() {
    return dateDelivranceNouveauPasseport;
}

public void setDateDelivranceNouveauPasseport(Date dateDelivranceNouveauPasseport) {
    this.dateDelivranceNouveauPasseport = dateDelivranceNouveauPasseport;
}

public Date getDateExpirationNouveauPasseport() {
    return dateExpirationNouveauPasseport;
}

public void setDateExpirationNouveauPasseport(Date dateExpirationNouveauPasseport) {
    this.dateExpirationNouveauPasseport = dateExpirationNouveauPasseport;
}

public String getPaysDelivranceNouveauPasseport() {
    return paysDelivranceNouveauPasseport;
}

public void setPaysDelivranceNouveauPasseport(String paysDelivranceNouveauPasseport) {
    this.paysDelivranceNouveauPasseport = paysDelivranceNouveauPasseport;
}
    public String getTypePerte() { return typePerte; }
    public void setTypePerte(String typePerte) { this.typePerte = typePerte; }

    public Long getIdDemandeur() { return idDemandeur; }
    public void setIdDemandeur(Long idDemandeur) { this.idDemandeur = idDemandeur; }

    public Long getIdVisa() { return idVisa; }
    public void setIdVisa(Long idVisa) { this.idVisa = idVisa; }

    public Long getIdVisaOrigine() { return idVisaOrigine; }
    public void setIdVisaOrigine(Long idVisaOrigine) { this.idVisaOrigine = idVisaOrigine; }

    public Long getIdCarteResident() { return idCarteResident; }
    public void setIdCarteResident(Long idCarteResident) { this.idCarteResident = idCarteResident; }

    public String getNumeroNouveauPasseport() { return numeroNouveauPasseport; }
    public void setNumeroNouveauPasseport(String numeroNouveauPasseport) { this.numeroNouveauPasseport = numeroNouveauPasseport; }

    public Long getIdTypeDemande() { return idTypeDemande; }
    public void setIdTypeDemande(Long idTypeDemande) { this.idTypeDemande = idTypeDemande; }

    public Date getDateDemande() { return dateDemande; }
    public void setDateDemande(Date dateDemande) { this.dateDemande = dateDemande; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }

    public List<Long> getDocumentsCoches() { return documentsCoches; }
    public void setDocumentsCoches(List<Long> documentsCoches) { this.documentsCoches = documentsCoches; }

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

    public Long getIdSituationFamiliale() { return idSituationFamiliale; }
    public void setIdSituationFamiliale(Long idSituationFamiliale) { this.idSituationFamiliale = idSituationFamiliale; }

    public Long getIdNationalite() { return idNationalite; }
    public void setIdNationalite(Long idNationalite) { this.idNationalite = idNationalite; }

    public String getNumeroPasseport() { return numeroPasseport; }
    public void setNumeroPasseport(String numeroPasseport) { this.numeroPasseport = numeroPasseport; }

    public Date getDateDelivrancePasseport() { return dateDelivrancePasseport; }
    public void setDateDelivrancePasseport(Date dateDelivrancePasseport) { this.dateDelivrancePasseport = dateDelivrancePasseport; }

    public Date getDateExpirationPasseport() { return dateExpirationPasseport; }
    public void setDateExpirationPasseport(Date dateExpirationPasseport) { this.dateExpirationPasseport = dateExpirationPasseport; }

    public String getPaysDelivrancePasseport() { return paysDelivrancePasseport; }
    public void setPaysDelivrancePasseport(String paysDelivrancePasseport) { this.paysDelivrancePasseport = paysDelivrancePasseport; }

    public String getReferenceVisa() { return referenceVisa; }
    public void setReferenceVisa(String referenceVisa) { this.referenceVisa = referenceVisa; }

    public Date getDateDebutVisa() { return dateDebutVisa; }
    public void setDateDebutVisa(Date dateDebutVisa) { this.dateDebutVisa = dateDebutVisa; }

    public Date getDateFinVisa() { return dateFinVisa; }
    public void setDateFinVisa(Date dateFinVisa) { this.dateFinVisa = dateFinVisa; }

    public String getTypeVisa() { return typeVisa; }
    public void setTypeVisa(String typeVisa) { this.typeVisa = typeVisa; }
}
