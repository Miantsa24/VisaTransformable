# TODO LIST — SPRINT 1 (BACKOFFICE UNIQUEMENT)

## Rôles:

* **Sprint1:**

  * **Team Lead** : Malala ETU003211
  * **Dev 1 (BackOffice - logique métier)** : Tojo ETU003362
  * **Dev 2 (BackOffice - interface & intégration)** : Alexandra ETU003306

---

# 🎯 Rôles et responsabilités ajustés

* **TL** : orchestration + validation métier + cohérence DB
* **Dev1** : construit le **cœur du système (DB + services + règles métier)**
* **Dev2** : construit le **wizard JSP + intégration avec contrôleurs**

---

# 📌 Tâches pour Sprint 1

---

## 🧠 TL (Team Lead)

1. **Initialisation projet**

   * Créer repo Git
   * Créer branches :

     * `main`
     * `staging`
     * `release`

2. **Validation métier**

   * Définir **documents obligatoires vs optionnels**
   * Définir valeurs initiales :

     * `statut_demande = demande_creee`
     * `type_demande = nouveau_titre`

3. **Découpage DB final**

   * Valider le script SQL
   * Vérifier :

     * cohérence des FK
     * contraintes
     * unicité

4. **Suivi dev**

   * Assignation des tâches
   * Vérification dépendances Dev1 → Dev2

5. **Code review & intégration**

   * Review PR
   * Test complet du workflow

6. **Tests métier**

   * document obligatoire manquant → rejet
   * visa expiré → rejet

---

## 👨‍💻 Dev1 — BackOffice (LOGIQUE MÉTIER + DB)

### 1. Création branche

* `feature/backoffice-core`

---

### 2. Base de données

* Intégrer le script MySQL
* Ajouter données de référence :

```sql
INSERT INTO situation_familiale(libelle) VALUES ('celibataire'); : celibatire, marie/mariee, divorce/divorcee, veuf/veuve

INSERT INTO nationalite(libelle) VALUES ('Francais'); : comme on veut : 5 donnees

INSERT INTO statut_demande(libelle) VALUES ('demande_creee'); : demande_creee, demande_rejetee

INSERT INTO type_demande(libelle) VALUES ('nouveau_titre'); : nouveau_titre, duplicatat(a prevoir uniquement)

INSERT INTO type_visa(libelle) VALUES ('investisseur'), ('travailleur');
```

* Insérer documents avec obligatoire TRUE/FALSE 

---

### 3. Mapping JPA

Créer entités :

* Demandeur
* Passeport
* Visa
* Demande
* Document
* DemandeDocument

---

### 4. Repository

Créer :

* DemandeRepository
* DemandeurRepository
* VisaRepository
* DocumentRepository
* DemandeDocumentRepository

---

### 5. Service métier

Créer `DemandeService`

#### Méthode :

```java
createDemande(DemandeDTO dto)
```

---

### Règles métier

1. Vérifier visa non expiré
2. Vérifier documents obligatoires
3. Créer :

   * demandeur
   * passeport
   * visa
   * demande
   * demande_document
   -> a inserer dans la base apres l'etape du formulaire : Etat civil : dans la table demandeur, Passeport : dans la table passeport, visa : dans la table visa
4. Affecter statut `demande_creee` si tout est ok (les documents obligatoires sont coches) : a inserer dans la base dans la table demande et demande_document

---

### 6. DTO

Créer `DemandeDTO` avec :

* données formulaire
* liste documents cochés

---

### 7. Contrôleur

```java
@PostMapping("/backoffice/demande")
```

---

### 8. Tests

* insertion complète
* rejet document manquant
* rejet visa expiré ou inexistant
* autres erreurs et scenarios possibles, ...

---

### 9. Commit & PR

* PR vers `main`

---

## 👩‍💻 Dev2 — BackOffice (JSP + FORMULAIRE)

### 1. Création branche

* `feature/backoffice-ui`

---

### 2. Structure JSP a mettre dans main/webapp/WEB-INF/jsp

Créer :

* `index.jsp`
* `step1-type.jsp`
* `step2-form.jsp`
* `step3-typeVisa.jsp`
* `step4-documents.jsp`
* `step5-confirmation.jsp`

---

### Etape 0 : `index.jsp`

* Page d'accueil + sidebar ou navbar 
    * menu : Faire une demande de visa long sejour qui affichera l'etape 1

### 3. Étape 1 : `step1-type.jsp`

* Boutons : 

  * Nouveau titre (actif) : redirigera vers etape 2
  * Duplicata (a prevoir juste)

---

### 4. Étape 2 — Formulaire

3 blocs avec chacun ses champs (voir dans comprehension.md) : tous dans le meme formulaire:

* état civil
* passeport
* visa

-> bouton enregistrer qui redirigera vers etape 3 (partie dev 2), insertion des informations dans base (partie dev 1)

---

### 5. Étape 3 — Type visa

* select :

  * investisseur
  * travailleur

  -> bouton choisir : redirigera vers etape 4

---

### 6. Étape 4 — Documents

* récupérer via contrôleur
* afficher :

  * commun
  * spécifique selon le type de visa selectionne dans etape 3

