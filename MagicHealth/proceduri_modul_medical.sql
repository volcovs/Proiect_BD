use lant_policlinici;

DROP PROCEDURE IF EXISTS emitere_bon;
DELIMITER //
-- procedura va crea o inregistrare noua in tabela bon_fiscal, setand pretul conform cu serviciul prestat
CREATE PROCEDURE emitere_bon(data_prog date, data_emitere date, ora time, pacient int, serviciu int, medic int) 
BEGIN	
	DECLARE plata float;
    SET plata := (SELECT pret_nou FROM serviciu_specializat WHERE id_serviciu = serviciu AND id_medic = medic);
    
    IF (plata is NULL) THEN
		SET plata := (SELECT pret FROM serviciu_medical WHERE id = serviciu);
	END IF;
    
	INSERT INTO bon_fiscal(data_timp_emitere, id_client, id_serviciu, id_medic, id_policlinica, suma) VALUES (cast(concat(data_emitere, ' ', ora) as datetime), pacient, serviciu, medic, 1, plata);
		
	UPDATE programare 
    SET bon = true 
    WHERE id_pacient = pacient AND id_serviciu = serviciu AND id_medic = medic AND date(data_ora) = data_prog;
END //

DELIMITER ; 

DROP PROCEDURE IF EXISTS vizualizare_pacienti_programati;
DELIMITER //
-- procedura va selecta programarile pentru ziua indicata si medicul indicat (interesat in special de pacientii programati)
CREATE PROCEDURE vizualizare_pacienti_programati(zi date, medic int) 
BEGIN
	SELECT nume, prenume, data_ora, id_policlinica, nr_cabinet, durata FROM pacient, programare WHERE date(programare.data_ora) = zi AND programare.id_medic = medic AND pacient.cnp = programare.id_pacient;
END //

DELIMITER ;

DROP PROCEDURE IF EXISTS vizualizare_istoric;
DELIMITER //
-- procedura afiseaza istoricul medical al unui pacient, selectand rapoartele medicale completate in urma consultarii acestuia
CREATE PROCEDURE vizualizare_istoric(id_pacient int) 
BEGIN
	DECLARE nume_p varchar(20);
    DECLARE prenume_p varchar(20);
    
    SET nume_p := (SELECT nume FROM pacient WHERE cnp = id_pacient);
    SET prenume_p := (SELECT prenume FROM pacient WHERE cnp = id_pacient);
    
	SELECT raport_medical.nume_pacient, raport_medical.prenume_pacient, raport_medical.id, diagnostic.descriere, raport_medical.data_consultatie
    FROM raport_medical, medic, diagnostic
    WHERE raport_medical.nume_pacient = nume_p AND raport_medical.prenume_pacient = prenume_p AND medic.nume = raport_medical.nume_medic
		AND raport_medical.prenume_medic = medic.prenume AND raport_medical.id_diagnostic = diagnostic.id AND raport_medical.parafat = true
	ORDER BY raport_medical.data_consultatie;
    
END //

DELIMITER ;

DROP PROCEDURE IF EXISTS completare_raport;
DELIMITER //
-- procedura creeaza o inregistrare noua in tabela raport_medical, folosind datele primite ca parametri
CREATE PROCEDURE completare_raport (nume_p varchar(20), prenume_p varchar(20), nume_m varchar(20), prenume_m varchar(20), nume_a varchar(20), prenume_a varchar(20), data_c datetime, istoric varchar(100), simpt varchar(100), id_serv int, diagnostic varchar(200), recomandari varchar(50))
BEGIN
    DECLARE id_diag int;
    DECLARE rez_serviciu int;
    DECLARE pacient_cnp int;
    DECLARE medic_cnp int;
    DECLARE id_program int;
    
    SET id_diag := (SELECT id FROM diagnostic WHERE descriere = diagnostic);
    SET rez_serviciu := (SELECT rezultat FROM analiza_medicala WHERE id = id_serv);
    
	INSERT INTO raport_medical(nume_pacient, prenume_pacient, nume_medic, prenume_medic, nume_asistent, prenume_asistent, data_consultatie, istoric_boala, simptome, 
    id_serviciu, rezultat, id_diagnostic, recomandari, parafat) VALUES
    (nume_p, prenume_p, nume_m, prenume_m, nume_a, prenume_a, data_c, istoric, simpt, id_serv, rez_serviciu, id_diag, recomandari, false);
    
    SET pacient_cnp := (SELECT cnp FROM pacient WHERE nume = nume_p AND prenume = prenume_p);
    SET medic_cnp := (SELECT cnp FROM medic WHERE nume = nume_m AND prenume = prenume_m);
    
    UPDATE programare
    SET prezent = true
    WHERE date(data_ora) = data_consultatie AND id_serviciu = id_serv AND id_pacient = pacient_cnp AND id_medic = medic_cnp;
    
    SET id_program := (SELECT id FROM programare WHERE date(data_ora) = data_consultatie AND id_serviciu = id_serv AND id_pacient = pacient_cnp AND id_medic = medic_cnp);
    
    INSERT INTO consultatie(id_programare, data_ora, id_medic, id_pacient, servicii, id_diagnostic) VALUES (id_program, data_consultatie, medic_cnp, pacient_cnp, id_serv, id_diag);
    
