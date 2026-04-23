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
}
