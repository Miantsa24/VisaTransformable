// package com.visa.transformable.service;

// import com.visa.transformable.dto.DuplicataDTO;
// import com.visa.transformable.model.*;
// import com.visa.transformable.repository.*;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.ArgumentCaptor;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.sql.Date;
// import java.time.LocalDate;
// import java.util.List;
// import java.util.Optional;
// import java.util.Set;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class DemandeServiceSprint2Test {

//     @Mock private DemandeurRepository demandeurRepository;
//     @Mock private SituationFamilialeRepository situationFamilialeRepository;
//     @Mock private NationaliteRepository nationaliteRepository;
//     @Mock private PasseportRepository passeportRepository;
//     @Mock private VisaRepository visaRepository;
//     @Mock private DemandeRepository demandeRepository;
//     @Mock private DocumentRepository documentRepository;
//     @Mock private DemandeDocumentRepository demandeDocumentRepository;
//     @Mock private StatutDemandeRepository statutDemandeRepository;
//     @Mock private TypeDemandeRepository typeDemandeRepository;
//     @Mock private CarteResidentRepository carteResidentRepository;
//     @Mock private HistoStatutDemandeRepository histoStatutDemandeRepository;

//     @InjectMocks
//     private DemandeService service;

//     @Test
//     void testCreateDemandeSprint2RejectsNullIdDemandeur() {
//         DuplicataDTO dto = baseDto();
//         dto.setIdDemandeur(null);  // NULL - personne INCONNUE
//         dto.setTypePerte("passeport_perdu");

//         // createDemandeSprint2() doit lever une exception pour personne inconnue
//         IllegalArgumentException ex = assertThrows(
//             IllegalArgumentException.class,
//             () -> service.createDemandeSprint2(dto)
//         );

//         assertTrue(ex.getMessage().contains("Demandeur introuvable"));
//         verifyNoInteractions(demandeRepository);
//     }

//     @Test
//     void testCreateDemandeSprint2FailsWhenTypePerteMissing() {
//         DuplicataDTO dto = baseDto();
//         dto.setIdDemandeur(10L);  // Personne EXISTANTE
//         dto.setTypePerte(null);   // Type de perte MANQUANT

//         IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.createDemandeSprint2(dto));

//         assertEquals("Type de perte obligatoire", ex.getMessage());
//         verifyNoInteractions(demandeRepository);
//     }

//     @Test
//     void testCreateDemandeSprint2FailsWhenNewPassportMissingForPassportLoss() {
//         DuplicataDTO dto = baseDto();
//         dto.setIdDemandeur(10L);
//         dto.setTypePerte("passeport_perdu");
//         dto.setNumeroNouveauPasseport(null);  // Nouveau passeport MANQUANT

//         Demandeur demandeur = new Demandeur();
//         demandeur.setId(10L);

//         Passeport basePasseport = new Passeport();
//         basePasseport.setId(20L);

//         Visa baseVisa = new Visa();
//         baseVisa.setId(30L);
//         baseVisa.setPasseport(basePasseport);

//         TypeDemande transfertVisa = new TypeDemande();
//         transfertVisa.setId(5L);
//         transfertVisa.setLibelle("transfert_visa");

//         StatutDemande demandeCreee = new StatutDemande();
//         demandeCreee.setId(7L);
//         demandeCreee.setLibelle("demande_creee");

//         when(demandeurRepository.findById(10L)).thenReturn(Optional.of(demandeur));
//         when(passeportRepository.findByDemandeurIdAndNumeroPasseport(10L, "P123")).thenReturn(Optional.of(basePasseport));
//         when(visaRepository.findByPasseportIdAndReference(20L, "V123")).thenReturn(Optional.of(baseVisa));
//         when(typeDemandeRepository.findByLibelle("transfert_visa")).thenReturn(Optional.of(transfertVisa));
//         when(statutDemandeRepository.findByLibelle("demande_creee")).thenReturn(Optional.of(demandeCreee));

//         IllegalArgumentException ex = assertThrows(
//             IllegalArgumentException.class,
//             () -> service.createDemandeSprint2(dto)
//         );

//         assertEquals("Nouveau numéro de passeport obligatoire", ex.getMessage());
//         verifyNoInteractions(demandeRepository);
//     }

