package com.visa.transformable.controller;

import com.visa.transformable.dto.DuplicataDTO;
import com.visa.transformable.model.*;
import com.visa.transformable.repository.CarteResidentRepository;
import com.visa.transformable.repository.DemandeurRepository;
import com.visa.transformable.repository.PasseportRepository;
import com.visa.transformable.repository.VisaRepository;
import com.visa.transformable.service.DemandeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Sprint2ControllerTest {

    @Mock private DemandeService demandeService;
    @Mock private DemandeurRepository demandeurRepository;
    @Mock private PasseportRepository passeportRepository;
    @Mock private VisaRepository visaRepository;
    @Mock private CarteResidentRepository carteResidentRepository;

    @InjectMocks
    private Sprint2Controller controller;

    @Test
    void prefillExistingPersonReturnsPopulatedDto() {
        Demandeur demandeur = new Demandeur();
        demandeur.setId(10L);
        demandeur.setNom("Doe");
        demandeur.setPrenoms("John");
        demandeur.setDateNaissance(Date.valueOf(LocalDate.of(1990, 1, 1)));
        demandeur.setLieuNaissance("Antananarivo");
        demandeur.setTelephone("0320000000");
        demandeur.setEmail("john@example.com");
        demandeur.setAdresse("Adresse");
        SituationFamiliale sf = new SituationFamiliale();
        sf.setId(2L);
        demandeur.setSituationFamiliale(sf);
        Nationalite nat = new Nationalite();
        nat.setId(3L);
        demandeur.setNationalite(nat);

        Passeport passeport = new Passeport();
        passeport.setId(20L);
        passeport.setNumeroPasseport("PP-001");
        passeport.setDateDelivrance(Date.valueOf(LocalDate.of(2020, 1, 1)));
        passeport.setDateExpiration(Date.valueOf(LocalDate.of(2030, 1, 1)));
        passeport.setPaysDelivrance("MG");

        Visa visa = new Visa();
        visa.setId(30L);
        visa.setReference("VISA-001");
        visa.setDateDebut(Date.valueOf(LocalDate.of(2024, 1, 1)));
        visa.setDateFin(Date.valueOf(LocalDate.of(2025, 1, 1)));

        CarteResident carteResident = new CarteResident();
        carteResident.setId(40L);

        when(demandeurRepository.findByEmail("john@example.com")).thenReturn(Optional.of(demandeur));
        when(passeportRepository.findByDemandeurId(10L)).thenReturn(List.of(passeport));
        when(visaRepository.findByPasseportId(20L)).thenReturn(List.of(visa));
        when(carteResidentRepository.findByDemandeurId(10L)).thenReturn(List.of(carteResident));

        ResponseEntity<DuplicataDTO> response = controller.prefill("john@example.com", null, null, null);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(10L, response.getBody().getIdDemandeur());
        assertEquals("Doe", response.getBody().getNom());
        assertEquals("PP-001", response.getBody().getNumeroPasseport());
        assertEquals("VISA-001", response.getBody().getReferenceVisa());
        assertEquals(40L, response.getBody().getIdCarteResident());
    }

    @Test
    void prefillUnknownPersonReturns404() {
        when(demandeurRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        ResponseEntity<DuplicataDTO> response = controller.prefill("missing@example.com", null, null, null);

        assertEquals(404, response.getStatusCode().value());
    }
}
