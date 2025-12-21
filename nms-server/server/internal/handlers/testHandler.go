package handlers

import (
	"database/sql"
	"encoding/json"
	"log"
	"net/http"
	"strconv"
	"time"
)

type TestStageOne struct {
	TestID        int    `json:"testID"`
	TestDate      string `json:"testDate"`
	ClockID       int    `json:"clockID"`
	DateQuestion  string `json:"dateQuestion"`
	News          string `json:"news"`
	RecallName    string `json:"recallName"`
	RecallSurname string `json:"recallSurname"`
	RecallNumber  string `json:"recallNumber"`
	RecallStreet  string `json:"recallStreet"`
	RecallCity    string `json:"recallCity"`

	ClockNumberRes  bool `json:"clockNumberRes"`
	ClockHandsRes   bool `json:"clockHandsRes"`
	DateQuestionRes bool `json:"dateQuestionRes"`
	NewsRes         bool `json:"newsRes"`
	RecallRes       int  `json:"recallRes"`
}

func HandleGetTestStageOne(w http.ResponseWriter, r *http.Request) {
	// Get the ID from the query parameters
	idStr := r.URL.Query().Get("id")
	if idStr == "" {
		http.Error(w, "id is required", http.StatusBadRequest)
		return
	}

	// Convert the ID string to an integer
	id, err := strconv.Atoi(idStr)
	if err != nil {
		http.Error(w, "invalid id", http.StatusBadRequest)
		return
	}

	// Query the database for TestStageOne data
	row := db.QueryRow(`
        SELECT
            tso.testID,
            tso.testDate,
            tso.clockID,
            tso.dateQuestion,
            tso.news,
            tso.recallName,
            tso.recallSurname,
            tso.recallNumber,
            tso.recallStreet,
            tso.recallCity,
            tso.clockNumberRes,
            tso.clockHandsRes,
            tso.dateQuestionRes,
            tso.newsRes,
            tso.recallRes
        FROM TestStageOne tso
        WHERE tso.testID = $1
    `, id)

	// Map the result to the TestStageOne struct
	var to TestStageOne
	err = row.Scan(
		&to.TestID,
		&to.TestDate,
		&to.ClockID,
		&to.DateQuestion,
		&to.News,
		&to.RecallName,
		&to.RecallSurname,
		&to.RecallNumber,
		&to.RecallStreet,
		&to.RecallCity,
		&to.ClockNumberRes,
		&to.ClockHandsRes,
		&to.DateQuestionRes,
		&to.NewsRes,
		&to.RecallRes,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			http.Error(w, "TestStageOne not found", http.StatusNotFound)
			return
		}
		http.Error(w, "failed to scan test", http.StatusInternalServerError)
		return
	}

	// Set the response header for JSON and encode the struct to JSON
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(to)
}

type TestStageTwo struct {
	TestID         int    `json:"testID"`
	TestDate       string `json:"testDate"`
	MemoryScore    int    `json:"memoryScore"`
	RecallRes      int    `json:"recallRes"`
	SpeakingScore  int    `json:"speakingScore"`
	FinancialScore int    `json:"financialScore"`
	MedicineScore  int    `json:"medicineScore"`
	TransportScore int    `json:"transportScore"`
	TotalScore     int    `json:"totalScore"`
}

func HandleGetTestStageTwo(w http.ResponseWriter, r *http.Request) {
	// Get the ID from the query parameters
	idStr := r.URL.Query().Get("id")
	if idStr == "" {
		http.Error(w, "id is required", http.StatusBadRequest)
		return
	}

	// Convert the ID string to an integer
	id, err := strconv.Atoi(idStr)
	if err != nil {
		http.Error(w, "invalid id", http.StatusBadRequest)
		return
	}

	// Query the database for TestStageTwo data
	row := db.QueryRow(`
        SELECT
            tst.testID,
            tst.testDate,
            tst.memoryScore,
            tst.recallRes,
            tst.speakingScore,
            tst.financialScore,
            tst.medicineScore,
            tst.transportScore
        FROM TestStageTwo tst
        WHERE tst.testID = $1
    `, id)

	// Map the result to the TestStageTwo struct
	var ts TestStageTwo
	err = row.Scan(
		&ts.TestID,
		&ts.TestDate,
		&ts.MemoryScore,
		&ts.RecallRes,
		&ts.SpeakingScore,
		&ts.FinancialScore,
		&ts.MedicineScore,
		&ts.TransportScore,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			http.Error(w, "TestStageTwo not found", http.StatusNotFound)
			return
		}
		http.Error(w, "failed to scan test", http.StatusInternalServerError)
		return
	}

	// Calculate the total score
	ts.TotalScore = ts.MemoryScore + ts.RecallRes + ts.SpeakingScore + ts.FinancialScore + ts.MedicineScore + ts.TransportScore

	// Set the response header for JSON and encode the struct to JSON
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(ts)
}

