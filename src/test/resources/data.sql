DELETE FROM LOGEMENT;
ALTER TABLE LOGEMENT ALTER COLUMN ID RESTART WITH 1; -- car auto increment
DELETE FROM TYPELOGEMENT;
ALTER TABLE TYPELOGEMENT ALTER COLUMN ID RESTART WITH 1; -- car auto increment
DELETE FROM ICONE;
ALTER TABLE ICONE ALTER COLUMN ID RESTART WITH 1; -- car auto increment
DELETE FROM ADRESSE;
ALTER TABLE ADRESSE ALTER COLUMN ID RESTART WITH 1; -- car auto increment
DELETE FROM PROPRIETAIRE;
DELETE FROM ADMINISTRATEUR;
DELETE FROM UTILISATEUR;
ALTER TABLE UTILISATEUR ALTER COLUMN ID RESTART WITH 1; -- car auto increment
