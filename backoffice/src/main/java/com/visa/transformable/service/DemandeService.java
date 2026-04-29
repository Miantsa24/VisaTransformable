package com.visa.transformable.service;

import com.visa.transformable.dto.DemandeDTO;
import com.visa.transformable.dto.DuplicataDTO;
import com.visa.transformable.model.*;
import com.visa.transformable.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class DemandeService {
    @Autowired
    private DemandeurRepository demandeurRepository;
    @Autowired
    private SituationFamilialeRepository situationFamilialeRepository;
    @Autowired
    private NationaliteRepository nationaliteRepository;
    @Autowired
    private PasseportRepository passeportRepository;
    @Autowired
    private VisaRepository visaRepository;
    @Autowired
    private DemandeRepository demandeRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private DemandeDocumentRepository demandeDocumentRepository;
    @Autowired
    private StatutDemandeRepository statutDemandeRepository;
    @Autowired
    private TypeDemandeRepository typeDemandeRepository;
    @Autowired
    private TypeVisaRepository typeVisaRepository;
    @Autowired
    private CarteResidentRepository carteResidentRepository;
    @Autowired
    private HistoStatutDemandeRepository histoStatutDemandeRepository;

    @Transactional
    public void createDemande(DemandeDTO dto) {
        if (dto.getIdDemandeur() == null) {
            throw new IllegalArgumentException("Demandeur introuvable");
        }
        if (dto.getIdVisa() == null) {
            throw new IllegalArgumentException("Visa introuvable");
        }

        List<Long> documentsCoches = dto.getDocumentsCoches() == null ? List.of() : dto.getDocumentsCoches();

        // 1. Vérifier visa non expiré
        Visa visa = visaRepository.findById(dto.getIdVisa())
                .orElseThrow(() -> new IllegalArgumentException("Visa introuvable"));
        if (visa.getDateFin() != null && visa.getDateFin().before(Date.valueOf(LocalDate.now()))) {
            throw new IllegalArgumentException("Le visa est expiré");
        }

        String typeVisaLibelle = visa.getTypeVisa() != null ? visa.getTypeVisa().getLibelle() : null;
        if (typeVisaLibelle == null || typeVisaLibelle.isBlank()) {
            throw new IllegalArgumentException("Type de visa introuvable");
        }

        // 2. Vérifier documents obligatoires
        List<Document> docsObligatoires = new java.util.ArrayList<>(
                documentRepository.findByObligatoireTrueAndTypeCible(Document.TypeCible.commun)
        );
        if ("investisseur".equalsIgnoreCase(typeVisaLibelle)) {
            docsObligatoires.addAll(documentRepository.findByObligatoireTrueAndTypeCible(Document.TypeCible.investisseur));
        } else if ("travailleur".equalsIgnoreCase(typeVisaLibelle)) {
            docsObligatoires.addAll(documentRepository.findByObligatoireTrueAndTypeCible(Document.TypeCible.travailleur));
        }
        List<Long> idsObligatoires = docsObligatoires.stream().map(Document::getId).toList();
        if (!documentsCoches.containsAll(idsObligatoires)) {
            throw new IllegalArgumentException("Tous les documents obligatoires doivent être cochés");
        }

        // 3. Créer demandeur, passeport, visa, demande
        Demandeur demandeur = demandeurRepository.findById(dto.getIdDemandeur())
                .orElseThrow(() -> new IllegalArgumentException("Demandeur introuvable"));
        Passeport passeport = visa.getPasseport();
        if (passeport == null) {
            throw new IllegalArgumentException("Passeport introuvable");
        }

        Demande demande = new Demande();
        demande.setDemandeur(demandeur);
        demande.setVisa(visa);
        if (dto.getIdTypeDemande() != null) {
    TypeDemande typeDemande = typeDemandeRepository.findById(dto.getIdTypeDemande())
        .orElseThrow(() -> new IllegalArgumentException("TypeDemande introuvable"));
    demande.setTypeDemande(typeDemande);
} else {
    demande.setTypeDemande(null);
}
        demande.setDateDemande(dto.getDateDemande());
        demande.setObservations(dto.getObservations());
        // 4. Affecter statut demande_creee
        StatutDemande statutCree = statutDemandeRepository.findByLibelle("demande_creee")
                .orElseThrow(() -> new IllegalArgumentException("Statut 'demande_creee' introuvable"));
        demande.setStatutDemande(statutCree);
        demande = demandeRepository.save(demande);

        // Créer les DemandeDocument
        for (Long docId : documentsCoches.stream().distinct().toList()) {
            Document doc = documentRepository.findById(docId)
                    .orElseThrow(() -> new IllegalArgumentException("Document introuvable"));
            DemandeDocument dd = new DemandeDocument();
            dd.setDemande(demande);
            dd.setDocument(doc);
            dd.setFourni(true);
            demandeDocumentRepository.save(dd);
        }
    }

    @Transactional
    public Demande createDemandeSprint2(DuplicataDTO dto) {
        // Cas 1 : Personne EXISTANTE - idDemandeur MUST NOT BE NULL
        if (dto.getIdDemandeur() == null) {
            throw new IllegalArgumentException("Demandeur introuvable - createDemandeSprint2 est réservé aux personnes existantes. Utilisez createTwoDemandes pour une personne inconnue.");
        }

        if (dto.getTypePerte() == null || dto.getTypePerte().isBlank()) {
            throw new IllegalArgumentException("Type de perte obligatoire");
        }

        Demande.TypePerte typePerte;
        try {
            typePerte = Demande.TypePerte.valueOf(dto.getTypePerte());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Type de perte invalide");
        }

        Demandeur demandeur = demandeurRepository.findById(dto.getIdDemandeur())
                .orElseThrow(() -> new IllegalArgumentException("Demandeur introuvable"));
        Passeport basePasseport = resolveBasePasseport(demandeur, dto);
        Visa baseVisa = resolveBaseVisa(basePasseport, dto);
        TypeDemande typeDemande = resolveTypeDemandeSprint2(typePerte);
        StatutDemande demandeCreee = statutDemandeRepository.findByLibelle("demande_creee")
                .orElseThrow(() -> new IllegalArgumentException("Statut 'demande_creee' introuvable"));

        Demande demande = new Demande();
        demande.setDemandeur(demandeur);
        demande.setVisa(baseVisa);
        if (dto.getIdVisaOrigine() != null) {
            demande.setVisaOrigine(visaRepository.findById(dto.getIdVisaOrigine())
                    .orElseThrow(() -> new IllegalArgumentException("Visa d'origine introuvable")));
        }
        demande.setTypeDemande(typeDemande);
        demande.setTypePerte(typePerte);
        demande.setDateDemande(dto.getDateDemande() != null ? dto.getDateDemande() : Date.valueOf(LocalDate.now()));
        demande.setObservations(dto.getObservations());
        demande.setStatutDemande(demandeCreee);

        if (typePerte == Demande.TypePerte.passeport_perdu) {
            if (dto.getNumeroNouveauPasseport() == null || dto.getNumeroNouveauPasseport().isBlank()) {
                throw new IllegalArgumentException("Nouveau numéro de passeport obligatoire");
            }
            Passeport nouveauPasseport = new Passeport();
            nouveauPasseport.setDemandeur(demandeur);
            nouveauPasseport.setNumeroPasseport(dto.getNumeroNouveauPasseport());
            nouveauPasseport.setDateDelivrance(dto.getDateDelivrancePasseport());
            nouveauPasseport.setDateExpiration(dto.getDateExpirationPasseport());
            nouveauPasseport.setPaysDelivrance(dto.getPaysDelivrancePasseport());
            nouveauPasseport = passeportRepository.save(nouveauPasseport);

            demande.setNouveauPasseport(nouveauPasseport);
            baseVisa.setPasseport(nouveauPasseport);
            baseVisa = visaRepository.save(baseVisa);
        } else if (typePerte == Demande.TypePerte.carte_resident_perdue) {
            CarteResident carteResident = resolveCarteResidentSprint2(demandeur, dto);
            demande.setCarteResident(carteResident);
        }

        demande = demandeRepository.save(demande);
        histoStatutDemandeRepository.save(createHistorique(demande, demandeCreee, "Sprint 2 - personne existante"));
        return demande;
    }

    @Transactional
    public List<Demande> createTwoDemandes(DuplicataDTO dto, List<Long> documentsCoches, String typePerte) {
        // Cas 2 : Personne INCONNUE - idDemandeur MUST BE NULL
        if (dto.getIdDemandeur() != null) {
            throw new IllegalArgumentException("Pour une personne existante, utilisez createDemandeSprint2()");
        }

        if (dto.getTypePerte() == null || dto.getTypePerte().isBlank()) {
            throw new IllegalArgumentException("Type de perte obligatoire");
        }

        Demande.TypePerte typePerte_enum;
        try {
            typePerte_enum = Demande.TypePerte.valueOf(typePerte);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Type de perte invalide");
        }

        // 1. Résoudre/créer Demandeur
        Demandeur demandeur = resolveDemandeurSprint2(dto);

        // 2. Résoudre/créer Passeport
        Passeport passeport = resolveBasePasseport(demandeur, dto);

        // 3. Résoudre/créer Visa
        Visa visa = resolveBaseVisa(passeport, dto);

//         if (dto.getTypeVisa() == null || dto.getTypeVisa().isBlank()) {
//     throw new IllegalStateException("Type de visa sera défini à l'étape 3");
// }

        // TypeVisa typeVisa = typeVisaRepository.findByLibelle(dto.getTypeVisa().toLowerCase())
        //         .orElseThrow(() -> new IllegalArgumentException("Type de visa invalide"));

        // visa.setTypeVisa(typeVisa);
        // visa = visaRepository.save(visa);

        // 4. Valider documents obligatoires
        String typeVisaLibelle = visa.getTypeVisa() != null ? visa.getTypeVisa().getLibelle() : null;
        if (typeVisaLibelle == null || typeVisaLibelle.isBlank()) {
            throw new IllegalArgumentException("Type de visa introuvable");
        }

        List<Document> docsObligatoires = new java.util.ArrayList<>(
                documentRepository.findByObligatoireTrueAndTypeCible(Document.TypeCible.commun)
        );
        if ("investisseur".equalsIgnoreCase(typeVisaLibelle)) {
            docsObligatoires.addAll(documentRepository.findByObligatoireTrueAndTypeCible(Document.TypeCible.investisseur));
        } else if ("travailleur".equalsIgnoreCase(typeVisaLibelle)) {
            docsObligatoires.addAll(documentRepository.findByObligatoireTrueAndTypeCible(Document.TypeCible.travailleur));
        }

        List<Long> idsObligatoires = docsObligatoires.stream().map(Document::getId).toList();
        if (!documentsCoches.containsAll(idsObligatoires)) {
            throw new IllegalArgumentException("Tous les documents obligatoires doivent être cochés");
        }

        // 5. Créer Demande 1 : nouveau_titre + approuvee
        TypeDemande typeDemandeNouveauTitre = typeDemandeRepository.findByLibelle("nouveau_titre")
                .orElseThrow(() -> new IllegalArgumentException("Type demande 'nouveau_titre' introuvable"));
        StatutDemande statutApprouvee = statutDemandeRepository.findByLibelle("approuvee")
                .orElseThrow(() -> new IllegalArgumentException("Statut 'approuvee' introuvable"));

        Demande demande1 = new Demande();
        demande1.setDemandeur(demandeur);
        demande1.setVisa(visa);
        demande1.setTypeDemande(typeDemandeNouveauTitre);
        demande1.setDateDemande(dto.getDateDemande() != null ? dto.getDateDemande() : Date.valueOf(LocalDate.now()));
        demande1.setObservations(dto.getObservations());
        demande1.setStatutDemande(statutApprouvee);
        demande1 = demandeRepository.save(demande1);
        histoStatutDemandeRepository.save(createHistorique(demande1, statutApprouvee, "Sprint 2 - personne inconnue - demande nouvelle"));

        // 6. Créer Demande 2 : duplicata/transfert + demande_creee
        TypeDemande typeDemandeTransfertOuDuplicata = resolveTypeDemandeSprint2(typePerte_enum);
        StatutDemande statutDemandeCreee = statutDemandeRepository.findByLibelle("demande_creee")
                .orElseThrow(() -> new IllegalArgumentException("Statut 'demande_creee' introuvable"));

        Demande demande2 = new Demande();
        demande2.setDemandeur(demandeur);
        demande2.setVisa(visa);
        demande2.setTypeDemande(typeDemandeTransfertOuDuplicata);
        demande2.setTypePerte(typePerte_enum);
        if (dto.getIdVisaOrigine() != null) {
            demande2.setVisaOrigine(visaRepository.findById(dto.getIdVisaOrigine())
                    .orElseThrow(() -> new IllegalArgumentException("Visa d'origine introuvable")));
        }
        demande2.setDateDemande(dto.getDateDemande() != null ? dto.getDateDemande() : Date.valueOf(LocalDate.now()));
        demande2.setObservations(dto.getObservations());
        demande2.setStatutDemande(statutDemandeCreee);

        // Gérer les cas spécifiques selon typePerte
        if (typePerte_enum == Demande.TypePerte.passeport_perdu) {
            if (dto.getNumeroNouveauPasseport() == null || dto.getNumeroNouveauPasseport().isBlank()) {
                throw new IllegalArgumentException("Nouveau numéro de passeport obligatoire");
            }
            Passeport nouveauPasseport = new Passeport();
            nouveauPasseport.setDemandeur(demandeur);
            nouveauPasseport.setNumeroPasseport(dto.getNumeroNouveauPasseport());
            nouveauPasseport.setDateDelivrance(dto.getDateDelivranceNouveauPasseport());
nouveauPasseport.setDateExpiration(dto.getDateExpirationNouveauPasseport());
nouveauPasseport.setPaysDelivrance(dto.getPaysDelivranceNouveauPasseport());
            nouveauPasseport = passeportRepository.save(nouveauPasseport);

            demande2.setNouveauPasseport(nouveauPasseport);
        } else if (typePerte_enum == Demande.TypePerte.carte_resident_perdue) {
            CarteResident carteResident = resolveCarteResidentSprint2(demandeur, dto);
            demande2.setCarteResident(carteResident);
        }

        demande2 = demandeRepository.save(demande2);
        histoStatutDemandeRepository.save(createHistorique(demande2, statutDemandeCreee, "Sprint 2 - personne inconnue - demande duplicata/transfert"));

        // 7. Créer les DemandeDocument PARTAGÉS (les mêmes documents pour les 2 demandes)
        for (Long docId : documentsCoches.stream().distinct().toList()) {
            Document doc = documentRepository.findById(docId)
                    .orElseThrow(() -> new IllegalArgumentException("Document introuvable"));

            // Document pour Demande 1
            DemandeDocument dd1 = new DemandeDocument();
            dd1.setDemande(demande1);
            dd1.setDocument(doc);
            dd1.setFourni(true);
            demandeDocumentRepository.save(dd1);

            // Document pour Demande 2 (même document partagé)
            DemandeDocument dd2 = new DemandeDocument();
            dd2.setDemande(demande2);
            dd2.setDocument(doc);
            dd2.setFourni(true);
            demandeDocumentRepository.save(dd2);
        }

        return List.of(demande1, demande2);
    }

    private Demandeur resolveDemandeurSprint2(DuplicataDTO dto) {
        if (dto.getIdDemandeur() != null) {
            return demandeurRepository.findById(dto.getIdDemandeur())
                    .orElseThrow(() -> new IllegalArgumentException("Demandeur introuvable"));
        }

        if (dto.getNom() == null || dto.getNom().isBlank() || dto.getPrenoms() == null || dto.getPrenoms().isBlank()) {
            throw new IllegalArgumentException("Informations du demandeur incomplètes");
        }

        Demandeur demandeur = null;
        if (dto.getDateNaissance() != null && dto.getLieuNaissance() != null) {
            demandeur = demandeurRepository
                    .findByNomAndPrenomsAndDateNaissanceAndLieuNaissance(dto.getNom(), dto.getPrenoms(), dto.getDateNaissance(), dto.getLieuNaissance())
                    .orElse(null);
        }

        if (demandeur != null) {
            return demandeur;
        }

        demandeur = new Demandeur();
        demandeur.setNom(dto.getNom());
        demandeur.setPrenoms(dto.getPrenoms());
        demandeur.setDateNaissance(dto.getDateNaissance());
        demandeur.setLieuNaissance(dto.getLieuNaissance());
        demandeur.setTelephone(dto.getTelephone());
        demandeur.setEmail(dto.getEmail());
        demandeur.setAdresse(dto.getAdresse());

        if (dto.getIdSituationFamiliale() != null) {
            demandeur.setSituationFamiliale(situationFamilialeRepository.findById(dto.getIdSituationFamiliale())
                    .orElseThrow(() -> new IllegalArgumentException("Situation familiale introuvable")));
        }
        if (dto.getIdNationalite() != null) {
            demandeur.setNationalite(nationaliteRepository.findById(dto.getIdNationalite())
                    .orElseThrow(() -> new IllegalArgumentException("Nationalité introuvable")));
        }
        return demandeurRepository.save(demandeur);
    }

    private Passeport resolveBasePasseport(Demandeur demandeur, DuplicataDTO dto) {
        if (dto.getIdVisa() != null) {
            Visa visa = visaRepository.findById(dto.getIdVisa())
                    .orElseThrow(() -> new IllegalArgumentException("Visa introuvable"));
            if (visa.getPasseport() != null) {
                return visa.getPasseport();
            }
        }

        if (dto.getNumeroPasseport() == null || dto.getNumeroPasseport().isBlank()) {
            throw new IllegalArgumentException("Numéro de passeport obligatoire");
        }

        return passeportRepository.findByDemandeurIdAndNumeroPasseport(demandeur.getId(), dto.getNumeroPasseport())
                .orElseGet(() -> {
                    Passeport passeport = new Passeport();
                    passeport.setDemandeur(demandeur);
                    passeport.setNumeroPasseport(dto.getNumeroPasseport());
                    passeport.setDateDelivrance(dto.getDateDelivrancePasseport());
                    passeport.setDateExpiration(dto.getDateExpirationPasseport());
                    passeport.setPaysDelivrance(dto.getPaysDelivrancePasseport());
                    return passeportRepository.save(passeport);
                });
    }

    private Visa resolveBaseVisa(Passeport passeport, DuplicataDTO dto) {
        if (dto.getIdVisa() != null) {
            return visaRepository.findById(dto.getIdVisa())
                    .orElseThrow(() -> new IllegalArgumentException("Visa introuvable"));
        }

        if (dto.getReferenceVisa() == null || dto.getReferenceVisa().isBlank()) {
            throw new IllegalArgumentException("Référence du visa obligatoire");
        }

        return visaRepository.findByPasseportIdAndReference(passeport.getId(), dto.getReferenceVisa())
                .orElseGet(() -> {
                    Visa visa = new Visa();
                    visa.setPasseport(passeport);
                    visa.setReference(dto.getReferenceVisa());
                    visa.setDateDebut(dto.getDateDebutVisa());
                    visa.setDateFin(dto.getDateFinVisa());
                    return visaRepository.save(visa);
                });
    }

    private TypeDemande resolveTypeDemandeSprint2(Demande.TypePerte typePerte) {
        String libelle = typePerte == Demande.TypePerte.passeport_perdu ? "transfert_visa" : "duplicata";
        return typeDemandeRepository.findByLibelle(libelle)
                .orElseThrow(() -> new IllegalArgumentException("TypeDemande introuvable"));
    }

    private CarteResident resolveCarteResidentSprint2(Demandeur demandeur, DuplicataDTO dto) {
        if (dto.getIdCarteResident() != null) {
            return carteResidentRepository.findById(dto.getIdCarteResident())
                    .orElseThrow(() -> new IllegalArgumentException("Carte de résident introuvable"));
        }
        throw new IllegalArgumentException("Carte de résident obligatoire pour une demande de duplicata");
    }

    private HistoStatutDemande createHistorique(Demande demande, StatutDemande statut, String commentaire) {
        HistoStatutDemande histo = new HistoStatutDemande();
        histo.setDemande(demande);
        histo.setStatutDemande(statut);
        histo.setCommentaire(commentaire);
        return histo;
    }
}