// --------------------------------- Insert Test Results -------------------------------------
type TestStageOneInsert struct {
	PatientID     int    `json:"patientID"`
	ClockID       int    `json:"clockID"`
	DateQuestion  string `json:"dateQuestion"`
	News          string `json:"news"`
	RecallName    string `json:"recallName"`
	RecallSurname string `json:"recallSurname"`
	RecallNumber  string `json:"recallNumber"`
	RecallStreet  string `json:"recallStreet"`
	RecallCity    string `json:"recallCity"`
}

func HandleInsertStageOne(w http.ResponseWriter, r *http.Request) {
	var req TestStageOneInsert
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	tx, err := db.Begin()
	if err != nil {
		http.Error(w, "Failed to start transaction: "+err.Error(), http.StatusInternalServerError)
		return
	}
	defer func() {
		if p := recover(); p != nil {
			tx.Rollback()
			panic(p)
		}
	}()

	var DoctorID int
	err = db.QueryRow(`
        SELECT DoctorID
		FROM Patient
		WHERE PatientID = $1
    `, req.PatientID).Scan(&DoctorID)
	if err != nil {
		tx.Rollback()
		log.Println("Select Doctor Error" + err.Error())
		http.Error(w, "Could not find doctor for patient: "+err.Error(), http.StatusInternalServerError)
		return
	}

	// 1. Create Test row
	var testID int
	err = tx.QueryRow(`
        INSERT INTO Test (stageOneStatus, patientID, doctorID)
        VALUES ($1, $2, $3) RETURNING testID
    `, 1, req.PatientID, DoctorID).Scan(&testID)
	if err != nil {
		tx.Rollback()
		log.Println("Select Create Test Row" + err.Error())
		http.Error(w, "Could not create test: "+err.Error(), http.StatusInternalServerError)
		return
	}

	// Grade clock (your existing grading logic here)

	// Grade date question
	currentDate := time.Now().Format("02/01/2006")

	// Insert into TestStageOne table
	_, err = tx.Exec(`
		INSERT INTO TestStageOne 
		(testID, testDate, clockID, dateQuestion, news, recallName, recallSurname, recallNumber, recallStreet, recallCity)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
	`, testID, currentDate, req.ClockID, req.DateQuestion, req.News,
		req.RecallName, req.RecallSurname, req.RecallNumber, req.RecallStreet, req.RecallCity)
	if err != nil {
		tx.Rollback()

		log.Println("Insert Test Data Row" + err.Error())
		http.Error(w, "Could not insert TestStageOne: "+err.Error(), http.StatusInternalServerError)
		return
	}

	// Commit transaction
	if err := tx.Commit(); err != nil {

		log.Println("Commit Transaction Error" + err.Error())
		http.Error(w, "Failed to commit transaction: "+err.Error(), http.StatusInternalServerError)
		return
	}

	json.NewEncoder(w).Encode(map[string]any{
		"message": "Stage One inserted",
		"testID":  testID,
	})
}

type TestStageTwoInsert struct {
	PatientID      int `json:"patientID"`
	MemoryScore    int `json:"memoryScore"`
	RecallScore    int `json:"recallScore"`
	SpeakingScore  int `json:"speakingScore"`
	FinancialScore int `json:"financialScore"`
	MedicineScore  int `json:"medicineScore"`
	TransportScore int `json:"transportScore"`
}

func HandleInsertStageTwo(w http.ResponseWriter, r *http.Request) {
	var req TestStageTwoInsert
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	tx, err := db.Begin()
	if err != nil {

		log.Println("Start Transaction Error" + err.Error())
		http.Error(w, "Failed to start transaction: "+err.Error(), http.StatusInternalServerError)
		return
	}
	defer func() {
		if p := recover(); p != nil {
			tx.Rollback()
			panic(p)
		}
	}()

	// Identify the correct test automatically
	var testID int
	err = tx.QueryRow(`
        SELECT testID
        FROM Test
        WHERE patientID = $1
    `, req.PatientID).Scan(&testID)
	if err != nil {
		tx.Rollback()
		log.Println("No active test found for Stage Two" + err.Error())
		http.Error(w, "No active test found for Stage Two", http.StatusNotFound)
		return
	}

	currentDate := time.Now().Format("02/01/2006")

	// Update Stage Two row
	_, err = tx.Exec(`
		INSERT INTO TestStageTwo 
		(testID, testDate, memoryScore, recallScore, speakingScore, financialScore, medicineScore, transportScore)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`, testID, currentDate, req.MemoryScore, req.RecallScore, req.SpeakingScore,
		req.FinancialScore, req.MedicineScore, req.TransportScore)
	if err != nil {
		tx.Rollback()

		log.Println("Failed to insert into stage two" + err.Error())
		http.Error(w, "Could not insert TestStageTwo: "+err.Error(), http.StatusInternalServerError)
		return
	}

	// Mark Stage Two complete
	_, err = tx.Exec(`
        UPDATE Test SET stageTwoStatus=$1
        WHERE testID=$2
    `, 1, testID)
	if err != nil {
		tx.Rollback()

		log.Println("Failed to mark stage two as complete" + err.Error())
		http.Error(w, "DB error: "+err.Error(), http.StatusInternalServerError)
		return
	}

	if err := tx.Commit(); err != nil {

		log.Println("Failed to commit" + err.Error())
		http.Error(w, "Failed to commit transaction", http.StatusInternalServerError)
		return
	}

	json.NewEncoder(w).Encode(map[string]any{
		"message": "Stage Two inserted",
		"testID":  testID,
	})
}

