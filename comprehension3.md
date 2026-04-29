# Sprint 3 — Upload des documents et approbation finale

---

## Contexte général du sprint

Le Sprint 3 est la continuation directe du Sprint 1. Il ne repart pas de zéro mais prolonge le flux existant en remplaçant le système de cases à cocher par un vrai système d'upload de fichiers, et en ajoutant une étape d'approbation finale avant la délivrance du visa.

```text
Demande enregistrée → Upload des documents → Validation → demande_creee → Approbation → visa_creé
```

---

## Objectif du Sprint 3

Modifier les pages existantes du Sprint 1 pour :

1. Remplacer les cases à cocher de `step4-documents.jsp` par des zones d'upload de fichier
2. Créer le statut `demande_creee` au clic sur **Enregistrer** (et non plus à la soumission)
3. Remplacer le bouton **Soumettre demande** par un bouton **Approuver** dans `step5-confirmation.jsp`
4. Créer le statut `visa_creé` au clic sur **Approuver**
5. Rediriger vers `success.jsp` affichant le visa et la carte de résident délivrés

---

## Logique métier globale

```text
Formulaire → Choix profil → Upload pièces → Enregistrer → demande_creee
→ Confirmation → Approuver → visa_creé → Délivrance
```

### Règles métier fondamentales

- Le Sprint 3 reprend exactement le même workflow que le Sprint 1, seule la partie documents change
- Les cases à cocher sont remplacées par des zones d'upload individuelles par document
- Une pièce n'est considérée comme valide que si un fichier est uploadé
- Si un document obligatoire n'est pas uploadé → rejet immédiat
- Si tous les documents obligatoires sont uploadés → clic sur **Enregistrer** → statut `demande_creee` créé en base
- Depuis `step5-confirmation.jsp`, le bouton **Approuver** crée le statut `visa_creé` en base
- Après approbation → redirection vers `success.jsp` avec affichage du visa et de la carte de résident
- Le fichier doit être au `format PDF`

---

## Étapes du formulaire Sprint 3

### Étape 1 — Choix du type de demande

(inchangé par rapport au Sprint 1)

| Bouton | Sprint concerné |
|---|---|
| Nouveau titre | Sprint 1 & 3 ✅ |
| Duplicata | À prévoir (sprint ultérieur) |

---

### Étape 2 — Formulaire de renseignement

(inchangé par rapport au Sprint 1)

#### Bloc 1 — État civil

| Champ | Détail |
|---|---|
| Nom | |
| Prénoms | |
| Nom de naissance | (nom de jeune fille) |
| Date de naissance | |
| Lieu de naissance | |
| Situation matrimoniale | |
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
| Pays de délivrance | |

#### Bloc 3 — Visa transformable

| Champ | Détail |
|---|---|
| Référence du visa | |
| Date d'entrée à Madagascar | |
| Lieu d'entrée | (ville) |
| Date d'expiration du visa | |

---

### Étape 3 — Choix du type de visa souhaité

(inchangé par rapport au Sprint 1)

Liste déroulante avec deux options :

- Investisseur
- Travailleur

---

### Étape 4 — Upload des pièces justificatives (`step4-documents.jsp`)

⚠️ **Modification Sprint 3** : les cases à cocher sont remplacées par des zones d'upload de fichier. Chaque document possède son propre espace d'upload indépendant.

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

### Comportement du bouton **Enregistrer**

| Situation | Action |
|---|---|
| Un document obligatoire non uploadé | Rejet avec message d'erreur |
| Tous les documents obligatoires uploadés | Création du statut `demande_creee` en base → redirection vers `step5-confirmation.jsp` |

---

### Étape 5 — Confirmation et approbation finale (`step5-confirmation.jsp`)

⚠️ **Modification Sprint 3** : le bouton **Soumettre demande** est remplacé par un bouton **Approuver**.

Cette page affiche la vérification finale de toutes les informations du demandeur.

#### Comportement du bouton **Approuver**

| Situation | Action |
|---|---|
| Pièces justificatives manquantes | Rejet avec message d'erreur |
| Toutes les pièces justificatives uploadées | Création du statut `visa_creé` en base → redirection vers `success.jsp` |

---

### Étape 6 — Page de succès (`success.jsp`)

Affichage d'un message de succès avec :

- le visa délivré au demandeur
- la carte de résident délivrée selon le type de demande (Investisseur ou Travailleur)

---

## Résumé du flux Sprint 3

```text
[Choix : Nouveau titre]
        ↓
[Saisie du formulaire de renseignement]
  → État civil + Passeport + Visa transformable
        ↓
[Choix du type de visa : Investisseur | Travailleur]
        ↓
[step4-documents.jsp]
[Upload des fichiers des pièces justificatives]
  → Documents communs + Documents spécifiques au type
        ↓
[Clic sur "Enregistrer"]
  → Obligatoire manquant ? → REJET
  → Tous obligatoires uploadés ? → ENREGISTREMENT (statut : demande_creee)
        ↓
[step5-confirmation.jsp]
[Vérification finale des informations du demandeur]
        ↓
[Clic sur "Approuver"]
  → Pièces manquantes ? → REJET
  → Toutes les pièces uploadées ? → APPROBATION (statut : visa_creé)
        ↓
[success.jsp]
[Message de succès + Visa + Carte de résident délivrés]
```

---

## Navigation — Menus et listes

| Menu / Bouton | Contenu affiché |
|---|---|
| Liste demandes | Toutes les demandes dont le statut est `demande_creee` |
| Liste visas | Toutes les demandes dont le statut est `visa_creé` |

---

## Notes pour l'équipe

| Rôle | Points d'attention |
|---|---|
| Team Lead | Définir le caractère obligatoire ou optionnel de chaque document avant le début du dev — point bloquant |
| Team Lead | Définir les formats de fichiers acceptés (PDF, JPEG, PNG ?) et la taille maximale par upload |
| Team Lead | Définir quel rôle peut cliquer sur "Approuver" |
| Dev 1 | Modifier `step4-documents.jsp` : remplacer les checkboxes par des zones d'upload individuelles avec retour visuel (uploadé ✅ / manquant ❌) |
| Dev 1 | Le statut `demande_creee` doit être créé en base au clic sur "Enregistrer" et non plus à la soumission |
| Dev 2 | Modifier `step5-confirmation.jsp` : remplacer le bouton "Soumettre demande" par le bouton "Approuver" |
| Dev 2 | Implémenter la création du statut `visa_creé` en base au clic sur "Approuver" |
| Dev 2 | Créer `success.jsp` avec affichage du message de succès, du visa et de la carte de résident |
| Tous | Le Sprint 3 modifie les pages existantes du Sprint 1 — ne pas créer de nouvelles pages inutilement |