//     // ==================== TEST 1 : Personne EXISTANTE ====================
//     @Test
//     void testCreateDemandeSprint2ExistingPersonPassportLost() {
//         // INPUT : DuplicataDTO avec idDemandeur ≠ null
//         DuplicataDTO dto = baseDto();
//         dto.setIdDemandeur(10L);  // Personne EXISTANTE
//         dto.setTypePerte("passeport_perdu");
//         dto.setNumeroNouveauPasseport("PP-NEW-999");
//         dto.setIdVisaOrigine(30L);

//         // Setup mocks pour personne existante
//         Demandeur existingDemandeur = new Demandeur();
//         existingDemandeur.setId(10L);
//         existingDemandeur.setNom("Doe");
//         existingDemandeur.setPrenoms("John");

//         Passeport basePasseport = new Passeport();
//         basePasseport.setId(20L);
//         basePasseport.setNumeroPasseport("P123");

//         Visa baseVisa = new Visa();
//         baseVisa.setId(30L);
//         baseVisa.setReference("V123");
//         baseVisa.setPasseport(basePasseport);

//         Passeport nouveauPasseport = new Passeport();
//         nouveauPasseport.setId(40L);
//         nouveauPasseport.setNumeroPasseport("PP-NEW-999");

//         TypeDemande transfertVisa = new TypeDemande();
//         transfertVisa.setId(5L);
//         transfertVisa.setLibelle("transfert_visa");

//         StatutDemande demandeCreee = new StatutDemande();
//         demandeCreee.setId(7L);
//         demandeCreee.setLibelle("demande_creee");

//         when(demandeurRepository.findById(10L)).thenReturn(Optional.of(existingDemandeur));
//         when(passeportRepository.findByDemandeurIdAndNumeroPasseport(10L, "P123")).thenReturn(Optional.of(basePasseport));
//         when(visaRepository.findByPasseportIdAndReference(20L, "V123")).thenReturn(Optional.of(baseVisa));
//         when(visaRepository.findById(30L)).thenReturn(Optional.of(baseVisa));
//         when(passeportRepository.save(any(Passeport.class))).thenReturn(nouveauPasseport);
//         when(visaRepository.save(any(Visa.class))).thenReturn(baseVisa);
//         when(typeDemandeRepository.findByLibelle("transfert_visa")).thenReturn(Optional.of(transfertVisa));
//         when(statutDemandeRepository.findByLibelle("demande_creee")).thenReturn(Optional.of(demandeCreee));

//         Demande savedDemande = new Demande();
//         savedDemande.setId(100L);
//         savedDemande.setDemandeur(existingDemandeur);
//         savedDemande.setVisa(baseVisa);
//         savedDemande.setTypeDemande(transfertVisa);
//         savedDemande.setTypePerte(Demande.TypePerte.passeport_perdu);
//         savedDemande.setStatutDemande(demandeCreee);
//         savedDemande.setNouveauPasseport(nouveauPasseport);

//         when(demandeRepository.save(any(Demande.class))).thenReturn(savedDemande);
//         when(histoStatutDemandeRepository.save(any(HistoStatutDemande.class))).thenAnswer(invocation -> invocation.getArgument(0));

//         // EXECUTION
//         Demande demande = service.createDemandeSprint2(dto);

//         // VERIFICATION - Output : 1 demande créée
//         assertNotNull(demande);
//         assertEquals(100L, demande.getId());

//         // Vérifier : Type = duplicata OU transfert_visa (selon typePerte)
//         assertEquals(transfertVisa, demande.getTypeDemande());
//         assertEquals("transfert_visa", demande.getTypeDemande().getLibelle());

//         // Vérifier : Statut = demande_creee
//         assertEquals(demandeCreee, demande.getStatutDemande());
//         assertEquals("demande_creee", demande.getStatutDemande().getLibelle());

//         // Vérifier : Documents = optionnels (pas testé ici, c'est une logique du contrôleur)
//         // Vérifier : Nouveau passeport créé si passeport_perdu
//         assertNotNull(demande.getNouveauPasseport());
//         assertEquals("PP-NEW-999", demande.getNouveauPasseport().getNumeroPasseport());

//         // Vérifier : Une et UNE SEULE demande créée
//         ArgumentCaptor<Demande> captor = ArgumentCaptor.forClass(Demande.class);
//         verify(demandeRepository).save(captor.capture());
//         List<Demande> savedDemandes = captor.getAllValues();
//         assertEquals(1, savedDemandes.size());
//     }

