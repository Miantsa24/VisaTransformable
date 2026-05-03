package com.visa.transformable.service;

import com.visa.transformable.dto.DemandeDTO;
import com.visa.transformable.model.*;
import com.visa.transformable.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;

@Service
public class DemandeService {
    @Autowired
    private DemandeurRepository demandeurRepository;
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
    private NationaliteRepository nationaliteRepository;
    @Autowired
    private SituationFamilialeRepository situationFamilialeRepository;
    @Autowired
    private HistoStatutDemandeRepository histoStatutDemandeRepository;
    @Autowired
    private HistoriqueModificationRepository historiqueModificationRepository;

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

    // 3. Récupération demandeur + passeport
    Demandeur demandeur = demandeurRepository.findById(dto.getIdDemandeur())
            .orElseThrow(() -> new IllegalArgumentException("Demandeur introuvable"));

    Passeport passeport = visa.getPasseport();
    if (passeport == null) {
        throw new IllegalArgumentException("Passeport introuvable");
    }

    // 4. Création demande
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

    // 5. Statut initial
    StatutDemande statutCree = statutDemandeRepository.findByLibelle("demande_creee")
            .orElseThrow(() -> new IllegalArgumentException("Statut 'demande_creee' introuvable"));

    demande.setStatutDemande(statutCree);
    demande = demandeRepository.save(demande);

    // =========================
    // 6. HISTORIQUE STATUT (AJOUT IMPORTANT)
    // =========================
    HistoStatutDemande histo = new HistoStatutDemande();
    histo.setDemande(demande);
    histo.setStatutDemande(statutCree);
    histo.setDateChangement(dto.getDateDemande()); // ou new Date() si tu préfères

    histoStatutDemandeRepository.save(histo);

    // =========================
    // 7. DOCUMENTS
    // =========================
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

