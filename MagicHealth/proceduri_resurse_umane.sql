use lant_policlinici;

DROP PROCEDURE IF EXISTS afisare_date_personale;
DELIMITER //
-- procedura afiseaza datele personale ale unui angajat, conform username-ului cu care se conecteaza la baza de date
CREATE PROCEDURE afisare_date_personale (username varchar(80)) 
BEGIN 
	SELECT cnp, nume, prenume, oras, strada, nr, telefon, email, cont_IBAN, nr_contract, data_angajare, data_concediere, functie
    FROM angajat, users WHERE angajat.cnp = users.cnp_user AND users.username = username;
END //

DELIMITER ;

DROP PROCEDURE IF EXISTS gasire_angajat;
DELIMITER //
-- procedura afiseaza datele personale ale angajatului cu numele, prenumele si functia date ca parametri
CREATE PROCEDURE gasire_angajat(nume_a varchar(20), prenume_a varchar(20), functie_a varchar(50)) 
BEGIN
	IF (functie_a = 'medic') THEN
		SELECT * FROM medic WHERE nume = nume_a AND prenume = prenume_a;
    ELSE IF (functie_a = 'asistent_medical') THEN 
		SELECT * FROM asistent_medical WHERE nume = nume_a AND prenume = prenume_a;
		ELSE 
			SELECT * FROM angajat WHERE nume = nume_a AND prenume = prenume_a AND functie = functie_a;
		END IF;
    END IF;
END //

DELIMITER ;

DROP PROCEDURE IF EXISTS afisare_orar_angajat;
DELIMITER //
-- procedura afiseaza orarul unui angajat, gasit dupa numele, prenumele si functia date ca parametri
CREATE PROCEDURE afisare_orar_angajat (nume_a varchar(20), prenume_a varchar(20), functie_a varchar(50))
BEGIN
	DECLARE person int;
    DECLARE orar int;
    SET person := (SELECT cnp FROM angajat WHERE nume = nume_a AND prenume = prenume_a AND functie = functie_a);
    SET orar := (SELECT count(*) FROM orar_medical WHERE id_angajat = person);

	-- selectie pe orar medical, daca angajatul are functia 'medic' sau 'asistent_medical' si are un orar specific
    -- selectie pe orar obisnuit, daca angajatul are orice alta functie sau nu are unul special
	IF (orar = 0) THEN 
		SELECT * FROM orar_angajat WHERE id_angajat = person;
	ELSE 
		SELECT * FROM orar_medical WHERE id_angajat = person AND zi >= date(now());
	END IF;
    
END //

DELIMITER ;

DROP PROCEDURE IF EXISTS adaugare_serviciu_specializat;
DELIMITER //
-- procedura va adauga in baza de date un nou serviciu specializat, care va fi caracteristic medicului cu cnp-ul transmis ca parametru si va 
-- avea pret si/sau durata specifica
CREATE PROCEDURE adaugare_serviciu_specializat(serviciu int, cnp_medic int, durata_specifica time, pret_specific float) 
BEGIN
	INSERT INTO serviciu_specializat(id_serviciu, id_medic, durata_noua, pret_nou) VALUES (serviciu, cnp_medic, durata_specifica, pret_specific);
END //

DELIMITER ;