```html
<input type="checkbox" name="documents" value="id_doc">
```
Bouton enregistrer qui fera la soumission si toutes les regles respectees
---

### 7. Étape 5 — Soumission

POST vers :

```
/backoffice/demande
```

---

### 8. Navigation wizard

* session ou hidden inputs

---

### 9. Gestion erreurs

Afficher :

* document obligatoire manquant
* visa expiré ou inexistant

---

### 10. Contrôleur

```java
@GetMapping("/backoffice/demande/new")
```

---

### 11. Commit & PR

* PR vers `main`

---

==============================================================================================
==============================================================================================

# 📌 Tâches pour Sprint 2

---

* **Sprint1:**

  * **Team Lead** : Tojo ETU003362
  * **Dev 1 (BackOffice :LOGIQUE MÉTIER + DB)** : Alexandra ETU003306
  * **Dev 2 (BackOffice :JSP + FORMULAIRE)** :  Malala ETU003211


## 🧠 TL (Team Lead)

1. **Validation métier**

  * Confirmer les 2 cas du sprint 2 :

    * passeport perdu → transfert de visa vers nouveau passeport
    * carte de résident perdue → duplicata de carte de résident
  * Définir le statut initial du sprint 2 : `approuvee`
  * Valider les règles de préremplissage si la personne existe déjà
  * Valider le comportement si aucune donnée antérieure n’existe

2. **Cohérence métier / DB**

  * Vérifier l’impact sur le schéma existant
  * Définir s’il faut ajouter une table ou enrichir les tables actuelles pour le duplicata / transfert
  * Vérifier la cohérence des FK, des contraintes et de l’unicité avec le nouveau flux

3. **Suivi d’intégration**

  * Vérifier la compatibilité avec le wizard Sprint 1
  * S’assurer que le nouveau flux ne casse pas l’enregistrement des demandes de visa long séjour

4. **Tests métier**

  * personne existante → formulaire prérempli
  * personne inconnue → saisie complète from scratch
  * passeport perdu → nouveau numéro de passeport obligatoire
  * carte de résident perdue → duplication enregistrée en statut `approuvee`

---

## 👨‍💻 Dev1 — BackOffice (LOGIQUE MÉTIER + DB)

### 1. Création branche

* `feature/backoffice-sprint2-core`

---

### 2. Base de données

* Étendre le script SQL si nécessaire pour supporter le nouveau flux
* Ajouter les valeurs de référence utiles au sprint 2 :

```sql
INSERT INTO type_demande(libelle) VALUES ('duplicata'), ('transfert_visa');
INSERT INTO statut_demande(libelle) VALUES ('approuvee');
```

* Définir les données nécessaires pour mémoriser :

  * le type de perte
  * le lien vers le visa d’origine si transfert
  * le nouveau passeport si passeport perdu
  * la carte de résident dupliquée si carte de résident perdue

---

### 3. Mapping JPA

Compléter ou créer les entités nécessaires pour le sprint 2 :

* DemandeDuplicata / DemandeTransfertVisa si séparation métier nécessaire
* ou enrichissement de `Demande`
* Réutilisation de :

  * Demandeur
  * Passeport
  * Visa
  * Demande

---

### 4. Repository

Créer ou compléter :

* repository de recherche des personnes par pièces d’identité / passeport / visa
* repository de persistance du flux duplicata / transfert

---

### 5. Service métier

Étendre `DemandeService` avec **DEUX méthodes distinctes** selon le cas :

#### Méthode 1 - Personne EXISTANTE :

```java
createDemandeSprint2(DuplicataDTO dto)
```

Crée **1 SEULE demande** :
- Type : duplicata OU transfert_visa (selon typePerte)
- Statut : demande_creee

#### Méthode 2 - Personne INCONNUE (NEW - À CRÉER) :

```java
createTwoDemandes(DuplicataDTO dto, List<Long> documentsCoches, String typePerte)
```

Crée **2 demandes** après validation documents :
- Demande 1 : type=nouveau_titre, statut=approuvee
- Demande 2 : type=duplicata/transfert, statut=demande_creee
- DemandeDocuments : PARTAGÉS

---

### Règles métier

**Cas 1 : Personne EXISTANTE** (prefill ≠ null)
1. GET /prefill retourne idDemandeur
2. Appeler `createDemandeSprint2(DuplicataDTO)`
3. Crée 1 seule demande : duplicata OU transfert_visa
4. Statut : demande_creee
5. Confirmation directe

**Cas 2 : Personne INCONNUE** (prefill = 404)
1. Formulaire vide + Step3 (type visa) + Step4 (documents obligatoires)
2. POST /backoffice/demande détecte `sprint2.typePerte` en session
3. Appeler `createTwoDemandes(DuplicataDTO, List<Long> documentsCoches, typePerte)`
4. Crée 2 demandes liées :
   - Demande 1 : nouveau_titre + approuvee
   - Demande 2 : duplicata/transfert + demande_creee
5. DemandeDocuments : partagés entre les 2

---

### 6. DTO

Créer un DTO dédié au sprint 2 avec :

