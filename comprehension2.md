# Sprint 2 — Traitement des cas de perte (Duplicata & Transfert de visa)

---

## Contexte et rappel Sprint 1

Le Sprint 1 a permis de mettre en place l'enregistrement d'une **demande de visa long séjour (Nouveau titre)** dans le backoffice. Le workflow établi est le suivant :

```
[Choix : Nouveau titre]
        ↓
[Saisie manuelle : État civil + Passeport + Visa transformable]
        ↓
[Choix du type de visa : Investisseur | Travailleur]
        ↓
[Pièces justificatives à cocher]
        ↓
[Validation → statut : demande_creee  OU  rejet]
```

> Dans le Sprint 1, le bouton **"Duplicata"** était présent mais non fonctionnel. Le Sprint 2 l'active et ajoute également le bouton **"Transfert de visa"**.

---

## Objectif du Sprint 2

Implémenter le traitement des **cas de perte** :

| Cas | Description |
|---|---|
| **Duplicata** | Perte de la **carte de résident** → demande de duplicata |
| **Transfert de visa** | Perte du **passeport** → le visa doit être transféré sur un nouveau passeport |

Ces deux flux partagent la même logique de départ : une **recherche du demandeur en base** avec gestion de deux sous-cas (données existantes ou non).

---

## Nouveauté base de données — Table `visa_long_sejour`

Le Sprint 2 introduit une nouvelle table : **`visa_long_sejour`**.

> Le visa transformable (table `visa` existante) est le visa temporaire de départ. Quand une demande de type `nouveau_titre` est **approuvée**, elle génère un visa long séjour. C'est ce visa long séjour qui est ensuite concerné par les cas de perte du Sprint 2.

### Table à ajouter : `visa_long_sejour`

```sql
CREATE TABLE visa_long_sejour (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_demande INT NOT NULL,             -- demande nouveau_titre approuvée à l'origine
    id_demandeur INT NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    id_type_visa INT,                    -- investisseur ou travailleur
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (id_demande) REFERENCES demande(id),
    FOREIGN KEY (id_demandeur) REFERENCES demandeur(id),
    FOREIGN KEY (id_type_visa) REFERENCES type_visa(id)
);
```

> Dans le formulaire des cas de perte, l'agent devra renseigner la **date de début** et la **date de fin** du visa long séjour concerné.

### Nouveaux statuts et types à insérer

```sql
-- Nouveau statut
INSERT INTO statut_demande(libelle) VALUES
  ('demande_approuvee');   -- utilisé pour la ligne nouveau_titre lors d'un cas de perte

-- Nouveau type de demande
INSERT INTO type_demande(libelle) VALUES
  ('transfert_visa');      -- (duplicata existe déjà, vérifier la coquille 'duplicatat')
```

---

## Étapes du formulaire Sprint 2 (Workflow UI)

### Étape 1 — Choix du type de demande

Sur l'écran d'accueil, **trois boutons** sont désormais affichés :

| Bouton | Sprint |
|---|---|
| **Nouveau titre** | Sprint 1 ✅ |
| **Duplicata** | Sprint 2 ✅ — actif |
| **Transfert de visa** | Sprint 2 ✅ — actif |

---

### Étape 2 — Mini-formulaire de recherche

Dès que l'agent clique sur **Duplicata** ou **Transfert de visa**, un **petit formulaire de recherche** apparaît avec les 4 champs suivants (tous optionnels) :

| Champ | Type |
|---|---|
| Email | Optionnel |
| Téléphone | Optionnel |
| Numéro de passeport | Optionnel |
| Référence du visa | Optionnel |

> L'agent remplit un ou plusieurs champs puis lance la recherche. Le système cherche en base si un demandeur correspondant existe.

---

### Étape 3 — Deux sous-cas selon le résultat de la recherche

---

#### CAS A — Le demandeur EXISTE déjà en base

Le formulaire de renseignement (identique au Sprint 1) s'affiche **pré-rempli** avec toutes les données existantes :

- Bloc État civil (données du demandeur)
- Bloc Passeport (données du passeport connu)
- Bloc Visa transformable (données du visa connu)

**Spécificité selon le type de demande :**

