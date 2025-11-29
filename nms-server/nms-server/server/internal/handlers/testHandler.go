package handlers

import (
	"database/sql"
	"encoding/json"
	"net/http"
	"strconv"
	"time"
)

// --------------------------------- Get Graded Details -------------------------------------
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
	//
	var DoctorID int
	err = db.QueryRow(`
        SELECT DoctorID
		FROM Patient
		WHERE PatientID = $1
    `, req.PatientID).Scan(&DoctorID)

	// 1. Create Test row
	var testID int
	err = tx.QueryRow(`
        INSERT INTO Test (stageOneStatus, patientID, doctorID)
        VALUES ($1, $2, $3) RETURNING testID
    `, 1, req.PatientID, DoctorID).Scan(&testID)

	if err != nil {
		http.Error(w, "Could not create test: "+err.Error(), http.StatusInternalServerError)
		return
	}

	// 2. Grade clock
	var clockNumberRes bool
	var clockHandsRes bool

	switch req.ClockID {
	case 1:
		clockNumberRes = true
		clockHandsRes = false
	case 2:
		clockNumberRes = false
		clockHandsRes = true
	case 3:
		clockNumberRes = true
		clockHandsRes = true
	case 4:
		clockNumberRes = false
		clockHandsRes = false
	case 5:
		clockNumberRes = false
		clockHandsRes = false
	case 6:
		clockNumberRes = false
		clockHandsRes = true
	case 7:
		clockNumberRes = true
		clockHandsRes = false
	case 8:
		clockNumberRes = true
		clockHandsRes = false
	default:
		clockNumberRes = false
		clockHandsRes = false
	}

	// 2. Grade recall
	expectedRecall := struct {
		Name    string
		Surname string
		Number  string
		Street  string
		City    string
	}{
		Name:    "John",
		Surname: "Brown",
		Number:  "42",
		Street:  "West St",
		City:    "Kensington",
	}

	recallRes := 5
	if req.RecallName != expectedRecall.Name {
		recallRes--
	}
	if req.RecallSurname != expectedRecall.Surname {
		recallRes--
	}
	if req.RecallNumber != expectedRecall.Number {
		recallRes--
	}
	if req.RecallStreet != expectedRecall.Street {
		recallRes--
	}
	if req.RecallCity != expectedRecall.City {
		recallRes--
	}

	// 3. Grade date question
	dateQuestionRes := 0
	currentDate := time.Now().Format("02/01/2006")
	if req.DateQuestion == currentDate {
		dateQuestionRes = 1
	}

	// 4. Insert into TestStageOne — store only recallRes, not full recall
	_, err = tx.Exec(`
    INSERT INTO TestStageOne (
        testid, testdate, clockID, datequestion, news, recallName, recallSurname, recallNumber, recallStreet, recallCity,
		clockNumberRes, clockHandsRes, dateQuestionRes, newsRes, recallRes
    ) VALUES ($1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12)
	`,
		testID,           // $1
		currentDate,      // $2
		req.ClockID,      // $3
		req.DateQuestion, // $4 → clocknumberres
		req.News,
		req.RecallName,
		req.RecallSurname,
		req.RecallNumber,
		req.RecallStreet,
		req.RecallCity,
		clockNumberRes,
		clockHandsRes,
		dateQuestionRes,
		false,
		recallRes,
	)

	if err := tx.Commit(); err != nil {
		http.Error(w, "Failed to commit transaction", http.StatusInternalServerError)
		return
	}

	if err != nil {
		http.Error(w, "Could not create stage one: "+err.Error(), http.StatusInternalServerError)
		return
	}

	json.NewEncoder(w).Encode(map[string]any{
		"message":   "Stage One Created",
		"testID":    testID,
		"recallRes": recallRes,
		"dateScore": dateQuestionRes,
	})
}

