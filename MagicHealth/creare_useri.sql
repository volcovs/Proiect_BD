use lant_policlinici;

#medic
CREATE USER IF NOT EXISTS p_ana
IDENTIFIED BY 'ana123';
GRANT SELECT ON users TO p_ana;
GRANT SELECT, UPDATE, DELETE ON raport_medical TO p_ana;
GRANT SELECT ON diagnostic TO p_ana;
GRANT SELECT ON programare TO p_ana;
GRANT SELECT ON pacient TO p_ana;
GRANT SELECT ON angajat TO p_ana;
GRANT SELECT ON medic TO p_ana;
GRANT SELECT ON policlinica TO p_ana;
GRANT SELECT ON orar_angajat TO p_ana;
GRANT SELECT ON orar_medical TO p_ana;
GRANT EXECUTE ON PROCEDURE vizualizare_istoric TO p_ana;
GRANT EXECUTE ON PROCEDURE afisare_rapoarte_neparafate TO p_ana;
GRANT EXECUTE ON PROCEDURE parafare_raport TO p_ana;
GRANT EXECUTE ON PROCEDURE vizualizare_pacienti_programati TO p_ana;
GRANT EXECUTE ON PROCEDURE afisare_orar_angajat TO p_ana;

#medic
CREATE USER IF NOT EXISTS n_vasile
IDENTIFIED BY 'vasi000';
GRANT SELECT ON users TO n_vasile;
GRANT SELECT ON diagnostic TO n_vasile;
GRANT SELECT, UPDATE, DELETE ON raport_medical TO n_vasile;
GRANT SELECT ON programare TO n_vasile;
GRANT SELECT ON pacient TO n_vasile;
GRANT SELECT ON angajat TO n_vasile;
GRANT SELECT ON medic TO n_vasile;
GRANT SELECT ON policlinica TO n_vasile;
GRANT SELECT ON orar_angajat TO n_vasile;
GRANT SELECT ON orar_medical TO n_vasile;
GRANT EXECUTE ON PROCEDURE vizualizare_istoric TO n_vasile;
GRANT EXECUTE ON PROCEDURE afisare_rapoarte_neparafate TO n_vasile;
GRANT EXECUTE ON PROCEDURE parafare_raport TO n_vasile;
GRANT EXECUTE ON PROCEDURE vizualizare_pacienti_programati TO n_vasile;
GRANT EXECUTE ON PROCEDURE afisare_orar_angajat TO n_vasile;

#medic
CREATE USER IF NOT EXISTS i_maria
IDENTIFIED BY 'ivan5';
GRANT SELECT ON users TO i_maria;
GRANT SELECT, UPDATE, DELETE ON raport_medical TO i_maria;
GRANT SELECT ON diagnostic TO i_maria;
GRANT SELECT ON programare TO i_maria;
GRANT SELECT ON pacient TO i_maria;
GRANT SELECT ON angajat TO i_maria;
GRANT SELECT ON medic TO i_maria;
GRANT SELECT ON policlinica TO i_maria;
GRANT SELECT ON orar_angajat TO i_maria;
GRANT SELECT ON orar_medical TO i_maria;
GRANT EXECUTE ON PROCEDURE vizualizare_istoric TO i_maria;
GRANT EXECUTE ON PROCEDURE afisare_rapoarte_neparafate TO i_maria;
GRANT EXECUTE ON PROCEDURE parafare_raport TO i_maria;
GRANT EXECUTE ON PROCEDURE vizualizare_pacienti_programati TO i_maria;
GRANT EXECUTE ON PROCEDURE afisare_orar_angajat TO i_maria;

#asistent medical
CREATE USER IF NOT EXISTS p_ion
IDENTIFIED BY 'ion2000';
GRANT SELECT ON users TO p_ion;
GRANT SELECT, INSERT, UPDATE, DELETE ON raport_medical TO p_ion;
GRANT SELECT ON pacient TO p_ion;
GRANT SELECT, UPDATE ON programare TO p_ion;
GRANT SELECT ON angajat TO p_ion;
GRANT SELECT ON asistent_medical TO p_ion;
GRANT SELECT ON medic TO p_ion;
GRANT SELECT ON orar_medical TO p_ion;
GRANT SELECT ON orar_angajat TO p_ion;
GRANT SELECT ON policlinica TO p_ion;
GRANT EXECUTE ON PROCEDURE completare_raport TO p_ion;
GRANT EXECUTE ON PROCEDURE afisare_orar_angajat TO p_ion;

