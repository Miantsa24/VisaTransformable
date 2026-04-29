🎯 Vue d'ensemble
Au lieu d'avoir un Sprint 2 isolé avec sa propre page de choix, le Sprint 2 s'intègre directement dans Step1-type.jsp (Sprint 1).

Le formulaire de recherche + préremplissage est conservé (comme tu as dit), mais le flux change complètement selon que la personne existe ou non en base.

🚪 Trois boutons dans Step1-type.jsp
Bouton	Flux	Résultat
Nouveau titre	Sprint 1 classique	1 demande : nouveau_titre + demande_creee
Duplicata	Sprint 2 (perte carte résident)	1 ou 2 demandes selon si personne existe
Transfert de visa	Sprint 2 (perte passeport)	1 ou 2 demandes selon si personne existe


📋 FLUX DÉTAILLÉ : DUPLICATA (Perte de carte résident)
CAS A : Personne EXISTE en base ✅
[Step1-type.jsp : Clic "Duplicata"]
        ↓
[Sprint2-form.jsp - Recherche]
  • Saisir UN critère : Email | Téléphone | N° Passeport | Ref Visa
  • Clic "Rechercher"
        ↓ AJAX GET /prefill?email=...
[200 OK : DuplicataDTO avec toutes les données]
        ↓
[Sprint2-form.jsp - Formulaire PRÉREMPLI]
  • État civil : rempli (éditable)
  • Passeport : rempli (éditable)
  • Visa : rempli (éditable)
  • Utilisateur peut modifier si besoin
        ↓
[Clic "Soumettre"]
        ↓ POST /sprint2/duplicata (avec idDemandeur ≠ null)
[Backend : createDemandeSprint2]
  → Crée 1 seule demande
        ↓
[Sprint2-confirmation.jsp]
  • Affiche succès
  • Statut final : demande_creee

✅ BASE : 1 ligne
   type_demande=duplicata, statut_demande=demande_creee

CAS B : Personne N'EXISTE PAS en base ❌
[Step1-type.jsp : Clic "Duplicata"]
        ↓
[Sprint2-form.jsp - Recherche]
  • Saisir critères (email, tél, etc.)
  • Clic "Rechercher"
        ↓ AJAX GET /prefill?email=...
[404 Not Found]
        ↓
[Sprint2-form.jsp - Formulaire VIERGE]
  • Utilisateur remplit TOUT :
    - État civil (nom*, prénoms*, date_naissance*, etc.)
    - Passeport (numéro*, dates, etc.)
    - Visa (référence*, dates, etc.)
        ↓
[Clic "Soumettre"]
        ↓ POST /sprint2/duplicata (avec idDemandeur = null)
[Backend : createDemandeSprint2 détecte idDemandeur=null]
  → Crée Demandeur
  → Redirige vers Step3 (choix type visa) ← IMPORTANT
        ↓
[Step3-typeVisa.jsp - Choix Investisseur | Travailleur]
  (Même logique que Sprint 1)
        ↓
[Step4-documents.jsp - Checkboxes documents]
  (Même logique que Sprint 1 : tous les obligatoires cochés)
        ↓
[Clic "Enregistrer"]
        ↓ POST /backoffice/demande
[Backend détecte : typePerte=carte_resident_perdue + documents valides]
  → Crée 2 demandes :
    • Demande 1 : nouveau_titre + approuvee (titre "prêt à dupliquer")
    • Demande 2 : duplicata + demande_creee (duplicata en attente)
        ↓
[Step5-confirmation.jsp OU success.jsp]
  • Affiche succès avec 2 demandes

✅ BASE : 2 lignes
   Ligne 1 : type_demande=nouveau_titre, statut_demande=approuvee
   Ligne 2 : type_demande=duplicata, statut_demande=demande_creee


📋 FLUX DÉTAILLÉ : TRANSFERT DE VISA (Perte de passeport)
CAS A : Personne EXISTE en base ✅
[Step1-type.jsp : Clic "Transfert de visa"]
        ↓
[Sprint2-form.jsp - Recherche + Préremplissage]
  (Même logique que Duplicata A)
        ↓
[Sprint2-form.jsp - Formulaire PRÉREMPLI]
  • État civil : rempli
  • ANCIEN Passeport : rempli
  • Visa : rempli
  
  ⭐ NOUVEAU champ :
  • Nouveau N° Passeport* : VIDE, OBLIGATOIRE à saisir
        ↓
