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

Créer `DuplicataService` ou étendre `DemandeService` selon le choix d’architecture

#### Méthode :

```java
createDuplicata(DuplicataDTO dto)
```

ou

```java
createDemandeSprint2(DemandeDTO dto)
```

---

### Règles métier

1. Déterminer le cas :

  * passeport perdu
  * carte de résident perdue
2. Si la personne existe déjà, charger et préremplir ses données
3. Si la personne n’existe pas, créer l’ensemble des informations from scratch
4. Si passeport perdu :

  * saisir uniquement le nouveau numéro de passeport
  * rattacher le visa au nouveau passeport
5. Si carte de résident perdue :

  * enregistrer la demande de duplicata
6. Affecter directement le statut `approuvee`

---

### 6. DTO

Créer un DTO dédié au sprint 2 avec :

* type du cas : `passeport_perdu` ou `carte_resident_perdue`
* données de recherche / préremplissage
* données du formulaire complet si la personne est inconnue
* nouveau numéro de passeport si nécessaire

---

### 7. Contrôleur

```java
@PostMapping("/backoffice/sprint2/duplicata")
```

et endpoints de lecture pour préremplissage si besoin

---

### 8. Tests

* personne existante → préremplissage OK
* personne inconnue → création from scratch OK
* passeport perdu → transfert vers nouveau passeport
* carte de résident perdue → duplicata enregistré
* statut initial = `approuvee`

---

### 9. Commit & PR

* PR vers `main`

---

## 👩‍💻 Dev2 — BackOffice (JSP + FORMULAIRE)

### 1. Création branche

* `feature/backoffice-sprint2-ui`

---

### 2. Structure JSP à mettre dans `main/webapp/WEB-INF/jsp`

Créer ou compléter :

* `sprint2-index.jsp`
* `sprint2-choice.jsp`
* `sprint2-form.jsp`
* `sprint2-confirmation.jsp`

---

### Étape 0 : entrée sprint 2

* Page d’accueil dédiée au sprint 2 ou menu dans la sidebar
* Lancer le flux duplicata / transfert de visa

---

### Étape 1 : choix du cas

* Boutons ou cartes :

  * Passeport perdu
  * Carte de résident perdue

---

### Étape 2 : formulaire

* Préremplir les données si la personne existe déjà
* Permettre la saisie complète si aucune donnée antérieure n’existe
* Afficher uniquement le champ supplémentaire utile en cas de passeport perdu : nouveau numéro de passeport

---

### Étape 3 : confirmation

* Résumer le cas choisi
* Résumer les données préremplies ou saisies
* Afficher le statut final `approuvee`

---

### 4. Navigation wizard

* session ou hidden inputs

---

### 5. Gestion erreurs

* personne introuvable au moment du préremplissage
* données manquantes si saisie from scratch
* numéro de passeport manquant en cas de passeport perdu

---

### 6. Contrôleur

```java
@GetMapping("/backoffice/sprint2/duplicata/new")
```

---

### 7. Commit & PR

 * PR vers `main`

---

==============================================================================================
==============================================================================================

# 📌 Tâches pour Sprint 3

---

## Rôles

| Rôle | Nom |
|---|---|
| Team Lead | Alexandra ETU003306 |
| Dev 1 (BackOffice : LOGIQUE MÉTIER + DB) | Malala ETU003211 |
| Dev 2 (BackOffice : JSP + VALIDATION DOCUMENTAIRE) | Tojo ETU003362 |

---

## 🧠 TL (Team Lead)

### 1. Validation métier

| Point | Détail |
|---|---|
| Documents obligatoires/optionnels | Confirmer le caractère obligatoire ou optionnel de chaque document (communs + spécifiques) — point bloquant avant le dev |
| Formats de fichiers | Confirmer que le format accepté pour les pièces justificatives est PDF uniquement, et confirmer la taille maximale par upload |
| Rôle d'approbation | Confirmer quel rôle peut cliquer sur le bouton "Approuver" (tous les agents ou rôle spécifique ?) |
| Statut demande_creee | Valider que le statut `demande_creee` est bien créé en base au clic sur "Enregistrer" dans `step4-documents.jsp` |
| Statut visa_creé | Valider que le statut `visa_creé` est bien créé en base au clic sur "Approuver" dans `step5-confirmation.jsp` |

### 2. Cohérence métier / DB

| Point | Détail |
|---|---|
| Schéma Sprint 1 | Vérifier l'impact sur le schéma existant du Sprint 1 pour stocker les fichiers uploadés |
| Liaison fichier | Vérifier que la colonne de stockage de fichier est bien liée à la table `demande_document` |
| Contraintes | Vérifier la cohérence des FK, contraintes et unicité avec le nouveau flux |
| Statut visa_creé | Ajouter le statut `visa_creé` dans la table `statut_demande` si non présent |

