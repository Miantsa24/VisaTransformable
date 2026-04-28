# Sprint 2 — Duplicata et transfert de visa en cas de perte

---

## Contexte général du sprint

Le Sprint 2 couvre les cas administratifs de **perte** qui ne relèvent plus d’une nouvelle demande de visa long séjour, mais d’une **régularisation** :

```text
Personne existante ou nouvelle → Cas de perte → Duplicata ou transfert → Statut approuvée
```

Deux scénarios utilisent le même parcours fonctionnel :

1. **Passeport perdu**
   - Il faut transférer le visa vers un **nouveau passeport**.
   - La seule donnée supplémentaire indispensable est le **nouveau numéro de passeport**.

2. **Carte de résident perdue**
   - Il faut enregistrer une **demande de duplicata de carte de résident**.

---

## Objectif du Sprint 2

Mettre en place un flux backoffice permettant de traiter les pertes administratives avec un **formulaire unique**, un **préremplissage si la personne existe déjà**, et un **statut final approuvée**.

Contrairement au Sprint 1, on ne démarre pas la demande avec `demande_creee` : le Sprint 2 est une **régularisation** et la demande est donc considérée comme **approuvée dès l’enregistrement**.

---

## Logique métier globale

```text
Personne → Vérification d’existence → Cas de perte → Préremplissage ou saisie complète → Enregistrement → Statut approuvée
```

### Règles métier fondamentales

- Le traitement concerne une **perte** et non une nouvelle demande standard.
- Le formulaire est **commun** aux deux cas.
- Si la personne existe déjà dans le système, ses informations doivent être **préremplies**.
- Si la personne n’existe pas, toutes les informations doivent être saisies **from scratch**.
- Si le cas est un **passeport perdu**, le système doit demander le **nouveau numéro de passeport**.
- Si le cas est une **carte de résident perdue**, le système enregistre une **demande de duplicata**.
- Le statut final est directement **`approuvee`**.

---

## Étapes du formulaire Sprint 2

### Étape 1 — Choix du cas

Deux choix sont proposés au départ :

| Choix | Résultat attendu |
|---|---|
| Passeport perdu | Transfert de visa vers nouveau passeport |
| Carte de résident perdue | Demande de duplicata de carte de résident |

---

### Étape 2 — Identification de la personne

Le système doit rechercher si la personne est déjà présente dans la base.

#### Cas A — Personne déjà connue

- Les informations doivent être **préremplies**.
- L’utilisateur complète seulement ce qui manque selon le cas de perte.

#### Cas B — Personne inconnue

- Tous les champs doivent être saisis manuellement.
- Le comportement est proche d’une nouvelle demande, mais avec une sortie métier différente : **approuvée**.

---

### Étape 3 — Informations à saisir

Le formulaire reprend la logique d’un dossier d’identité complet.

#### Bloc 1 — État civil

| Champ | Détail |
|---|---|
| Nom | |
| Prénoms | |
| Nom de naissance | |
| Date de naissance | |
| Lieu de naissance | |
| Situation matrimoniale | |
| Nationalité | |
| Profession | |
| Adresse locale | |
| Contact | Email + téléphone |

#### Bloc 2 — Passeport

| Champ | Détail |
|---|---|
| Numéro de passeport | |
| Date de délivrance | |
| Date d’expiration | |
| Pays de délivrance | |

#### Bloc 3 — Visa / dossier administratif

| Champ | Détail |
|---|---|
| Référence du visa | |
| Date d’entrée à Madagascar | |
| Lieu d’entrée | |
| Date d’expiration du visa | |

---

### Étape 4 — Sélection du type de perte

Deux branches métier doivent être prises en charge :

- **Passeport perdu**
- **Carte de résident perdue**

Le même formulaire doit pouvoir gérer les deux branches sans dupliquer inutilement l’interface.

---

### Étape 5 — Validation et enregistrement

- Si le dossier est complet, l’enregistrement est effectué avec le statut **`approuvee`**.
- Si une information obligatoire manque, la demande est rejetée avec un message explicite.

---

## Résumé du flux Sprint 2

```text
[Choix du cas : Passeport perdu | Carte de résident perdue]
        ↓
[Recherche de la personne]
  → trouvée ? préremplissage
  → non trouvée ? saisie complète
        ↓
[Saisie / complétion du formulaire]
        ↓
[Validation métier]
  → données manquantes ? → REJET
  → dossier complet ? → ENREGISTREMENT
        ↓
[Statut final : approuvée]
```

---

## Notes pour l’équipe

| Rôle | Points d’attention |
|---|---|
| Team Lead | Fixer la structure de données pour distinguer duplicata et transfert de visa |
| Dev 1 | Prévoir les règles de préremplissage et la création from scratch |
| Dev 2 | Construire un seul wizard pour les deux cas de perte |
| Tous | Le statut final du sprint 2 doit être directement `approuvee` |
