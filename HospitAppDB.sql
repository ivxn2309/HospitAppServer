

-- ------------------------------------------------
-- DROPS
-- ------------------------------------------------

DROP TABLE IF EXISTS Admin;
DROP TABLE IF EXISTS Report;
DROP TABLE IF EXISTS Appointment;
DROP TABLE IF EXISTS Schedule;
DROP TABLE IF EXISTS Patient;
DROP TABLE IF EXISTS Doctor;

-- ------------------------------------------------
-- Doctor
-- ------------------------------------------------
DROP TABLE IF EXISTS Doctor;
CREATE TABLE IF NOT EXISTS Doctor (
    username VARCHAR(64) NOT NULL,
    password VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    license VARCHAR(90) NOT NULL,
    specialty VARCHAR(100),
    PRIMARY KEY(username)
);
-- En el campo license puede poner mas de una cédula separado por punto y coma (;)
-- El campo specialty es para almacenar la especialidad del médico, si necesita especificar más de una se separa por (;)

-- ------------------------------------------------
-- Doctor
-- ------------------------------------------------
DROP TABLE IF EXISTS Patient;
CREATE TABLE IF NOT EXISTS Patient (
    nss VARCHAR(64) NOT NULL,
    password VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    address VARCHAR(100) NOT NULL,
    is_active TINYINT(1) NOT NULL,
    doctor_username VARCHAR(64),
    FOREIGN KEY(doctor_username) REFERENCES Doctor(username),
    PRIMARY KEY(nss)
);
-- El campo is_active toma 0 como false y cualquier otro numero verdadero

-- ------------------------------------------------
-- Schedule
-- ------------------------------------------------
DROP TABLE IF EXISTS Schedule;
CREATE TABLE IF NOT EXISTS Schedule (
    id_schedule SERIAL,
    doctor_username VARCHAR(100) NOT NULL,
    monday VARCHAR(150),
    tuesday VARCHAR(150),
    wednesday VARCHAR(150),
    thursday VARCHAR(150),
    friday VARCHAR(150),
    FOREIGN KEY(doctor_username) REFERENCES Doctor(username),
    PRIMARY KEY(id_schedule)
);

-- ------------------------------------------------
-- Appointment
-- ------------------------------------------------
DROP TABLE IF EXISTS Appointment;
CREATE TABLE IF NOT EXISTS Appointment (
    id_appointment SERIAL,
    date DATE,
    is_finished TINYINT(1),
    iscanceled TINYINT(1),
    time VARCHAR(50),
    patient_nss VARCHAR(64) NOT NULL,
    doctor_username VARCHAR(100) NOT NULL,
    FOREIGN KEY(doctor_username) REFERENCES Doctor(username),
    FOREIGN KEY(patient_nss) REFERENCES Patient(nss),
    PRIMARY KEY(id_appointment)
);
-- Tanto is_finished como is_canceled toman 0 como false y cualquier otro numero verdadero, ... 
-- una vez traducido a java debe hacerse lo necesario para que sea booleano

-- ------------------------------------------------
-- Report
-- ------------------------------------------------

DROP TABLE IF EXISTS Report;
CREATE TABLE IF NOT EXISTS Report (
    id_report SERIAL,
    description VARCHAR(200),
    medicine VARCHAR(200),
    indications VARCHAR(200),
    patient_nss VARCHAR(64) NOT NULL,
    id_appointment BIGINT(20) UNSIGNED NOT NULL,
    FOREIGN KEY(id_appointment) REFERENCES Appointment(id_appointment),
    PRIMARY KEY(id_report)
);
-- Se eliminó la referencia a Doctor ya que se puede conseguir a través de la entidad Appointment
-- En la descripción va lo que el doctor observa durante la consulta
-- En medicine se coloca los medicamentos que fueron recetados separados por (;)
-- En indications las instrucciones de uso de los medicamentos en el mismo orden separados por (;)

-- ------------------------------------------------
-- Admin
-- ------------------------------------------------

DROP TABLE IF EXISTS Admin;
CREATE TABLE IF NOT EXISTS Admin (
    username VARCHAR(64) NOT NULL,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY(username)
);