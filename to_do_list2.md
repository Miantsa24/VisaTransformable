# TODO LIST — SPRINT 2 (BACKOFFICE UNIQUEMENT — VERSION FINALE)

## Rôles:

* **Sprint2:**

  * **Team Lead** :  Tojo ETU003362  
  * **Dev 1 (BackOffice - logique métier)** : Malala ETU003211 
  * **Dev 2 (BackOffice - interface & intégration)** : Alexandra ETU003306  

---

# 🎯 Rôles et responsabilités ajustés

* **TL** : validation métier critique (CAS A / CAS B + double insertion)
* **Dev1** : logique métier + recherche + transaction
* **Dev2** : wizard dynamique + gestion des cas conditionnels

---

# 📌 Tâches pour Sprint 2

---

## 🧠 TL (Team Lead)

### 1. Validation métier CRITIQUE

Valider absolument :

* ✔ CAS A (existe) vs CAS B (n’existe pas)
* ✔ règle **1 insertion vs 2 insertions**
* ✔ lien `id_demande_origine`
* ✔ transfert → **nouveau passeport obligatoire**

---

### 2. Validation du flux UI (IMPORTANT)

Confirmer que le flow est EXACTEMENT :

step1-type.jsp
→ step2-search.jsp
→ sprint2-form.jsp (TOUJOURS affiché)
    ├─ CAS A → soumission directe
    └─ CAS B → step3 + step4
→ step3-TypeVisa.jsp (CAS B uniquement)
→ step4-documents.jsp (CAS B uniquement)
→ step5-confirmation.jsp

---

### 3. Validation base de données

* ajout :
  * `id_demande_origine`
  * `id_nouveau_passeport`
  * `type_perte`
* création :
  * `visa_long_sejour`

---

### 4. Correction données EXISTANTES

⚠️ incohérence actuelle :
approuvee vs demande_approuvee

✔ garder :
demande_approuvee

---

### 5. Tests métier

Tester :

* CAS A duplicata
* CAS A transfert
* CAS B duplicata
* CAS B transfert

---

## 👨‍💻 Dev1 — BackOffice (LOGIQUE MÉTIER + DB)

### 1. Création branche
feature/backoffice-sprint2-core

### 2. Base de données

ALTER TABLE demande
ADD COLUMN id_demande_origine INT NULL,
ADD FOREIGN KEY (id_demande_origine) REFERENCES demande(id);

CREATE TABLE visa_long_sejour (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_demande INT NOT NULL,
    id_demandeur INT NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    id_type_visa INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (id_demande) REFERENCES demande(id),
    FOREIGN KEY (id_demandeur) REFERENCES demandeur(id),
    FOREIGN KEY (id_type_visa) REFERENCES type_visa(id)
);

UPDATE type_demande 
SET libelle = 'duplicata' 
WHERE libelle = 'duplicatat';

INSERT INTO type_demande(libelle) VALUES ('transfert_visa');
INSERT INTO statut_demande(libelle) VALUES ('demande_approuvee');

### 3. Repository
searchDemandeur(email, telephone, numeroPasseport, referenceVisa)

### 4. Service métier

searchDemandeur(SearchDTO dto):
Retour :
  demandeur
  passeport
  visa
  demande existante (nouveau_titre) : si existe en base

createDemandePerte(DemandeDTO dto) : regle metier

### 5. Règles métier

CAS A : existe en base 
1 seule demande duplicata/transfert avec id_demande_origine, statut : demande_creee
  TRANSFERT
    créer nouveau passeport
    remplir :
      id_nouveau_passeport
      type_perte = passeport_perdu

CAS B : n'existe pas en base
1. nouveau_titre (demande_approuvee)
2. duplicata/transfert (demande_creee)

DUPLICATA :
carte_resident_perdue

VISA LONG SEJOUR : id_demande_origine
lié à la demande nouveau_titre

@Transactional obligatoire

6. DTO
  SearchDTO
    email
    telephone
    numeroPasseport
    referenceVisa
  DemandeDTO
    typeDemande (duplicata/transfert)
    données formulaire (sprint2-form.jsp)
    nouveau passeport (si transfert)
    dates visa long séjour
    documents (CAS B uniquement)

7. Contrôleurs
Recherche
@PostMapping("/backoffice/demande/search")
Création
@PostMapping("/backoffice/demande/perte")


8. Tests

Tester :

recherche vide
recherche multicritère
CAS A → 1 ligne
CAS B → 2 lignes
transfert sans nouveau passeport → rejet
transaction rollback OK

---

## 👩‍💻 Dev2 — BackOffice (JSP + FORMULAIRE)

### Pages JSP

* step1-type.jsp : update
* step2-search.jsp : NOUVEAU
* sprint2-form.jsp : NOUVEAU
* step3-TypeVisa.jsp (Sprint 1)
* step4-documents.jsp (Sprint 1)
* step5-confirmation.jsp


### Étape 1 — Choix

    Ajouter :

      Duplicata
      Transfert de visa

### Étape 2 — Recherche

    Formulaire :

      email
      téléphone
      numéro passeport
      référence visa

### Étape 3 — sprint2-form.jsp (CLÉ)

    Contient :

      état civil
      passeport
      visa transformable
      ✅ visa long séjour (date début + fin)

    ✔ CAS A (EXISTE)
      pré-remplir tout mais reste modifiable + 2 champs a saisir obligatoirement : visa long séjour (date début + fin)

        -> TRANSFERT : cas ou la personne existe en base donc l'ancien passeport est dans la base, il faut donc que la personne nous donne les informations du nouveau passeport
        vider champs passeport, rendre obligatoire :
          numero
          date_delivrance
          date_expiration
          pays

      ✔ CAS B
      formulaire vide a saisir manuellement, puis passer a step 3 et step 4 de sprint 1 (choix type visa et la partie de piece justificatif)

### Navigation conditionnelle
  ✔ CAS A
    sprint2-form → step5-confirmation

  ✔ CAS B
    sprint2-form
    → step3-TypeVisa
    → step4-documents
    → step5-confirmation


### Spécificités

* transfert = passeport obligatoire
* documents = seulement CAS B, dans cas A les pieces dans nouveau_titre sont donc copies dans le duplicata
* gestion erreurs + session wizard

---

# ⚠️ Points critiques

* Transaction obligatoire
* Bonne détection CAS A / CAS B
* Cohérence demande_approuvee
* Liaison correcte visa_long_sejour
