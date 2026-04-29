package com.visa.transformable.service;

import com.visa.transformable.dto.DemandeDTO;
import com.visa.transformable.model.*;
import com.visa.transformable.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DemandeServiceUpdateTest {

    @Mock private DemandeurRepository demandeurRepository;
    @Mock private SituationFamilialeRepository situationFamilialeRepository;
    @Mock private NationaliteRepository nationaliteRepository;
    @Mock private PasseportRepository passeportRepository;
    @Mock private VisaRepository visaRepository;
    @Mock private DemandeRepository demandeRepository;
    @Mock private DocumentRepository documentRepository;
    @Mock private DemandeDocumentRepository demandeDocumentRepository;
    @Mock private StatutDemandeRepository statutDemandeRepository;
    @Mock private TypeDemandeRepository typeDemandeRepository;
    @Mock private TypeVisaRepository typeVisaRepository;
    @Mock private CarteResidentRepository carteResidentRepository;
    @Mock private HistoStatutDemandeRepository histoStatutDemandeRepository;

    @InjectMocks
    private DemandeService service;

    @Test
    void updateDemandeKeepsExistingDocumentsAndAddsNewUncheckedOnes() {
        Demandeur demandeur = new Demandeur();
        demandeur.setId(1L);
        demandeur.setNom("Dupont");
        demandeur.setPrenoms("Jean");
        demandeur.setDateNaissance(Date.valueOf(LocalDate.of(1990, 1, 1)));
        demandeur.setLieuNaissance("Paris");

        Passeport passeport = new Passeport();
        passeport.setId(2L);
        passeport.setDemandeur(demandeur);
        passeport.setNumeroPasseport("P123");

        TypeVisa typeVisa = new TypeVisa();
        typeVisa.setId(3L);
        typeVisa.setLibelle("investisseur");

        Visa visa = new Visa();
        visa.setId(4L);
        visa.setPasseport(passeport);
        visa.setReference("V123");
        visa.setTypeVisa(typeVisa);

        Demande demande = new Demande();
        demande.setId(5L);
        demande.setDemandeur(demandeur);
        demande.setVisa(visa);
        demande.setDateDemande(Date.valueOf(LocalDate.of(2026, 1, 1)));

        Document existing = new Document();
        existing.setId(10L);
        Document missing = new Document();
        missing.setId(11L);

        DemandeDocument demandeDocument = new DemandeDocument();
        demandeDocument.setDocument(existing);
        demandeDocument.setDemande(demande);
        demandeDocument.setFourni(true);

        when(demandeRepository.findById(5L)).thenReturn(Optional.of(demande));
        when(demandeurRepository.save(any(Demandeur.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(passeportRepository.save(any(Passeport.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(visaRepository.save(any(Visa.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(demandeRepository.save(any(Demande.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(demandeDocumentRepository.findByDemandeId(5L)).thenReturn(List.of(demandeDocument));
        when(documentRepository.findByObligatoireTrueAndTypeCible(Document.TypeCible.commun)).thenReturn(List.of(existing));
        when(documentRepository.findByObligatoireTrueAndTypeCible(Document.TypeCible.investisseur)).thenReturn(List.of(missing));
        when(typeVisaRepository.findByLibelle("investisseur")).thenReturn(Optional.of(typeVisa));
        when(documentRepository.findById(11L)).thenReturn(Optional.of(missing));
        when(demandeDocumentRepository.existsByDemandeIdAndDocumentId(5L, 10L)).thenReturn(true);
        when(demandeDocumentRepository.existsByDemandeIdAndDocumentId(5L, 11L)).thenReturn(false);
        when(demandeDocumentRepository.save(any(DemandeDocument.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DemandeDTO dto = new DemandeDTO();
        dto.setNom("Dupont");
        dto.setPrenoms("Jean");
        dto.setDateNaissance(Date.valueOf(LocalDate.of(1990, 1, 1)));
        dto.setLieuNaissance("Paris");
        dto.setEmail("jean.dupont@example.com");
        dto.setTelephone("0320000000");
        dto.setNumeroPasseport("P123");
        dto.setReferenceVisa("V123");
        dto.setTypeVisa("investisseur");
        dto.setDateDelivrancePasseport(Date.valueOf(LocalDate.of(2020, 1, 1)));
        dto.setDateExpirationPasseport(Date.valueOf(LocalDate.of(2030, 1, 1)));
        dto.setDateDebutVisa(Date.valueOf(LocalDate.of(2025, 1, 1)));
        dto.setDateFinVisa(Date.valueOf(LocalDate.of(2026, 1, 1)));
        dto.setDocumentsCoches(List.of(11L));

        Demande updated = service.updateDemande(5L, dto);

        assertEquals("jean.dupont@example.com", updated.getDemandeur().getEmail());
        verify(demandeDocumentRepository, times(1)).save(any(DemandeDocument.class));
    }
}
