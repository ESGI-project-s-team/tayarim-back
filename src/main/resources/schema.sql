DROP TABLE IF EXISTS Contenir;
DROP TABLE IF EXISTS Respecter;
DROP TABLE IF EXISTS Indisponibilite;
DROP TABLE IF EXISTS Avis;
DROP TABLE IF EXISTS ImageLogement;
DROP TABLE IF EXISTS Reservation;
DROP TABLE IF EXISTS Depense;
DROP TABLE IF EXISTS Logement;
DROP TABLE IF EXISTS TypeLogement;
DROP TABLE IF EXISTS Amenagement;
DROP TABLE IF EXISTS ReglesLogement;
DROP TABLE IF EXISTS Icone;
DROP TABLE IF EXISTS CategorieAmenagement;
DROP TABLE IF EXISTS Administrateur;
DROP TABLE IF EXISTS Proprietaire;
DROP TABLE IF EXISTS Utilisateur;

CREATE TABLE IF NOT EXISTS Utilisateur (
                                           id INT PRIMARY KEY AUTO_INCREMENT,
                                           nom VARCHAR(100),
                                           prenom VARCHAR(100),
                                           email VARCHAR(100),
                                           numTel VARCHAR(100),
                                           motDePasse VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS Proprietaire (
                                            idUser INT PRIMARY KEY,
                                            dateInscription DATETIME,
                                            isPasswordUpdated BOOLEAN,
                                            commission FLOAT
);

CREATE TABLE IF NOT EXISTS Administrateur (
    idUser INT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS Logement (
                                        id INT PRIMARY KEY AUTO_INCREMENT,
                                        titre VARCHAR(100),
                                        isLouable BOOL,
                                        nombresDeChambres INT,
                                        nombresDeLits INT,
                                        nombresSallesDeBains INT,
                                        capaciteMaxPersonne INT,
                                        nombresNuitsMin INT,
                                        description VARCHAR(500),
                                        note FLOAT,
                                        prixParNuit FLOAT,
                                        defaultCheckIn TIME,
                                        defaultCheckOut TIME,
                                        intervalReservation INT,
                                        ville VARCHAR(30),
                                        adresse VARCHAR(150),
                                        codePostal VARCHAR(10),
                                        pays VARCHAR(30),
                                        etage VARCHAR(30),
                                        numeroDePorte VARCHAR(30),
                                        idTypeLogement INT,
                                        idProprietaire INT
);

CREATE TABLE IF NOT EXISTS TypeLogement (
                                            id INT PRIMARY KEY AUTO_INCREMENT,
                                            nom VARCHAR(100),
                                            icone VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS Reservation (
                                           id INT PRIMARY KEY AUTO_INCREMENT,
                                           idCommande VARCHAR(100),
                                           statut VARCHAR(100),
                                           email VARCHAR(100),
                                           numtel VARCHAR(100),
                                           nom VARCHAR(100),
                                           prenom VARCHAR(100),
                                           nbPersonnes INT,
                                           montant FLOAT,
                                           checkIn TIME,
                                           checkOut TIME,
                                           dateArrivee DATE,
                                           dateDepart DATE,
                                           idLogement INT,
                                           dateReservation DATETIME,
                                           paymentIntent VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Amenagement (
                                           id INT PRIMARY KEY AUTO_INCREMENT,
                                           nom VARCHAR(100),
                                           icone VARCHAR(1000),
                                           idCategorieAmenagement INT
);

CREATE TABLE IF NOT EXISTS CategorieAmenagement (
                                                    id INT PRIMARY KEY AUTO_INCREMENT,
                                                    nom VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS Depense (
                                       id INT PRIMARY KEY AUTO_INCREMENT,
                                       libelle VARCHAR(200),
                                       prix FLOAT,
                                       date DATE,
                                       idLogement INT
);

CREATE TABLE IF NOT EXISTS Icone (
                                     id INT PRIMARY KEY AUTO_INCREMENT,
                                     svg VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS ReglesLogement (
                                              id INT PRIMARY KEY AUTO_INCREMENT,
                                              nom VARCHAR(100),
                                              icone VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS ImageLogement (
                                             id INT PRIMARY KEY AUTO_INCREMENT,
                                             url VARCHAR(1000),
                                             idLogement INT,
                                             isMainImage BOOLEAN
);

CREATE TABLE IF NOT EXISTS Avis (
                                    id INT PRIMARY KEY AUTO_INCREMENT,
                                    texte VARCHAR(100),
                                    pseudo VARCHAR(100),
                                    note INT,
                                    date DATETIME,
                                    idLogement INT
);

CREATE TABLE IF NOT EXISTS Indisponibilite (
                                               id INT PRIMARY KEY AUTO_INCREMENT,
                                               dateDebut DATE,
                                               dateFin DATE,
                                               idLogement INT
);

CREATE TABLE IF NOT EXISTS Respecter (
                                         idLogement INT,
                                         idReglesLogement INT,
                                         PRIMARY KEY(idLogement,idReglesLogement)
);

CREATE TABLE IF NOT EXISTS Contenir (
                                        idLogement INT,
                                        idAmenagement INT,
                                        PRIMARY KEY(idLogement,idAmenagement)
);

ALTER TABLE Proprietaire ADD FOREIGN KEY (idUser) REFERENCES Utilisateur(id);

ALTER TABLE Administrateur ADD FOREIGN KEY (idUser) REFERENCES Utilisateur(id);

ALTER TABLE Logement ADD FOREIGN KEY (idTypeLogement) REFERENCES TypeLogement(id);

ALTER TABLE Logement ADD FOREIGN KEY (idProprietaire) REFERENCES Proprietaire(idUser);

ALTER TABLE Reservation ADD FOREIGN KEY (idLogement) REFERENCES Logement(id);

ALTER TABLE Amenagement ADD FOREIGN KEY (idCategorieAmenagement) REFERENCES CategorieAmenagement(id);

ALTER TABLE Depense ADD FOREIGN KEY (idLogement) REFERENCES Logement(id);

ALTER TABLE ImageLogement ADD FOREIGN KEY (idLogement) REFERENCES Logement(id);

ALTER TABLE Avis ADD FOREIGN KEY (idLogement) REFERENCES Logement(id);

ALTER TABLE Indisponibilite ADD FOREIGN KEY (idLogement) REFERENCES Logement(id);

ALTER TABLE Respecter ADD FOREIGN KEY (idLogement) REFERENCES Logement(id);

ALTER TABLE Respecter ADD FOREIGN KEY (idReglesLogement) REFERENCES ReglesLogement(id);

ALTER TABLE Contenir ADD FOREIGN KEY (idLogement) REFERENCES Logement(id);

ALTER TABLE Contenir ADD FOREIGN KEY (idAmenagement) REFERENCES Amenagement(id);