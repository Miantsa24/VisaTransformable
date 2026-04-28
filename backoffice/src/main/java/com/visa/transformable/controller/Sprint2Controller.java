package com.visa.transformable.controller;

import com.visa.transformable.dto.DuplicataDTO;
import com.visa.transformable.model.CarteResident;
import com.visa.transformable.model.Demandeur;
import com.visa.transformable.model.Passeport;
import com.visa.transformable.model.Visa;
import com.visa.transformable.repository.CarteResidentRepository;
import com.visa.transformable.repository.DemandeurRepository;
import com.visa.transformable.repository.PasseportRepository;
import com.visa.transformable.repository.VisaRepository;
import com.visa.transformable.service.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/backoffice/sprint2/duplicata")
public class Sprint2Controller {
    @Autowired
    private DemandeService demandeService;
    @Autowired
    private DemandeurRepository demandeurRepository;
    @Autowired
    private PasseportRepository passeportRepository;
    @Autowired
    private VisaRepository visaRepository;
    @Autowired
    private CarteResidentRepository carteResidentRepository;

    @GetMapping("/new")
    public String newDuplicata(HttpSession session) {
        session.removeAttribute("sprint2.prefillDemandeurId");
        session.removeAttribute("sprint2.prefillVisaId");
        session.removeAttribute("sprint2.prefillCarteResidentId");
        session.removeAttribute("sprint2.typePerte");
        return "sprint2-choice";
    }

    @GetMapping("/prefill")
    public ResponseEntity<DuplicataDTO> prefill(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "telephone", required = false) String telephone,
            @RequestParam(value = "numero_passeport", required = false) String numeroPasseport,
            @RequestParam(value = "reference_visa", required = false) String referenceVisa) {
        Demandeur demandeur = null;
        if (email != null && !email.isBlank()) {
            demandeur = demandeurRepository.findByEmail(email).orElse(null);
        }
        if (demandeur == null && telephone != null && !telephone.isBlank()) {
            demandeur = demandeurRepository.findByTelephone(telephone).orElse(null);
        }
        if (demandeur == null && numeroPasseport != null && !numeroPasseport.isBlank()) {
            demandeur = findDemandeurByNumeroPasseport(numeroPasseport);
        }
        if (demandeur == null && referenceVisa != null && !referenceVisa.isBlank()) {
            demandeur = findDemandeurByReferenceVisa(referenceVisa);
        }
        if (demandeur == null) {
            return ResponseEntity.notFound().build();
        }

        DuplicataDTO dto = new DuplicataDTO();
        dto.setIdDemandeur(demandeur.getId());
        dto.setNom(demandeur.getNom());
        dto.setPrenoms(demandeur.getPrenoms());
        dto.setDateNaissance(demandeur.getDateNaissance());
        dto.setLieuNaissance(demandeur.getLieuNaissance());
        dto.setTelephone(demandeur.getTelephone());
        dto.setEmail(demandeur.getEmail());
        dto.setAdresse(demandeur.getAdresse());
        if (demandeur.getSituationFamiliale() != null) {
            dto.setIdSituationFamiliale(demandeur.getSituationFamiliale().getId());
        }
        if (demandeur.getNationalite() != null) {
            dto.setIdNationalite(demandeur.getNationalite().getId());
        }

        List<Passeport> passeports = passeportRepository.findByDemandeurId(demandeur.getId());
        if (!passeports.isEmpty()) {
            Passeport passeport = passeports.get(0);
            dto.setNumeroPasseport(passeport.getNumeroPasseport());
            dto.setDateDelivrancePasseport(passeport.getDateDelivrance());
            dto.setDateExpirationPasseport(passeport.getDateExpiration());
            dto.setPaysDelivrancePasseport(passeport.getPaysDelivrance());

            List<Visa> visas = visaRepository.findByPasseportId(passeport.getId());
            if (!visas.isEmpty()) {
                Visa visa = visas.get(0);
                dto.setIdVisa(visa.getId());
                dto.setReferenceVisa(visa.getReference());
                dto.setDateDebutVisa(visa.getDateDebut());
                dto.setDateFinVisa(visa.getDateFin());
            }
        }

        List<CarteResident> cartesResident = carteResidentRepository.findByDemandeurId(demandeur.getId());
        if (!cartesResident.isEmpty()) {
            dto.setIdCarteResident(cartesResident.get(0).getId());
        }

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public String createDuplicata(@ModelAttribute DuplicataDTO dto, Model model) {
        try {
            var demande = demandeService.createDemandeSprint2(dto);
            model.addAttribute("demande", demande);
            model.addAttribute("typePerte", dto.getTypePerte());
            return "sprint2-confirmation";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("duplicata", dto);
            return "sprint2-form";
        }
    }

    private Demandeur findDemandeurByNumeroPasseport(String numeroPasseport) {
        return passeportRepository.findByNumeroPasseport(numeroPasseport)
                .map(Passeport::getDemandeur)
                .orElse(null);
    }

    private Demandeur findDemandeurByReferenceVisa(String referenceVisa) {
        return visaRepository.findByReference(referenceVisa)
                .map(Visa::getPasseport)
                .map(Passeport::getDemandeur)
                .orElse(null);
    }
}