#asistent medical
CREATE USER IF NOT EXISTS d_valentina
IDENTIFIED BY 'dragan100';
GRANT SELECT ON users TO d_valentina;
GRANT SELECT, INSERT, UPDATE, DELETE ON raport_medical TO d_valentina;
GRANT SELECT ON pacient TO d_valentina;
GRANT SELECT, UPDATE ON programare TO d_valentina;
GRANT SELECT ON angajat TO d_valentina;
GRANT SELECT ON asistent_medical TO d_valentina;
GRANT SELECT ON medic TO d_valentina;
GRANT EXECUTE ON PROCEDURE completare_raport TO d_valentina;
GRANT EXECUTE ON PROCEDURE afisare_orar_angajat TO d_valentina;

#receptioner
CREATE USER IF NOT EXISTS m_andreea
IDENTIFIED BY 'moga33';
GRANT SELECT ON users TO m_andreea;
GRANT SELECT, INSERT ON pacient TO m_andreea;
GRANT SELECT ON programare TO m_andreea;
GRANT SELECT ON medic TO m_andreea;
GRANT SELECT ON angajat TO m_andreea;
GRANT SELECT ON serviciu_medical TO m_andreea;
GRANT ALL ON bon_fiscal TO m_andreea;
GRANT SELECT ON cabinet_medical TO m_andreea;
GRANT SELECT ON policlinica TO m_andreea;
GRANT SELECT ON orar_medical TO m_andreea;
GRANT SELECT ON orar_angajat TO m_andreea;
GRANT EXECUTE ON PROCEDURE afisare_orar_angajat TO m_andreea;
GRANT EXECUTE ON PROCEDURE creare_programare TO m_andreea;
GRANT EXECUTE ON PROCEDURE emitere_bon TO m_andreea;
GRANT EXECUTE ON PROCEDURE vizualizare_pacienti_programati TO m_andreea;

#inspector resurse umane
CREATE USER IF NOT EXISTS c_andrei
IDENTIFIED BY 'andrei';
GRANT SELECT ON users TO c_andrei;
GRANT ALL ON angajat TO c_andrei;
GRANT SELECT ON orar_angajat TO c_andrei;
GRANT SELECT ON orar_medical TO c_andrei;
GRANT SELECT ON medic TO c_andrei;
GRANT SELECT ON asistent_medical TO c_andrei;
GRANT SELECT ON serviciu_medical TO c_andrei;
GRANT SELECT ON departament TO c_andrei;
GRANT ALL ON serviciu_specializat TO c_andrei;
GRANT EXECUTE ON PROCEDURE gasire_angajat TO c_andrei;
GRANT EXECUTE ON PROCEDURE afisare_orar_angajat TO c_andrei;
GRANT EXECUTE ON PROCEDURE adaugare_serviciu_specializat TO c_andrei;

#contabil
CREATE USER IF NOT EXISTS p_sara
IDENTIFIED BY 'popas';
GRANT SELECT ON users TO p_sara;
GRANT SELECT ON angajat TO p_sara;
GRANT SELECT ON medic TO p_sara;
GRANT SELECT ON asistent_medical TO p_sara;
GRANT SELECT ON policlinica TO p_sara;
GRANT EXECUTE ON PROCEDURE profit_curent_lunar TO p_sara;
GRANT EXECUTE ON PROCEDURE profit_lunar TO p_sara;
GRANT EXECUTE ON PROCEDURE venituri_lunare TO p_sara;
GRANT EXECUTE ON PROCEDURE cheltuieli_lunare TO p_sara;
GRANT EXECUTE ON PROCEDURE profit_lunar_medic_specialitate TO p_sara;
GRANT EXECUTE ON PROCEDURE profit_lunar_medic_locatie TO p_sara;

#admin
CREATE USER IF NOT EXISTS d_alexandra
IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON *.* TO d_alexandra WITH GRANT OPTION;
