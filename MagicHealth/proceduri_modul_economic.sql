use lant_policlinici;

DROP PROCEDURE IF EXISTS venituri_lunare;
DELIMITER //
-- procedura va determina veniturile lunare ale policlinicii cu numarul indicat, adunand platile pacientilor de pe bonurile fiscale emise pentru
-- unitatea medicala respectiva si fondul lunar disponibil
CREATE PROCEDURE venituri_lunare(an int, luna int, nr_poli int, OUT venit float) 
BEGIN
	DECLARE curent float;
	DECLARE finished integer DEFAULT 0;
    DECLARE fond float;
    
    DECLARE c1 CURSOR FOR SELECT suma FROM bon_fiscal WHERE MONTH(data_timp_emitere) = luna AND YEAR(data_timp_emitere) = an AND id_policlinica = nr_poli;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;
	SET venit = 0;
    OPEN c1;
    FETCH NEXT FROM c1 INTO curent;
    WHILE (finished = 0) DO 
    BEGIN
        SET venit = venit + curent;
		FETCH NEXT FROM c1 INTO curent;
    END;
    END WHILE;
    CLOSE c1;

	SET fond = (SELECT fond_lunar FROM policlinica WHERE id = nr_poli);
	SET venit = venit + fond;
END //

DELIMITER ;

DROP PROCEDURE IF EXISTS cheltuieli_lunare;
DELIMITER //
-- procedura determina cheltuielile lunare ale policlinicii cu numarul indicat, adunand salariile angajatilor care sunt inca activi,
-- au lucrat in luna respectiva, dar si pe unitatea medicala respectiva. Se aduna si procentul achitat medicilor pentru fiecare serviciu prestat
-- de acestia
CREATE PROCEDURE cheltuieli_lunare(an int, luna int, id_poli int, OUT cheltuieli float)
BEGIN
	DECLARE person int;
	DECLARE salar_curent float;
	DECLARE finished integer DEFAULT 0;
    DECLARE finished2 integer DEFAULT 0;
    DECLARE finished3 integer DEFAULT 0;
    DECLARE verif_orar_med int;
    DECLARE verif_orar_ang int;
    DECLARE profit_var float DEFAULT 0;
    DECLARE pret float;
    DECLARE salariu float;
    DECLARE procent int;
    DECLARE profit_serviciu float;
    DECLARE nr int;
    
	SET cheltuieli = 0;
    
    BEGIN
    DECLARE c1 CURSOR FOR SELECT DISTINCT cnp FROM angajat WHERE data_concediere IS NULL AND YEAR(data_angajare) <= an AND MONTH(data_angajare) <= luna AND functie <> 'medic';
    #inseamna ca angajatul a lucrat in luna respectiva, urmeaza verificarea locatiei pe care a lucrat
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;
    OPEN c1;
    FETCH NEXT FROM c1 INTO person;
    WHILE (finished = 0) DO 
		BEGIN
			SET verif_orar_med := (SELECT id FROM orar_medical WHERE id_angajat = person AND id_policlinica = id_poli AND MONTH(zi) = luna AND YEAR(zi) = an LIMIT 1);
            IF (verif_orar_med IS NOT NULL) THEN
				SET salar_curent := (SELECT salariu_negociat FROM angajat WHERE cnp = person);
				SET cheltuieli = cheltuieli + salar_curent;
			ELSE 
            -- sediul central (policlinica nr. 1, in cazul dat, cea din Cluj-Napoca) achita salariul angajatilor care nu au un orar specific
				IF (id_poli = 1) THEN
					SET salar_curent := (SELECT salariu_negociat FROM angajat WHERE cnp = person);
					SET cheltuieli = cheltuieli + salar_curent;
				END IF;
			END IF;
			FETCH NEXT FROM c1 INTO person;
		END;
    END WHILE;
    CLOSE c1;
    END;
    
	 BEGIN
    -- cursor pentru medicii de pe bon_fiscal, unde locatia coincide cu cea specificata
    DECLARE c2 CURSOR FOR SELECT DISTINCT id_medic FROM bon_fiscal WHERE id_policlinica = id_poli AND MONTH(data_timp_emitere) = luna AND YEAR(data_timp_emitere) = an;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished2 = 1;
   
    OPEN c2;
    FETCH NEXT FROM c2 INTO person;
    -- urmeaza calculul profitului pentru fiecare medic in parte
    WHILE (finished2 = 0) DO 
		BEGIN
			SET salariu := (SELECT salariu_negociat FROM angajat WHERE cnp = person);
            SET profit_var = salariu;
			SET cheltuieli = cheltuieli + profit_var;
            FETCH NEXT FROM c2 INTO person;
		END;
	END WHILE;
    CLOSE c2;
    END;
    
    BEGIN 
    DECLARE c3 CURSOR FOR SELECT nr_inregistrare FROM bon_fiscal WHERE id_policlinica = id_poli AND MONTH(data_timp_emitere) = luna AND YEAR(data_timp_emitere) = an;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished3 = 1;
   
    OPEN c3;
    FETCH NEXT FROM c3 INTO nr;
    -- urmeaza calculul profitului din servicii pentru fiecare medic
    WHILE (finished3 = 0) DO 
		BEGIN
			SET pret := (SELECT suma FROM bon_fiscal WHERE nr_inregistrare = nr);
            SET procent := (SELECT procent_din_servicii FROM medic, bon_fiscal WHERE medic.cnp = bon_fiscal.id_medic AND nr_inregistrare = nr);
            SET profit_serviciu = (pret * procent)/100;
            
			SET cheltuieli = cheltuieli + profit_serviciu;
            FETCH NEXT FROM c3 INTO nr;
		END;
	END WHILE;
    CLOSE c3;
    END;