```sql
INSERT INTO statut_demande(libelle) VALUES ('visa_creé');
```

### 3. Suivi d'intégration

| Point | Détail |
|---|---|
| Compatibilité | Vérifier la compatibilité avec les flux Sprint 1 et Sprint 2 |
| Pages modifiées | S'assurer que la modification de `step4-documents.jsp` et `step5-confirmation.jsp` ne casse pas les statuts déjà en place |
| Liste demandes | Vérifier que le menu "Liste demandes" affiche bien uniquement les demandes en `demande_creee` |
| Liste visas | Vérifier que le menu "Liste visas" affiche bien uniquement les demandes en `visa_creé` |

### 4. Tests métier

| Cas | Résultat attendu |
|---|---|
| Document uploadé | Pièce automatiquement validée |
| Pièce obligatoire manquante | Rejet au clic sur "Enregistrer", statut `demande_creee` non créé |
| Toutes les pièces obligatoires uploadées | Statut `demande_creee` créé en base, redirection vers `step5-confirmation.jsp` |
| Bouton "Approuver" | Visible uniquement quand statut = `demande_creee` |
| Approbation finale | Statut `visa_creé` créé en base, redirection vers `success.jsp` |
| success.jsp | Affiche bien le visa et la carte de résident délivrés |

---

## 👨‍💻 Dev 1 — BackOffice (LOGIQUE MÉTIER + DB)

### 1. Création branche

| Tâche | Détail |
|---|---|
| Branche | Créer la branche `feature/backoffice-sprint3-core` |

### 2. Base de données

| Tâche | Détail |
|---|---|
| Table demande_document | Étendre la table `demande_document` pour stocker le fichier uploadé par document |
| Stockage fichier | Ajouter une colonne `fichier_path` ou `fichier_data` pour stocker le chemin ou le contenu du fichier |
| Validation | Ajouter une colonne `est_valide` (boolean) sur `demande_document` pour suivre l'état de validation de chaque pièce |
| Statut visa_creé | Ajouter le statut `visa_creé` dans la table `statut_demande` |

```sql
INSERT INTO statut_demande(libelle) VALUES ('visa_creé');
```

### 3. Mapping JPA

| Entité | Modification |
|---|---|
| DemandeDocument | Ajouter `fichierPath` ou `fichierData` |
| DemandeDocument | Ajouter `estValide` (boolean) |
| Demande | Réutiliser sans modification |
| Document | Réutiliser sans modification |
| Demandeur | Réutiliser sans modification |

### 4. Repository

| Tâche | Détail |
|---|---|
| DemandeDocumentRepository | Récupérer les documents d'une demande avec leur état de validation |
| DemandeDocumentRepository | Mettre à jour le statut de validation d'une pièce après upload |
| Vérification | Ajouter une méthode pour vérifier si toutes les pièces obligatoires d'une demande sont validées |

### 5. Service métier

Créer ou étendre `DemandeService` avec les méthodes suivantes :

```java
uploadDocument(Long demandeId, Long documentId, MultipartFile fichier)
```
- Vérifier que le fichier uploadé est bien au format PDF avant de l'associer au document
- Si le format n'est pas PDF → retourner une erreur métier explicite
- Si le format est PDF → associer le fichier et passer estValide à true

Associer le fichier uploadé au document de la demande et passer automatiquement `estValide` à `true`.

```java
validerDemandeDocuments(Long demandeId)
```

Vérifier que toutes les pièces obligatoires sont validées. Si oui → passer le statut de la demande à `demande_creee`. Si non → retourner une erreur métier explicite.

```java
approuverDemande(Long demandeId)
```

Vérifier que le statut de la demande est bien `demande_creee`. Si oui → passer le statut à `visa_creé`. Si non → retourner une erreur métier explicite.

### 6. DTO

| DTO | Champs |
|---|---|
| DocumentUploadDTO | `demandeId`, `documentId`, `fichier` (MultipartFile) |
| DemandeDocumentStatusDTO | `documentId`, `libelle`, `estObligatoire`, `estValide`, `fichierPath` |

### 7. Contrôleurs

| Endpoint | Rôle |
|---|---|
| `@PostMapping("/backoffice/sprint3/documents/upload")` | Créer l'endpoint d'upload |
| `@PostMapping("/backoffice/sprint3/documents/valider")` | Vérifier toutes les pièces et créer le statut `demande_creee` en base |
| `@PostMapping("/backoffice/sprint3/demande/approuver")` | Créer le statut `visa_creé` en base |
| `@GetMapping("/backoffice/sprint3/documents/{demandeId}")` | Récupérer les documents d'une demande |

