use lant_policlinici;

#doar pentru administrator, care poate adauga/sterge/edita utilizatori
DROP PROCEDURE IF EXISTS user_nou;
DELIMITER //
-- procedura va adauga un utilizator nou in baza de date, si ii va oferi si privilegiile necesare pentru accesul la informatii si proceduri
CREATE PROCEDURE user_nou(cnp int, functie varchar(50), username_nou varchar(80), parola_user varchar(80)) 
BEGIN
	INSERT INTO users VALUES (cnp, username_nou, functie);
	SET @stmt = 'CREATE USER IF NOT EXISTS ? IDENTIFIED BY ?;';
    SET @grant_all = 'GRANT SELECT ON users TO ?;';
    
    -- grantari specifice medic
    SET @grant_medic_1 = 'GRANT SELECT, UPDATE, DELETE ON raport_medical TO ?;';
    SET @grant_medic_2 = 'GRANT SELECT ON programare TO ?;';
    SET @grant_medic_3 = 'GRANT SELECT ON pacient TO ?;';
    SET @grant_medic_4 = 'GRANT SELECT ON angajat TO ?;';
    SET @grant_medic_5 = 'GRANT SELECT ON medic TO ?;';
    SET @grant_medic_6 = 'GRANT SELECT ON diagnostic TO ?;';
    SET @grant_medic_7 = 'GRANT EXECUTE ON PROCEDURE vizualizare_istoric TO ?;';
    SET @grant_medic_8 = 'GRANT EXECUTE ON PROCEDURE afisare_rapoarte_neparafate TO ?;';
    SET @grant_medic_9 = 'GRANT EXECUTE ON PROCEDURE parafare_raport TO ?;';
    SET @grant_medic_10 = 'GRANT EXECUTE ON PROCEDURE vizualizare_pacienti_programati TO ?;';
    SET @grant_medic_11 = 'GRANT EXECUTE ON PROCEDURE afisare_orar_angajat TO ?;';
    
    -- grantari specifice asistent
    SET @grant_asistent_1 = 'GRANT SELECT, INSERT, UPDATE, DELETE ON raport_medical TO ?;';
    SET @grant_asistent_2 = 'GRANT SELECT ON pacient TO ?;';
    SET @grant_asistent_3 = 'GRANT SELECT, UPDATE ON programare TO ?;';
    SET @grant_asistent_4 = 'GRANT SELECT ON angajat TO ?;';
    SET @grant_asistent_5 = 'GRANT SELECT ON asistent_medical TO ?;';
	SET @grant_asistent_6 = 'GRANT SELECT ON medic TO ?;';
    SET @grant_asistent_7 = 'GRANT SELECT ON orar_medical TO ?;';
    SET @grant_asistent_8 = 'GRANT SELECT ON orar_angajat TO ?;';
    SET @grant_asistent_9 = 'GRANT SELECT ON policlinica TO ?;';
    SET @grant_asistent_10 = 'GRANT EXECUTE ON PROCEDURE completare_raport TO ?;';
    SET @grant_asistent_11 = 'GRANT EXECUTE ON PROCEDURE afisare_orar_angajat TO ?;';

    -- grantari specifice receptioner
    SET @grant_receptioner_1 = 'GRANT SELECT ON pacient TO ?;';
    SET @grant_receptioner_2 = 'GRANT SELECT ON programare TO ?;';
    SET @grant_receptioner_3 = 'GRANT SELECT ON angajat TO ?;';
    SET @grant_receptioner_4 = 'GRANT SELECT ON medic TO ?;';
    SET @grant_receptioner_5 = 'GRANT ALL ON bon_fiscal TO ?;';
	SET @grant_receptioner_6 = 'GRANT SELECT ON serviciu_medical TO ?;';
    SET @grant_receptioner_7 = 'GRANT SELECT ON cabinet_medical TO ?;';
    SET @grant_receptioner_8 = 'GRANT SELECT ON policlinica TO ?;';
    SET @grant_receptioner_9 = 'GRANT SELECT ON orar_medical TO ?;';
    SET @grant_receptioner_10 = 'GRANT SELECT ON orar_angajat TO ?;';
    SET @grant_receptioner_11 = 'GRANT EXECUTE ON PROCEDURE afisare_orar_angajat TO ?;';
    SET @grant_receptioner_12 = 'GRANT EXECUTE ON PROCEDURE creare_programare TO ?;';
    SET @grant_receptioner_13 = 'GRANT EXECUTE ON PROCEDURE emitere_bon TO ?;';
    SET @grant_receptioner_14 = 'GRANT EXECUTE ON PROCEDURE vizualizare_pacienti_programati TO ?;';

    -- grantari specifice contabil
    SET @grant_contabil_1 = 'GRANT SELECT ON angajat TO ?;';
    SET @grant_contabil_2 = 'GRANT SELECT ON medic TO ?;';
    SET @grant_contabil_3 = 'GRANT SELECT ON asistent_medical TO ?;';
    SET @grant_contabil_4 = 'GRANT SELECT ON policlinica TO ?;';
    SET @grant_contabil_5 = 'GRANT EXECUTE ON PROCEDURE profit_curent_lunar TO ?;';
    SET @grant_contabil_6 = 'GRANT EXECUTE ON PROCEDURE profit_lunar TO ?;';
    SET @grant_contabil_7 = 'GRANT EXECUTE ON PROCEDURE venituri_lunare TO ?;';
    SET @grant_contabil_8 = 'GRANT EXECUTE ON PROCEDURE cheltuieli_lunare TO ?;';
    SET @grant_contabil_9 = 'GRANT EXECUTE ON PROCEDURE profit_lunar_medic_specialitate TO ?;';
    SET @grant_contabil_10 = 'GRANT EXECUTE ON PROCEDURE profit_lunar_medic_locatie TO ?;';
    
    -- grantari specifice inspector
    SET @grant_inspector_1 = 'GRANT ALL ON angajat TO ?;';
    SET @grant_inspector_2 = 'GRANT SELECT ON orar_angajat TO ?;';
    SET @grant_inspector_3 = 'GRANT SELECT ON orar_medical TO ?;';
    SET @grant_inspector_4 = 'GRANT SELECT ON medic TO ?;';
    SET @grant_inspector_5 = 'GRANT SELECT ON asistent_medical TO ?;';
	SET @grant_inspector_6 = 'GRANT SELECT ON serviciu_medical TO  ?;';
    SET @grant_inspector_7 = 'GRANT ALL ON serviciu_specializat TO ?;';
	SET @grant_inspector_8 = 'GRANT EXECUTE ON PROCEDURE gasire_angajat TO ?;';
    SET @grant_inspector_9 = 'GRANT EXECUTE ON PROCEDURE afisare_orar_angajat TO ?;';
    SET @grant_inspector_10 = 'GRANT EXECUTE ON PROCEDURE adaugare_serviciu_specializat TO ?;';
    SET @grant_inspector_11 = 'GRANT SELECT ON departament TO ?;';

    -- grantari specifice admin
    SET @grant_admin = 'GRANT ALL PRIVILEGES ON *.* TO ?;';
    
    -- creare user nou cu credentialele specificate de administrator sau super-administrator
    PREPARE stmt FROM @stmt;
	SET @nume = username_nou;
	SET @parola = parola_user;
	EXECUTE stmt USING @nume, @parola;
    
	SET @nume = username_nou;
    
	IF (functie = 'asistent medical') THEN
        PREPARE stmta1 FROM @grant_asistent_1;
		EXECUTE stmta1 USING @nume;
        
        PREPARE stmta2 FROM @grant_asistent_2;
		EXECUTE stmta2 USING @nume;
        
        PREPARE stmta3 FROM @grant_asistent_3;
		EXECUTE stmta3 USING @nume;
        
        PREPARE stmta4 FROM @grant_asistent_4;
		EXECUTE stmta4 USING @nume;
        
        PREPARE stmta5 FROM @grant_asistent_5;
		EXECUTE stmta5 USING @nume;
        
        PREPARE stmta6 FROM @grant_asistent_6;
        EXECUTE stmta6 USING @nume;
        
		PREPARE stmta7 FROM @grant_asistent_7;
		EXECUTE stmta7 USING @nume;
        
        PREPARE stmta8 FROM @grant_asistent_8;
        EXECUTE stmta8 USING @nume;
        
		PREPARE stmta9 FROM @grant_asistent_9;
		EXECUTE stmta9 USING @nume;
        
        PREPARE stmta10 FROM @grant_asistent_10;
        EXECUTE stmta10 USING @nume;
        
		PREPARE stmta11 FROM @grant_asistent_11;
		EXECUTE stmta11 USING @nume;
	END IF;
    
    IF(functie = 'medic') THEN
		PREPARE stmtm1 FROM @grant_medic_1;
		EXECUTE stmtm1 USING @nume;
        
        PREPARE stmtm2 FROM @grant_medic_2;
		EXECUTE stmtm2 USING @nume;
        
        PREPARE stmtm3 FROM @grant_medic_3;
		EXECUTE stmtm3 USING @nume;
        
        PREPARE stmtm4 FROM @grant_medic_4;
		EXECUTE stmtm4 USING @nume;
        
        PREPARE stmtm5 FROM @grant_medic_5;
		EXECUTE stmtm5 USING @nume;
        
        PREPARE stmtm6 FROM @grant_medic_6;
        EXECUTE stmt6 USING @nume;
        
        PREPARE stmtm7 FROM @grant_medic_7;
        EXECUTE stmt7 USING @nume;
        
        PREPARE stmtm8 FROM @grant_medic_8;
        EXECUTE stmt8 USING @nume;
        
        PREPARE stmtm9 FROM @grant_medic_9;
        EXECUTE stmt9 USING @nume;
        
        PREPARE stmtm10 FROM @grant_medic_10;
        EXECUTE stmt10 USING @nume;
        
		PREPARE stmtm11 FROM @grant_medic_11;
        EXECUTE stmt11 USING @nume;
	END IF;
    
	IF(functie = 'receptioner') THEN
		PREPARE stmtr1 FROM @grant_receptioner_1;
		EXECUTE stmtr1 USING @nume;
        
        PREPARE stmtr2 FROM @grant_receptioner_2;
		EXECUTE stmtr2 USING @nume;
        
        PREPARE stmtr3 FROM @grant_receptioner_3;
		EXECUTE stmtr3 USING @nume;
        
        PREPARE stmtr4 FROM @grant_receptioner_4;
		EXECUTE stmtr4 USING @nume;
        
        PREPARE stmtr5 FROM @grant_receptioner_5;
		EXECUTE stmtr5 USING @nume;
        
        PREPARE stmtr6 FROM @grant_receptioner_6;
		EXECUTE stmtr6 USING @nume;
        
        PREPARE stmtr7 FROM @grant_receptioner_7;
		EXECUTE stmtr7 USING @nume;
        
        PREPARE stmtr8 FROM @grant_receptioner_8;
		EXECUTE stmtr8 USING @nume;
        
        PREPARE stmtr9 FROM @grant_receptioner_9;
		EXECUTE stmtr9 USING @nume;
        
        PREPARE stmtr10 FROM @grant_receptioner_10;
		EXECUTE stmtr10 USING @nume;
        
        PREPARE stmtr11 FROM @grant_receptioner_11;
		EXECUTE stmtr11 USING @nume;
        
        PREPARE stmtr12 FROM @grant_receptioner_12;
		EXECUTE stmtr12 USING @nume;
        
        PREPARE stmtr13 FROM @grant_receptioner_13;
		EXECUTE stmtr13 USING @nume;
        
        PREPARE stmtr14 FROM @grant_receptioner_14;
		EXECUTE stmtr14 USING @nume;
    END IF;
    
    IF(functie = 'inspector resurse umane') THEN
		PREPARE stmti1 FROM @grant_inspector_1;
		EXECUTE stmti1 USING @nume;
        
        PREPARE stmti2 FROM @grant_inspector_2;
		EXECUTE stmti2 USING @nume;
        
        PREPARE stmti3 FROM @grant_inspector_3;
		EXECUTE stmti3 USING @nume;
        
        PREPARE stmti4 FROM @grant_inspector_4;
		EXECUTE stmti4 USING @nume;
        
        PREPARE stmti5 FROM @grant_inspector_5;
		EXECUTE stmti5 USING @nume;
        
        PREPARE stmti6 FROM @grant_inspector_6;
		EXECUTE stmti6 USING @nume;
        
        PREPARE stmti7 FROM @grant_inspector_7;
		EXECUTE stmti7 USING @nume;
        
        PREPARE stmti8 FROM @grant_inspector_8;
		EXECUTE stmti8 USING @nume;
        
        PREPARE stmti9 FROM @grant_inspector_9;
		EXECUTE stmti9 USING @nume;
        
        PREPARE stmti10 FROM @grant_inspector_10;
		EXECUTE stmti10 USING @nume;
                
        PREPARE stmti11 FROM @grant_inspector_11;
		EXECUTE stmti11 USING @nume;
    END IF;
    
    IF(functie = 'expert financiar-contabil') THEN
		PREPARE stmtc1 FROM @grant_contabil_1;
		EXECUTE stmtc1 USING @nume;
        
        PREPARE stmtc2 FROM @grant_contabil_2;
		EXECUTE stmtc2 USING @nume;
        
        PREPARE stmtc3 FROM @grant_contabil_3;
		EXECUTE stmtc3 USING @nume;
        
        PREPARE stmtc4 FROM @grant_contabil_4;
		EXECUTE stmtc4 USING @nume;
        
        PREPARE stmtc5 FROM @grant_contabil_5;
		EXECUTE stmtc5 USING @nume;
        
        PREPARE stmtc6 FROM @grant_contabil_6;
		EXECUTE stmtc6 USING @nume;
        
        PREPARE stmtc7 FROM @grant_contabil_7;
		EXECUTE stmtc7 USING @nume;
        
        PREPARE stmtc8 FROM @grant_contabil_8;
		EXECUTE stmtc8 USING @nume;
        
        PREPARE stmtc9 FROM @grant_contabil_9;
		EXECUTE stmtc9 USING @nume;
        
        PREPARE stmtc10 FROM @grant_contabil_10;
		EXECUTE stmtc10 USING @nume;
    END IF;
    
    -- se ajunge aici, doar daca user-ul conectat la baza de date e super-admin
    IF (functie = 'admin') THEN
		PREPARE stmt_admin FROM @grant_admin;
        EXECUTE stmt_admin USING @nume;
	END IF;
END //

DELIMITER ;