//     @Test
//     void testCreateDemandeSprint2ExistingPersonCardLost() {
//         // INPUT : DuplicataDTO avec idDemandeur ≠ null pour carte perdue
//         DuplicataDTO dto = baseDto();
//         dto.setIdDemandeur(10L);  // Personne EXISTANTE
//         dto.setTypePerte("carte_resident_perdue");
//         dto.setIdCarteResident(50L);

//         // Setup mocks
//         Demandeur existingDemandeur = new Demandeur();
//         existingDemandeur.setId(10L);

//         Passeport basePasseport = new Passeport();
//         basePasseport.setId(20L);

//         Visa baseVisa = new Visa();
//         baseVisa.setId(30L);
//         baseVisa.setPasseport(basePasseport);

//         CarteResident carteResident = new CarteResident();
//         carteResident.setId(50L);

//         TypeDemande duplicata = new TypeDemande();
//         duplicata.setId(5L);
//         duplicata.setLibelle("duplicata");

//         StatutDemande demandeCreee = new StatutDemande();
//         demandeCreee.setId(7L);
//         demandeCreee.setLibelle("demande_creee");

//         when(demandeurRepository.findById(10L)).thenReturn(Optional.of(existingDemandeur));
//         when(passeportRepository.findByDemandeurIdAndNumeroPasseport(10L, "P123")).thenReturn(Optional.of(basePasseport));
//         when(visaRepository.findByPasseportIdAndReference(20L, "V123")).thenReturn(Optional.of(baseVisa));
//         when(carteResidentRepository.findById(50L)).thenReturn(Optional.of(carteResident));
//         when(typeDemandeRepository.findByLibelle("duplicata")).thenReturn(Optional.of(duplicata));
//         when(statutDemandeRepository.findByLibelle("demande_creee")).thenReturn(Optional.of(demandeCreee));

//         Demande savedDemande = new Demande();
//         savedDemande.setId(100L);
//         savedDemande.setDemandeur(existingDemandeur);
//         savedDemande.setVisa(baseVisa);
//         savedDemande.setTypeDemande(duplicata);
//         savedDemande.setTypePerte(Demande.TypePerte.carte_resident_perdue);
//         savedDemande.setStatutDemande(demandeCreee);
//         savedDemande.setCarteResident(carteResident);

//         when(demandeRepository.save(any(Demande.class))).thenReturn(savedDemande);
//         when(histoStatutDemandeRepository.save(any(HistoStatutDemande.class))).thenAnswer(invocation -> invocation.getArgument(0));

//         // EXECUTION
//         Demande demande = service.createDemandeSprint2(dto);

//         // VERIFICATION - Output : 1 demande créée
//         assertNotNull(demande);
//         assertEquals(100L, demande.getId());

//         // Vérifier : Type = duplicata OU transfert_visa (selon typePerte)
//         assertEquals(duplicata, demande.getTypeDemande());
//         assertEquals("duplicata", demande.getTypeDemande().getLibelle());

//         // Vérifier : Statut = demande_creee
//         assertEquals(demandeCreee, demande.getStatutDemande());
//         assertEquals("demande_creee", demande.getStatutDemande().getLibelle());

//         // Vérifier : Carte de résident associée
//         assertEquals(carteResident, demande.getCarteResident());

//         // Vérifier : Une et UNE SEULE demande créée
//         ArgumentCaptor<Demande> captor = ArgumentCaptor.forClass(Demande.class);
//         verify(demandeRepository).save(captor.capture());
//         List<Demande> savedDemandes = captor.getAllValues();
//         assertEquals(1, savedDemandes.size());
//     }

//     // ==================== TEST 2 : Personne INCONNUE ====================
//     @Test
//     void testCreateTwoDemandesUnknownPersonPassportLost() {
//         // INPUT : DuplicataDTO avec idDemandeur = null
//         DuplicataDTO dto = baseDto();
//         dto.setIdDemandeur(null);  // Personne INCONNUE
//         dto.setTypePerte("passeport_perdu");
//         dto.setNumeroNouveauPasseport("PP-NEW-888");
//         dto.setIdSituationFamiliale(1L);
//         dto.setIdNationalite(2L);

//         List<Long> documentsCoches = List.of(10L, 11L);  // 2 documents sélectionnés

