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