// --------------------------------- Grade Test Results -------------------------------------

type GradeTest struct {
	TestID          int  `json:"testID"`
	ClockNumberRes  bool `json:"clockNumberRes"`
	ClockHandsRes   bool `json:"clockHandsRes"`
	DateQuestionRes bool `json:"dateQuestionRes"`
	NewsRes         bool `json:"newsRes"`
	RecallRes       int  `json:"recallRes"`
}

func HandleGradeStageOne(w http.ResponseWriter, r *http.Request) {
	var req GradeTest

	// Decode JSON body
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON: "+err.Error(), http.StatusBadRequest)
		return
	}

	tx, err := db.Begin()
	if err != nil {
		http.Error(w, "Failed to start transaction: "+err.Error(), http.StatusInternalServerError)
		return
	}
	// Update TestStageOne record with grading results
	_, err = tx.Exec(`
        UPDATE TestStageOne SET
            clockNumberRes=$1,
            clockHandsRes=$2,
            dateQuestionRes=$3,
            newsScore=$4,
            recallRes=$5
        WHERE testID=$6
    `, req.ClockNumberRes, req.ClockHandsRes, req.DateQuestionRes, req.NewsRes, req.RecallRes, req.TestID)
	if err != nil {
		http.Error(w, "DB update error: "+err.Error(), http.StatusInternalServerError)
		return
	}

	// Compute total score to decide if Stage 2 is needed
	score := 0
	if req.ClockNumberRes {
		score++
	}
	if req.ClockHandsRes {
		score++
	}
	if req.DateQuestionRes {
		score++
	}
	if req.NewsRes {
		score++
	}
	score += req.RecallRes

	/*
		if it's 0, it hasn't been taken
		if it's 1, it has been taken but hasn't been graded
		if it's 2, it has been taken, graded, and doesn't need a stage 2
		if it's 3, it has been taken, graded, and needs a stage 2
		if it's 4, it has been taken, graded, and needs proper testing (the 0 to 4 scoring)
	*/
	if score == 9 {
		_, err := tx.Exec(`
				UPDATE Test SET
					stageOneStatus=$1,
				WHERE testID=$2
			`, 2, req.TestID)
		if err != nil {
			http.Error(w, "DB update error: "+err.Error(), http.StatusInternalServerError)
			return
		}
	}
	if 5 <= score && score < 9 {
		_, err := tx.Exec(`
				UPDATE Test SET
					stageOneStatus=$1,
				WHERE testID=$2
			`, 3, req.TestID)
		if err != nil {
			http.Error(w, "DB update error: "+err.Error(), http.StatusInternalServerError)
			return
		}
	} else {
		_, err := tx.Exec(`
				UPDATE Test SET
					stageOneStatus=$1,
				WHERE testID=$2
			`, 4, req.TestID)
		if err != nil {
			http.Error(w, "DB update error: "+err.Error(), http.StatusInternalServerError)
			return
		}
	}

	if err := tx.Commit(); err != nil {
		http.Error(w, "Failed to commit transaction", http.StatusInternalServerError)
		return
	}

	// Respond with JSON result
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]any{
		"message":       "Stage One graded",
		"stageOneScore": score,
	})
}

type TestStageTwoGrade struct {
	PatientID      int `json:"patientID"`
	MemoryScore    int `json:"memoryScore"`
	RecallScore    int `json:"recallScore"`
	SpeakingScore  int `json:"speakingScore"`
	FinancialScore int `json:"financialScore"`
	MedicineScore  int `json:"medicineScore"`
	TransportScore int `json:"transportScore"`
}