//         // Setup mocks - Demandeur
//         SituationFamiliale sf = new SituationFamiliale();
//         sf.setId(1L);
//         Nationalite nat = new Nationalite();
//         nat.setId(2L);
//         Demandeur savedDemandeur = new Demandeur();
//         savedDemandeur.setId(100L);

//         // Setup mocks - Passeport et Visa
//         Passeport passeport = new Passeport();
//         passeport.setId(200L);
//         passeport.setNumeroPasseport("P123");

//         TypeVisa typeVisa = new TypeVisa();
//         typeVisa.setId(1L);
//         typeVisa.setLibelle("travailleur");

//         Visa visa = new Visa();
//         visa.setId(300L);
//         visa.setReference("V123");
//         visa.setPasseport(passeport);
//         visa.setTypeVisa(typeVisa);

//         // Setup mocks - Type demande et Statut
//         TypeDemande nouveauTitre = new TypeDemande();
//         nouveauTitre.setId(1L);
//         nouveauTitre.setLibelle("nouveau_titre");

//         TypeDemande transfertVisa = new TypeDemande();
//         transfertVisa.setId(2L);
//         transfertVisa.setLibelle("transfert_visa");

//         StatutDemande approuvee = new StatutDemande();
//         approuvee.setId(6L);
//         approuvee.setLibelle("approuvee");

//         StatutDemande demandeCreee = new StatutDemande();
//         demandeCreee.setId(7L);
//         demandeCreee.setLibelle("demande_creee");

//         // Setup mocks - Documents
//         Document doc1 = new Document();
//         doc1.setId(10L);
//         doc1.setLibelle("Pièce identité");
//         doc1.setObligatoire(true);

//         Document doc2 = new Document();
//         doc2.setId(11L);
//         doc2.setLibelle("Certificat résidence");
//         doc2.setObligatoire(true);

//         Passeport newPassport = new Passeport();
//         newPassport.setId(400L);
//         newPassport.setNumeroPasseport("PP-NEW-888");

//         when(situationFamilialeRepository.findById(1L)).thenReturn(Optional.of(sf));
//         when(nationaliteRepository.findById(2L)).thenReturn(Optional.of(nat));
//         when(demandeurRepository.save(any(Demandeur.class))).thenReturn(savedDemandeur);
//         when(passeportRepository.findByDemandeurIdAndNumeroPasseport(100L, "P123")).thenReturn(Optional.of(passeport));
//         when(visaRepository.findByPasseportIdAndReference(200L, "V123")).thenReturn(Optional.of(visa));
//         when(documentRepository.findByObligatoireTrueAndTypeCible(Document.TypeCible.commun))
//                 .thenReturn(List.of(doc1));
//         when(documentRepository.findByObligatoireTrueAndTypeCible(Document.TypeCible.travailleur))
//                 .thenReturn(List.of(doc2));
//         when(documentRepository.findById(10L)).thenReturn(Optional.of(doc1));
//         when(documentRepository.findById(11L)).thenReturn(Optional.of(doc2));
//         when(typeDemandeRepository.findByLibelle("nouveau_titre")).thenReturn(Optional.of(nouveauTitre));
//         when(typeDemandeRepository.findByLibelle("transfert_visa")).thenReturn(Optional.of(transfertVisa));
//         when(statutDemandeRepository.findByLibelle("approuvee")).thenReturn(Optional.of(approuvee));
//         when(statutDemandeRepository.findByLibelle("demande_creee")).thenReturn(Optional.of(demandeCreee));
//         when(passeportRepository.save(any(Passeport.class))).thenReturn(newPassport);

//         Demande demande1 = new Demande();
//         demande1.setId(501L);
//         demande1.setDemandeur(savedDemandeur);
//         demande1.setVisa(visa);
//         demande1.setTypeDemande(nouveauTitre);
//         demande1.setStatutDemande(approuvee);

//         Demande demande2 = new Demande();
//         demande2.setId(502L);
//         demande2.setDemandeur(savedDemandeur);
//         demande2.setVisa(visa);
//         demande2.setTypeDemande(transfertVisa);
//         demande2.setTypePerte(Demande.TypePerte.passeport_perdu);
//         demande2.setStatutDemande(demandeCreee);
//         demande2.setNouveauPasseport(newPassport);

//         when(demandeRepository.save(any(Demande.class)))
//                 .thenReturn(demande1)
//                 .thenReturn(demande2);

//         when(demandeDocumentRepository.save(any(DemandeDocument.class)))
//                 .thenAnswer(invocation -> invocation.getArgument(0));