### 8. Tests

| Cas | Résultat attendu |
|---|---|
| Upload d'un fichier | `estValide` passe à `true` pour la pièce correspondante |
| Pièce obligatoire non uploadée | `validerDemandeDocuments` retourne une erreur, statut `demande_creee` non créé |
| Toutes les pièces obligatoires uploadées | Statut `demande_creee` créé en base |
| `approuverDemande` avec statut ≠ `demande_creee` | Erreur retournée |
| `approuverDemande` avec statut = `demande_creee` | Statut `visa_creé` créé en base |

### 9. Commit & PR

| Tâche | Détail |
|---|---|
| PR | PR vers `main` |

---

## 👩‍💻 Dev 2 — BackOffice (JSP + VALIDATION DOCUMENTAIRE)

### 1. Création branche

| Tâche | Détail |
|---|---|
| Branche | Créer la branche `feature/backoffice-sprint3-ui` |

### 2. Pages JSP à modifier

| Page | Action |
|---|---|
| `step4-documents.jsp` | Modifier |
| `step5-confirmation.jsp` | Modifier |
| `success.jsp` | Créer |

### 3. Modification de `step4-documents.jsp`

| Point | Détail |
|---|---|
| Checkboxes | Remplacer toutes les cases à cocher (`<input type="checkbox">`) par des zones d'upload |
| Input file | `html<input type="file" name="fichier_doc" data-document-id="${doc.id}" accept=".pdf">` |
| Documents communs | Afficher les documents communs pour tous les profils |
| Documents spécifiques | Afficher les documents spécifiques selon le type de visa sélectionné à l'étape 3 (Investisseur ou Travailleur) |
| État visuel | Afficher `Uploadé ✅` / `Non uploadé ❌` |
| Mise à jour état | Mettre à jour l'état visuel de chaque pièce immédiatement après l'upload |
| Bouton Enregistrer | Si une pièce obligatoire manque → message d'erreur explicite, blocage de la progression |
| Bouton Enregistrer | Si toutes les pièces obligatoires sont uploadées → appeler l'endpoint de validation, créer le statut `demande_creee` en base → rediriger vers `step5-confirmation.jsp` |

### 4. Modification de `step5-confirmation.jsp`

| Point | Détail |
|---|---|
| Affichage | Conserver l'affichage de la vérification finale des informations du demandeur (inchangé) |
| Bouton | Remplacer "Soumettre demande" par "Approuver" |
| Visibilité | Afficher le bouton "Approuver" uniquement si le statut de la demande est `demande_creee` |
| Au clic | Appeler l'endpoint d'approbation |
| Au clic | Créer le statut `visa_creé` en base |
| Au clic | Rediriger vers `success.jsp` |

### 5. Création de `success.jsp`

| Point | Détail |
|---|---|
| Message | Afficher un message de succès après approbation |
| Visa | Afficher les informations du visa délivré au demandeur |
| Carte résident | Afficher la carte de résident délivrée selon le type de demande (Investisseur ou Travailleur) |
| Navigation | Ajouter un lien ou bouton de retour vers la liste des demandes ou l'accueil |

### 6. Menus et navigation

| Menu / Bouton | Contenu |
|---|---|
| Liste demandes | Affiche toutes les demandes dont le statut est `demande_creee` |
| Liste visas | Affiche toutes les demandes dont le statut est `visa_creé` |

### 7. Gestion des erreurs

| Cas | Action |
|---|---|
| Fichier obligatoire manquant | Afficher un message d'erreur si un fichier obligatoire est manquant dans `step4-documents.jsp` |
| Statut incorrect | Afficher un message d'erreur si le bouton "Approuver" est cliqué alors que le statut n'est pas `demande_creee` |
| Reprise upload | Permettre à l'agent de reprendre l'upload après une erreur sans perdre les fichiers déjà déposés |
- Afficher un message d'erreur si le fichier uploadé n'est pas au format PDF

### 8. Navigation wizard

| Point | Détail |
|---|---|
| État | Conserver la gestion d'état entre les étapes via session ou hidden inputs (comme Sprint 1) |
| Accessibilité | S'assurer que les données des étapes 1 à 3 sont toujours accessibles dans `step4-documents.jsp` et `step5-confirmation.jsp` |

### 9. Contrôleur

| Endpoint | Rôle |
|---|---|
| `@GetMapping("/backoffice/sprint3/documents")` | Créer ou compléter le contrôleur GET pour afficher la page documents |

### 10. Commit & PR

| Tâche | Détail |
|---|---|
| PR | PR vers `main` |



==============================================================================================
==============================================================================================

