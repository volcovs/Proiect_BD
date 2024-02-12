use lant_policlinici; 

 INSERT INTO departament(nume) VALUES ('resurse umane'), ('economic'), ('medical');
 
 INSERT INTO specialitate(denumire) VALUES ('ecografie'), ('endoscopie digestiva'), ('ecocardiografie'), ('cardiologie interventionala'), ('bronhoscopie'), 
 ('EEG'), ('EMG'), ('dializa'), ('chirurgie laparoscopica'), ('chirurgie toracica'), ('chirurgie spinala'), ('litotritie extracorporeala'), ('explorare computer tomografica');
 
 INSERT INTO serviciu_medical(id_specialitate, grad_medic, pret, durata, nume, echipament_necesar) VALUES 
 (6, 'specialist', 500.0, '01:0:0', 'Electroencefalograma', 'EEG'), (3, 'primar', 300.0, '00:15:00', 'Ecocardiografie', 'Ecograf'),
 (8, 'specialist', 200.0, '00:30:00', 'Dializa', NULL), (11, 'specialist', 1000.0, '03:00:00', 'Chirurgie spinala', 'Robot chirurgical');
 
 INSERT INTO diagnostic(descriere) VALUES ('Probleme de vedere'), ('Inima sanatoasa'), ('Suflu sistolic'), ('Hernie de disc'), ('Tahicardie'), ('Aritmie'), 
 ('Pietre la rinichi'), ('Scolioza'), ('Gripa'), ('Raceala');
 
INSERT INTO policlinica(denumire, oras, strada, nr, servicii, program, telefon, fond_lunar) VALUES('MagicHealth', 'Cluj-Napoca', 'Observator', 30, 'Electroencefalograma, Ecografie, Chirurgie toracica', '08:00 - 16:00', '02203000', 30000.0),
	('MagicHealth', 'Bistrita', 'Trandafirului', 2, 'Dializa, EMG, Chirurgie toracica', '09:00 - 17:00', '02234000', 9000.0), ('MagicHealth', 'Bucuresti', 'Ion Creanga', 10, 'Bronhoscopie, Ecocardiografie', '08:00 - 15:00', '02256030',9000.0);
    
INSERT INTO medic(cnp, nume, prenume, oras, strada, nr, telefon, email, cont_IBAN, nr_contract, data_angajare, salariu_negociat, nr_ore_luna, data_inceput_concediu, data_final_concediu,
		specialitate_1, specialitate_2, grad, cod_parafa, titlu_stiintific, post_didactic, procent_din_servicii) VALUES
        (60251, 'Pop', 'Ana', 'Cluj-Napoca', 'Eminescu', 325, NULL, 'anapop@gmail.com', 'RO125', 1, '2010-03-25', 3000.0, 10, NULL, NULL, 12, NULL, 'primar', 222, 'doctorand', NULL, 80),
        (52444, 'Neaga', 'Vasile', 'Bistrita', 'Principala', 20, 0744, NULL, 'RO034', 2, '2015-10-10', 9000.0, 100, '2022-08-10', '2022-09-01', 2, 3, 'specialist', 1000, NULL, 'conferentiar', 75),
        (20113, 'Ivan', 'Maria', 'Bucuresti', 'Vladimirescu', 15, NULL, NULL, 'RO112', 3, '2020-02-02', 8000.0, 50, '2022-09-10', '2022-09-25', 10, 11, 'specialist', 277, 'doctor in stiinte medicale', 'profesor', 90);
        
INSERT INTO asistent_medical(cnp, nume, prenume, oras, strada, nr, telefon, email, cont_IBAN, nr_contract, data_angajare, salariu_negociat, nr_ore_luna, data_inceput_concediu, data_final_concediu,
		tip, grad) VALUES 
        (10333, 'Popescu', 'Ion', 'Cluj-Napoca', 'Republicii', 14, 0725, NULL, 'RO111', 4, '2019-05-03', 5000.0, 100, NULL, NULL, 'laborator', 'principal'),
        (24551, 'Dragan', 'Valentina', 'Iasi', 'Enescu', 5, 0744, 'd.vali@yahoo.com', 'RO152', 5, '2020-10-11', 6000.0, 80, '2022-07-13', '2022-07-31', 'generalist', 'secundar');
        
INSERT INTO angajat(cnp, nume, prenume, oras, strada, nr, telefon, email, cont_IBAN, nr_contract, data_angajare, functie, salariu_negociat, nr_ore_luna, id_departament, data_inceput_concediu, data_final_concediu) VALUES
	(60499, 'Moga', 'Andreea', 'Vaslui', 'Salcamului', 20, NULL, NULL, 'RO012', 6, '2021-02-03', 'receptioner', 3500.0, 100, 1, NULL, NULL),
    (54222, 'Cioban', 'Andrei', 'Constanta', 'Albastra', 107, 0719, NULL, 'RO987', 7, '2019-10-10', 'inspector resurse umane', 6000.0, 140, 1, '2022-06-10', '2022-06-25'),
    (21021, 'Popa', 'Sara', 'Bacau', 'Independentei', 100, 0711, NULL, 'RO101', 8, '2018-09-10', 'expert financiar-contabil', 7000.0, 160, 2, NULL, NULL),
    (83563, 'Dragos', 'Alexandra', 'Iasi', 'Verde', 15, NULL, 'admin@mail.ro', 'RO404', 9, '2021-03-14', 'admin', 7500.0, 130, 1, NULL, NULL);
    