//         when(histoStatutDemandeRepository.save(any(HistoStatutDemande.class)))
//                 .thenAnswer(invocation -> invocation.getArgument(0));

//         // EXECUTION
//         List<Demande> demandes = service.createTwoDemandes(dto, documentsCoches, "passeport_perdu");

//         // VERIFICATION - Output : 2 demandes créées
//         assertNotNull(demandes);
//         assertEquals(2, demandes.size());

//         // Vérifier Demande 1
//         Demande d1 = demandes.get(0);
//         assertEquals(501L, d1.getId());
//         assertEquals(nouveauTitre, d1.getTypeDemande());
//         assertEquals("nouveau_titre", d1.getTypeDemande().getLibelle());
//         assertEquals(approuvee, d1.getStatutDemande());
//         assertEquals("approuvee", d1.getStatutDemande().getLibelle());

//         // Vérifier Demande 2
//         Demande d2 = demandes.get(1);
//         assertEquals(502L, d2.getId());
//         assertEquals(transfertVisa, d2.getTypeDemande());
//         assertEquals("transfert_visa", d2.getTypeDemande().getLibelle());
//         assertEquals(demandeCreee, d2.getStatutDemande());
//         assertEquals("demande_creee", d2.getStatutDemande().getLibelle());
//         assertEquals(Demande.TypePerte.passeport_perdu, d2.getTypePerte());

//         // Vérifier : Nouveau passeport (si typePerte=passeport_perdu)
//         assertNotNull(d2.getNouveauPasseport());
//         assertEquals("PP-NEW-888", d2.getNouveauPasseport().getNumeroPasseport());

//         // Vérifier : DemandeDocuments PARTAGÉS (même documents pour les 2)
//         ArgumentCaptor<DemandeDocument> docCaptor = ArgumentCaptor.forClass(DemandeDocument.class);
//         verify(demandeDocumentRepository, times(4)).save(docCaptor.capture());  // 2 docs * 2 demandes = 4 appels
//         List<DemandeDocument> demandeDocuments = docCaptor.getAllValues();

//         // Vérifier que les mêmes documents sont associés aux 2 demandes
//         List<DemandeDocument> d1Docs = demandeDocuments.stream()
//                 .filter(dd -> dd.getDemande().getId() == 501L).toList();
//         List<DemandeDocument> d2Docs = demandeDocuments.stream()
//                 .filter(dd -> dd.getDemande().getId() == 502L).toList();

//         assertEquals(2, d1Docs.size());
//         assertEquals(2, d2Docs.size());

//         // Vérifier que les IDs des documents sont identiques
//         Set<Long> d1DocIds = d1Docs.stream().map(dd -> dd.getDocument().getId()).collect(java.util.stream.Collectors.toSet());
//         Set<Long> d2DocIds = d2Docs.stream().map(dd -> dd.getDocument().getId()).collect(java.util.stream.Collectors.toSet());
//         assertEquals(d1DocIds, d2DocIds);
//     }

//     @Test
//     void testCreateTwoDemandesUnknownPersonCardLost() {
//         // INPUT : DuplicataDTO avec idDemandeur = null - carte résident perdue
//         DuplicataDTO dto = baseDto();
//         dto.setIdDemandeur(null);
//         dto.setTypePerte("carte_resident_perdue");
//         dto.setIdCarteResident(50L);

//         List<Long> documentsCoches = List.of(10L);

//         SituationFamiliale sf = new SituationFamiliale();
//         sf.setId(1L);
//         Nationalite nat = new Nationalite();
//         nat.setId(2L);
//         Demandeur savedDemandeur = new Demandeur();
//         savedDemandeur.setId(100L);

//         Passeport passeport = new Passeport();
//         passeport.setId(200L);

//         TypeVisa typeVisa = new TypeVisa();
//         typeVisa.setId(1L);
//         typeVisa.setLibelle("investisseur");

//         Visa visa = new Visa();
//         visa.setId(300L);
//         visa.setPasseport(passeport);
//         visa.setTypeVisa(typeVisa);

//         Document doc1 = new Document();
//         doc1.setId(10L);
//         doc1.setObligatoire(true);

//         CarteResident carteResident = new CarteResident();
//         carteResident.setId(50L);

//         TypeDemande nouveauTitre = new TypeDemande();
//         nouveauTitre.setId(1L);
//         nouveauTitre.setLibelle("nouveau_titre");