END //

DELIMITER ;

DROP PROCEDURE IF EXISTS parafare_raport;
DELIMITER //
-- procedura actualizeaza atributul "parafat", indicand ca medicul a confirmat salvarea raportului medical
CREATE PROCEDURE parafare_raport (nr_raport int) 
BEGIN
	UPDATE raport_medical
    SET parafat = true
    WHERE id = nr_raport;
END //

DELIMITER ;

DROP PROCEDURE IF EXISTS afisare_rapoarte_neparafate;
DELIMITER //
-- procedura afiseaza rapoartele care inca nu au fost parafate de medicul care a oferit consultatia
CREATE PROCEDURE afisare_rapoarte_neparafate(cnp_medic int)
BEGIN
	DECLARE numele varchar(20);
    DECLARE prenumele varchar(20);
    
    SET numele = (SELECT nume FROM medic WHERE cnp = cnp_medic);
    SET prenumele = (SELECT prenume FROM medic WHERE cnp = cnp_medic);
    
	SELECT * FROM raport_medical WHERE nume_medic = numele AND prenume_medic = prenumele AND parafat = false;
END //

DELIMITER ;

DROP PROCEDURE IF EXISTS vizualizare_programari_pe_cabinet;
DELIMITER //
-- procedura afiseaza programarile care s-au facut pentru data ulterioara, pe cabinetul indicat
CREATE PROCEDURE vizualizare_programari_pe_cabinet(unitatea int, cabinetul int) 
BEGIN
	SELECT * FROM programare WHERE id_policlinica = unitatea AND nr_cabinet = cabinetul AND data_ora >= now();
END //

DELIMITER ;

use lant_policlinici;

DROP PROCEDURE IF EXISTS creare_programare;
DELIMITER //
-- procedura va crea o programare noua in baza de date, dupa ce verifica toate constrangerile
CREATE PROCEDURE creare_programare(data_pr date, timp_pr time, p_id_pacient int, 
	p_id_medic int, p_id_serviciu int, p_id_cabinet int, p_oras varchar(20))