| Type | Comportement sur le bloc Passeport |
|---|---|
| **Duplicata** (perte carte résident) | Le passeport existant est affiché, il reste utilisable tel quel mais peut etre modifier |
| **Transfert de visa** (perte passeport) | Le passeport perdu est affiché à titre informatif, mais l'agent **doit saisir un nouveau passeport** (numéro, date de délivrance, date d'expiration, pays de délivrance) — les infos de l'ancien passeport ne suffisent pas et ne peut etre modifier |

L'agent peut **modifier** les champs pré-remplis si nécessaire.

Le formulaire comporte également un bloc supplémentaire pour le **visa long séjour**, quand la personne existe en base alors il est pre rempli par celui en base mais peut etre modifiable :

| Champ | Détail |
|---|---|
| Date de début du visa long séjour | obligatoire |
| Date de fin du visa long séjour | obligatoire |


L'agent **confirme** puis **soumet** le formulaire directement (pas de choix de type de visa ni de pièces justificatives dans ce cas, ces infos étant déjà connues).

---

#### CAS B — Le demandeur N'EXISTE PAS en base

Le formulaire de renseignement s'affiche **vide** (identique à celui du Sprint 1) :

- Bloc État civil → à saisir entièrement
- Bloc Passeport → à saisir entièrement
- Bloc Visa transformable → à saisir entièrement
- Bloc Visa long séjour → date de début + date de fin à saisir obligatoirement car personne non connue

Puis le flux reprend **exactement comme au Sprint 1** :

```
[Saisie complète État civil + Passeport + Visa transformable + Visa long séjour]
        ↓
[Choix du type de visa : Investisseur | Travailleur]
        ↓
[Pièces justificatives à cocher]
  → Documents communs + Documents spécifiques au type choisi
        ↓
[Validation des documents obligatoires]
        ↓
[Soumission]
```

---

### Étape 4 — Enregistrement en base (règle métier critique)

Le nombre de lignes insérées dépend du cas détecté à l'étape 3.

#### CAS A — Le demandeur existe déjà en base

La personne possède déjà une demande `nouveau_titre` en base. On insère **une seule ligne** :

| Ligne | `id_type_demande` | `id_statut_demande` | `id_demande_origine` |
|---|---|---|---|
| **Ligne unique** | `duplicata` OU `transfert_visa` | `demande_creee` | ID de la demande `nouveau_titre` existante |

#### CAS B — Le demandeur n'existe pas en base

Aucune demande antérieure n'existe. On insère **deux lignes** :

| Ligne | `id_type_demande` | `id_statut_demande` | `id_demande_origine` |
|---|---|---|---|
| **Ligne 1** | `nouveau_titre` | `demande_approuvee` | NULL |
| **Ligne 2** | `duplicata` OU `transfert_visa` | `demande_creee` | ID de la ligne 1 (insérée juste avant) |

> La ligne `nouveau_titre / approuvee` est créée artificiellement pour représenter le titre original que le demandeur possédait avant la perte, afin de maintenir la cohérence de l'historique métier.

**Pour le Transfert de visa**, la colonne `id_nouveau_passeport` de la table `demande` doit être renseignée avec l'ID du nouveau passeport saisi.

---

## Résumé du flux Sprint 2

