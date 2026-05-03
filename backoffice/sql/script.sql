CREATE DATABASE IF NOT EXISTS visa_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE visa_db;

-- =========================
-- TABLES DE REFERENCE
-- =========================

CREATE TABLE situation_familiale (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL
);

CREATE TABLE nationalite (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL
);

CREATE TABLE statut_demande (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE type_demande (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE type_visa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL
);

-- =========================
-- TABLE DEMANDEUR
-- =========================

CREATE TABLE demandeur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenoms VARCHAR(150),
    date_naissance DATE,
    lieu_naissance VARCHAR(150),
    telephone VARCHAR(20),
    email VARCHAR(150),
    adresse TEXT,
    id_situation_familiale INT,
    id_nationalite INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (id_situation_familiale) REFERENCES situation_familiale(id),
    FOREIGN KEY (id_nationalite) REFERENCES nationalite(id)
);

-- =========================
-- PASSEPORT (1 demandeur → N passeports)
-- =========================

CREATE TABLE passeport (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_demandeur INT NOT NULL,
    numero_passeport VARCHAR(50) NOT NULL UNIQUE,
    date_delivrance DATE,
    date_expiration DATE,
    pays_delivrance VARCHAR(100),

    FOREIGN KEY (id_demandeur) REFERENCES demandeur(id) ON DELETE CASCADE
);

-- =========================
-- CARTE DE RESIDENT
-- =========================

CREATE TABLE carte_resident (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_demandeur INT NOT NULL,
    numero_carte_resident VARCHAR(50) NOT NULL UNIQUE,
    date_delivrance DATE,
    date_expiration DATE,

    FOREIGN KEY (id_demandeur) REFERENCES demandeur(id) ON DELETE CASCADE
);

-- =========================
-- VISA (lié à un passeport)
-- =========================

CREATE TABLE visa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    reference VARCHAR(100) NOT NULL UNIQUE,
    date_debut DATE,
    date_fin DATE,
    id_type_visa INT,
    id_passeport INT NOT NULL,

    FOREIGN KEY (id_type_visa) REFERENCES type_visa(id),
    FOREIGN KEY (id_passeport) REFERENCES passeport(id) ON DELETE CASCADE
);

-- =========================
-- DEMANDE (N demandes → 1 visa)
-- =========================

CREATE TABLE demande (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date_demande DATE NOT NULL,
    id_statut_demande INT,
    id_demandeur INT NOT NULL,
    id_visa INT NOT NULL,
    id_type_demande INT,
    type_perte ENUM('passeport_perdu', 'carte_resident_perdue') DEFAULT NULL,
    id_visa_origine INT DEFAULT NULL,
    id_nouveau_passeport INT DEFAULT NULL,
    id_carte_resident INT DEFAULT NULL,
    observations TEXT,
    date_traitement DATE,

    FOREIGN KEY (id_statut_demande) REFERENCES statut_demande(id),
    FOREIGN KEY (id_demandeur) REFERENCES demandeur(id) ON DELETE CASCADE,
    FOREIGN KEY (id_visa) REFERENCES visa(id),
    FOREIGN KEY (id_type_demande) REFERENCES type_demande(id),
    FOREIGN KEY (id_visa_origine) REFERENCES visa(id),
    FOREIGN KEY (id_nouveau_passeport) REFERENCES passeport(id),
    FOREIGN KEY (id_carte_resident) REFERENCES carte_resident(id)
);

-- =========================
-- HISTORIQUE (obligatoire métier)
-- =========================

CREATE TABLE histo_statut_demande (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_demande INT NOT NULL,
    id_statut_demande INT NOT NULL,
    date_changement DATETIME DEFAULT CURRENT_TIMESTAMP,
    commentaire TEXT,

    FOREIGN KEY (id_demande) REFERENCES demande(id) ON DELETE CASCADE,
    FOREIGN KEY (id_statut_demande) REFERENCES statut_demande(id)
);

CREATE TABLE document (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(255) NOT NULL,
    obligatoire BOOLEAN NOT NULL,
    type_cible ENUM('commun', 'investisseur', 'travailleur') NOT NULL
);

-- =========================
-- DOCUMENTS FOURNIS PAR DEMANDE
-- =========================

CREATE TABLE demande_document (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_demande INT NOT NULL,
    id_document INT NOT NULL,
    fourni BOOLEAN DEFAULT FALSE,

    FOREIGN KEY (id_demande) REFERENCES demande(id) ON DELETE CASCADE,
    FOREIGN KEY (id_document) REFERENCES document(id)
);

CREATE TABLE historique_modification (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_demande INT NOT NULL,
    section       VARCHAR(255),
    champ         VARCHAR(255),
    ancienne_valeur TEXT,
    nouvelle_valeur TEXT,
    date_modification TIMESTAMP,

    FOREIGN KEY (id_demande) REFERENCES demande(id) ON DELETE CASCADE
);
