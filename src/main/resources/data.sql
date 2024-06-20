INSERT INTO UTILISATEUR (NOM, PRENOM, EMAIL, NUMTEL, MOTDEPASSE) VALUES ('John', 'Doe', 'johndoe@gmail.com', '+330612345678', '$2a$12$3hQDUblvPShmuQg/.g0Qk.wHAGjqPL54RMO/lNgsei/HQGo0ZLIYm');
INSERT INTO PROPRIETAIRE (IDUSER, DATEINSCRIPTION, ISPASSWORDUPDATED, COMMISSION) VALUES ((SELECT max(ID) FROM UTILISATEUR), CURRENT_TIME(), TRUE, 20.00);
INSERT INTO TYPELOGEMENT (NOM, ICONE) VALUES ( 'Appartement', 'MdApartment' );
INSERT INTO TYPELOGEMENT (NOM, ICONE) VALUES ( 'Maison', 'FaHouse' );
INSERT INTO TYPELOGEMENT (NOM, ICONE) VALUES ( 'Studio', 'FaHouseUser' );
INSERT INTO TYPELOGEMENT (NOM, ICONE) VALUES ( 'Loft', 'PiWarehouseFill' );
INSERT INTO TYPELOGEMENT (NOM, ICONE) VALUES ( 'Penthouse', 'FaPeopleRoof' );
INSERT INTO TYPELOGEMENT (NOM, ICONE) VALUES ( 'Villa', 'MdVilla' );
INSERT INTO TYPELOGEMENT (NOM, ICONE) VALUES ( 'StudentResidence', 'PiStudent' );
INSERT INTO REGLESLOGEMENT (NOM, ICONE) VALUES ( 'Children allowed', 'FaChild' );
INSERT INTO REGLESLOGEMENT (NOM, ICONE) VALUES ( 'Smoking allowed', 'FaSmoking' );
INSERT INTO REGLESLOGEMENT (NOM, ICONE) VALUES ( 'Pets allowed', 'MdOutlinePets' );
INSERT INTO REGLESLOGEMENT (NOM, ICONE) VALUES ( 'Events allowed', 'LuPartyPopper' );
INSERT INTO REGLESLOGEMENT (NOM, ICONE) VALUES ( 'Infants allowed', 'FaBaby' );
INSERT INTO CATEGORIEAMENAGEMENT (NOM) VALUES ( 'Kitchen' );
INSERT INTO AMENAGEMENT (NOM, ICONE, IDCATEGORIEAMENAGEMENT) VALUES ( 'Oven', 'PiOvenDuotone', (SELECT max(ID) FROM CATEGORIEAMENAGEMENT) );
INSERT INTO AMENAGEMENT (NOM, ICONE, IDCATEGORIEAMENAGEMENT) VALUES ( 'Washer', 'BiSolidWasher', (SELECT max(ID) FROM CATEGORIEAMENAGEMENT) );
INSERT INTO CATEGORIEAMENAGEMENT (NOM) VALUES ( 'Technology' );
INSERT INTO AMENAGEMENT (NOM, ICONE, IDCATEGORIEAMENAGEMENT) VALUES ( 'Elevator', 'MdElevator', (SELECT max(ID) FROM CATEGORIEAMENAGEMENT) );
INSERT INTO AMENAGEMENT (NOM, ICONE, IDCATEGORIEAMENAGEMENT) VALUES ( 'Internet', 'FaWifi', (SELECT max(ID) FROM CATEGORIEAMENAGEMENT) );
INSERT INTO CATEGORIEAMENAGEMENT (NOM) VALUES ( 'Other' );
INSERT INTO AMENAGEMENT (NOM, ICONE, IDCATEGORIEAMENAGEMENT) VALUES ( 'Free Parking', 'FaParking', (SELECT max(ID) FROM CATEGORIEAMENAGEMENT) );
INSERT INTO AMENAGEMENT (NOM, ICONE, IDCATEGORIEAMENAGEMENT) VALUES ( 'Swimming Pool', 'PiSwimmingPool', (SELECT max(ID) FROM CATEGORIEAMENAGEMENT) );
INSERT INTO AMENAGEMENT (NOM, ICONE, IDCATEGORIEAMENAGEMENT) VALUES ( 'Air Conditioning', 'TbAirConditioning', (SELECT max(ID) FROM CATEGORIEAMENAGEMENT) );
INSERT INTO AMENAGEMENT (NOM, ICONE, IDCATEGORIEAMENAGEMENT) VALUES ( 'Handicap Accessible', 'BiHandicap', (SELECT max(ID) FROM CATEGORIEAMENAGEMENT) );
INSERT INTO LOGEMENT (TITRE, ISLOUABLE, NOMBRESDECHAMBRES, NOMBRESDELITS, NOMBRESSALLESDEBAINS, CAPACITEMAXPERSONNE, NOMBRESNUITSMIN, DESCRIPTION, NOTE, PRIXPARNUIT, DEFAULTCHECKIN, DEFAULTCHECKOUT, INTERVALRESERVATION, VILLE, ADRESSE, CODEPOSTAL, PAYS, ETAGE, NUMERODEPORTE, IDTYPELOGEMENT, IDPROPRIETAIRE) VALUES ( 'Jolie petit endroit', true, 1, 1, 1, 1, 6, 'une description', 2.5, 25.30, CURRENT_TIME(), CURRENT_TIME(), 1, 'Etampes', '85 rue de la république', '91150', 'France', '1', '1', 1, (SELECT max(ID) FROM UTILISATEUR));
INSERT INTO LOGEMENT (TITRE, ISLOUABLE, NOMBRESDECHAMBRES, NOMBRESDELITS, NOMBRESSALLESDEBAINS, CAPACITEMAXPERSONNE, NOMBRESNUITSMIN, DESCRIPTION, NOTE, PRIXPARNUIT, DEFAULTCHECKIN, DEFAULTCHECKOUT, INTERVALRESERVATION, VILLE, ADRESSE, CODEPOSTAL, PAYS, ETAGE, NUMERODEPORTE, IDTYPELOGEMENT, IDPROPRIETAIRE) VALUES ( 'superbe', true, 1, 1, 1, 1, 6, 'une description', 2.5, 25.30, CURRENT_TIME(), CURRENT_TIME(), 1, 'Etampes', '85 rue de la république', '91150', 'France', '1', '1', 1, (SELECT max(ID) FROM UTILISATEUR));
INSERT INTO RESPECTER (IDLOGEMENT, IDREGLESLOGEMENT) VALUES (1, 1);
INSERT INTO RESPECTER (IDLOGEMENT, IDREGLESLOGEMENT) VALUES (1, 2);
INSERT INTO RESPECTER (IDLOGEMENT, IDREGLESLOGEMENT) VALUES (1, 3);
INSERT INTO RESPECTER (IDLOGEMENT, IDREGLESLOGEMENT) VALUES (1, 4);
INSERT INTO RESPECTER (IDLOGEMENT, IDREGLESLOGEMENT) VALUES (1, 5);
INSERT INTO CONTENIR (IDLOGEMENT, IDAMENAGEMENT) VALUES (1, 1);
INSERT INTO CONTENIR (IDLOGEMENT, IDAMENAGEMENT) VALUES (1, 2);
INSERT INTO CONTENIR (IDLOGEMENT, IDAMENAGEMENT) VALUES (1, 3);
INSERT INTO CONTENIR (IDLOGEMENT, IDAMENAGEMENT) VALUES (1, 4);
INSERT INTO CONTENIR (IDLOGEMENT, IDAMENAGEMENT) VALUES (1, 5);
INSERT INTO CONTENIR (IDLOGEMENT, IDAMENAGEMENT) VALUES (1, 6);
INSERT INTO CONTENIR (IDLOGEMENT, IDAMENAGEMENT) VALUES (1, 7);
INSERT INTO CONTENIR (IDLOGEMENT, IDAMENAGEMENT) VALUES (1, 8);
INSERT INTO RESERVATION (IDCOMMANDE, STATUT, EMAIL, NUMTEL, NOM, PRENOM, NBPERSONNES, MONTANT, CHECKIN, CHECKOUT, DATEARRIVEE, DATEDEPART, IDLOGEMENT, DATERESERVATION) VALUES ( 'RESA-293254', 'payed', 'luclient@gmail.com', '0698762345', 'Lient', 'Luc', 1, 25.30, CURRENT_TIME(), CURRENT_TIME(), CURRENT_DATE(), (SELECT DATEADD(DAY , 1, CURRENT_TIMESTAMP)), 1, CURRENT_DATE());
INSERT INTO RESERVATION (IDCOMMANDE, STATUT, EMAIL, NUMTEL, NOM, PRENOM, NBPERSONNES, MONTANT, CHECKIN, CHECKOUT, DATEARRIVEE, DATEDEPART, IDLOGEMENT, DATERESERVATION) VALUES ( 'RESA-293255', 'payed', 'luclient@gmail.com', '0698762345', 'Lient', 'Luc', 1, 25.30, CURRENT_TIME(), CURRENT_TIME(), CURRENT_DATE(), (SELECT DATEADD(DAY , 2, CURRENT_TIMESTAMP)), 1, CURRENT_DATE());
INSERT INTO RESERVATION (IDCOMMANDE, STATUT, EMAIL, NUMTEL, NOM, PRENOM, NBPERSONNES, MONTANT, CHECKIN, CHECKOUT, DATEARRIVEE, DATEDEPART, IDLOGEMENT, DATERESERVATION) VALUES ( 'RESA-293255', 'payed', 'luclient@gmail.com', '0698762345', 'Lient', 'Luc', 1, 25.30, CURRENT_TIME(), CURRENT_TIME(), (SELECT DATEADD(DAY , 3, CURRENT_TIMESTAMP)), (SELECT DATEADD(DAY , 10, CURRENT_TIMESTAMP)), 1, CURRENT_DATE());

