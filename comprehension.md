# Sprint 1 — Enregistrement d'une demande de visa long séjour (Backoffice)

---

## Contexte général du projet

Un visa transformable est un visa temporaire (ex. : valable 3 mois) qui peut être converti en **visa long séjour**. Cette conversion se fait via une demande formelle, selon le profil du demandeur :

```
Visa temporaire → Visa transformable → Demande de visa long séjour → Travailleur OU Investisseur
```

---

## Objectif du Sprint 1

Mettre en place le **formulaire d'enregistrement d'une demande de visa long séjour** dans le backoffice, avec gestion des pièces justificatives selon le type de visa souhaité, et enregistrement en base de données avec un statut initial.

---

## Workflow global de l'application

```
Personne → Passeport → Visa transformable → Choix du type (Investisseur | Travailleur) → Documents → Décision
```

### Règles métier fondamentales
- Une demande est **liée à un visa**
- Un visa **appartient à une personne**
- Une demande possède **un statut** (ici : `demande_creee` à la création)
- La demande doit être soumise **avant l'expiration du visa transformable**

---

## Étapes du formulaire (Workflow UI)

### Étape 1 — Choix du type de demande
Deux boutons sont affichés :

| Bouton | Sprint concerné |
|---|---|
| **Nouveau titre** | Sprint 1 ✅ |
| **Duplicata** | À prévoir (sprint ultérieur) |

> Pour ce sprint, seul **Nouveau titre** est fonctionnel.

---

### Étape 2 — Formulaire de renseignement
Le formulaire est composé de 3 blocs à saisir manuellement :

#### Bloc 1 — État civil
| Champ | Détail |
|---|---|
| Nom | |
| Prénoms | |
| Nom de naissance | (nom de jeune fille) |
| Date de naissance | |
| Lieu de naissance | |
| Situation matrimoniale | (célibataire, marié(e), etc.) |
| Nationalité | |
| Profession | |
| Adresse locale | (à Madagascar) |
| Contact | Email + numéro de téléphone |

#### Bloc 2 — Passeport
| Champ | Détail |
|---|---|
| Numéro de passeport | |
| Date de délivrance | |
| Date d'expiration | |
| Pays de delivrance | |


#### Bloc 3 — Visa transformable
| Champ | Détail |
|---|---|
| Référence du visa | |
| Date d'entrée à Madagascar | |
| Lieu d'entrée | (ville) |
| Date d'expiration du visa | |

---

### Étape 3 — Choix du type de visa souhaité
Une **liste déroulante** avec deux options :
- Investisseur
- Travailleur

---

### Étape 4 — Pièces justificatives (cases à cocher)

Les documents sont affichés sous forme de **liste de cases à cocher**, indiquant si le document est présent ou non. Certains sont **obligatoires**, d'autres **optionnels**.

> ⚠️ **Règle critique :** Si un document **obligatoire** n'est pas coché, la demande est **rejetée immédiatement**. Si tous les documents obligatoires sont cochés, la demande est **enregistrée** avec le statut `demande_creee`.

---

#### Documents communs aux deux types de visa

*(Toujours affichés, quel que soit le type choisi)*

| # | Document | Caractère |
|---|---|---|
| 1 | 2 photos d'identité | À définir |
| 2 | Notice de renseignement | À définir |
| 3 | Demande adressée à Monsieur le Ministre de l'Intérieur et de la Décentralisation (avec adresse e-mail et numéro de téléphone portable) | À définir |
| 4 | Photocopie certifiée du visa en cours de validité | À définir |
| 5 | Photocopie certifiée de la première page du passeport | À définir |
| 6 | Photocopie certifiée de la carte de résident en cours de validité | À définir |
| 7 | Certificat de résidence à Madagascar | À définir |
| 8 | Extrait de casier judiciaire (de moins de 3 mois) | À définir |

---

#### Documents spécifiques — Investisseur

*(Affichés uniquement si le type "Investisseur" est sélectionné)*

| # | Document | Caractère |
|---|---|---|
| 1 | Statuts de la société | À définir |
| 2 | Extrait d'inscription au registre du commerce | À définir |
| 3 | Carte fiscale | À définir |

---

#### Documents spécifiques — Travailleur

*(Affichés uniquement si le type "Travailleur" est sélectionné)*

| # | Document | Caractère |
|---|---|---|
| 1 | Autorisation d'emploi délivrée à Madagascar par le Ministère de la Fonction Publique | À définir |
| 2 | Attestation d'emploi délivrée par l'employeur | À définir |

---

### Étape 5 — Enregistrement
- Si la validation des documents obligatoires passe → enregistrement en base avec le statut : **`demande_creee`**
- Sinon → rejet du formulaire avec message d'erreur

---

## Résumé du flux Sprint 1

```
[Choix : Nouveau titre]
        ↓
[Saisie du formulaire de renseignement]
  → État civil + Passeport + Visa transformable
        ↓
[Choix du type de visa : Investisseur | Travailleur]
        ↓
[Affichage des pièces justificatives à cocher]
  → Documents communs + Documents spécifiques au type
        ↓
[Validation]
  → Obligatoire manquant ? → REJET
  → Tous obligatoires cochés ? → ENREGISTREMENT (statut : demande_creee)
```

---

## Notes pour l'équipe

| Rôle | Points d'attention |
|---|---|
| **Team Lead** | Définir quels documents sont obligatoires vs optionnels avant le début du dev — c'est un point bloquant |
| **Dev 1 & Dev 2** | Le formulaire est en plusieurs étapes (wizard), prévoir une gestion d'état entre les étapes |
| **Tous** | Le type "Duplicata" doit être prévu dans la structure du code mais non implémenté ce sprint |