END //
DELIMITER ;

DROP PROCEDURE IF EXISTS profit_lunar;
DELIMITER //
-- procedura va salva, intr-o tabela temporara, profitul obtinut de fiecare policlinica in parte, in lunile precedente celei indicate, ale 
-- anului indicat
CREATE PROCEDURE profit_lunar(an int, luna int)
BEGIN
	DECLARE venit float;
    DECLARE chelt float;
    DECLARE profit float;
	DECLARE iterator int default 1;
    DECLARE nume_poli varchar(20);
    DECLARE nr_poli int;
	DECLARE finished int DEFAULT 0;
    DECLARE c1 CURSOR FOR SELECT id FROM policlinica;
    CREATE TEMPORARY TABLE profit_lunar_policlinici (
		luna int,
        suma float,
        nume_policlinica varchar(20));
        
	BEGIN
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;
    OPEN c1;
		FETCH NEXT FROM c1 INTO nr_poli;
        WHILE (finished = 0) DO
        BEGIN
			SET nume_poli = (SELECT oras FROM policlinica WHERE id = nr_poli);
			SET iterator = 1;
            SET venit = 0;
            SET chelt = 0;
             WHILE (iterator < luna) DO
				BEGIN
					CALL venituri_lunare(an, iterator, nr_poli, venit); 
					CALL cheltuieli_lunare(an, iterator, nr_poli, chelt); 
					SET profit = venit - chelt;
					IF (profit is NULL) THEN
						SET profit = 0;
					END IF;

					INSERT INTO profit_lunar_policlinici VALUES (iterator, profit, nume_poli); 
					SET iterator = iterator + 1;
				END;
			END WHILE;
			
            FETCH NEXT FROM c1 INTO nr_poli;
        END;
        END WHILE;
		CLOSE c1;
	END;
    
    SELECT * FROM profit_lunar_policlinici;
    DROP TEMPORARY TABLE profit_lunar_policlinici;
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS profit_curent_lunar;
DELIMITER //
-- procedura va salva, intr-o tabela temporara, profitul lunar pentru fiecare policlinica, doar pentru luna din anul indicat
CREATE PROCEDURE profit_curent_lunar(an int, luna int)
BEGIN
	DECLARE venit float;
    DECLARE chelt float;
    DECLARE profit float;
    DECLARE nume_poli varchar(20);
    DECLARE nr_poli int;
	DECLARE finished int DEFAULT 0;
    DECLARE c1 CURSOR FOR SELECT id FROM policlinica;
    CREATE TEMPORARY TABLE profit_lunar_policlinici (
		luna int,
        suma float,
        nume_policlinica varchar(20));
        
	BEGIN
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;
    OPEN c1;
		FETCH NEXT FROM c1 INTO nr_poli;
        WHILE (finished = 0) DO
        BEGIN
			SET nume_poli = (SELECT oras FROM policlinica WHERE id = nr_poli);
            SET venit = 0;
            SET chelt = 0;
					CALL venituri_lunare(an, luna, nr_poli, venit); 
					CALL cheltuieli_lunare(an, luna, nr_poli, chelt); 
					SET profit = venit - chelt;

					INSERT INTO profit_lunar_policlinici VALUES (luna, profit, nume_poli); 
            FETCH NEXT FROM c1 INTO nr_poli;
        END;
        END WHILE;
		CLOSE c1;
	END;
    
    SELECT * FROM profit_lunar_policlinici;
    DROP TEMPORARY TABLE profit_lunar_policlinici;
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS profit_lunar_medic_locatie;
DELIMITER //
-- procedura calculeaza profitul obtinut in anul si luna indicata, pe unitatea medicala indicata, de catre medici
CREATE PROCEDURE profit_lunar_medic_locatie(an int, luna int, location int)
BEGIN
	DECLARE person int;
    DECLARE profit_var float DEFAULT 0;
    DECLARE pret float;
    DECLARE salariu float;
    DECLARE procent int;
    DECLARE profit_serviciu float;
    DECLARE finished integer DEFAULT 0;
    DECLARE finished2 integer DEFAULT 0;
    DECLARE nr int;
    
	CREATE TEMPORARY TABLE profit_lunar_medic (
		cnp_medic int primary key,
        suma float);
    
    BEGIN
    -- cursor pentru medicii de pe bon_fiscal, unde locatia coincide cu cea specificata
    DECLARE c1 CURSOR FOR SELECT DISTINCT id_medic FROM bon_fiscal WHERE id_policlinica = location AND MONTH(data_timp_emitere) = luna AND YEAR(data_timp_emitere) = an;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;
   
    OPEN c1;
    FETCH NEXT FROM c1 INTO person;
    -- urmeaza calculul profitului pentru fiecare medic in parte
    WHILE (finished = 0) DO 
		BEGIN
			SET salariu := (SELECT salariu_negociat FROM angajat WHERE cnp = person);
            SET profit_var = salariu;
            -- se introduce mai intai salariul fiecarui medic, apoi se aduna procentul din pretul serviciilor prestate
            INSERT INTO profit_lunar_medic VALUES (person, profit_var);
            FETCH NEXT FROM c1 INTO person;
		END;
	END WHILE;
    CLOSE c1;
    END;
    
    BEGIN 
    DECLARE c2 CURSOR FOR SELECT nr_inregistrare FROM bon_fiscal WHERE id_policlinica = location AND MONTH(data_timp_emitere) = luna AND YEAR(data_timp_emitere) = an;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished2 = 1;
   
    OPEN c2;
    FETCH NEXT FROM c2 INTO nr;
    -- urmeaza calculul profitului din servicii pentru fiecare medic
    WHILE (finished2 = 0) DO 
		BEGIN
			SET pret := (SELECT suma FROM bon_fiscal WHERE nr_inregistrare = nr);
            SET procent := (SELECT procent_din_servicii FROM medic, bon_fiscal WHERE medic.cnp = bon_fiscal.id_medic AND nr_inregistrare = nr);
            SET profit_serviciu = (pret * procent)/100;
            SET person := (SELECT id_medic FROM bon_fiscal WHERE nr_inregistrare = nr);
            
            UPDATE profit_lunar_medic
            SET suma = suma + profit_serviciu
            WHERE cnp_medic = person;
            FETCH NEXT FROM c2 INTO nr;
		END;
	END WHILE;
    CLOSE c2;
    END;

	SELECT cnp_medic, suma FROM profit_lunar_medic;
    DROP TEMPORARY TABLE profit_lunar_medic;