BEGIN
	-- return 4500 in caz de eroare 
    DECLARE id_poli int;
    DECLARE id_poli_oras int;
    DECLARE echipament_necesar varchar(50);
    DECLARE echipament_cabinet varchar(50);
    DECLARE specialitate_necesara int;
    DECLARE spec_cabinet_1 int;
    DECLARE spec_cabinet_2 int;
	DECLARE specialitate_medic_1 int;
    DECLARE specialitate_medic_2 int;
    DECLARE timp_serviciu time;
    DECLARE finished integer DEFAULT 0;
	DECLARE curent int;
    DECLARE data_timp_curent datetime;
    DECLARE durata_curent time;
    
    -- verificam data si timpul sa nu fie in trecut
    IF (data_pr < date(now()) OR (data_pr >= date(now()) AND timp_pr < time(now()))) THEN
		SIGNAL SQLSTATE '45000';
	END IF;
    
    -- verificam daca cabinetul medical exista, si daca apartine policlinicii din orasul specificat de receptioner
    SET id_poli_oras := (SELECT id FROM policlinica WHERE oras = p_oras);
    SET id_poli := (SELECT id_poli FROM cabinet_medical WHERE id_cabinet = p_id_cabinet);
    IF (id_poli is NULL OR id_poli_oras is NULL OR id_poli <> id_poli_oras) THEN
		SIGNAL SQLSTATE '45000';
	END IF;
    
    -- verificam daca medicul are specialitatile necesare pentru serviciu, si daca cabinetul presteaza respectivul serviciu (are echipamentul necesar si specialitatea)
    SET specialitate_necesara := (SELECT id_specialitate FROM serviciu_medical WHERE id = p_id_serviciu);
    SET specialitate_medic_1 := (SELECT specialitate_1 FROM medic WHERE cnp = p_id_medic);
    SET specialitate_medic_2 := (SELECT specialitate_2 FROM medic WHERE cnp = p_id_medic);
    SET spec_cabinet_1 := (SELECT specializare1 FROM cabinet_medical WHERE id_cabinet = p_id_cabinet);
    SET spec_cabinet_2 := (SELECT specializare2 FROM cabinet_medical WHERE id_cabinet = p_id_cabinet);
	IF ((specialitate_medic_1 <> specialitate_necesara AND specialitate_medic_2 <> specialitate_necesara) OR (spec_cabinet_1 <> specialitate_necesara AND spec_cabinet_2 <> specialitate_necesara)) THEN
		-- RETURN 45000;
        SIGNAL SQLSTATE '45000';
	ELSE 
		SET echipament_necesar := (SELECT echipament_necesar FROM serviciu_medical WHERE id = p_id_serviciu);
        SET echipament_cabinet := (SELECT echipament_disponibil FROM cabinet_medical WHERE id_cabinet = p_id_cabinet); 
        
        IF (echipament_necesar IS NOT NULL AND echipament_cabinet <> echipament_necesar) THEN
			SIGNAL SQLSTATE '45000';
		END IF;
        
        SET timp_serviciu := (SELECT durata FROM serviciu_specializat WHERE id_serviciu = p_id_serviciu);
        IF (timp_serviciu IS NULL) THEN 
			SET timp_serviciu := (SELECT durata FROM serviciu_medical WHERE id = p_id_serviciu);
		END IF;
        
        BEGIN
        -- se ajunge aici daca cabinetul e valid, iar medicul are specialitatea necesara
        -- se verifica in continuare daca timpul programarii nu se suprapune cu alta programare existenta
        DECLARE c1 CURSOR FOR SELECT id FROM programare WHERE id_medic = p_id_medic OR id_pacient = p_id_pacient OR nr_cabinet = p_id_cabinet;
        DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;
        OPEN c1;
        FETCH NEXT FROM c1 INTO curent;
        WHILE (finished = 0) DO
			BEGIN
            SET data_timp_curent := (SELECT data_ora FROM programare WHERE id = curent);
            SET durata_curent := (SELECT durata FROM programare WHERE id = curent);
            IF (cast(concat(data_pr, ' ', timp_pr) as datetime) BETWEEN data_timp_curent AND DATE_ADD(data_timp_curent, INTERVAL TIME_TO_SEC(durata_curent) SECOND)) THEN
				SIGNAL SQLSTATE '45000';
			ELSE 
				IF (DATE_ADD(cast(concat(data_pr, ' ', timp_pr) as datetime), INTERVAL TIME_TO_SEC(timp_serviciu) SECOND) BETWEEN data_timp_curent AND DATE_ADD(data_timp_curent, INTERVAL TIME_TO_SEC(durata_curent) SECOND)) THEN
					SIGNAL SQLSTATE '45000';
				END IF;
            END IF;
            FETCH NEXT FROM c1 INTO curent;
            END;
		END WHILE;
        CLOSE c1;
        
        -- se ajunge aici daca se poate adauga programarea
        INSERT INTO programare(data_ora, id_medic, id_pacient, id_serviciu, id_policlinica, nr_cabinet, durata, prezent) VALUES (cast(concat(data_pr, ' ', timp_pr) as datetime), p_id_medic, p_id_pacient, p_id_serviciu, id_poli, p_id_cabinet, timp_serviciu, false);
        
        END;
	END IF;
END //

DELIMITER ;