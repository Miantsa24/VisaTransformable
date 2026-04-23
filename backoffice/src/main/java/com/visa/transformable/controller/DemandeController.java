package com.visa.transformable.controller;

import com.visa.transformable.dto.DemandeDTO;
import com.visa.transformable.repository.DemandeRepository;
import com.visa.transformable.repository.TypeDemandeRepository;
import com.visa.transformable.service.DemandeService;

import java.util.List;
import java.sql.Date;
import java.time.LocalDate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/backoffice/demande")
public class DemandeController {
    @Autowired
    private DemandeService demandeService;
    @Autowired
    private DemandeRepository demandeRepository;
    @Autowired
    private TypeDemandeRepository typeDemandeRepository;

    @GetMapping("/liste")
    public String listeDemandes(Model model) {
        model.addAttribute("demandes", demandeRepository.findAll());
        return "liste-demandes";
    }

    @GetMapping("/new")
    public String newDemande(HttpSession session) {
        session.removeAttribute("currentDemandeurId");
        session.removeAttribute("currentPasseportId");
        session.removeAttribute("currentVisaId");
        session.removeAttribute("currentTypeDemandeId");
        session.removeAttribute("currentTypeDemandeLibelle");
        session.removeAttribute("currentTypeVisa");
        session.removeAttribute("currentSelectedDocumentIds");
        return "step1-type";
    }

    @PostMapping
    public String createDemande(@RequestParam(value = "documents", required = false) List<Long> documentsCoches,
                                HttpSession session) {
        Long idDemandeur = (Long) session.getAttribute("currentDemandeurId");
        Long idVisa = (Long) session.getAttribute("currentVisaId");
        Long idTypeDemande = (Long) session.getAttribute("currentTypeDemandeId");
        if (idTypeDemande == null) {
            String typeDemandeLibelle = (String) session.getAttribute("currentTypeDemandeLibelle");
            if (typeDemandeLibelle != null && !typeDemandeLibelle.isBlank()) {
                idTypeDemande = typeDemandeRepository.findByLibelle(typeDemandeLibelle)
                        .map(typeDemande -> typeDemande.getId())
                        .orElse(null);
            }
        }
        List<Long> selectedDocs = (List<Long>) session.getAttribute("currentSelectedDocumentIds");
        if (selectedDocs == null) {
            selectedDocs = documentsCoches;
        }
        if (selectedDocs == null) {
            selectedDocs = new java.util.ArrayList<>();
        }

        if (idDemandeur == null || idVisa == null) {
            return "redirect:/step5-confirmation?error=" + encode("Session expirée ou incomplète.");
        }

        DemandeDTO dto = new DemandeDTO();
        dto.setIdDemandeur(idDemandeur);
        dto.setIdVisa(idVisa);
        dto.setIdTypeDemande(idTypeDemande);
        dto.setDateDemande(Date.valueOf(LocalDate.now()));
        dto.setDocumentsCoches(selectedDocs);

        try {
            demandeService.createDemande(dto);
            session.removeAttribute("currentDemandeurId");
            session.removeAttribute("currentPasseportId");
            session.removeAttribute("currentVisaId");
            session.removeAttribute("currentTypeDemandeId");
            session.removeAttribute("currentTypeDemandeLibelle");
            session.removeAttribute("currentTypeVisa");
            session.removeAttribute("currentSelectedDocumentIds");
            return "success";
        } catch (IllegalArgumentException e) {
            return "redirect:/step5-confirmation?error=" + encode(e.getMessage());
        }
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}

