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

import com.visa.transformable.model.Document;
import com.visa.transformable.repository.DemandeDocumentRepository;
import com.visa.transformable.repository.DocumentRepository;
import com.visa.transformable.repository.HistoStatutDemandeRepository;
import com.visa.transformable.repository.NationaliteRepository;
import com.visa.transformable.repository.PasseportRepository;
import com.visa.transformable.repository.SituationFamilialeRepository;
import com.visa.transformable.repository.TypeVisaRepository;
import com.visa.transformable.repository.VisaRepository;

@Controller
@RequestMapping("/backoffice/demande")
public class DemandeController {
    @Autowired
    private DemandeService demandeService;
    @Autowired
    private DemandeRepository demandeRepository;
    @Autowired
    private TypeDemandeRepository typeDemandeRepository;
    @Autowired
    private DemandeDocumentRepository demandeDocumentRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private TypeVisaRepository typeVisaRepository;
    @Autowired
    private SituationFamilialeRepository situationFamilialeRepository;
    @Autowired
    private NationaliteRepository nationaliteRepository;
    @Autowired
    private PasseportRepository passeportRepository;
    @Autowired
    private VisaRepository visaRepository;
    @Autowired
    private HistoStatutDemandeRepository histoStatutDemandeRepository;

    @GetMapping("/liste")
    public String listeDemandes(Model model) {
        model.addAttribute("demandes", demandeRepository.findAll());
        return "liste-demandes";
    }

@GetMapping("/{id}/historique")
    public String historiqueDemande(@PathVariable Long id, Model model) {
        var demande = demandeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Demande introuvable"));
        model.addAttribute("demande", demande);
        model.addAttribute("historiques", histoStatutDemandeRepository.findByDemandeIdOrderByDateChangementDesc(id));
        return "demande-history";
    }

    @GetMapping("/{id}/modifier")
    public String modifierDemande(@PathVariable Long id, Model model) {
        var demande = demandeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Demande introuvable"));

        var documentsSelectionnes = new java.util.HashSet<>(demandeDocumentRepository.findByDemandeId(id).stream()
                .filter(dd -> Boolean.TRUE.equals(dd.getFourni()))
                .map(dd -> dd.getDocument().getId())
                .toList());

        var typeVisa = demande.getVisa() != null && demande.getVisa().getTypeVisa() != null
                ? demande.getVisa().getTypeVisa().getLibelle()
                : null;

        model.addAttribute("demande", demande);
        model.addAttribute("documentsSelectionnes", documentsSelectionnes);
        model.addAttribute("docsCommuns", documentRepository.findByTypeCible(Document.TypeCible.commun));
        model.addAttribute("docsInvestisseur", documentRepository.findByTypeCible(Document.TypeCible.investisseur));
        model.addAttribute("docsTravailleur", documentRepository.findByTypeCible(Document.TypeCible.travailleur));
        model.addAttribute("typeVisas", typeVisaRepository.findAll());
        model.addAttribute("typeVisaCourant", typeVisa);
        model.addAttribute("situationsFamiliales", situationFamilialeRepository.findAll());
        model.addAttribute("nationalites", nationaliteRepository.findAll());
        model.addAttribute("dateModification", Date.valueOf(java.time.LocalDate.now()));
        return "demande-edit";
    }

    @PostMapping("/{id}/modifier")
    public String modifierDemande(@PathVariable Long id, @ModelAttribute DemandeDTO dto, Model model) {
        try {
            demandeService.updateDemande(id, dto);
            return "redirect:/backoffice/demande/liste";
        } catch (IllegalArgumentException e) {
            var demande = demandeRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Demande introuvable"));
            model.addAttribute("demande", demande);
            model.addAttribute("documentsSelectionnes", new java.util.HashSet<>(demandeDocumentRepository.findByDemandeId(id).stream()
                    .filter(dd -> Boolean.TRUE.equals(dd.getFourni()))
                    .map(dd -> dd.getDocument().getId())
                    .toList()));
            model.addAttribute("docsCommuns", documentRepository.findByTypeCible(Document.TypeCible.commun));
            model.addAttribute("docsInvestisseur", documentRepository.findByTypeCible(Document.TypeCible.investisseur));
            model.addAttribute("docsTravailleur", documentRepository.findByTypeCible(Document.TypeCible.travailleur));
            model.addAttribute("typeVisas", typeVisaRepository.findAll());
            model.addAttribute("typeVisaCourant", demande.getVisa() != null && demande.getVisa().getTypeVisa() != null
                    ? demande.getVisa().getTypeVisa().getLibelle()
                    : null);
            model.addAttribute("situationsFamiliales", situationFamilialeRepository.findAll());
            model.addAttribute("nationalites", nationaliteRepository.findAll());
            model.addAttribute("dateModification", dto.getDateModification() != null ? dto.getDateModification() : Date.valueOf(java.time.LocalDate.now()));
            model.addAttribute("erreur", e.getMessage());
            return "demande-edit";
        }
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