[Utilisateur saisit nouveau numéro de passeport]
        ↓
[Clic "Soumettre"]
        ↓ POST /sprint2/duplicata (avec numeroNouveauPasseport)
[Backend : createDemandeSprint2]
  → Crée Nouveau Passeport
  → Transfère Visa au nouveau passeport
  → Crée 1 demande transfert_visa
        ↓
[Sprint2-confirmation.jsp]
  • Affiche : Ancien PP → Nouveau PP (transfert OK)
  • Statut final : demande_creee

✅ BASE : 1 ligne
   type_demande=transfert_visa, statut_demande=demande_creee
   + Visa transféré au nouveau passeport

CAS B : Personne N'EXISTE PAS en base ❌
[Step1-type.jsp : Clic "Transfert de visa"]
        ↓
[Sprint2-form.jsp - Recherche]
  • Pas trouvée → 404
        ↓
[Sprint2-form.jsp - Formulaire VIERGE]
  • État civil à saisir
  • ANCIEN Passeport à saisir
  • Visa à saisir
  • NOUVEAU Passeport* à saisir (OBLIGATOIRE)
        ↓
[Clic "Soumettre"]
        ↓ POST /sprint2/duplicata (avec idDemandeur=null + numeroNouveauPasseport)
[Backend : createDemandeSprint2]
  → Crée Demandeur
  → Redirige vers Step3
        ↓
[Step3-typeVisa.jsp + Step4-documents.jsp]
  (Même logique que Duplicata B)
        ↓
[Clic "Enregistrer"]
        ↓ POST /backoffice/demande
[Backend crée 2 demandes]
  • Demande 1 : nouveau_titre + approuvee
  • Demande 2 : transfert_visa + demande_creee
  + Visa transféré au nouveau passeport
        ↓
[Confirmation]

✅ BASE : 2 lignes
   Ligne 1 : type_demande=nouveau_titre, statut_demande=approuvee
   Ligne 2 : type_demande=transfert_visa, statut_demande=demande_creee
   + Visa transféré

🔑 Points clés du flux
Aspect	Détail
Point d'entrée	step1-type.jsp (3 boutons)
Si personne existe	1 demande + statut demande_creee
Si personne n'existe	2 demandes : nouveau_titre (approuvee) + duplicata/transfert (demande_creee)
Nouveau passeport	Obligatoire pour transfert de visa (même si personne existe)
Documents	Validés uniquement si personne n'existe (Sprint 1)
Redirection	Si idDemandeur=null → Step3-typeVisa → Step4-documents
Avantage du flux	Crée un titre approuvé qu'on peut dupliquer/transférer immédiatement

⚠️ Modifications nécessaires
Backend (Dev 1) - À MODIFIER
createDemandeSprint2(DuplicataDTO dto) :

❌ ACTUELLEMENT :
  - Crée 1 demande toujours avec statut approuvee

✅ À FAIRE :
  - Si idDemandeur ≠ null → Crée 1 demande + statut demande_creee
  - Si idDemandeur = null :
    → Crée Demandeur d'abord
    → NE PAS créer la demande ici
    → Retourner le Demandeur créé pour passer à Step3
    → Step4 doit créer les 2 demandes (nouveau_titre + duplicata/transfert)

Frontend (Dev 2) - TOI
Pages à créer/modifier :
1. step1-type.jsp : Ajouter boutons "Duplicata" et "Transfert de visa"
2. sprint2-form.jsp : Section recherche + Section formulaire
   - Recherche : AJAX /prefill
   - Formulaire : Préremplissage OR Vierge
3. sprint2-confirmation.jsp : Affichage succès
4. Modifier pageController.java ou créer contrôleur dédié

✅ Résumé logique du flux
Step1-type.jsp : 3 boutons (Nouveau titre | Duplicata | Transfert visa)
Sprint2-form.jsp : Recherche + Formulaire
Branching :
Si personne existe : Confirmation directe → 1 demande
Si personne n'existe : Step3 → Step4 → 2 demandes
Résultat final : Demande(s) en base avec statuts appropriés

Ligne 1 : DemandeDocument créées avec docs cochés (pour historique)
Ligne 2 : Même DemandeDocuments 