END //

DELIMITER ;

DROP PROCEDURE IF EXISTS profit_lunar_medic_specialitate;
DELIMITER //
-- procedura calculeaza profitul obtinut in anul si luna indicata, de catre medicii cu specialitatea indicata
CREATE PROCEDURE profit_lunar_medic_specialitate(an int, luna int, spec varchar(200))
BEGIN
	DECLARE person int;
    DECLARE profit_var float DEFAULT 0;
    DECLARE pret float;
    DECLARE salariu float;
    DECLARE procent int;
    DECLARE profit_serviciu float;
    DECLARE finished integer DEFAULT 0;
    DECLARE finished2 integer DEFAULT 0;
    DECLARE nr int;
    DECLARE specializare int;
    
	CREATE TEMPORARY TABLE profit_lunar_medic (
		cnp_medic int primary key,
        suma float);
    
    SET specializare := (SELECT id FROM specialitate WHERE denumire = spec);
    
    BEGIN
    -- cursor pentru medicii de pe bon_fiscal, care au specialitatea specificata
    DECLARE c1 CURSOR FOR SELECT DISTINCT id_medic FROM bon_fiscal, medic WHERE medic.cnp = bon_fiscal.id_medic AND MONTH(data_timp_emitere) = luna AND YEAR(data_timp_emitere) = an AND (specialitate_1 = specializare OR specialitate_2 = specializare);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;
   
    OPEN c1;
    FETCH NEXT FROM c1 INTO person;
    -- urmeaza calculul profitului pentru fiecare medic in parte
    WHILE (finished = 0) DO 
		BEGIN
			SET salariu := (SELECT salariu_negociat FROM angajat WHERE cnp = person);
            SET profit_var = salariu;
            -- se introduce mai intai salariul fiecarui medic, apoi se aduna procentul din pretul serviciilor prestate
            INSERT INTO profit_lunar_medic VALUES (person, profit_var);
            FETCH NEXT FROM c1 INTO person;
		END;
	END WHILE;
    CLOSE c1;
    END;
    
    BEGIN 
    DECLARE c2 CURSOR FOR SELECT nr_inregistrare FROM bon_fiscal, medic WHERE medic.cnp = bon_fiscal.id_medic AND MONTH(data_timp_emitere) = luna AND YEAR(data_timp_emitere) = an AND (specialitate_1 = specializare OR specialitate_2 = specializare);
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished2 = 1;
   
    OPEN c2;
    FETCH NEXT FROM c2 INTO nr;
    -- urmeaza calculul profitului din servicii pentru fiecare medic
    WHILE (finished2 = 0) DO 
		BEGIN
			SET pret := (SELECT suma FROM bon_fiscal WHERE nr_inregistrare = nr);
            SET procent := (SELECT procent_din_servicii FROM medic, bon_fiscal WHERE medic.cnp = bon_fiscal.id_medic AND nr_inregistrare = nr);
            SET profit_serviciu = (pret * procent)/100;
            SET person := (SELECT id_medic FROM bon_fiscal WHERE nr_inregistrare = nr);
            
            UPDATE profit_lunar_medic
            SET suma = suma + profit_serviciu
            WHERE cnp_medic = person;
            FETCH NEXT FROM c2 INTO nr;
		END;
	END WHILE;
    CLOSE c2;
    END;

	SELECT cnp_medic, suma FROM profit_lunar_medic;
    DROP TEMPORARY TABLE profit_lunar_medic;
END //

DELIMITER ;