-- Création de la base de données
CREATE DATABASE IF NOT EXISTS bdagile;
USE bdagile;

-- Table `voyage`
CREATE TABLE IF NOT EXISTS voyage (
    id INT AUTO_INCREMENT PRIMARY KEY,
    depart VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    description TEXT,
    date_depart DATE NOT NULL,
    date_retour DATE NOT NULL,
    places_disponibles INT NOT NULL,
    prix DECIMAL(10, 2) NOT NULL
);

-- Table `voyageur`
CREATE TABLE IF NOT EXISTS voyageur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    telephone VARCHAR(15),
    mot_de_passe VARCHAR(255) NOT NULL
);

-- Table `reservation`
CREATE TABLE IF NOT EXISTS reservation (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_voyage INT NOT NULL,
    idVoyageur INT NOT NULL,
    nombre_places INT NOT NULL,
    dateReservation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut ENUM('confirmée', 'annulée', 'en attente') DEFAULT 'en attente',
    montantPaye DECIMAL(10, 2) DEFAULT 0.00,
    FOREIGN KEY (id_voyage) REFERENCES voyage(id) ON DELETE CASCADE,
    FOREIGN KEY (idVoyageur) REFERENCES voyageur(id) ON DELETE CASCADE
);

INSERT INTO voyageur (nom, prenom, email, mot_de_passe)
VALUES ('Administrateur', 'admin', 'admin@gmail.com', '123456');