```
[Choix : Duplicata OU Transfert de visa]
        ↓
[Mini-formulaire de recherche]
  → Email (opt.) | Téléphone (opt.) | N° passeport (opt.) | Réf. visa (opt.)
        ↓
     ┌──────────────────────────────┐
     │      Recherche en base       │
     └──────────────────────────────┘
           /                    \
    EXISTE                    N'EXISTE PAS
       ↓                           ↓
[Formulaire pré-rempli]    [Formulaire vide]
  (modifiable)              (saisie complète)
       ↓                           ↓
[Transfert de visa ?]              │
  → Nouveau passeport obligatoire  │
       ↓                           ↓
[Bloc Visa long séjour]    [Bloc Visa long séjour]
  date_debut + date_fin      date_debut + date_fin
       ↓                           ↓
  [Confirmer & Soumettre]  [Choix type visa : Investisseur | Travailleur]
                            [Pièces justificatives à cocher]
                            [Validation documents obligatoires]
                            [Confirmer & Soumettre]
       ↓                           ↓
       └─────────────┬─────────────┘
                     ↓
         [Cas A — existe en base]      [Cas B — n'existe pas en base]
                     ↓                           ↓
         ┌───────────────────────┐   ┌───────────────────────────────────────┐
         │  INSERT 1 ligne :     │   │  INSERT demande ligne 1 :             │
         │  type  = duplicata    │   │  type_demande = nouveau_titre         │
         │         ou transfert  │   │  statut       = demande_approuvee     │
         │  statut= demande_creee│   │  id_demande_origine = NULL            │
         │  id_demande_origine   │   ├───────────────────────────────────────┤
         │    = ID nouveau_titre │   │  INSERT demande ligne 2 :             │
         │      existant en base │   │  type_demande = duplicata             │
         └───────────────────────┘   │               OU transfert_visa       │
                                     │  statut       = demande_creee         │
                                     │  id_demande_origine = ID ligne 1      │
                                     └───────────────────────────────────────┘
                     ↓
         INSERT visa_long_sejour
           (id_demande = ligne 1, date_debut, date_fin)
```

---

## Tableau récapitulatif des comportements par type

| | Duplicata | Transfert de visa |
|---|---|---|
| **Motif** | Perte de carte de résident | Perte de passeport |
| **Passeport** | Réutiliser l'existant (si Cas A) | Saisir un **nouveau** passeport obligatoirement |
| **Visa long séjour** | Date début + fin à renseigner | Date début + fin à renseigner |
| **`type_perte`** | `carte_resident_perdue` | `passeport_perdu` |
| **`id_nouveau_passeport`** | NULL | ID du nouveau passeport créé |
| **`id_demande_origine`** | ID de la demande `nouveau_titre` existante | ID de la demande `nouveau_titre` existante |
| **Cas A — lignes créées** | 1 ligne : `duplicata / demande_creee` | 1 ligne : `transfert_visa / demande_creee` |
| **Cas B — lignes créées** | 2 lignes : `nouveau_titre / approuvee` + `duplicata / creee` | 2 lignes : `nouveau_titre / approuvee` + `transfert_visa / creee` |

---

## Impact sur la base de données existante

### Modification de la table `demande` existante

Ajouter la colonne `id_demande_origine` pour tracer le lien entre un duplicata/transfert et sa demande `nouveau_titre` d'origine :

```sql
ALTER TABLE demande
  ADD COLUMN id_demande_origine INT DEFAULT NULL,
  ADD FOREIGN KEY (id_demande_origine) REFERENCES demande(id);
```

> Cette colonne est `NULL` pour toute demande de type `nouveau_titre`, et renseignée avec l'ID de la demande originale pour tout `duplicata` ou `transfert_visa`.

```sql
-- Corriger la coquille dans les données existantes
UPDATE type_demande SET libelle = 'duplicata' WHERE libelle = 'duplicatat';

-- Ajouter les nouveaux types et statuts
INSERT INTO type_demande(libelle) VALUES ('transfert_visa');
INSERT INTO statut_demande(libelle) VALUES ('demande_approuvee');
```

### Nouvelle table à créer

```sql
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
```

---

## Notes pour l'équipe

| Rôle | Points d'attention |
|---|---|
| **Team Lead** | Valider la règle de la double insertion (nouveau_titre approuvé + duplicata/transfert créé) avec le métier avant dev |
| **Dev 1 & Dev 2** | La recherche Cas A / Cas B doit être robuste : gérer les recherches multicritères (OR sur email, téléphone, passeport, référence visa) |
| **Dev 1 & Dev 2** | Pour le transfert de visa (Cas A), le formulaire doit vider et rendre obligatoires les champs du nouveau passeport même si les anciennes données sont pré-remplies |
| **Dev 1 & Dev 2** | Le bloc "visa long séjour" (date_debut / date_fin) est un ajout par rapport au Sprint 1 — prévoir son affichage dans le wizard |
| **Tous** | La double insertion en base doit être atomique (transaction SQL) pour éviter les incohérences |
| **Tous** | S'assurer que la coquille `duplicatat` est corrigée en `duplicata` avant de démarrer le dev |
