DROP DATABASE IF EXISTS lant_policlinici;
CREATE DATABASE IF NOT EXISTS lant_policlinici;
use lant_policlinici;

DROP TABLE IF EXISTS policlinica;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS angajat;
DROP TABLE IF EXISTS asistent_medical;
DROP TABLE IF EXISTS medic;
DROP TABLE IF EXISTS specialitate;
DROP TABLE IF EXISTS departament;
DROP TABLE IF EXISTS serviciu_medical;
DROP TABLE IF EXISTS programare;
DROP TABLE IF EXISTS consultatie;
DROP TABLE IF EXISTS diagnostic;
DROP TABLE IF EXISTS pacient;
DROP TABLE IF EXISTS raport_medical;
DROP TABLE IF EXISTS analiza_medicala;
DROP TABLE IF EXISTS bon_fiscal;
DROP TABLE IF EXISTS orar_medical;
DROP TABLE IF EXISTS orar_angajat; -- orar generic pentru un angajat
DROP TABLE IF EXISTS cabinet_medical;
DROP TABLE IF EXISTS serviciu_specializat;

#1
CREATE TABLE IF NOT EXISTS policlinica (
	id int not null auto_increment primary key,
    denumire varchar(20),
    oras varchar(20),
    strada varchar(20),
    nr int, 
    servicii varchar(500),
    program varchar(100), 
    telefon varchar(10),
    fond_lunar float,
    nr_cabinete int DEFAULT 0
);

#2
CREATE TABLE IF NOT EXISTS departament (
	id int not null auto_increment primary key,
    nume enum('resurse umane', 'economic', 'medical')
);

#3
CREATE TABLE IF NOT EXISTS specialitate (
	id int not null auto_increment primary key,
    denumire varchar(200)
);

#4
CREATE TABLE IF NOT EXISTS cabinet_medical (
	id_cabinet int not null auto_increment,
	specializare1 int,
    specializare2 int,
    id_unitate_medicala int not null,
    echipament_disponibil varchar(50),
    PRIMARY KEY (id_cabinet, id_unitate_medicala),
    
    FOREIGN KEY (specializare1) REFERENCES specialitate(id),
    FOREIGN KEY (specializare2) REFERENCES specialitate(id),
    FOREIGN KEY (id_unitate_medicala) REFERENCES policlinica(id)
);

#5
CREATE TABLE IF NOT EXISTS diagnostic (
	id int not null auto_increment primary key,
    descriere varchar(200)
);

#6
CREATE TABLE IF NOT EXISTS pacient (
	cnp int not null unique primary key,
    nume varchar(20),
    prenume varchar(20),
    data_nasterii date,
    oras varchar(20),
    strada varchar(20),
    nr int,
    telefon bigint,
    email varchar(30),
    
    INDEX n (nume),
    INDEX p (prenume)
);

#7
CREATE TABLE IF NOT EXISTS angajat (
	cnp int not null unique primary key,
    nume varchar(20),
    prenume varchar(20),
    oras varchar(20),
    strada varchar(20),
    nr int,
    telefon bigint,
    email varchar(30),
    cont_IBAN varchar(30) unique not null,
    nr_contract int,
    data_angajare date,
    data_concediere date,
    functie enum('inspector resurse umane', 'expert financiar-contabil', 'receptioner', 'asistent medical', 'medic', 'admin', 'super-admin'),
    salariu_negociat float,
    nr_ore_luna int,
    id_departament int,
    data_inceput_concediu date,
    data_final_concediu date,
    
    FOREIGN KEY (id_departament) REFERENCES departament(id)
);

#8
CREATE TABLE IF NOT EXISTS users (
	cnp_user int not null unique primary key,
    username varchar(80),
    rol varchar(80),
    
    FOREIGN KEY (cnp_user) REFERENCES angajat(cnp)
);

#9
CREATE TABLE IF NOT EXISTS asistent_medical (
	cnp int not null unique primary key,
    nume varchar(20),
    prenume varchar(20),
    oras varchar(20),
    strada varchar(20),
    nr int,
    telefon bigint,
    email varchar(30),
    cont_IBAN varchar(30) unique not null,
    nr_contract int,
    data_angajare date,
	data_concediere date,
    salariu_negociat float,
    nr_ore_luna int,
    data_inceput_concediu date,
    data_final_concediu date,
    tip enum('generalist', 'laborator', 'radiologie'),
    grad enum('secundar', 'principal'),
    
    INDEX n_a(nume),
    INDEX p_a(prenume)
);

#10
CREATE TABLE IF NOT EXISTS medic (
	cnp int not null unique primary key,
    nume varchar(20),
    prenume varchar(20),
    oras varchar(20),
    strada varchar(20),
    nr int,
    telefon bigint,
    email varchar(30),
    cont_IBAN varchar(30) unique not null,
    nr_contract int,
    data_angajare date,
	data_concediere date,
    salariu_negociat float,
    nr_ore_luna int,
    data_inceput_concediu date,
    data_final_concediu date,
    specialitate_1 int not null,
    specialitate_2 int,
    grad enum('specialist', 'primar'),
    cod_parafa int,
    titlu_stiintific enum('doctorand', 'doctor in stiinte medicale'),
    post_didactic enum('preparator', 'asistent', 'sef de lucrari', 'conferentiar', 'profesor'),
    procent_din_servicii int,
    
    INDEX n_m(nume),
    INDEX p_m(prenume),
    
    FOREIGN KEY (specialitate_1) REFERENCES specialitate(id),
    FOREIGN KEY (specialitate_2) REFERENCES specialitate(id)
);