func HandleGradeStageTwo(w http.ResponseWriter, r *http.Request) {
	var req TestStageTwoGrade
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", 400)
		return
	}

	// Identify the correct test automatically
	tx, err := db.Begin()
	if err != nil {
		http.Error(w, "Failed to start transaction: "+err.Error(), http.StatusInternalServerError)
		return
	}

	var testID int
	err = tx.QueryRow(`
        SELECT testID
        FROM Test
        WHERE patientID = $1
    `, req.PatientID).Scan(&testID)
	if err != nil {
		http.Error(w, "No active test found for Stage Two", 404)
		return
	}

	// Update Stage Two row
	_, err = tx.Exec(`
        UPDATE TestStageTwo SET
            memoryScore=$1, recallRes=$2, speakingScore=$3,
            financialScore=$4, medicineScore=$5, transportScore=$6
        WHERE testID=$7
    `, req.MemoryScore, req.RecallScore, req.SpeakingScore,
		req.FinancialScore, req.MedicineScore, req.TransportScore,
		testID)
	if err != nil {
		http.Error(w, "DB error: "+err.Error(), 500)
		return
	}

	// Mark Stage Two complete
	_, _ = tx.Exec(`
        UPDATE Test SET stageTwoStatus=$1
        WHERE testID=$2
    `, 2, testID)

	if err := tx.Commit(); err != nil {
		http.Error(w, "Failed to commit transaction", http.StatusInternalServerError)
		return
	}

	json.NewEncoder(w).Encode(map[string]any{
		"message": "Stage Two graded",
		"testID":  testID,
	})
}

type TestStatus struct {
	StageOneStatus  int `json:"stageOneStatus"`
	StageTwoStatus  int `json:"stageTwoStatus"`
	LifestyleStatus int `json:"lifestyleStatus"`
	SpeechStatus    int `json:"speechStatus"`
}

func HandleGetPatientTestStatus(w http.ResponseWriter, r *http.Request) {
	idStr := r.URL.Query().Get("id")
	if idStr == "" {
		http.Error(w, "id is required", http.StatusBadRequest)
		return
	}

	id, err := strconv.Atoi(idStr)
	if err != nil {
		http.Error(w, "invalid id", http.StatusBadRequest)
		return
	}

	var ts TestStatus

	err = db.QueryRow(`
        SELECT 
            t.stageOneStatus, 
            t.stageTwoStatus, 
            COALESCE(l.lifestyleStatus, 0) AS lifestyleStatus, 
            COALESCE(s.speechStatus, 0) AS speechStatus
        FROM Test t
        LEFT JOIN Lifestyle l ON t.patientID = l.patientID
        LEFT JOIN SpeechTest s ON t.patientID = s.patientID
        WHERE t.patientID = $1;
    `, id).Scan(&ts.StageOneStatus, &ts.StageTwoStatus, &ts.LifestyleStatus, &ts.SpeechStatus)
	if err != nil {
		if err == sql.ErrNoRows {
			http.Error(w, "patient not found", http.StatusNotFound)
			return
		}
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(ts)
}

type RiskScore struct {
	RiskScore int `json:"riskScore"`
}

type RiskStatus struct {
	StageOneStatus  int
	StageTwoStatus  int
	LifestyleStatus sql.NullInt32
	SpeechStatus    sql.NullInt32
}

func HandleGetPatientRisk(w http.ResponseWriter, r *http.Request) {
	var rs RiskStatus
	var score RiskScore

	idStr := r.URL.Query().Get("id")
	if idStr == "" {
		http.Error(w, "id is required", http.StatusBadRequest)
		return
	}
	id, err := strconv.Atoi(idStr)
	if err != nil {
		http.Error(w, "invalid id", http.StatusBadRequest)
		return
	}

	err = db.QueryRow(`
        SELECT 
            t.stageOneStatus, 
            t.stageTwoStatus, 
            l.lifestyleStatus, 
            s.speechStatus
        FROM Test t
        LEFT JOIN Lifestyle l ON t.patientID = l.patientID
        LEFT JOIN SpeechTest s ON t.patientID = s.patientID
        WHERE t.patientID = $1;
    `, id).Scan(&rs.StageOneStatus, &rs.StageTwoStatus, &rs.LifestyleStatus, &rs.SpeechStatus)
	if err != nil {
		if err == sql.ErrNoRows {
			http.Error(w, "patient not found", http.StatusNotFound)
			return
		}
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	if rs.StageOneStatus == 2 {
		score.RiskScore++
	}
	if rs.StageTwoStatus == 2 {
		score.RiskScore++
	}
	if rs.LifestyleStatus.Valid && rs.LifestyleStatus.Int32 == 2 {
		score.RiskScore++
	}
	if rs.SpeechStatus.Valid && rs.SpeechStatus.Int32 == 2 {
		score.RiskScore++
	}
	json.NewEncoder(w).Encode(score)
}
