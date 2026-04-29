# 📋 RÉSUMÉ DES SPRINTS 1 & 2

---

## 🎯 **SPRINT 1 — Enregistrement demande de visa long séjour**

### Contexte
Conversion d'un visa temporaire → visa transformable → demande de visa long séjour

### Workflow
```
Choix type (Nouveau titre) 
    ↓
Formulaire (État civil + Passeport + Visa)
    ↓
Choix type visa (Investisseur | Travailleur)
    ↓
Pièces justificatives + Validation obligatoires
    ↓
Statut final : demande_creee
```

### Les 3 blocs du formulaire
1. **État civil** : Nom, prénoms, date/lieu naissance, situation matrimoniale, nationalité, profession, adresse, contact
2. **Passeport** : Numéro, dates délivrance/expiration, pays
3. **Visa** : Référence, date entrée, lieu, date expiration

### Documents
- **Communs** (8 docs) : Photos, notice, demande au Ministre, visa + passeport + carte résident + certificat + casier judiciaire
- **Investisseur** (3 docs) : Statuts, registre commerce, carte fiscale
- **Travailleur** (2 docs) : Autorisation emploi, attestation employeur

### ✅ Validation
- Obligatoires manquants ? **REJET**
- Tous obligatoires cochés ? **ENREGISTREMENT (demande_creee)**

### État actuel
- ✅ Backend : 100% complet (Dev 1)
- ✅ Frontend : 100% complet (Dev 2)

---

## 🆕 **SPRINT 2 — Duplicata et transfert (perte)**

### Contexte
Régularisation administrative en cas de perte (passeport ou carte de résident)

### Workflow NOUVEAU INTÉGRÉ
```
Step1-type : Ajouter 2 nouveaux boutons (duplicata)
    ↓
Sprint2-form : Recherche + formulaire (préremplissage si existe)
    ↓
SI PERSONNE INCONNUE → Step3 + Step4 (réutiliser Sprint 1)
    ↓
Création 2 demandes :
  • Demande 1 : nouveau_titre + approuvee
  • Demande 2 : duplicata/transfert + demande_creee
    ↓
Sprint2-confirmation : Résumé final
```

### 2 Cas de perte distincts
1. **Passeport perdu** → Transfert visa vers nouveau passeport
2. **Carte de résident perdue** → Duplicata de carte résident

### Les 3 blocs du formulaire (identique à Sprint 1)
1. **État civil** 
2. **Passeport**
3. **Visa**
+ **Bloc bonus** (si passeport perdu) : Nouveau n° passeport obligatoire

### 🔄 2 Flux distincts (IMPORTANT)

**Flux A : Personne EXISTANTE** (vient du prefill)
```
POST /backoffice/sprint2/duplicata (avec idDemandeur)
    ↓
createDemandeSprint2()
    ↓
1 demande : duplicata/transfert + demande_creee
```

**Flux B : Personne INCONNUE** (404 du prefill)
```
Formulaire vide + Step3 (type visa) + Step4 (documents)
    ↓
createTwoDemandes() 
    ↓
2 demandes (nouveau_titre approuvee + duplicata/transfert demande_creee)
```

### ✅ Validation
- Obligatoires manquants ? **REJET**
- Complet ? **ENREGISTREMENT (approuvee ou mix approuvee + demande_creee)**

### État actuel
- 🔧 Backend : À modifier (Dev 1)
- ⏳ Frontend : À commencer (Dev 2)

---

## 🔑 Clé de compréhension

```
Sprint 1 = Nouvelle demande (demande_creee)
Sprint 2 = Régularisation de perte (approuvee)
```

---

## 📊 Comparaison rapide

| Aspect | Sprint 1 | Sprint 2 |
|--------|----------|----------|
| **Type** | Nouvelle demande | Régularisation (perte) |
| **Statut final** | demande_creee | approuvee (ou 2 demandes) |
| **Personne** | Toujours nouvelle | Existante OU nouvelle |
| **Préremplissage** | Non | Oui, si existe |
| **Formulaire** | 1 seul | 2 branches (passeport/carte) |
| **Demandes créées** | 1 | 1 (existante) OU 2 (inconnue) |
| **Documents** | Obligatoires distincts | À définir par les règles Sprint 1 |
| **État avancement** | ✅ 100% | 🔧 Backend + ⏳ Frontend |
