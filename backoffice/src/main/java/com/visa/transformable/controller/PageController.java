package com.visa.transformable.controller;

import com.visa.transformable.dto.DemandeDTO;
import com.visa.transformable.dto.DuplicataDTO;
import com.visa.transformable.model.Nationalite;
import com.visa.transformable.model.SituationFamiliale;
import com.visa.transformable.model.TypeVisa;
import com.visa.transformable.repository.DemandeRepository;
import com.visa.transformable.repository.DocumentRepository;
import com.visa.transformable.repository.NationaliteRepository;
import com.visa.transformable.repository.PasseportRepository;
import com.visa.transformable.repository.SituationFamilialeRepository;
import com.visa.transformable.repository.StatutDemandeRepository;
import com.visa.transformable.repository.TypeDemandeRepository;
import com.visa.transformable.repository.TypeVisaRepository;
import com.visa.transformable.repository.DemandeurRepository;
import com.visa.transformable.repository.CarteResidentRepository;
import com.visa.transformable.repository.VisaRepository;
import com.visa.transformable.service.DemandeService;
import com.visa.transformable.service.DemandeurService;
import com.visa.transformable.service.PasseportService;
import com.visa.transformable.service.VisaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class PageController {

    @Autowired
    private DemandeService demandeService;

    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping("/step1-type")
    public String step1Type() {
        return "step1-type";
    }

    @Autowired
    private TypeDemandeRepository typeDemandeRepository;
    @Autowired
    private StatutDemandeRepository statutDemandeRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private DemandeRepository demandeRepository;
    @Autowired
    private SituationFamilialeRepository situationFamilialeRepository;
    @Autowired
    private NationaliteRepository nationaliteRepository;
    @Autowired
    private TypeVisaRepository typeVisaRepository;

    @GetMapping("/step2-form")
    public String step2Form(@RequestParam(value = "type", required = false) String type,
                            Model model,
                            HttpSession session) {
        java.util.List<SituationFamiliale> situations = situationFamilialeRepository.findAll();
        java.util.List<Nationalite> nationalites = nationaliteRepository.findAll();
        model.addAttribute("situationsFamiliales", situations);
        model.addAttribute("nationalites", nationalites);
        if (type != null && !type.isBlank()) {
            String typeDemandeLibelle = normaliserTypeDemande(type);
            typeDemandeRepository.findByLibelle(typeDemandeLibelle).ifPresent(typeDemande -> {
                session.setAttribute("currentTypeDemandeId", typeDemande.getId());
                session.setAttribute("currentTypeDemandeLibelle", typeDemande.getLibelle());
            });
        }
        return "step2-form";
    }

    @Autowired
    private DemandeurRepository demandeurRepository;
    @Autowired
    private PasseportRepository passeportRepository;
    @Autowired
    private VisaRepository visaRepository;

    @Autowired
    private DemandeurService demandeurService;
    @Autowired
    private PasseportService passeportService;
    @Autowired
    private VisaService visaService;

    @PostMapping("/step3-typeVisa")
    public String enregistrerDemande(
            @RequestParam String nom,
            @RequestParam String prenoms,
            @RequestParam String date_naissance,
            @RequestParam String lieu_naissance,
            @RequestParam(required = false) String situation_matrimoniale,
            @RequestParam(required = false) String nationalite,
            @RequestParam(required = false) String profession,
            @RequestParam(required = false) String adresse_locale,
            @RequestParam String telephone,
            @RequestParam String email,
            @RequestParam String numero_passeport,
            @RequestParam(required = false) String date_delivrance,
            @RequestParam(required = false) String date_expiration,
            @RequestParam(required = false) String pays_delivrance,
            @RequestParam String reference_visa,
            @RequestParam(required = false) String date_entree,
            @RequestParam(required = false) String lieu_entree,
            @RequestParam(required = false) String date_expiration_visa,
            @RequestParam(required = false) String type_visa,
            Model model,
            HttpSession session,
        @RequestParam("type_visa") String typeVisa) {

        if (session.getAttribute("sprint2.typePerte") != null) {

        DuplicataDTO dto = (DuplicataDTO) session.getAttribute("sprint2.dto");

        if (dto == null) {
            return "redirect:/step1-type?error=session_expired";
        }

        // ✅ stocker type visa
        session.setAttribute("currentTypeVisa", type_visa);

        // ✅ mettre à jour DTO
        dto.setTypeVisa(type_visa);
        session.setAttribute("sprint2.dto", dto);

        // ✅ redirection correcte
        return "redirect:/step4-documents?type_visa=" + type_visa;
    }
        StringBuilder erreurs = new StringBuilder();
        // Vérification non-nullité/non-vacuité
        if (nom.isEmpty()) erreurs.append("Nom obligatoire. ");
        if (date_naissance.isEmpty()) erreurs.append("Date de naissance obligatoire. ");
        if (lieu_naissance.isEmpty()) erreurs.append("Lieu de naissance obligatoire. ");
        if (telephone.isEmpty()) erreurs.append("Téléphone obligatoire. ");
        if (email.isEmpty()) erreurs.append("Email obligatoire. ");
        if (numero_passeport.isEmpty()) erreurs.append("Numéro de passeport obligatoire. ");
        if (reference_visa.isEmpty()) erreurs.append("Référence du visa obligatoire. ");
        // Vérification existence/doublon en base
        if (demandeurRepository.findByEmail(email).isPresent()) {
            erreurs.append("Email déjà utilisé. ");
        }
        if (demandeurRepository.findByTelephone(telephone).isPresent()) {
            erreurs.append("Téléphone déjà utilisé. ");
        }
        if (passeportRepository.findByNumeroPasseport(numero_passeport).isPresent()) {
            erreurs.append("Numéro de passeport déjà utilisé. ");
        }
        if (visaRepository.findByReference(reference_visa).isPresent()) {
            erreurs.append("Référence de visa déjà utilisée. ");
        }
        if (erreurs.length() > 0) {
            model.addAttribute("erreurs", erreurs.toString());
            return "step2-form";
        }
        // Création et insertion des entités avec tous les champs
        com.visa.transformable.model.Demandeur demandeur = new com.visa.transformable.model.Demandeur();
        demandeur.setNom(nom);
        demandeur.setPrenoms(prenoms);
        demandeur.setDateNaissance(java.sql.Date.valueOf(date_naissance));
        demandeur.setLieuNaissance(lieu_naissance);
        demandeur.setTelephone(telephone);
        demandeur.setEmail(email);
        demandeur.setAdresse(adresse_locale);
        // Mapping situation matrimoniale
        if (situation_matrimoniale != null && !situation_matrimoniale.isEmpty()) {
            situationFamilialeRepository.findById(Long.parseLong(situation_matrimoniale)).ifPresent(demandeur::setSituationFamiliale);
        }
        // Mapping nationalité
        if (nationalite != null && !nationalite.isEmpty()) {
            nationaliteRepository.findById(Long.parseLong(nationalite)).ifPresent(demandeur::setNationalite);
        }
        demandeur = demandeurService.createDemandeur(demandeur);

        com.visa.transformable.model.Passeport passeport = new com.visa.transformable.model.Passeport();
        passeport.setDemandeur(demandeur);
        passeport.setNumeroPasseport(numero_passeport);
        if (date_delivrance != null && !date_delivrance.isEmpty())
            passeport.setDateDelivrance(java.sql.Date.valueOf(date_delivrance));
        if (date_expiration != null && !date_expiration.isEmpty())
            passeport.setDateExpiration(java.sql.Date.valueOf(date_expiration));
        passeport.setPaysDelivrance(pays_delivrance);
        passeport = passeportService.createPasseport(passeport);

        com.visa.transformable.model.Visa visa = new com.visa.transformable.model.Visa();
        visa.setReference(reference_visa);
        visa.setPasseport(passeport);
        if (date_entree != null && !date_entree.isEmpty())
            visa.setDateDebut(java.sql.Date.valueOf(date_entree));
        if (date_expiration_visa != null && !date_expiration_visa.isEmpty())
            visa.setDateFin(java.sql.Date.valueOf(date_expiration_visa));
        visa = visaService.createVisa(visa);

        session.setAttribute("currentDemandeurId", demandeur.getId());
        session.setAttribute("currentPasseportId", passeport.getId());
        session.setAttribute("currentVisaId", visa.getId());

        return "redirect:/step3-typeVisa";
    }

    @GetMapping("/step3-typeVisa")
    public String step3TypeVisa() {
        return "step3-typeVisa";
    }

    @PostMapping("/step4-documents")
    public String handleTypeVisa(@RequestParam("type_visa") String typeVisa, Model model, HttpSession session) {
        // Tu peux stocker le choix en session ou dans le modèle si besoin
        model.addAttribute("typeVisaChoisi", typeVisa);
        session.setAttribute("currentTypeVisa", typeVisa);
        Long currentVisaId = (Long) session.getAttribute("currentVisaId");
        if (currentVisaId != null) {
            visaRepository.findById(currentVisaId).ifPresent(visa -> {
                String libelle = typeVisa.trim().toLowerCase();
                com.visa.transformable.model.TypeVisa typeVisaEntity = typeVisaRepository.findByLibelle(libelle)
                        .orElseGet(() -> {
                            com.visa.transformable.model.TypeVisa nouveauTypeVisa = new com.visa.transformable.model.TypeVisa();
                            nouveauTypeVisa.setLibelle(libelle);
                            return typeVisaRepository.save(nouveauTypeVisa);
                        });
                visa.setTypeVisa(typeVisaEntity);
                visaRepository.save(visa);
            });
        }
        return "redirect:/step4-documents?type_visa=" + typeVisa;
    }

    private String normaliserTypeDemande(String type) {
        return type.trim().toLowerCase().replace(' ', '_');
    }

    @GetMapping("/step4-documents")
    public String step4Documents(@RequestParam(value = "type_visa", required = false) String typeVisa, Model model, HttpSession session) {
        if (typeVisa != null && !typeVisa.isEmpty()) {
            session.setAttribute("currentTypeVisa", typeVisa);
        }
        // Documents communs depuis la base
        java.util.List<com.visa.transformable.model.Document> docsCommuns = documentRepository.findByTypeCible(com.visa.transformable.model.Document.TypeCible.commun);
        model.addAttribute("docsCommuns", docsCommuns);
        // Documents spécifiques selon le type de visa
        java.util.List<com.visa.transformable.model.Document> docsSpecifiques = new java.util.ArrayList<>();
        if ("investisseur".equals(typeVisa)) {
            docsSpecifiques = documentRepository.findByTypeCible(com.visa.transformable.model.Document.TypeCible.investisseur);
        } else if ("travailleur".equals(typeVisa)) {
            docsSpecifiques = documentRepository.findByTypeCible(com.visa.transformable.model.Document.TypeCible.travailleur);
        }
        model.addAttribute("docsSpecifiques", docsSpecifiques);
        model.addAttribute("typeVisa", typeVisa);
        return "step4-documents";
    }

    
@PostMapping("/step4-documents/validation")
    public String validerDocuments(
    @RequestParam(value = "documents", required = false) java.util.List<Long> documentsCoches,
    @RequestParam("type_visa") String typeVisa,
    Model model,
    HttpSession session
) {
    session.setAttribute("currentTypeVisa", typeVisa);
    // Récupérer tous les documents obligatoires (communs + spécifiques)
    java.util.List<com.visa.transformable.model.Document> obligatoires = documentRepository.findByObligatoireTrueAndTypeCible(com.visa.transformable.model.Document.TypeCible.commun);
    if ("investisseur".equals(typeVisa)) {
        obligatoires.addAll(documentRepository.findByObligatoireTrueAndTypeCible(com.visa.transformable.model.Document.TypeCible.investisseur));
    } else if ("travailleur".equals(typeVisa)) {
        obligatoires.addAll(documentRepository.findByObligatoireTrueAndTypeCible(com.visa.transformable.model.Document.TypeCible.travailleur));
    }

    // Vérifier que tous les obligatoires sont cochés
    boolean tousCoches = documentsCoches != null && obligatoires.stream().allMatch(doc -> documentsCoches.contains(doc.getId()));
    if (!tousCoches) {
        model.addAttribute("erreur", "Tous les documents obligatoires doivent être cochés.");
        // Recharge la page avec les listes de documents
        model.addAttribute("docsCommuns", documentRepository.findByTypeCible(com.visa.transformable.model.Document.TypeCible.commun));
        if ("investisseur".equals(typeVisa)) {
            model.addAttribute("docsSpecifiques", documentRepository.findByTypeCible(com.visa.transformable.model.Document.TypeCible.investisseur));
        } else if ("travailleur".equals(typeVisa)) {
            model.addAttribute("docsSpecifiques", documentRepository.findByTypeCible(com.visa.transformable.model.Document.TypeCible.travailleur));
        }
        model.addAttribute("typeVisa", typeVisa);
        return "step4-documents";
    }
    session.setAttribute("currentSelectedDocumentIds", documentsCoches);
    return "redirect:/step5-confirmation";
}

    @GetMapping("/step5-confirmation")
    public String step5Confirmation(Model model, HttpSession session, @RequestParam(value = "error", required = false) String error) {
        populateConfirmationModel(model, session);
        if (error != null && !error.isEmpty()) {
            model.addAttribute("error", error);
        }
        return "step5-confirmation";
    }

    private void populateConfirmationModel(Model model, HttpSession session) {
        Long demandeurId = (Long) session.getAttribute("currentDemandeurId");
        Long passeportId = (Long) session.getAttribute("currentPasseportId");
        Long visaId = (Long) session.getAttribute("currentVisaId");
        String typeVisa = (String) session.getAttribute("currentTypeVisa");
        java.util.List<Long> selectedDocumentIds = (java.util.List<Long>) session.getAttribute("currentSelectedDocumentIds");

        com.visa.transformable.model.Demandeur demandeur = demandeurId != null
                ? demandeurRepository.findById(demandeurId).orElse(null)
                : null;
        com.visa.transformable.model.Passeport passeport = passeportId != null
                ? passeportRepository.findById(passeportId).orElse(null)
                : null;
        com.visa.transformable.model.Visa visa = visaId != null
                ? visaRepository.findById(visaId).orElse(null)
                : null;

        java.util.List<com.visa.transformable.model.Document> documentsSelectionnees = new java.util.ArrayList<>();
        java.util.List<com.visa.transformable.model.Document> documentsCommunsChoisis = new java.util.ArrayList<>();
        java.util.List<com.visa.transformable.model.Document> documentsSpecifiquesChoisis = new java.util.ArrayList<>();

        if (selectedDocumentIds != null) {
            for (Long id : selectedDocumentIds) {
                documentRepository.findById(id).ifPresent(doc -> {
                    documentsSelectionnees.add(doc);
                    if (doc.getTypeCible() == com.visa.transformable.model.Document.TypeCible.commun) {
                        documentsCommunsChoisis.add(doc);
                    } else {
                        documentsSpecifiquesChoisis.add(doc);
                    }
                });
            }
        }

        model.addAttribute("demandeur", demandeur);
        model.addAttribute("passeport", passeport);
        model.addAttribute("visa", visa);
        model.addAttribute("typeVisa", typeVisa);
        model.addAttribute("typeVisaLabel", buildTypeVisaLabel(typeVisa));
        model.addAttribute("documentsSelectionnees", documentsSelectionnees);
        model.addAttribute("documentsCommunsChoisis", documentsCommunsChoisis);
        model.addAttribute("documentsSpecifiquesChoisis", documentsSpecifiquesChoisis);
    }

    private String buildTypeVisaLabel(String typeVisa) {
        if (typeVisa == null || typeVisa.isEmpty()) {
            return "Visa";
        }
        return "Visa pour " + typeVisa.substring(0, 1).toUpperCase() + typeVisa.substring(1).toLowerCase();
    }

    // ==================== SPRINT 2 ENDPOINTS ====================

    @Autowired
    private CarteResidentRepository carteResidentRepository;

    /**
     * GET /sprint2-form - Afficher le formulaire Sprint 2 avec recherche + préremplissage
     */
    @GetMapping("/sprint2-form")
    public String sprint2Form(@RequestParam(value = "type", required = false) String type,
                             Model model,
                             HttpSession session) {
        java.util.List<SituationFamiliale> situations = situationFamilialeRepository.findAll();
        java.util.List<Nationalite> nationalites = nationaliteRepository.findAll();
        model.addAttribute("situationsFamiliales", situations);
        model.addAttribute("nationalites", nationalites);
        // Nettoyer les attributs Sprint 2 de session
        session.removeAttribute("sprint2.dto");
        session.removeAttribute("sprint2.typePerte");
        return "sprint2-form";
    }

    /**
     * GET /prefill - AJAX GET pour rechercher une personne existante
     * Retourne DuplicataDTO avec préremplissage OU 404 si non trouvée
     */
    @GetMapping("/prefill")
    @ResponseBody
    public ResponseEntity<DuplicataDTO> prefill(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "telephone", required = false) String telephone,
            @RequestParam(value = "numeroPasseport", required = false) String numeroPasseport,
            @RequestParam(value = "referenceVisa", required = false) String referenceVisa) {

        com.visa.transformable.model.Demandeur demandeur = null;

        // Chercher par email
        if (email != null && !email.isBlank()) {
            demandeur = demandeurRepository.findByEmail(email).orElse(null);
        }
        // Chercher par téléphone si pas trouvé
        if (demandeur == null && telephone != null && !telephone.isBlank()) {
            demandeur = demandeurRepository.findByTelephone(telephone).orElse(null);
        }
        // Chercher par passeport si pas trouvé
        if (demandeur == null && numeroPasseport != null && !numeroPasseport.isBlank()) {
            var passeport = passeportRepository.findByNumeroPasseport(numeroPasseport);
            if (passeport.isPresent()) {
                demandeur = passeport.get().getDemandeur();
            }
        }
        // Chercher par visa si pas trouvé
        if (demandeur == null && referenceVisa != null && !referenceVisa.isBlank()) {
            var visa = visaRepository.findByReference(referenceVisa);
            if (visa.isPresent() && visa.get().getPasseport() != null) {
                demandeur = visa.get().getPasseport().getDemandeur();
            }
        }

        // Personne non trouvée
        if (demandeur == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Personne trouvée - Construire DuplicataDTO
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

        // Récupérer passeport et visa
        var passeports = demandeur.getPasseports();
        if (!passeports.isEmpty()) {
            com.visa.transformable.model.Passeport passeport = passeports.get(0);
            dto.setNumeroPasseport(passeport.getNumeroPasseport());
            dto.setDateDelivrancePasseport(passeport.getDateDelivrance());
            dto.setDateExpirationPasseport(passeport.getDateExpiration());
            dto.setPaysDelivrancePasseport(passeport.getPaysDelivrance());

            // Récupérer visa
            var visas = passeport.getVisas();
            if (!visas.isEmpty()) {
                com.visa.transformable.model.Visa visa = visas.get(0);
                dto.setReferenceVisa(visa.getReference());
                dto.setDateDebutVisa(visa.getDateDebut());
                dto.setDateFinVisa(visa.getDateFin());
                dto.setIdVisa(visa.getId());
            }
        }

        return ResponseEntity.ok(dto);
    }

    /**
     * POST /sprint2/duplicata - Soumettre le formulaire Sprint 2
     * Branchement selon idDemandeur :
     * - Si ≠ null : créer 1 demande + confirmation
     * - Si = null : créer Demandeur + sauver en session + rediriger Step3
     */
    @PostMapping("/sprint2/duplicata")
    public String sprint2Duplicata(
            @RequestParam(value = "typePerte") String typePerte,
            @RequestParam(value = "idDemandeur", required = false) Long idDemandeur,
            @RequestParam(value = "nom") String nom,
            @RequestParam(value = "prenoms") String prenoms,
            @RequestParam(value = "dateNaissance") String dateNaissance,
            @RequestParam(value = "lieuNaissance") String lieuNaissance,
            @RequestParam(value = "idSituationFamiliale", required = false) Long idSituationFamiliale,
            @RequestParam(value = "idNationalite", required = false) Long idNationalite,
            @RequestParam(value = "adresse", required = false) String adresse,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "telephone") String telephone,
            @RequestParam(value = "numeroPasseport") String numeroPasseport,
            @RequestParam(value = "dateDelivrancePasseport", required = false) String dateDelivrancePasseport,
            @RequestParam(value = "dateExpirationPasseport", required = false) String dateExpirationPasseport,
            @RequestParam(value = "paysDelivrancePasseport", required = false) String paysDelivrancePasseport,
            @RequestParam(value = "referenceVisa") String referenceVisa,
            @RequestParam(value = "dateDebutVisa", required = false) String dateDebutVisa,
            @RequestParam(value = "dateFinVisa", required = false) String dateFinVisa,
            @RequestParam(value = "numeroNouveauPasseport", required = false) String numeroNouveauPasseport,
            @RequestParam(value = "dateDelivranceNouveauPasseport", required = false) String dateDelivranceNouveauPasseport,
            @RequestParam(value = "dateExpirationNouveauPasseport", required = false) String dateExpirationNouveauPasseport,
            @RequestParam(value = "paysDelivranceNouveauPasseport", required = false) String paysDelivranceNouveauPasseport,
            @RequestParam(value = "idCarteResident", required = false) Long idCarteResident,
            Model model,
            HttpSession session) {

        try {
            // Construire DuplicataDTO
            DuplicataDTO dto = new DuplicataDTO();
            dto.setIdDemandeur(idDemandeur);
            dto.setTypePerte(typePerte);
            dto.setNom(nom);
            dto.setPrenoms(prenoms);
            if (dateNaissance != null && !dateNaissance.isBlank()) {
                dto.setDateNaissance(java.sql.Date.valueOf(dateNaissance));
            }
            dto.setLieuNaissance(lieuNaissance);
            dto.setIdSituationFamiliale(idSituationFamiliale);
            dto.setIdNationalite(idNationalite);
            dto.setAdresse(adresse);
            dto.setEmail(email);
            dto.setTelephone(telephone);
            dto.setNumeroPasseport(numeroPasseport);
            if ("passeport_perdu".equals(typePerte) && 
                (numeroNouveauPasseport == null || numeroNouveauPasseport.isBlank())) {
                model.addAttribute("erreur", "Nouveau numéro de passeport obligatoire.");
                return "sprint2-form";
            }
            if (dateDelivrancePasseport != null && !dateDelivrancePasseport.isBlank()) {
                dto.setDateDelivrancePasseport(java.sql.Date.valueOf(dateDelivrancePasseport));
            }
            if (dateExpirationPasseport != null && !dateExpirationPasseport.isBlank()) {
                dto.setDateExpirationPasseport(java.sql.Date.valueOf(dateExpirationPasseport));
            }
            dto.setPaysDelivrancePasseport(paysDelivrancePasseport);
            dto.setReferenceVisa(referenceVisa);
            if (dateDebutVisa != null && !dateDebutVisa.isBlank()) {
                dto.setDateDebutVisa(java.sql.Date.valueOf(dateDebutVisa));
            }
            if (dateFinVisa != null && !dateFinVisa.isBlank()) {
                dto.setDateFinVisa(java.sql.Date.valueOf(dateFinVisa));
            }

            // CAS 1 : Personne EXISTANTE (idDemandeur ≠ null)
            if (idDemandeur != null) {            dto.setNumeroNouveauPasseport(numeroNouveauPasseport);
            if (dateDelivranceNouveauPasseport != null && !dateDelivranceNouveauPasseport.isBlank()) {
                dto.setDateDelivranceNouveauPasseport(java.sql.Date.valueOf(dateDelivranceNouveauPasseport));
            }
            if (dateExpirationNouveauPasseport != null && !dateExpirationNouveauPasseport.isBlank()) {
                dto.setDateExpirationNouveauPasseport(java.sql.Date.valueOf(dateExpirationNouveauPasseport));
            }
            dto.setPaysDelivranceNouveauPasseport(paysDelivranceNouveauPasseport);
            dto.setIdCarteResident(idCarteResident);

                // Appeler createDemandeSprint2() pour créer 1 seule demande
                com.visa.transformable.model.Demande demande = demandeService.createDemandeSprint2(dto);
                model.addAttribute("demande", demande);
                model.addAttribute("typePerte", typePerte);
                return "redirect:/sprint2-confirmation?demande=" + demande.getId();
            }

            // CAS 2 : Personne INCONNUE (idDemandeur = null)
            // Sauvegarder en session et rediriger vers Step3
            session.setAttribute("sprint2.dto", dto);
            session.setAttribute("sprint2.typePerte", typePerte);
            return "redirect:/step3-typeVisa";

        } catch (IllegalArgumentException e) {
            model.addAttribute("erreur", e.getMessage());
            return "sprint2-form";
        }
    }

    /**
     * GET /sprint2-confirmation - Afficher la confirmation Sprint 2
     */
    @GetMapping("/sprint2-confirmation")
    public String sprint2Confirmation(
            @RequestParam(value = "demande", required = false) Long demandeId,
            Model model) {
        if (demandeId != null) {
            var demande = demandeRepository.findById(demandeId).orElse(null);
            if (demande != null) {
                model.addAttribute("demande", demande);
                model.addAttribute("typePerte", demande.getTypeDemande().getLibelle());
            }
        }
        return "sprint2-confirmation";
    }
}

