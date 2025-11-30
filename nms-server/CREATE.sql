CREATE TABLE IF NOT EXISTS Account(
	ID SERIAL PRIMARY KEY, 
    email VARCHAR(50) NOT NULL UNIQUE,
	password VARCHAR(100) NOT NULL, 
    role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS Users (
	userID INT PRIMARY KEY NOT NULL,
	firstName VARCHAR(50) NOT NULL,
	lastName VARCHAR(50) NOT NULL,
	phone VARCHAR(16) NOT NULL,
	FOREIGN KEY(userID) REFERENCES Account(ID) ON DELETE CASCADE 
);

CREATE TABLE IF NOT EXISTS Doctor (
	doctorID INT PRIMARY KEY,
    doctorNO VARCHAR(12),
    approved BOOLEAN DEFAULT false,
	FOREIGN KEY(doctorID) REFERENCES Users(userID) ON DELETE CASCADE 
);

CREATE TABLE IF NOT EXISTS Patient (
	patientID INT PRIMARY KEY NOT NULL,
    doctorID INT,
	eircode VARCHAR(10),
	FOREIGN KEY(patientID) REFERENCES Users(userID) ON DELETE CASCADE,
	FOREIGN KEY(doctorID) REFERENCES Doctor(doctorID)
);

CREATE TABLE IF NOT EXISTS Clinic (
	clinicID SERIAL PRIMARY KEY,
	name VARCHAR(50),
	phone VARCHAR(16),
    county VARCHAR(15),
	eircode VARCHAR(8)
);

CREATE TABLE IF NOT EXISTS DoctorEmployment (
    doctorID INT,
    clinicID INT,
    PRIMARY KEY (doctorID, clinicID),
    FOREIGN KEY (doctorID) REFERENCES Doctor(doctorID) ON DELETE CASCADE,
    FOREIGN KEY (clinicID) REFERENCES Clinic(clinicID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Lifestyle (
	lifestyleID SERIAL PRIMARY KEY,
	lifestyleStatus INT DEFAULT 0,
    patientID INT,
	gender INT,
	age INT,
	dHand INT,
	weight FLOAT,
	avgTemp FLOAT,
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
    FOREIGN KEY (patientID) REFERENCES Patient(patientID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Lifestyle (
    lifestyleID SERIAL PRIMARY KEY,
	lifestyleStatus INT DEFAULT 0,
    patientID INT,
    diabetic INT,                                   -- 0 for no, 1 for yes
    alcohollevel FLOAT,                             -- ???
    heartrate INT,                                  -- float with heartrate
    bloodoxygen FLOAT,                              -- float with blood oxygen
    bodytemperature FLOAT,                          -- float with temperature
    weight FLOAT,                                   -- float with weight
    mri_delay FLOAT,                                -- ???
    age INT,                                        -- int with age
    dominanthand INT,                               -- 0 for left, 1 for right
    gender INT,                                     -- 0 for female, 1 for male
    familyhistory INT,                              -- 0 for no, 1 for yes
    smoked INT,                                     -- 0 for no, 1 for yes
    apoeε4 INT,                                     -- 0 for no, 1 for yes
    physicalactivity VARCHAR(50),                   -- 'Sedentary', 'Moderate Activity', 'Mild Activity'
    depressionstatus INT,                           -- 0 for no depression, 1 for mild, 2 for moderate, etc.
    cognitivetestscores INT,                        -- ???
    medicationhistory INT,                          -- 0 for no, 1 for yes
    nutritiondiet VARCHAR(50),                      -- 'Low-Carb Diet', 'Mediterranean Diet', 'Balanced Diet'
    sleepquality INT,                               -- Assuming scale 1-5 or some numeric scale
    chronichealthconditions VARCHAR(50),            -- 'N/A', 'Heart Disease', 'Hypertension', 'Diabetes'
    cumulativeprimary VARCHAR(5),                   -- 'TRUE' or 'FALSE'
    cumulativesecondary VARCHAR(5),                 -- 'TRUE' or 'FALSE'
    cumulativedegree VARCHAR(5),                    -- 'TRUE' or 'FALSE'
    dementiastatus VARCHAR(50)                      -- 'N/A', 'Early', 'Moderate', 'Severe'
    FOREIGN KEY (patientID) REFERENCES Patient(patientID) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS Test (
    testID SERIAL PRIMARY KEY,
    stageOneStatus INT DEFAULT 0, -- changed to int
    stageTwoStatus INT DEFAULT 0, -- changed to int
    doctorID INT,
    patientID INT,
    FOREIGN KEY (patientID) REFERENCES Patient(patientID) ON DELETE CASCADE,
    FOREIGN KEY (doctorID) REFERENCES Doctor(doctorID) 
);

CREATE TABLE IF NOT EXISTS TestStageOne (
    testID INT PRIMARY KEY REFERENCES Test(testID) ON DELETE CASCADE,
    testDate VARCHAR(20) NOT NULL, 
    clockID INT,
    dateQuestion TEXT,
    news TEXT,
    recallName TEXT,
    recallSurname TEXT,
    recallNumber TEXT,
    recallStreet TEXT,
    recallCity TEXT,

    clockNumberRes BOOLEAN, 
    clockHandsRes BOOLEAN,
    dateQuestionRes BOOLEAN, 
    newsRes BOOLEAN,
    recallRes INT
);

-- add checks after
CREATE TABLE IF NOT EXISTS TestStageTwo (
    testID INT PRIMARY KEY REFERENCES Test(testID) ON DELETE CASCADE,
    testDate VARCHAR(20) NOT NULL,
    memoryScore INT, 
    recallScore INT,
    speakingScore INT,
    financialScore INT,
    medicineScore INT,
    transportScore INT,
    totalScore INT GENERATED ALWAYS AS (
        COALESCE(memoryScore,0) +
        COALESCE(recallScore,0) +
        COALESCE(speakingScore,0) +
        COALESCE(financialScore,0) +
        COALESCE(medicineScore,0) +
        COALESCE(transportScore,0)
    ) STORED
);

-- add checks after
CREATE TABLE SpeechResponse (
    speechTestID SERIAL PRIMARY KEY,
    testDate VARCHAR(20) NOT NULL,
    patientID INT, 
    llmResponse  TEXT,
    FOREIGN KEY (patientID) REFERENCES Patient(patientID) ON DELETE CASCADE
);
