package com.visa.transformable.controller;

import com.visa.transformable.dto.DemandeDTO;
import com.visa.transformable.dto.DuplicataDTO;
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
                                HttpSession session, Model model) {
        // Détection Sprint 2 : personne inconnue (après Step3 + Step4)
        if (session.getAttribute("sprint2.typePerte") != null) {
            // C'est Sprint 2 personne INCONNUE
            try {
                String typePerte = (String) session.getAttribute("sprint2.typePerte");
                com.visa.transformable.dto.DuplicataDTO sprint2Dto = 
                    (com.visa.transformable.dto.DuplicataDTO) session.getAttribute("sprint2.dto");
                
                if (sprint2Dto == null) {
                    return "redirect:/step1-type?error=" + encode("Données Sprint 2 manquantes en session.");
                }
                
                // Appeler createTwoDemandes() pour créer 2 demandes liées
                var demandes = demandeService.createTwoDemandes(sprint2Dto, documentsCoches, typePerte);
                
                // Nettoyer la session Sprint 2
                session.removeAttribute("sprint2.dto");
                session.removeAttribute("sprint2.typePerte");
                
                // Nettoyer aussi les attributs Sprint 1 si présents
                session.removeAttribute("currentDemandeurId");
                session.removeAttribute("currentPasseportId");
                session.removeAttribute("currentVisaId");
                session.removeAttribute("currentTypeDemandeId");
                session.removeAttribute("currentTypeDemandeLibelle");
                session.removeAttribute("currentTypeVisa");
                session.removeAttribute("currentSelectedDocumentIds");
                
                // Passer les 2 demandes au modèle
                model.addAttribute("demandes", demandes);
                model.addAttribute("typePerte", typePerte);
                return "sprint2-confirmation";
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", e.getMessage());
                return "redirect:/step4-documents?error=" + encode(e.getMessage());
            }
        }
        
        // Sinon : Sprint 1 classique
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

