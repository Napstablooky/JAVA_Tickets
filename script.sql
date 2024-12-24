
CREATE DATABASE IF NOT EXISTS ticketapp;
USE ticketapp;

CREATE TABLE IF NOT EXISTS configuration (
    id INT AUTO_INCREMENT PRIMARY KEY,
    processeur VARCHAR(255),
    RAM VARCHAR(255),
    stockage VARCHAR(255),
    gpu VARCHAR(255),
    os VARCHAR(255),
    ecran VARCHAR(255)
);


CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    last_connexion DATETIME,
    statut BOOLEAN NOT NULL DEFAULT TRUE,
    role ENUM('Utilisateur','Intervenant') NOT NULL
);


CREATE TABLE IF NOT EXISTS poste_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    etat ENUM('En_Fonction','En_maintenance','En_commande') NOT NULL,
    configuration_id INT,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE SET NULL,
    FOREIGN KEY (configuration_id) REFERENCES configuration(id) ON DELETE SET NULL
);


CREATE TABLE IF NOT EXISTS ticket (
    id INT AUTO_INCREMENT PRIMARY KEY,
    impact ENUM('Bloquant','Mineur','Majeur') NOT NULL,
    titre VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    date_crea DATETIME NOT NULL,
    etat ENUM('Ouvert','En_cours','Resolu','Ferme','Annule') NOT NULL,
    date_maj DATETIME,
    createur_id INT,
    poste_info_id INT,
    type_demande VARCHAR(255) NOT NULL,
    FOREIGN KEY (createur_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (poste_info_id) REFERENCES poste_info(id) ON DELETE CASCADE
);

INSERT INTO user (nom, email, password, last_connexion, statut, role) VALUES
('Alice Martin', 'alice.martin@example.com', 'password123', '2024-12-12', TRUE, 'Utilisateur'),
('Jean Dupont', 'jean.dupont@example.com', 'password456', '2024-12-12', FALSE, 'Intervenant'),
('Claire Durand', 'claire.durand@example.com', 'password789', '2024-12-12', TRUE, 'Utilisateur'),
('Marc Leblanc', 'marc.leblanc@example.com', 'password321', '2024-12-12', FALSE, 'Intervenant'),
('Sophie Laurent', 'sophie.laurent@example.com', 'password654', '2024-12-12', TRUE, 'Utilisateur');


INSERT INTO configuration (processeur, RAM, stockage, gpu, os, ecran) VALUES
('Intel i5', '16GB DDR4', '1TB SSD', 'NVIDIA RTX 3060', 'Windows 11 Pro', '27 pouces, 144Hz'),
('Intel i5', '16GB DDR4', '1TB SSD', 'NVIDIA RTX 3060', 'Windows 11 Pro', '27 pouces, 144Hz'),
('Intel i5', '16GB DDR4', '1TB SSD', 'NVIDIA RTX 3060', 'Windows 11 Pro', '27 pouces, 144Hz'),
('Intel i5', '16GB DDR4', '1TB SSD', 'NVIDIA RTX 3060', 'Windows 11 Pro', '27 pouces, 144Hz'),
('Intel i5', '16GB DDR4', '1TB SSD', 'NVIDIA RTX 3060', 'Windows 11 Pro', '27 pouces, 144Hz');


INSERT INTO poste_info (user_id, etat, configuration_id) VALUES
(1, 'En_Fonction', 1),
(2, 'En_maintenance', 2),
(3, 'En_Fonction', 3),
(4, 'En_commande', 4),
(5, 'En_Fonction', 5);

INSERT INTO ticket (impact, titre, description, date_crea, etat, date_maj, createur_id, poste_info_id, type_demande) VALUES
('Bloquant', 'Problème démarrage', 'L\'ordinateur ne démarre pas malgré plusieurs tentatives.', '2024-12-12', 'Ouvert', '2024-12-12', 1, 1, 'Matériel'),
('Majeur', 'Problème logiciel comptabilité', 'Le logiciel ne se connecte pas au serveur.', '2024-12-12', 'Ouvert', '2024-12-12', 2, 2, 'Logiciel'),
('Majeur', 'Problème réseau', 'Connexion intermittente au réseau local.', '2024-12-12', 'En_cours', '2024-12-12', 3, 3, 'Réseau'),
('Mineur', 'Problème d\'imprimante', 'L\'imprimante ne répond pas et affiche une erreur E23.', '2024-12-12', 'Ouvert', '2024-12-12', 4, 4, 'Périphérique'),
('Majeur', 'Problème de mise à jour', 'Le système refuse d\'installer les mises à jour Windows.', '2024-12-12', 'Ouvert', '2024-12-12', 5, 5, 'Logiciel');
