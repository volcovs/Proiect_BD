use lant_policlinici;

DROP TRIGGER IF EXISTS adaugare_asistent;
DELIMITER //
-- trigger care adauga datele personale ale asistentului medical si in tabela angajat

CREATE TRIGGER adaugare_asistent BEFORE INSERT ON asistent_medical 
FOR EACH ROW
	BEGIN
		INSERT INTO angajat VALUES(new.cnp, new.nume, new.prenume, new.oras, new.strada, new.nr, new.telefon, new.email, new.cont_IBAN, new.nr_contract, new.data_angajare, new.data_concediere,
        'asistent medical',  new.salariu_negociat, new.nr_ore_luna, 3, new.data_inceput_concediu, new.data_final_concediu);
    END//

DELIMITER ;

DROP TRIGGER IF EXISTS adaugare_medic;
DELIMITER //
-- trigger care adauga datele personale ale medicului si in tabela angajat

CREATE TRIGGER adaugare_medic BEFORE INSERT ON medic 
FOR EACH ROW
	BEGIN
		INSERT INTO angajat VALUES(new.cnp, new.nume, new.prenume, new.oras, new.strada, new.nr, new.telefon, new.email, new.cont_IBAN, new.nr_contract, new.data_angajare, new.data_concediere,
        'medic',  new.salariu_negociat, new.nr_ore_luna, 3, new.data_inceput_concediu, new.data_final_concediu);
    END//

DELIMITER ;

DROP TRIGGER IF EXISTS stergere_asistent;
DELIMITER //
-- acest trigger va sterge asistentul medical si din tabela angajat

CREATE TRIGGER stergere_asistent BEFORE DELETE ON asistent_medical
FOR EACH ROW
	BEGIN
		DELETE FROM angajat
		WHERE angajat.cnp = old.cnp;
	END //
    
DELIMITER ;

DROP TRIGGER IF EXISTS stergere_medic;
DELIMITER //
-- acest trigger va sterge medicul si din tabela angajat

CREATE TRIGGER stergere_medic BEFORE DELETE ON medic
FOR EACH ROW
	BEGIN
		DELETE FROM angajat
        WHERE angajat.cnp = old.cnp;
	END //

DELIMITER ;

DROP TRIGGER IF EXISTS update_asistent;
DELIMITER //
-- acest trigger actualizeaza datele despre un asistent medical si in tabela angajat

CREATE TRIGGER update_asistent BEFORE UPDATE ON asistent_medical 
FOR EACH ROW
	BEGIN 
		UPDATE angajat
        SET nume = new.nume, prenume = new.prenume, oras = new.oras, strada = new.strada, nr = new.nr, telefon = new.telefon, email = new.email, cont_IBAN = new.cont_IBAN, 
        data_concediere = new.data_concediere
        WHERE angajat.cnp = new.cnp;
	END //

DELIMITER ;

DROP TRIGGER IF EXISTS update_medic;
DELIMITER //
-- acest trigger actualizeaza datele despre un medic si in tabela angajat

CREATE TRIGGER update_medic BEFORE UPDATE ON medic 
FOR EACH ROW
	BEGIN 
		UPDATE angajat
        SET nume = new.nume, prenume = new.prenume, oras = new.oras, strada = new.strada, nr = new.nr, telefon = new.telefon, email = new.email, cont_IBAN = new.cont_IBAN, 
        data_concediere = new.data_concediere
        WHERE angajat.cnp = new.cnp;
	END //

DELIMITER ;

DROP TRIGGER IF EXISTS consult;
DELIMITER //
-- acest trigger actualizeaza indicatorul de prezenta al pacientului la data si ora indicate in programare (consultatia a avut loc => pacientul s-a prezentat
-- si va trebui sa achite pretul serviciului medical prestat)

CREATE TRIGGER consult BEFORE INSERT ON consultatie
FOR EACH ROW
	BEGIN
		UPDATE programare
        SET prezent = true
        WHERE id = new.id_programare;
    END //
    
DELIMITER ;

DROP TRIGGER IF EXISTS orar_medic;
DELIMITER //
-- acest trigger creeaza automat orarul unui medic in momentul in care se adauga o programare noua

CREATE TRIGGER orar_medic BEFORE INSERT ON programare
FOR EACH ROW
	BEGIN
		INSERT INTO orar_medical(zi, timp_inceput, timp_final, id_policlinica, id_angajat) VALUES(date(new.data_ora), time(new.data_ora), time(DATE_ADD(new.data_ora, INTERVAL TIME_TO_SEC(new.durata) SECOND)), new.id_policlinica, new.id_medic);
	END //
    
DELIMITER ;

DROP TRIGGER IF EXISTS cabinet_nou;
DELIMITER //
-- acest trigger incrementeaza numarul de cabinete medicale disponibile pe locatia medicala al celui nou-adaugat

CREATE TRIGGER cabinet_nou BEFORE INSERT ON cabinet_medical 
FOR EACH ROW
	BEGIN
		DECLARE unitate_medicala int;
        SET unitate_medicala := (SELECT id FROM policlinica WHERE id = new.id_unitate_medicala);
        IF (unitate_medicala IS NOT NULL) THEN
			UPDATE policlinica
			SET nr_cabinete = nr_cabinete + 1
			WHERE id = new.id_unitate_medicala;
		END IF;
	END //
    
DELIMITER ;

DROP TRIGGER IF EXISTS duplicate_servicii_specializate;
DELIMITER //
-- acest trigger va elimina inregistrarile mai vechi care reprezinta specializari pentru acelasi medic si acelasi serviciu ca cel ce urmeaza a fi adaugat

CREATE TRIGGER duplicate_servicii_specializate BEFORE INSERT ON serviciu_specializat
FOR EACH ROW
	BEGIN
		DECLARE exista_serviciu int;
        
        SET exista_serviciu := (SELECT id_inregistrare FROM serviciu_specializat WHERE id_serviciu = new.id_serviciu AND id_medic = new.id_medic);
        -- se presupune ca va exista mereu o singura inregistrare mai veche pentru a fi eliminata
        IF (exista_serviciu IS NOT NULL) THEN
			DELETE FROM serviciu_specializat 
            WHERE id_inregistrare = exista_serviciu;
		END IF;
    
    END //
    
DELIMITER ;