    private void saveHistorique(
        Demande demande,
        String section,
        String champ,
        String ancienneValeur,
        String nouvelleValeur,
        java.sql.Timestamp date
) {
    if ((ancienneValeur == null && nouvelleValeur == null) ||
        (ancienneValeur != null && ancienneValeur.equals(nouvelleValeur))) {
        return; // pas de changement
    }

    HistoriqueModification h = new HistoriqueModification();
    h.setDemande(demande);
    h.setSection(section);
    h.setChamp(champ);
    h.setAncienneValeur(ancienneValeur);
    h.setNouvelleValeur(nouvelleValeur);
    h.setDateModification(date);

    historiqueModificationRepository.save(h);
}

@Transactional
public void updateDemande(Long id, DemandeDTO dto) {

    // =========================
    // 1. Récupération
    // =========================
    Demande demande = demandeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Demande introuvable"));

    Demandeur demandeur = demande.getDemandeur();
    Visa visa = demande.getVisa();
    Passeport passeport = visa.getPasseport();

    java.sql.Timestamp dateModif = null;
    if (dto.getDateModification() != null) {
        dateModif = new java.sql.Timestamp(dto.getDateModification().getTime());
    }

    // =========================
    // 2. ETAT CIVIL
    // =========================
    saveHistorique(demande, "Etat civil", "nom",
            demandeur.getNom(), dto.getNom(), dateModif);
    demandeur.setNom(dto.getNom());

    saveHistorique(demande, "Etat civil", "prenoms",
            demandeur.getPrenoms(), dto.getPrenoms(), dateModif);
    demandeur.setPrenoms(dto.getPrenoms());

    saveHistorique(demande, "Etat civil", "dateNaissance",
            String.valueOf(demandeur.getDateNaissance()), String.valueOf(dto.getDateNaissance()), dateModif);
    demandeur.setDateNaissance(dto.getDateNaissance());

    saveHistorique(demande, "Etat civil", "lieuNaissance",
            demandeur.getLieuNaissance(), dto.getLieuNaissance(), dateModif);
    demandeur.setLieuNaissance(dto.getLieuNaissance());

    saveHistorique(demande, "Etat civil", "email",
            demandeur.getEmail(), dto.getEmail(), dateModif);
    demandeur.setEmail(dto.getEmail());

    saveHistorique(demande, "Etat civil", "telephone",
            demandeur.getTelephone(), dto.getTelephone(), dateModif);
    demandeur.setTelephone(dto.getTelephone());

    // =========================
    // 3. PASSEPORT
    // =========================
    saveHistorique(demande, "Passeport", "numeroPasseport",
            passeport.getNumeroPasseport(), dto.getNumeroPasseport(), dateModif);
    passeport.setNumeroPasseport(dto.getNumeroPasseport());

    saveHistorique(demande, "Passeport", "dateDelivrance",
            String.valueOf(passeport.getDateDelivrance()), String.valueOf(dto.getDateDelivrancePasseport()), dateModif);
    passeport.setDateDelivrance(dto.getDateDelivrancePasseport());

    saveHistorique(demande, "Passeport", "dateExpiration",
            String.valueOf(passeport.getDateExpiration()), String.valueOf(dto.getDateExpirationPasseport()), dateModif);
    passeport.setDateExpiration(dto.getDateExpirationPasseport());

    saveHistorique(demande, "Passeport", "paysDelivrance",
            passeport.getPaysDelivrance(), dto.getPaysDelivrancePasseport(), dateModif);
    passeport.setPaysDelivrance(dto.getPaysDelivrancePasseport());

    // =========================
    // 4. VISA
    // =========================
    saveHistorique(demande, "Visa", "reference",
            visa.getReference(), dto.getReferenceVisa(), dateModif);
    visa.setReference(dto.getReferenceVisa());

    saveHistorique(demande, "Visa", "dateDebut",
            String.valueOf(visa.getDateDebut()), String.valueOf(dto.getDateDebutVisa()), dateModif);
    visa.setDateDebut(dto.getDateDebutVisa());

    saveHistorique(demande, "Visa", "dateFin",
            String.valueOf(visa.getDateFin()), String.valueOf(dto.getDateFinVisa()), dateModif);
    visa.setDateFin(dto.getDateFinVisa());

    if (dto.getTypeVisa() != null && !dto.getTypeVisa().isBlank()) {

        String ancienType = visa.getTypeVisa() != null ? visa.getTypeVisa().getLibelle() : null;

        TypeVisa typeVisa = typeVisaRepository.findByLibelle(dto.getTypeVisa().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Type de visa invalide"));

        saveHistorique(demande, "Visa", "typeVisa",
                ancienType, typeVisa.getLibelle(), dateModif);

        visa.setTypeVisa(typeVisa);
    }

    // =========================
    // 5. DOCUMENTS (AJOUT)
    // =========================
    if (dto.getDocumentsCoches() != null) {

        List<DemandeDocument> existants = demandeDocumentRepository.findByDemandeId(id);

        Set<Long> dejaPresents = existants.stream()
                .map(dd -> dd.getDocument().getId())
                .collect(Collectors.toSet());

        for (Long docId : dto.getDocumentsCoches()) {

            if (dejaPresents.contains(docId)) continue;

            Document doc = documentRepository.findById(docId)
                    .orElseThrow(() -> new IllegalArgumentException("Document invalide"));

            // HISTORIQUE DOCUMENT
            saveHistorique(
                    demande,
                    "Documents",
                    "ajout",
                    null,
                    doc.getLibelle(),
                    dateModif
            );

            DemandeDocument dd = new DemandeDocument();
            dd.setDemande(demande);
            dd.setDocument(doc);
            dd.setFourni(true);

            demandeDocumentRepository.save(dd);
        }
    }

    // =========================
    // 6. HISTO STATUT (inchangé)
    // =========================
    // if (dto.getDateModification() != null) {
    //     HistoStatutDemande histo = new HistoStatutDemande();
    //     histo.setDemande(demande);
    //     histo.setDateChangement(dto.getDateModification());
    //     histo.setStatutDemande(demande.getStatutDemande());

    //     histoStatutDemandeRepository.save(histo);
    // }

    // =========================
    // 7. SAVE
    // =========================
    demandeRepository.save(demande);
}
}