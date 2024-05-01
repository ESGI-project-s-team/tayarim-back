INSERT INTO UTILISATEUR (NOM, PRENOM, EMAIL, NUMTEL, MOTDEPASSE) VALUES ('John', 'Doe', 'johndoe@gmail.com', '+330612345678', '$2a$12$3hQDUblvPShmuQg/.g0Qk.wHAGjqPL54RMO/lNgsei/HQGo0ZLIYm');
INSERT INTO PROPRIETAIRE (IDUSER, DATEINSCRIPTION, ISPASSWORDUPDATED) VALUES ((SELECT max(ID) FROM UTILISATEUR), CURRENT_TIME(), TRUE);
INSERT INTO ADRESSE (VILLE, RUE, NUMERO, SUFFIXENUMERO, CODEPOSTAL, PAYS, ETAGE, NUMERODEPORTE, LONGITUDE, LATITUDE) VALUES ( 'FakeCity', 'FakeStreet', (SELECT max(ID) FROM UTILISATEUR), '', '00001', 'France', '1', '1', 0.90, 0.45 );
INSERT INTO ICONE (SVG) VALUES ( 'lien vers svg' );
INSERT INTO TYPELOGEMENT (NOM, IDICONE) VALUES ( 'Appartement', (SELECT max(ID) FROM UTILISATEUR) );
INSERT INTO LOGEMENT (TYPE, LOUABLE, NOMBRESDECHAMBRES, NOMBRESDELITS, NOMBRESSALLESDEBAINS, CAPACITEMAXPERSONNE, NOMBRESNUITSMIN, DESCRIPTION, NOTE, PRIXPARNUIT, DEFAULTCHECKIN, DEFAULTCHECKOUT, INTERVALRESERVATION, IDADRESSE, IDTYPELOGEMENT, IDPROPRIETAIRE) VALUES ( 'En trop sa non ?', true, 1, 1, 1, 1, 6, 'une description', 2.5, 25.30, CURRENT_TIME(), CURRENT_TIME(), 2, 1, 1, (SELECT max(ID) FROM UTILISATEUR));

INSERT INTO UTILISATEUR (NOM, PRENOM, EMAIL, NUMTEL, MOTDEPASSE) VALUES ('Jean', 'Paul', 'jeanpaul@gmail.com', '+330623456789', '$2a$12$3hQDUblvPShmuQg/.g0Qk.wHAGjqPL54RMO/lNgsei/HQGo0ZLIYm');
INSERT INTO PROPRIETAIRE (IDUSER, DATEINSCRIPTION, ISPASSWORDUPDATED) VALUES ((SELECT max(ID) FROM UTILISATEUR), CURRENT_TIME(), TRUE);

INSERT INTO UTILISATEUR (NOM, PRENOM, EMAIL, NUMTEL, MOTDEPASSE) VALUES ('Jade', 'Min', 'jadmin@gmail.com', '+330634567890', '$2a$12$3hQDUblvPShmuQg/.g0Qk.wHAGjqPL54RMO/lNgsei/HQGo0ZLIYm');
INSERT INTO ADMINISTRATEUR (IDUSER) VALUES ((SELECT max(ID) FROM UTILISATEUR));