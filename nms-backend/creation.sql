CREATE DATABASE app_main_data

CREATE TABLE Account (
	AccountID VARCHAR(10) PRIMARY KEY NOT NULL,
	Email VARCHAR(50) NOT NULL UNIQUE,
	Password VARCHAR(100) NOT NULL 
);

CREATE TABLE Users (
	userID VARCHAR(10) references Account(AccountID) PRIMARY KEY NOT NULL,
	firstName VARCHAR(50) NOT NULL,
	lastName VARCHAR(50) NOT NULL
);

CREATE TABLE Patient (
	patientID VARCHAR(10) PRIMARY KEY NOT NULL,
	userID VARCHAR(10) references User(userID),
	eircode VARCHAR(8)
);

CREATE TABLE Doctor (
	doctorID VARCHAR(10) PRIMARY KEY,
	userID VARCHAR(10) references User(userID),
	speciality VARCHAR(50)
);

CREATE TABLE Clinic (
	clinicNo VARCHAR(10) PRIMARY KEY,
	name VARCHAR(50),
	phoneNumber VARCHAR(15),
	eircode VARCHAR(8)
);


CREATE TABLE DoctorEmployment (
    doctorID VARCHAR(10),
    clinicNo VARCHAR(10),
    PRIMARY KEY (doctorID, clinicNo),
    FOREIGN KEY (doctorID) REFERENCES Doctor(doctorID),
    FOREIGN KEY (clinicNo) REFERENCES Clinic(clinicNo)
);

CREATE TABLE Test(
	testNo VARCHAR(10) PRIMARY KEY,
	stageOneStatus BOOLEAN,
	stageTwoStatus BOOLEAN,
	doctorID VARCHAR(10) references Doctor(doctorID),
	patientID VARCHAR(10) references Patient(patientID),
	Result VARCHAR(20)
);
CREATE TABLE TestStageOne(
	testNo VARCHAR(10) PRIMARY KEY references Test(testNo),
	testDate DATE,
	dateQuestion TEXT,
	clockNumber BYTEA,
	clockHands BYTEA,
	news TEXT,
	recall TEXT
);
CREATE TABLE TestStageOneResult(
	testNo VARCHAR(10) PRIMARY KEY references Test(testNo),
	reviewDate DATE,
	dateQuestion INT,
	clockNumber INT,
	clockHands INT,
	news INT,
	recall INT 
);
CREATE TABLE TestStageTwo(
	testNo VARCHAR(10) PRIMARY KEY references Test(testNo),
	testDate DATE,
	patientMemory VARCHAR(15),
	patientRecall VARCHAR(15),
	speakingDifficulty VARCHAR(15),
	financialIndependence VARCHAR(15),
	manageMedicine VARCHAR(15),
	transportAssistance VARCHAR(15),
	score INT
);