* type du cas : `passeport_perdu` ou `carte_resident_perdue`
* données de recherche / préremplissage
* données du formulaire complet si la personne est inconnue
* nouveau numéro de passeport si nécessaire

---

### 7. Contrôleur

#### Endpoint 1 : `@PostMapping("/backoffice/sprint2/duplicata")`

**Gère DEUX cas distincts** :

```
if (dto.getIdDemandeur() != null) {
  // Cas 1 : Personne EXISTANTE (vient du prefill)
  → Appeler : createDemandeSprint2(dto)
  → Créer 1 demande
  → Rediriger : sprint2-confirmation.jsp
} else {
  // Cas 2 : Personne INCONNUE (prefill = 404)
  → Sauver en session : sprint2.dto, sprint2.typePerte
  → Rediriger : /step3-typeVisa (réutiliser Sprint 1)
}
```

#### Endpoint 2 : Modification `@PostMapping("/backoffice/demande")`

**Détecte si c'est Sprint 2** :

```
if (session.getAttribute("sprint2.typePerte") != null) {
  // C'est Sprint 2 personne INCONNUE (après Step3 + Step4)
  → Appeler : createTwoDemandes(dto, documentsCoches, typePerte)
  → Créer 2 demandes
  → Nettoyer session
  → Rediriger : sprint2-confirmation.jsp
} else {
  // Sprint 1 classique
  → Appeler : createDemande(dto)
}
```

---

### 8. Tests - Tester les 2 MÉTHODES

#### Test 1 : `createDemandeSprint2()` - Personne EXISTANTE
- Input : DuplicataDTO avec `idDemandeur ≠ null`
- Output : 1 demande créée
- Vérifier :
  - Type = duplicata OU transfert_visa (selon typePerte)
  - Statut = demande_creee
  - Documents = optionnels

#### Test 2 : `createTwoDemandes()` - Personne INCONNUE
- Input : DuplicataDTO avec `idDemandeur = null`, List<Long> documentsCoches, typePerte
- Output : 2 demandes créées
- Vérifier :
  - Demande 1 : type=nouveau_titre, statut=approuvee
  - Demande 2 : type=duplicata/transfert, statut=demande_creee
  - DemandeDocuments : PARTAGÉS (même documents pour les 2)
  - Nouveau passeport (si typePerte=passeport_perdu)

---

### 9. Commit & PR

* PR vers `main`

---

## 👩‍💻 Dev2 — BackOffice (JSP + FORMULAIRE)

### 1. Création branche

* `feature/backoffice-sprint2-ui`

---

### 2. Structure JSP - NOUVEAU FLUX INTÉGRÉ ⭐

**Modifier** : `step1-type.jsp` (ajouter 2 nouveaux boutons)  
**Créer** : `sprint2-form.jsp` (recherche + formulaire)  
**Créer** : `sprint2-confirmation.jsp` (confirmation)  

⚠️ **NE PAS créer** : sprint2-index.jsp, sprint2-choice.jsp

---

### Étape 0 : INTÉGRATION À STEP1-TYPE.JSP ⭐

Modifier `step1-type.jsp` pour ajouter 2 nouveaux boutons aux 3 existants

---

### Étape 1 : RECHERCHE + PRÉREMPLISSAGE ⭐

Page `sprint2-form.jsp` avec 2 sections :

**Section 1 : Recherche** - 4 champs optionnels (email, tel, num passeport, ref visa) + AJAX GET /prefill  
**Section 2 : Formulaire** - 2 blocs (etat civil et visa ) + Bloc nouveau passeport (si transfert) | 3 blocs (etat civil et visa + passeport) : editable si duplicata
---

### Étape 2 : BRANCHEMENT POST /backoffice/sprint2/duplicata ⭐

- Si idDemandeur ≠ null : créer 1 demande + confirmation
- Si idDemandeur = null : créer Demandeur + rediriger Step3 → Step4 → 2 demandes

---

### Étape 3 : STEP3 + STEP4 (si personne inconnue) ⭐

**NE réutiliser QUE si** : `sprint2.typePerte != null` (personne inconnue)

**Logique** :
1. Pages Sprint 1 réutilisées (`step3-typeVisa.jsp`, `step4-documents.jsp`)
2. MAIS en arrière-plan :
   - Session contient `sprint2.dto` + `sprint2.typePerte`
   - Validation documents obligatoires (même que Sprint 1)
3. POST /backoffice/demande détecte Sprint 2 en session
4. Appelle `createTwoDemandes()` → crée Demande 1 (approuvee) + Demande 2 (demande_creee)
5. **Même DemandeDocuments pour les 2 demandes** (audit trail)

---

### 4. CONFIRMATION (sprint2-confirmation.jsp) ⭐

Afficher cas, résumé, statut(s) final(aux)

---

### 5. Controllers - Endpoints

Ajouter/modifier dans `PageController.java` :
- `@PostMapping("/backoffice/sprint2/duplicata/form")`
- Modifier `@PostMapping("/backoffice/sprint2/duplicata")`
- Modifier `@PostMapping("/backoffice/demande")` (détecter Sprint 2)

---