//         TypeDemande duplicata = new TypeDemande();
//         duplicata.setId(3L);
//         duplicata.setLibelle("duplicata");

//         StatutDemande approuvee = new StatutDemande();
//         approuvee.setId(6L);
//         approuvee.setLibelle("approuvee");

//         StatutDemande demandeCreee = new StatutDemande();
//         demandeCreee.setId(7L);
//         demandeCreee.setLibelle("demande_creee");

//         when(situationFamilialeRepository.findById(1L)).thenReturn(Optional.of(sf));
//         when(nationaliteRepository.findById(2L)).thenReturn(Optional.of(nat));
//         when(demandeurRepository.save(any(Demandeur.class))).thenReturn(savedDemandeur);
//         when(passeportRepository.findByDemandeurIdAndNumeroPasseport(100L, "P123")).thenReturn(Optional.of(passeport));
//         when(visaRepository.findByPasseportIdAndReference(200L, "V123")).thenReturn(Optional.of(visa));
//         when(documentRepository.findByObligatoireTrueAndTypeCible(Document.TypeCible.commun))
//                 .thenReturn(List.of(doc1));
//         when(documentRepository.findByObligatoireTrueAndTypeCible(Document.TypeCible.investisseur))
//                 .thenReturn(List.of());
//         when(documentRepository.findById(10L)).thenReturn(Optional.of(doc1));
//         when(typeDemandeRepository.findByLibelle("nouveau_titre")).thenReturn(Optional.of(nouveauTitre));
//         when(typeDemandeRepository.findByLibelle("duplicata")).thenReturn(Optional.of(duplicata));
//         when(statutDemandeRepository.findByLibelle("approuvee")).thenReturn(Optional.of(approuvee));
//         when(statutDemandeRepository.findByLibelle("demande_creee")).thenReturn(Optional.of(demandeCreee));
//         when(carteResidentRepository.findById(50L)).thenReturn(Optional.of(carteResident));

//         Demande demande1 = new Demande();
//         demande1.setId(601L);
//         demande1.setTypeDemande(nouveauTitre);
//         demande1.setStatutDemande(approuvee);

//         Demande demande2 = new Demande();
//         demande2.setId(602L);
//         demande2.setTypeDemande(duplicata);
//         demande2.setTypePerte(Demande.TypePerte.carte_resident_perdue);
//         demande2.setStatutDemande(demandeCreee);
//         demande2.setCarteResident(carteResident);

//         when(demandeRepository.save(any(Demande.class)))
//                 .thenReturn(demande1)
//                 .thenReturn(demande2);

//         when(demandeDocumentRepository.save(any(DemandeDocument.class)))
//                 .thenAnswer(invocation -> invocation.getArgument(0));

//         when(histoStatutDemandeRepository.save(any(HistoStatutDemande.class)))
//                 .thenAnswer(invocation -> invocation.getArgument(0));

//         // EXECUTION
//         List<Demande> demandes = service.createTwoDemandes(dto, documentsCoches, "carte_resident_perdue");

//         // VERIFICATION
//         assertNotNull(demandes);
//         assertEquals(2, demandes.size());

//         Demande d1 = demandes.get(0);
//         assertEquals("nouveau_titre", d1.getTypeDemande().getLibelle());
//         assertEquals("approuvee", d1.getStatutDemande().getLibelle());

//         Demande d2 = demandes.get(1);
//         assertEquals("duplicata", d2.getTypeDemande().getLibelle());
//         assertEquals("demande_creee", d2.getStatutDemande().getLibelle());
//         assertEquals(Demande.TypePerte.carte_resident_perdue, d2.getTypePerte());
//         assertEquals(carteResident, d2.getCarteResident());

//         // Vérifier : DemandeDocuments PARTAGÉS
//         ArgumentCaptor<DemandeDocument> docCaptor = ArgumentCaptor.forClass(DemandeDocument.class);
//         verify(demandeDocumentRepository, times(2)).save(docCaptor.capture());  // 1 doc * 2 demandes = 2 appels
//         List<DemandeDocument> demandeDocuments = docCaptor.getAllValues();
//         assertEquals(2, demandeDocuments.size());
//     }

