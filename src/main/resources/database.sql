CREATE TABLE IF NOT EXISTS utilisateurs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    mot_de_passe_hash VARCHAR(64) NOT NULL,
    role VARCHAR(30) NOT NULL DEFAULT 'ADMIN',
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS livres (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    auteur VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    categorie VARCHAR(100) NOT NULL,
    annee_publication INT NOT NULL,
    nb_exemplaires INT NOT NULL DEFAULT 1,
    nb_disponibles INT NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS abonnes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    telephone VARCHAR(30),
    date_inscription DATE NOT NULL,
    statut VARCHAR(20) NOT NULL DEFAULT 'ACTIF'
);

CREATE TABLE IF NOT EXISTS emprunts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_livre INT NOT NULL,
    id_abonne INT NOT NULL,
    date_emprunt DATE NOT NULL,
    date_retour_prevue DATE NOT NULL,
    date_retour_effective DATE NULL,
    statut VARCHAR(20) NOT NULL DEFAULT 'EN_COURS',
    FOREIGN KEY (id_livre) REFERENCES livres(id) ON DELETE CASCADE,
    FOREIGN KEY (id_abonne) REFERENCES abonnes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS penalites (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_emprunt INT NOT NULL UNIQUE,
    montant DECIMAL(10,2) NOT NULL,
    date_calcul DATE NOT NULL,
    statut_paiement VARCHAR(20) NOT NULL DEFAULT 'IMPAYE',
    FOREIGN KEY (id_emprunt) REFERENCES emprunts(id) ON DELETE CASCADE
);

MERGE INTO utilisateurs (email, mot_de_passe_hash, role, nom, prenom) KEY(email) VALUES
('admin@biblio.com', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'ADMIN', 'Admin', 'Systeme');

INSERT IGNORE INTO livres (titre, auteur, isbn, categorie, annee_publication, nb_exemplaires, nb_disponibles) VALUES
('Introduction a l''algorithmique', 'Thomas Cormen', '978-2-10-054526-1', 'Informatique', 2018, 5, 3),
('Les Miserables', 'Victor Hugo', '978-2-253-09647-8', 'Litterature', 1862, 3, 2),
('Physique quantique', 'Richard Feynman', '978-0-201-02118-9', 'Sciences', 2011, 4, 4),
('Histoire de l''Afrique', 'Catherine Coquery-Vidrovitch', '978-2-13-054526-2', 'Histoire', 2015, 2, 1),
('Programmation Java', 'Joshua Bloch', '978-0-321-35668-0', 'Informatique', 2019, 6, 4),
('Le Petit Prince', 'Antoine de Saint-Exupery', '978-2-07-061275-8', 'Litterature', 1943, 4, 3),
('Biologie cellulaire', 'Bruce Alberts', '978-0-8153-4502-4', 'Sciences', 2017, 3, 2),
('Economie generale', 'Paul Samuelson', '978-0-07-351129-0', 'Economie', 2010, 2, 2),
('Mathematiques superieures', 'Bernard Kolman', '978-0-13-469645-4', 'Mathematiques', 2016, 5, 3),
('Droit constitutionnel', 'Georges Burdeau', '978-2-13-054526-3', 'Droit', 2014, 3, 2);

INSERT IGNORE INTO abonnes (nom, prenom, email, telephone, date_inscription, statut) VALUES
('Kouassi', 'Jean', 'jean.kouassi@univ.ci', '0701020304', '2024-09-01', 'ACTIF'),
('Traore', 'Aminata', 'aminata.traore@univ.ci', '0702030405', '2024-09-15', 'ACTIF'),
('Diallo', 'Moussa', 'moussa.diallo@univ.ci', '0703040506', '2024-10-01', 'ACTIF'),
('Bamba', 'Fatou', 'fatou.bamba@univ.ci', '0704050607', '2024-10-20', 'ACTIF'),
('N''Guessan', 'Koffi', 'koffi.nguessan@univ.ci', '0705060708', '2024-11-05', 'ACTIF');

INSERT IGNORE INTO emprunts (id_livre, id_abonne, date_emprunt, date_retour_prevue, date_retour_effective, statut) VALUES
(1, 1, '2025-01-10', '2025-01-24', NULL, 'EN_COURS'),
(2, 2, '2025-01-05', '2025-01-19', NULL, 'EN_RETARD'),
(3, 3, '2025-02-01', '2025-02-15', NULL, 'EN_COURS'),
(4, 4, '2024-12-01', '2024-12-15', NULL, 'EN_RETARD'),
(5, 5, '2025-02-10', '2025-02-24', NULL, 'EN_COURS'),
(6, 1, '2024-11-01', '2024-11-15', '2024-11-14', 'RETOURNE'),
(7, 2, '2024-10-15', '2024-10-29', '2024-10-28', 'RETOURNE'),
(8, 3, '2024-12-20', '2025-01-03', NULL, 'EN_RETARD');

INSERT IGNORE INTO penalites (id_emprunt, montant, date_calcul, statut_paiement) VALUES
(2, 250.00, '2025-01-15', 'IMPAYE'),
(4, 500.00, '2025-01-15', 'IMPAYE'),
(8, 150.00, '2025-01-15', 'IMPAYE');
