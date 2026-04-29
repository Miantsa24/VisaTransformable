-- Situation familiale
INSERT INTO situation_familiale(libelle) VALUES 
  ('celibataire'), 
  ('marie'), 
  ('mariee'), 
  ('divorce'), 
  ('divorcee'), 
  ('veuf'), 
  ('veuve');

-- Nationalité (exemples)
INSERT INTO nationalite(libelle) VALUES 
  ('Francais'), 
  ('Malagasy'), 
  ('Italien'), 
  ('Chinois'), 
  ('Comorien');

-- Statut demande
INSERT INTO statut_demande(libelle) VALUES 
  ('demande_creee'), 
  ('demande_rejetee'),
  ('approuvee');

-- Type demande
INSERT INTO type_demande(libelle) VALUES 
  ('nouveau_titre'), 
  ('duplicata'),
  ('transfert_visa');

-- Type visa
INSERT INTO type_visa(libelle) VALUES 
  ('investisseur'), 
  ('travailleur');

  -- Documents communs
INSERT INTO document (libelle, obligatoire, type_cible) VALUES
  ('2 photos d''identité', TRUE, 'commun'),
  ('Notice de renseignement', FALSE, 'commun'),
  ('Demande adressée à Monsieur le Ministre de l''Intérieur et de la Décentralisation (avec adresse e-mail et numéro de téléphone portable)', FALSE, 'commun'),
  ('Photocopie certifiée du visa en cours de validité', TRUE, 'commun'),
  ('Photocopie certifiée de la première page du passeport', FALSE, 'commun'),
  ('Photocopie certifiée de la carte de résident en cours de validité', TRUE, 'commun'),
  ('Certificat de résidence à Madagascar', TRUE, 'commun'),
  ('Extrait de casier judiciaire (de moins de 3 mois)', TRUE, 'commun');

-- Documents Investisseur
INSERT INTO document (libelle, obligatoire, type_cible) VALUES
  ('Statuts de la société', TRUE, 'investisseur'),
  ('Extrait d''inscription au registre du commerce', FALSE, 'investisseur'),
  ('Carte fiscale', TRUE, 'investisseur');

-- Documents Travailleur
INSERT INTO document (libelle, obligatoire, type_cible) VALUES
  ('Autorisation d''emploi délivrée à Madagascar par le Ministère de la Fonction Publique', TRUE, 'travailleur'),
  ('Attestation d''emploi délivrée par l''employeur', FALSE, 'travailleur');