//     @Test
//     void testCreateTwoDemandesRejectsExistingPerson() {
//         // Rejet si idDemandeur != null
//         DuplicataDTO dto = baseDto();
//         dto.setIdDemandeur(10L);  // Personne EXISTANTE - rejet !
//         dto.setTypePerte("passeport_perdu");

//         IllegalArgumentException ex = assertThrows(
//             IllegalArgumentException.class,
//             () -> service.createTwoDemandes(dto, List.of(10L), "passeport_perdu")
//         );

//         assertTrue(ex.getMessage().contains("createDemandeSprint2()"));
//         verifyNoInteractions(demandeRepository);
//     }

//     @Test
//     void testCreateTwoDemandesRejectsMissingObligatoryDocuments() {
//         // Rejet si documents obligatoires manquants
//         DuplicataDTO dto = baseDto();
//         dto.setIdDemandeur(null);
//         dto.setTypePerte("passeport_perdu");
//         dto.setNumeroNouveauPasseport("PP-NEW-999");

//         List<Long> documentsCoches = List.of(10L);  // Seulement 1 doc, mais 2 obligatoires

//         SituationFamiliale sf = new SituationFamiliale();
//         sf.setId(1L);
//         Nationalite nat = new Nationalite();
//         nat.setId(2L);
//         Demandeur savedDemandeur = new Demandeur();
//         savedDemandeur.setId(100L);

//         Passeport passeport = new Passeport();
//         passeport.setId(200L);

//         TypeVisa typeVisa = new TypeVisa();
//         typeVisa.setId(1L);
//         typeVisa.setLibelle("travailleur");

//         Visa visa = new Visa();
//         visa.setId(300L);
//         visa.setPasseport(passeport);
//         visa.setTypeVisa(typeVisa);

//         Document doc1 = new Document();
//         doc1.setId(10L);
//         doc1.setObligatoire(true);

//         Document doc2 = new Document();
//         doc2.setId(11L);  // Document obligatoire NON coché
//         doc2.setObligatoire(true);

//         when(situationFamilialeRepository.findById(1L)).thenReturn(Optional.of(sf));
//         when(nationaliteRepository.findById(2L)).thenReturn(Optional.of(nat));
//         when(demandeurRepository.save(any(Demandeur.class))).thenReturn(savedDemandeur);
//         when(passeportRepository.findByDemandeurIdAndNumeroPasseport(100L, "P123")).thenReturn(Optional.of(passeport));
//         when(visaRepository.findByPasseportIdAndReference(200L, "V123")).thenReturn(Optional.of(visa));
//         when(documentRepository.findByObligatoireTrueAndTypeCible(Document.TypeCible.commun))
//                 .thenReturn(List.of(doc1));
//         when(documentRepository.findByObligatoireTrueAndTypeCible(Document.TypeCible.travailleur))
//                 .thenReturn(List.of(doc2));

//         IllegalArgumentException ex = assertThrows(
//             IllegalArgumentException.class,
//             () -> service.createTwoDemandes(dto, documentsCoches, "passeport_perdu")
//         );

//         assertEquals("Tous les documents obligatoires doivent être cochés", ex.getMessage());
//         verifyNoInteractions(demandeRepository);
//     }

//     private DuplicataDTO baseDto() {
//         DuplicataDTO dto = new DuplicataDTO();
//         dto.setNom("Doe");
//         dto.setPrenoms("John");
//         dto.setDateNaissance(Date.valueOf(LocalDate.of(1990, 1, 1)));
//         dto.setLieuNaissance("Antananarivo");
//         dto.setTelephone("0320000000");
//         dto.setEmail("john.doe@example.com");
//         dto.setAdresse("Adresse");
//         dto.setIdSituationFamiliale(1L);
//         dto.setIdNationalite(2L);
//         dto.setDateDemande(Date.valueOf(LocalDate.now()));
//         dto.setNumeroPasseport("P123");
//         dto.setDateDelivrancePasseport(Date.valueOf(LocalDate.of(2020, 1, 1)));
//         dto.setDateExpirationPasseport(Date.valueOf(LocalDate.of(2030, 1, 1)));
//         dto.setPaysDelivrancePasseport("MG");
//         dto.setReferenceVisa("V123");
//         dto.setDateDebutVisa(Date.valueOf(LocalDate.of(2024, 1, 1)));
//         dto.setDateFinVisa(Date.valueOf(LocalDate.of(2025, 1, 1)));
//         dto.setDocumentsCoches(List.of());
//         return dto;
//     }
// }