INSERT INTO pacient(cnp, nume, prenume, data_nasterii, oras, strada, nr, telefon, email) VALUES 
	(54223, 'Pop', 'Marius', '1980-10-05', 'Zalau', 'Crinului', 4, NULL, NULL),
    (13452, 'Cozma', 'Marian', '1968-09-10', 'Galati', 'Calea Manastur', 10, 0767, 'cozma@gmail.com'), 
    (26767, 'Popa', 'Dorina', '1999-10-10', 'Ramnicu-Valcea', 'George Enescu', 25, NULL, NULL),
    (62128, 'Rus', 'Alexia', '2000-02-13', 'Cluj-Napoca', 'Romulus Vuia', 7, 0756, NULL);
    
INSERT INTO users(cnp_user, username, rol) VALUES (60251, 'p_ana', 'medic'), (52444, 'n_vasile', 'medic'), (20113, 'i_maria', 'medic'), (10333, 'p_ion', 'asistent_medical'),
	(24551, 'd_valentina', 'asistent_medical'), (60499, 'm_andreea', 'receptioner'), (54222, 'c_andrei', 'inspector_resurse_umane'), (21021, 'p_sara', 'contabil'), 
    (83563, 'd_alexandra', 'admin'); 
    
INSERT INTO cabinet_medical(specializare1, specializare2, id_unitate_medicala, echipament_disponibil) VALUES 
(12, NULL, 1, NULL), (2, 3, 1, 'Ecograf'),
(8, 3, 1, 'Ecograf'), (11, NULL, 2, 'EEG'), 
(NULL, 8, 2, NULL), (3, NULL, 3, 'Ecograf'),
(2, NULL, 3, NULL); 

INSERT INTO orar_angajat(zi, timp_inceput, timp_final, id_angajat) VALUES 
('luni', '08:00:00', '16:00:00', 60499), ('marti', '08:00:00', '16:00:00', 60499), ('miercuri', '08:00:00', '16:00:00', 60499),
('joi', '08:00:00', '16:00:00', 60499), ('vineri', '08:00:00', '14:00:00', 60499), ('luni', '08:00:00', '16:00:00', 54222), 
('marti', '08:00:00', '16:00:00', 54222), ('miercuri', '08:00:00', '16:00:00', 54222), ('joi', '08:00:00', '16:00:00', 54222), 
('vineri', '08:00:00', '14:00:00', 54222), ('luni', '08:00:00', '16:00:00', 21021), ('marti', '08:00:00', '16:00:00', 21021), 
('miercuri', '08:00:00', '16:00:00', 21021), ('joi', '08:00:00', '16:00:00', 21021), ('vineri', '09:00:00', '15:00:00', 21021), 
('luni', '09:00:00', '14:00:00', 83563), ('marti', '09:00:00', '14:00:00', 83563), ('miercuri', '09:00:00', '14:00:00', 83563),
('joi', '09:00:00', '14:00:00', 83563), ('vineri', '09:00:00', '15:00:00', 83563);

INSERT INTO orar_medical(zi, timp_inceput, timp_final, id_policlinica, id_angajat) VALUES 
('2023-01-13', '09:00:00', '16:00:00', 1, 10333), ('2023-01-16', '09:00:00', '16:00:00', 1, 10333),
('2023-01-20', '10:00:00', '16:00:00', 1, 10333), ('2022-12-20', '10:00:00', '16:00:00', 2, 24551),
('2023-01-21', '08:00:00', '12:00:00', 2, 24551), ('2023-01-25', '10:00:00', '16:00:00', 2, 24551), 
('2023-01-13', '10:00:00', '17:00:00', 3, 24551);

INSERT INTO programare(data_ora, id_medic, id_pacient, id_serviciu, id_policlinica, nr_cabinet, durata, prezent, bon) VALUES
('2022-12-05 12:00:00', 60251, 13452, 1, 1, 1, '01:00:00', true, true), ('2022-12-19 11:00:00', 60251, 13452, 1, 1, 1, '01:30:00', true, false),
('2023-01-13 13:00:00', 60251, 62128, 2, 1, 3, '00:45:00', false, false), ('2022-01-13 11:00:00', 52444, 26767, 1, 1, 1, '01:30:00', false, false),
('2023-01-17 09:00:00', 60251, 26767, 2, 1, 3, '00:30:00', false, false), ('2023-01-23 12:30:00', 60251, 54223, 3, 1, 3, '02:00:00', false, false);

INSERT INTO consultatie(id_programare, data_ora, id_medic, id_pacient, servicii, id_diagnostic) VALUES (1, '2022-12-05 12:00:00', 60251, 13452, 'Electroencefalograma', 1),
(2, '2022-12-19 11:00:00', 60251, 13452, 'Electroencefalograma', 1);

INSERT INTO raport_medical(nume_pacient, prenume_pacient, nume_medic, prenume_medic, nume_asistent, prenume_asistent, data_consultatie, istoric_boala, simptome,
    id_serviciu, rezultat, id_diagnostic, recomandari, parafat) VALUES 
    ('Cozma', 'Marian', 'Pop', 'Ana', NULL, NULL, '2022-12-05 12:00:00', 'Pacientul prezinta migrene puternice de mai multe ori intr-o zi', 'migrene, ameteala', 1, NULL, 1, 
    'Se recomanda purtarea ochelarilor la calculator', true),  
    ('Cozma', 'Marian', 'Pop', 'Ana', NULL, NULL, '2022-12-19 11:00:00', 'Pacientul prezinta hipertensiune si atacuri de panica', 'tremuraturi, frisoane', 1, NULL, 1, 
    'Se recomanda diminuarea surselor de stres si odihna adecvata', false);
    