type TestStageTwoInsert struct {
	PatientID      int `json:"patientID"`
	MemoryScore    int `json:"memoryScore"`
	RecallRes      int `json:"recallRes"`
	SpeakingScore  int `json:"speakingScore"`
	FinancialScore int `json:"financialScore"`
	MedicineScore  int `json:"medicineScore"`
	TransportScore int `json:"transportScore"`
}

func HandleInsertStageTwo(w http.ResponseWriter, r *http.Request) {
	var req TestStageTwoInsert
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", 400)
		return
	}

	// Identify the correct test automatically
	var testID int
	tx, err := db.Begin()
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
    `, req.MemoryScore, req.RecallRes, req.SpeakingScore,
		req.FinancialScore, req.MedicineScore, req.TransportScore,
		testID)

	if err != nil {
		http.Error(w, "DB error: "+err.Error(), 500)
		return
	}

	// Mark Stage Two complete
	_, err = tx.Exec(`
        UPDATE Test SET stageTwoStatus=$1
        WHERE testID=$2
    `, 1, testID)

	if err := tx.Commit(); err != nil {
		http.Error(w, "Failed to commit transaction", http.StatusInternalServerError)
		return
	}

	json.NewEncoder(w).Encode(map[string]any{
		"message": "Stage Two graded",
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
	RecallRes      int `json:"recallRes"`
	SpeakingScore  int `json:"speakingScore"`
	FinancialScore int `json:"financialScore"`
	MedicineScore  int `json:"medicineScore"`
	TransportScore int `json:"transportScore"`
}

func HandleGradeStageTwo(w http.ResponseWriter, r *http.Request) {
	var req TestStageTwoInsert
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", 400)
		return
	}

	// Identify the correct test automatically
	var testID int
	tx, err := db.Begin()
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
    `, req.MemoryScore, req.RecallRes, req.SpeakingScore,
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

// ------------------------ Get Overall Status -----------------------------
type TestStatus struct {
	StageOneStatus  int  `json:"stageOneStatus"`
	StageTwoStatus  int  `json:"stageTwoStatus"`
	LifestyleStatus int  `json:"lifestyleStatus"`
	SpeechStatus    bool `json:"speechStatus"`
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

	row := db.QueryRow(`
		SELECT testID, stageOneStatus, stageTwoStatus
		FROM Test
		WHERE patientID = $1
		`, id)

	var ts TestStatus
	if err := row.Scan(&ts.StageOneStatus, &ts.StageTwoStatus); err != nil {
		http.Error(w, "failed to scan test status", http.StatusInternalServerError)
		return
	}

	row = db.QueryRow(`
		SELECT speechTestID
		FROM SpeechResponse
		WHERE patientID = $1
		`, id)

	var speechTestID int
	err = row.Scan(&speechTestID)
	if err == sql.ErrNoRows {
		ts.SpeechStatus = false
	} else if err != nil {
		ts.SpeechStatus = false
	} else {
		ts.SpeechStatus = true
	}

	var lifestyleStatusRead int
	err = db.QueryRow(`
		SELECT lifestyleStatus
		FROM Lifestyle
		WHERE patientID = $1
	`, id).Scan(&lifestyleStatusRead)

	if err == sql.ErrNoRows {
		ts.LifestyleStatus = 0
	} else if err != nil {
		ts.LifestyleStatus = 0
	} else {
		ts.LifestyleStatus = lifestyleStatusRead
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(ts)
}

// Set Speech
// ------------------------ Get Overall Status -----------------------------
type SpeechInsert struct {
	LlmResponse string `json:"llmResponse"`
}

func HandleSpeechInsert(w http.ResponseWriter, r *http.Request) {
	var req SpeechInsert
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", 400)
		return
	}

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

	_, err = db.Exec(`
		INSERT INTO SpeechResponse (
			testDate, patientID, llmResponse
		) VALUES ($1,$2,$3)
		`, time.Now().Format("02/01/2006"), id, req.LlmResponse)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
}
