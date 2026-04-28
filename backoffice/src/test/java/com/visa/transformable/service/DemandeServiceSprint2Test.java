package com.visa.transformable.service;

import com.visa.transformable.dto.DuplicataDTO;
import com.visa.transformable.model.*;
import com.visa.transformable.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DemandeServiceSprint2Test {

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
    @Mock private CarteResidentRepository carteResidentRepository;
    @Mock private HistoStatutDemandeRepository histoStatutDemandeRepository;

    @InjectMocks
    private DemandeService service;

    @Test
    void prefillNotInServiceButCreateFromScratchForPassportLostCreatesApprovedDemand() {
        DuplicataDTO dto = baseDto();
        dto.setTypePerte("passeport_perdu");
        dto.setIdDemandeur(null);
        dto.setIdVisa(null);
        dto.setNumeroPasseport("PP-NEW-001");
        dto.setReferenceVisa("VISA-NEW-001");
        dto.setNumeroNouveauPasseport("PP-NEW-002");

        SituationFamiliale sf = new SituationFamiliale();
        sf.setId(1L);
        Nationalite nat = new Nationalite();
        nat.setId(2L);
        Demandeur savedDemandeur = new Demandeur();
        savedDemandeur.setId(10L);
        Passeport savedPasseport = new Passeport();
        savedPasseport.setId(20L);
        Visa savedVisa = new Visa();
        savedVisa.setId(30L);
        savedVisa.setPasseport(savedPasseport);
        savedVisa.setReference("VISA-NEW-001");
        Passeport newPassport = new Passeport();
        newPassport.setId(40L);
        newPassport.setNumeroPasseport("PP-NEW-002");
        TypeDemande typeDemande = new TypeDemande();
        typeDemande.setId(5L);
        typeDemande.setLibelle("transfert_visa");
        StatutDemande approuvee = new StatutDemande();
        approuvee.setId(6L);
        approuvee.setLibelle("approuvee");

        when(situationFamilialeRepository.findById(1L)).thenReturn(Optional.of(sf));
        when(nationaliteRepository.findById(2L)).thenReturn(Optional.of(nat));
        when(demandeurRepository.save(any(Demandeur.class))).thenReturn(savedDemandeur);
        when(passeportRepository.findByDemandeurIdAndNumeroPasseport(10L, "PP-NEW-001")).thenReturn(Optional.empty());
        when(passeportRepository.save(any(Passeport.class))).thenReturn(savedPasseport, newPassport);
        when(visaRepository.findByPasseportIdAndReference(20L, "VISA-NEW-001")).thenReturn(Optional.empty());
        when(visaRepository.save(any(Visa.class))).thenReturn(savedVisa, savedVisa);
        when(typeDemandeRepository.findByLibelle("transfert_visa")).thenReturn(Optional.of(typeDemande));
        when(statutDemandeRepository.findByLibelle("approuvee")).thenReturn(Optional.of(approuvee));
        when(demandeRepository.save(any(Demande.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(histoStatutDemandeRepository.save(any(HistoStatutDemande.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Demande demande = service.createDemandeSprint2(dto);

        assertNotNull(demande);
        assertEquals(approuvee, demande.getStatutDemande());
        assertEquals(typeDemande, demande.getTypeDemande());
        assertEquals(Demande.TypePerte.passeport_perdu, demande.getTypePerte());
        assertNotNull(demande.getNouveauPasseport());
        verify(histoStatutDemandeRepository).save(any(HistoStatutDemande.class));
    }

    @Test
    void cardLostRegistersDuplicataWithApprovedStatus() {
        DuplicataDTO dto = baseDto();
        dto.setTypePerte("carte_resident_perdue");
        dto.setIdDemandeur(10L);
        dto.setIdVisa(30L);
        dto.setIdCarteResident(50L);

        Demandeur demandeur = new Demandeur();
        demandeur.setId(10L);
        Passeport passeport = new Passeport();
        passeport.setId(20L);
        Visa visa = new Visa();
        visa.setId(30L);
        visa.setPasseport(passeport);
        visa.setReference("VISA-001");
        CarteResident carteResident = new CarteResident();
        carteResident.setId(50L);
        TypeDemande typeDemande = new TypeDemande();
        typeDemande.setId(5L);
        typeDemande.setLibelle("duplicata");
        StatutDemande approuvee = new StatutDemande();
        approuvee.setId(6L);
        approuvee.setLibelle("approuvee");

        when(demandeurRepository.findById(10L)).thenReturn(Optional.of(demandeur));
        when(visaRepository.findById(30L)).thenReturn(Optional.of(visa));
        when(typeDemandeRepository.findByLibelle("duplicata")).thenReturn(Optional.of(typeDemande));
        when(statutDemandeRepository.findByLibelle("approuvee")).thenReturn(Optional.of(approuvee));
        when(carteResidentRepository.findById(50L)).thenReturn(Optional.of(carteResident));
        when(demandeRepository.save(any(Demande.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(histoStatutDemandeRepository.save(any(HistoStatutDemande.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Demande demande = service.createDemandeSprint2(dto);

        assertEquals(Demande.TypePerte.carte_resident_perdue, demande.getTypePerte());
        assertEquals(approuvee, demande.getStatutDemande());
        assertEquals(typeDemande, demande.getTypeDemande());
        assertEquals(carteResident, demande.getCarteResident());
    }

    @Test
    void createDemandeSprint2FailsWhenTypePerteMissing() {
        DuplicataDTO dto = baseDto();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.createDemandeSprint2(dto));

        assertEquals("Type de perte obligatoire", ex.getMessage());
        verifyNoInteractions(demandeRepository);
    }

    private DuplicataDTO baseDto() {
        DuplicataDTO dto = new DuplicataDTO();
        dto.setNom("Doe");
        dto.setPrenoms("John");
        dto.setDateNaissance(Date.valueOf(LocalDate.of(1990, 1, 1)));
        dto.setLieuNaissance("Antananarivo");
        dto.setTelephone("0320000000");
        dto.setEmail("john.doe@example.com");
        dto.setAdresse("Adresse");
        dto.setIdSituationFamiliale(1L);
        dto.setIdNationalite(2L);
        dto.setDateDemande(Date.valueOf(LocalDate.now()));
        dto.setNumeroPasseport("P123");
        dto.setDateDelivrancePasseport(Date.valueOf(LocalDate.of(2020, 1, 1)));
        dto.setDateExpirationPasseport(Date.valueOf(LocalDate.of(2030, 1, 1)));
        dto.setPaysDelivrancePasseport("MG");
        dto.setReferenceVisa("V123");
        dto.setDateDebutVisa(Date.valueOf(LocalDate.of(2024, 1, 1)));
        dto.setDateFinVisa(Date.valueOf(LocalDate.of(2025, 1, 1)));
        dto.setDocumentsCoches(List.of());
        return dto;
    }
}