INSERT INTO UTILISATEUR (NOM, PRENOM, EMAIL, NUMTEL, MOTDEPASSE) VALUES ('Jean', 'Paul', 'jeanpaul@gmail.com', '+330623456789', '$2a$12$3hQDUblvPShmuQg/.g0Qk.wHAGjqPL54RMO/lNgsei/HQGo0ZLIYm');
INSERT INTO PROPRIETAIRE (IDUSER, DATEINSCRIPTION, ISPASSWORDUPDATED, COMMISSION) VALUES ((SELECT max(ID) FROM UTILISATEUR), CURRENT_TIME(), TRUE, 20.00);

INSERT INTO UTILISATEUR (NOM, PRENOM, EMAIL, NUMTEL, MOTDEPASSE) VALUES ('Jade', 'Min', 'jadmin@gmail.com', '+330634567890', '$2a$12$3hQDUblvPShmuQg/.g0Qk.wHAGjqPL54RMO/lNgsei/HQGo0ZLIYm');
INSERT INTO ADMINISTRATEUR (IDUSER) VALUES ((SELECT max(ID) FROM UTILISATEUR));

INSERT INTO UTILISATEUR (NOM, PRENOM, EMAIL, NUMTEL, MOTDEPASSE) VALUES ('Ilad', 'Min', 'jiladmin@gmail.com', '+330634567891', '$2a$12$3hQDUblvPShmuQg/.g0Qk.wHAGjqPL54RMO/lNgsei/HQGo0ZLIYm');
INSERT INTO ADMINISTRATEUR (IDUSER) VALUES ((SELECT max(ID) FROM UTILISATEUR));