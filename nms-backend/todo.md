# Account
- AccountID (string)
- EMail (string)
- Password (string/hash)
# User
- AccountID (PK, FK)
- FirstName (String)
- LastName (String)
- PhoneNumber (String)
# Patient
- AccountID (FK)
- PatientID (PK String)
- Eircode (string)
# Doctor
- DoctorID (PK String)
- AccountID (FK)
- Speciality (string)
# Clinic 
- ClinicNo (PK String)
- Name (String)
- PhoneNumber (String)
- Eircode (string)
# DoctorEmployment
- ClinicNo (CK, FK)
- DoctorID (CK, FK)
# Test
- TestNo (PK, AutoInc)
- StageOneStatus
- StageTwoStatus
- DoctorID (FK)
- PatientID (FK)
- Result
# TestStageOne
- TestNo (FK)
- TestDate (Date)
- DateQuestion (String)
- ClockNumber (BLOB)
- ClockHands (BLOB)
- News (String)
- Recall(String)
# TestStageOneResult
- TestNo (PK, FK)
- DateQuestion (int)
- ClockNumber (int)
- ClockHands (int)
- News (int)
- Recall(int)
# TestStageTwo
- TestNo (FK)
- TestDate (Date)
- PatientMemory (custom, yes, no, maybe)
- PatientRecall (custom, yes, no, maybe)
- SpeakingDifficulty (custom, yes, no, maybe)
- FinancialIndependence (custom, yes, no, maybe, n/a)
- ManageMedicine (custom, yes, no, maybe, n/a)
- TransportAssisstance (custom, yes, no, maybe, n/a)
- Score (int)

