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