#11
CREATE TABLE IF NOT EXISTS serviciu_medical (
	id int not null auto_increment primary key,
    id_specialitate int not null,
    grad_medic enum('specialist', 'primar'),
    pret float,
    durata time,
    nume varchar(50),
    echipament_necesar varchar(50),
    
    FOREIGN KEY (id_specialitate) REFERENCES specialitate(id)
);

#12
CREATE TABLE IF NOT EXISTS programare (
	id int not null auto_increment primary key,
    data_ora datetime,
    id_medic int not null,
    id_pacient int not null,
    id_serviciu int not null,
    id_policlinica int not null,
    nr_cabinet int,
    durata time,
    prezent boolean,
    bon boolean DEFAULT false, 
    
    FOREIGN KEY (id_medic) REFERENCES medic(cnp),
    FOREIGN KEY (id_pacient) REFERENCES pacient(cnp),
    FOREIGN KEY (id_serviciu) REFERENCES serviciu_medical(id),
    FOREIGN KEY (id_policlinica) REFERENCES policlinica(id),
    FOREIGN KEY (nr_cabinet) REFERENCES cabinet_medical(id_cabinet)
);

#13
CREATE TABLE IF NOT EXISTS consultatie (
	id int not null auto_increment primary key,
    id_programare int not null,
    data_ora datetime,
    id_medic int not null,
    id_pacient int not null,
    servicii varchar(100),
    id_diagnostic int not null,

	INDEX moment (data_ora),
    
    FOREIGN KEY (id_programare) REFERENCES programare(id),
    FOREIGN KEY (id_medic) REFERENCES medic(cnp),
    FOREIGN KEY (id_pacient) REFERENCES pacient(cnp),
    FOREIGN KEY (id_diagnostic) REFERENCES diagnostic(id)
);

#14
CREATE TABLE IF NOT EXISTS raport_medical (
	id int not null auto_increment primary key,
    nume_pacient varchar(20) not null,
    prenume_pacient varchar(20) not null,
    nume_medic varchar(20) not null,
    prenume_medic varchar(20) not null,
    nume_asistent varchar(20),
    prenume_asistent varchar(20),
    data_consultatie datetime,
    istoric_boala varchar(100),
    simptome varchar(100),
    id_serviciu int,
    rezultat int,
    id_diagnostic int,
    recomandari varchar(500),
    parafat boolean,
    
    FOREIGN KEY (nume_pacient) REFERENCES pacient(nume),
    FOREIGN KEY (prenume_pacient) REFERENCES pacient(prenume),
    FOREIGN KEY (nume_medic) REFERENCES medic(nume),
    FOREIGN KEY (prenume_medic) REFERENCES medic(prenume),
    FOREIGN KEY (nume_asistent) REFERENCES asistent_medical(nume),
    FOREIGN KEY (prenume_asistent) REFERENCES asistent_medical(prenume),
    FOREIGN KEY (id_serviciu) REFERENCES serviciu_medical(id),
    FOREIGN KEY (id_diagnostic) REFERENCES diagnostic(id)
);


#15
CREATE TABLE IF NOT EXISTS bon_fiscal (
	nr_inregistrare int not null auto_increment primary key,
    data_timp_emitere datetime,
    id_client int not null,
    id_serviciu int not null,
    id_medic int not null,
    id_policlinica int not null,
    suma float,
    
    FOREIGN KEY (id_client) REFERENCES pacient(cnp),
    FOREIGN KEY (id_serviciu) REFERENCES serviciu_medical(id),
    FOREIGN KEY (id_medic) REFERENCES medic(cnp),
    FOREIGN KEY (id_policlinica) REFERENCES policlinica(id)
);

#16
CREATE TABLE IF NOT EXISTS analiza_medicala (
	id int not null auto_increment primary key,
    id_pacient int not null,
    id_asistent int not null,
    rezultat float,
    validat boolean,
    
    FOREIGN KEY (id_pacient) REFERENCES pacient(cnp),
    FOREIGN KEY (id_asistent) REFERENCES asistent_medical(cnp)
);

#17
CREATE TABLE IF NOT EXISTS orar_medical (
	id int not null auto_increment primary key,
    zi date,
    timp_inceput time,
    timp_final time,
    -- locatie
    id_policlinica int not null,
    id_angajat int not null,
    
    FOREIGN KEY (id_policlinica) REFERENCES policlinica(id),
    FOREIGN KEY (id_angajat) REFERENCES angajat(cnp)
);

#18
CREATE TABLE IF NOT EXISTS orar_angajat (
	id int not null auto_increment primary key,
    zi enum('luni', 'marti', 'miercuri', 'joi', 'vineri'),
    timp_inceput time,
    timp_final time,
    id_angajat int not null,
    
    FOREIGN KEY (id_angajat) REFERENCES angajat(cnp)
);

#19
CREATE TABLE IF NOT EXISTS serviciu_specializat(
	id_inregistrare int not null auto_increment primary key,
    id_serviciu int,
    id_medic int,
    durata_noua time,
    pret_nou float,
    
	FOREIGN KEY (id_serviciu) REFERENCES serviciu_medical(id),
    FOREIGN KEY (id_medic) REFERENCES medic(cnp)
);