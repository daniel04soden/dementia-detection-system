
CREATE TABLE Account (
	ID SERIAL PRIMARY KEY, email VARCHAR(50) NOT NULL UNIQUE,
	password VARCHAR(100) NOT NULL 
);

CREATE TABLE Users (
	userID INT PRIMARY KEY,
	doctorID INT,
	firstName VARCHAR(50) NOT NULL,
	lastName VARCHAR(50) NOT NULL,
	phoneNumber VARCHAR(16) NOT NULL,
	FOREIGN KEY(userID) REFERENCES Account(ID) ON DELETE CASCADE 
	FOREIGN KEY(doctorID) REFERENCES Doctor(doctorID)  
);

CREATE TABLE Patient (
	patientID INT PRIMARY KEY,
	eircode VARCHAR(10),
	FOREIGN KEY(patientID) REFERENCES Users(userID) ON DELETE CASCADE
);

CREATE TABLE Doctor (
	doctorID INT PRIMARY KEY,
	doctorNumber VARCHAR(16),
	FOREIGN KEY(doctorID) REFERENCES Users(userID) ON DELETE CASCADE 
);

CREATE TABLE Clinic (
	clinicID SERIAL PRIMARY KEY,
	name VARCHAR(50),
	phoneNumber VARCHAR(15),
	eircode VARCHAR(8)
);

CREATE TABLE DoctorEmployment (
    doctorID INT,
    clinicID INT,
    PRIMARY KEY (doctorID, clinicID),
    FOREIGN KEY (doctorID) REFERENCES Doctor(doctorID) ON DELETE CASCADE,
    FOREIGN KEY (clinicID) REFERENCES Clinic(clinicID) ON DELETE CASCADE
);

CREATE TABLE Lifestyle (
	lifestyleID SERIAL PRIMARY KEY,
	userID INT,
	gender VARCHAR(10),
	age INT,
	dHand INT,
	weight DOUBLE,
	avgTemp DOUBLE,
	restingHr INT,
	oxLv INT,
	history BOOLEAN,
	smoke BOOLEAN,
	apoe BOOLEAN,
	activityLv VARCHAR(20),
	depressed BOOLEAN,
	diet VARCHAR(20),
	goodSleep BOOLEAN,
	edu VARCHAR(30),
    FOREIGN KEY (userID) REFERENCES Users(userID) ON DELETE CASCADE,
)

CREATE TABLE Test(
	testNo INT SERIAL KEY,
	stageOneStatus BOOLEAN,
	stageTwoStatus BOOLEAN,
	doctorID VARCHAR(10) references Doctor(doctorID),
	patientID VARCHAR(10) references Patient(patientID),
	result VARCHAR(20)
);

CREATE TABLE Sessions (
    sessionID VARCHAR(32) PRIMARY KEY,
    userID INT REFERENCES Users(userID),
    expiresAt TIMESTAMP NOT NULL
);
