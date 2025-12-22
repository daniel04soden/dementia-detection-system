package handlers

import (
	"bytes"
	"database/sql"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"strconv"
	"strings"

	"nms-server/server/internal/auth"
)

type LifestyleItem struct {
	Diabetic                int
	AlcoholLevel            float64
	HeartRate               int
	BloodOxygen             float64
	BodyTemperature         float64
	Weight                  float64
	MRIDelay                float64
	Age                     int
	DominantHand            int
	Gender                  int
	FamilyHistory           int
	Smoked                  int
	APOE4                   int
	PhysicalActivity        string
	DepressionStatus        int
	CognitiveTestScores     int
	MedicationHistory       int
	NutritionDiet           string
	SleepQuality            int
	ChronicHealthConditions string
	Education               string
}

type LifestyleInsert struct {
	PatientID               int     `json:"patientID"`
	Diabetic                int     `json:"diabetic"`
	AlcoholLevel            float64 `json:"alcoholLevel"`
	HeartRate               int     `json:"heartRate"`
	BloodOxygen             float64 `json:"bloodOxygen"`
	BodyTemperature         float64 `json:"bodyTemperature"`
	Weight                  float64 `json:"weight"`
	MRIDelay                float64 `json:"mriDelay"`
	Age                     int     `json:"age"`
	DominantHand            int     `json:"dominantHand"`     // assuming Dominant_Hand is an integer (0 for left, 1 for right)
	Gender                  int     `json:"gender"`           // assuming Gender is an integer (0 for female, 1 for male)
	FamilyHistory           int     `json:"familyHistory"`    // assuming FamilyHistory is an integer (0 for no, 1 for yes)
	Smoked                  int     `json:"smoked"`           // assuming Smoked is an integer (0 for no, 1 for yes)
	APOE4                   int     `json:"apoe4"`            // assuming APOEε4 is an integer (0 for no, 1 for yes)
	PhysicalActivity        string  `json:"physicalActivity"` // assuming values: Sedentary, Moderate Activity, Mild Activity
	DepressionStatus        int     `json:"depressionStatus"` // assuming 0 for no depression, 1 for mild, 2 for moderate, etc.
	CognitiveTestScores     int     `json:"cognitiveTestScores"`
	MedicationHistory       int     `json:"medicationHistory"`       // assuming 0 for no, 1 for yes
	NutritionDiet           string  `json:"nutritionDiet"`           // assuming values: Low-Carb Diet, Mediterranean Diet, Balanced Diet
	SleepQuality            int     `json:"sleepQuality"`            // assuming scale 1-5 or some numeric scale
	ChronicHealthConditions string  `json:"chronicHealthConditions"` // assuming values: N/A, Heart Disease, Hypertension, Diabetes
	Education               string  `json:"education"`               // assuming values: Primary, Secondary, Degree
}

// lifestyleStatus
// 1 is sent but not corrected
// 2 is corrected by a doctor

func HandleInsertLifestyle(w http.ResponseWriter, r *http.Request) {
	var req LifestyleInsert
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		log.Println("Cant decode json" + err.Error())
		http.Error(w, "Invalid JSON body", http.StatusBadRequest)
		return
	}

	// 	cumulativePrimary := "FALSE"
	// 	cumulativeSecondary := "FALSE"
	// 	cumulativeDegree := "FALSE"
	// 	if req.Education == "Primary" {
	// 		cumulativePrimary = "TRUE"
	// 	}
	// 	if req.Education == "Secondary" {
	// 		cumulativePrimary = "TRUE"
	// 		cumulativeSecondary = "TRUE"
	// 	}
	// 	if req.Education == "Third-level" {
	// 		cumulativePrimary = "TRUE"
	// 		cumulativeSecondary = "TRUE"
	// 		cumulativeDegree = "TRUE"
	// 	}

	_, err := db.Exec(`
		INSERT INTO Lifestyle (
			lifestyleStatus, patientID, diabetic, alcoholLevel, heartRate, bloodOxygen, bodyTemperature, 
			weight, mriDelay, age, dominantHand, gender, familyHistory, smoked, apoe, 
			physicalActivity, depressionStatus, cognitiveTestScores, medicationHistory, 
			nutritionDiet, sleepQuality, chronicHealthConditions, education 
		) 
		VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, 
			$15, $16, $17, $18, $19, $20, $21, $22, $23
		)`,
		1, req.PatientID, req.Diabetic, req.AlcoholLevel, req.HeartRate, req.BloodOxygen,
		req.BodyTemperature, req.Weight, req.MRIDelay, req.Age, req.DominantHand,
		req.Gender, req.FamilyHistory, req.Smoked, req.APOE4, req.PhysicalActivity,
		req.DepressionStatus, req.CognitiveTestScores, req.MedicationHistory, req.NutritionDiet,
		req.SleepQuality, req.ChronicHealthConditions, req.Education)
	if err != nil {
		http.Error(w, fmt.Sprintf("insert failed: %v", err), http.StatusBadRequest)
		return
	}

	json.NewEncoder(w).Encode(map[string]any{
		"message": "success",
	})
}

type LifestyleResponse struct {
	LifestyleID             int     `json:"lifestyleID"`
	LifestyleStatus         int     `json:"lifestyleStatus"`
	PatientID               int     `json:"patientID"`
	Diabetic                int     `json:"diabetic"`
	AlcoholLevel            float64 `json:"alcoholLevel"`
	HeartRate               int     `json:"heartRate"`
	BloodOxygen             float64 `json:"bloodOxygen"`
	BodyTemperature         float64 `json:"bodyTemperature"`
	Weight                  float64 `json:"weight"`
	MRIDelay                float64 `json:"mriDelay"`
	Age                     int     `json:"age"`
	DominantHand            int     `json:"dominantHand"`
	Gender                  int     `json:"gender"`
	FamilyHistory           int     `json:"familyHistory"`
	Smoked                  int     `json:"smoked"`
	APOE4                   int     `json:"apoe4"`
	PhysicalActivity        string  `json:"physicalActivity"`
	DepressionStatus        int     `json:"depressionStatus"`
	CognitiveTestScores     int     `json:"cognitiveTestScores"`
	MedicationHistory       int     `json:"medicationHistory"`
	NutritionDiet           string  `json:"nutritionDiet"`
	SleepQuality            int     `json:"sleepQuality"`
	ChronicHealthConditions string  `json:"chronicHealthConditions"`
	Education               string  `json:"education"`
}

func HandleGetLifestyle(w http.ResponseWriter, r *http.Request) {
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

	var lifestyle LifestyleResponse
	query := `
		SELECT 
			lifestyleID, lifestyleStatus, patientID, diabetic, alcoholLevel, heartRate, bloodOxygen, 
			bodyTemperature, weight, mriDelay, age, dominantHand, gender, familyHistory, smoked, apoe, 
			physicalActivity, depressionStatus, cognitiveTestScores, medicationHistory, nutritionDiet, 
			sleepQuality, chronicHealthConditions, education
		FROM Lifestyle 
		WHERE patientID = $1
	`

	err = db.QueryRow(query, id).Scan(
		&lifestyle.LifestyleID, &lifestyle.LifestyleStatus, &lifestyle.PatientID, &lifestyle.Diabetic,
		&lifestyle.AlcoholLevel, &lifestyle.HeartRate, &lifestyle.BloodOxygen, &lifestyle.BodyTemperature,
		&lifestyle.Weight, &lifestyle.MRIDelay, &lifestyle.Age, &lifestyle.DominantHand, &lifestyle.Gender,
		&lifestyle.FamilyHistory, &lifestyle.Smoked, &lifestyle.APOE4, &lifestyle.PhysicalActivity,
		&lifestyle.DepressionStatus, &lifestyle.CognitiveTestScores, &lifestyle.MedicationHistory,
		&lifestyle.NutritionDiet, &lifestyle.SleepQuality, &lifestyle.ChronicHealthConditions,
		&lifestyle.Education,
	)
	// Check for errors
	if err != nil {
		if err == sql.ErrNoRows {
			http.Error(w, "No lifestyle data found for the given patientID", http.StatusNotFound)
		} else {
			http.Error(w, "Error retrieving data", http.StatusInternalServerError)
		}
		return
	}

	// Return the data as JSON
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(lifestyle)
}

type AnswersAnalyse struct {
	Answers LifestyleAIAnalyse `json:"answers"`
}

type LifestyleAIAnalyse struct {
	Diabetic                int     `json:"Diabetic"`
	AlcoholLevel            float64 `json:"AlcoholLevel"`
	HeartRate               int     `json:"HeartRate"`
	BloodOxygen             float64 `json:"BloodOxygen"`
	BodyTemperature         float64 `json:"BodyTemperature"`
	Weight                  float64 `json:"Weight"`
	MRIDelay                float64 `json:"MRI_Delay"`
	Age                     int     `json:"Age"`
	DominantHand            int     `json:"Dominant_Hand"`
	Gender                  int     `json:"Gender"`
	FamilyHistory           int     `json:"Family_History"`
	Smoked                  int     `json:"Smoked"`
	APOE4                   int     `json:"Dementia_gene"`
	PhysicalActivity        string  `json:"Physical_Activity"`
	DepressionStatus        int     `json:"Depression_Status"`
	CognitiveTestScores     int     `json:"Cognitive_Test_Scores"`
	MedicationHistory       int     `json:"Medication_History"`
	NutritionDiet           string  `json:"Nutrition_Diet"`
	SleepQuality            int     `json:"Sleep_Quality"`
	ChronicHealthConditions string  `json:"Chronic_Health_Conditions"`
	CumulativePrimary       string  `json:"Cumulative_Primary"`
	CumulativeSecondary     string  `json:"Cumulative_Secondary"`
	CumulativeDegree        string  `json:"Cumulative_Degree"`
}

func HandleAIReviewLifestyle(w http.ResponseWriter, r *http.Request) {
	authHeader := r.Header.Get("Authorization")
	var token string
	token, ok := strings.CutPrefix(authHeader, "Bearer ")
	if !ok {
		fmt.Println("cant cut prefix")
		http.Error(w, "Missing or invalid Authorization header", http.StatusUnauthorized)
		return
	}

	claims, err := auth.ValidateJWT(token)
	if err != nil {

		fmt.Println("expired token")
		http.Error(w, "Invalid or expired token", http.StatusUnauthorized)
		return
	}

	row := db.QueryRow(`
		SELECT premium 
		FROM Patient
		WHERE patientID = $1
		`, claims.UserID)

	var premium bool

	err = row.Scan(&premium)
	if err != nil {

		fmt.Println("cant scan if premium" + err.Error())
		http.Error(w, "failed to scan patient", http.StatusInternalServerError)
		return
	}

	if !premium {
		http.Error(w, "Access not allowed", http.StatusUnauthorized)
	}

	var ls LifestyleItem

	row = db.QueryRow(`
		SELECT diabetic, alcoholLevel,
		heartRate, bloodOxygen, bodyTemperature, weight, MRIDelay,
		age, dominantHand, gender, familyHistory, smoked, apoe, physicalActivity,
		depressionStatus, cognitiveTestScores, medicationHistory, nutritionDiet,
		sleepQuality, chronicHealthConditions, education
		FROM Lifestyle 
		WHERE patientID = $1
		`, claims.UserID)
	err = row.Scan(&ls.Diabetic, &ls.AlcoholLevel, &ls.HeartRate, &ls.BloodOxygen, &ls.BodyTemperature, &ls.Weight,
		&ls.MRIDelay, &ls.Age, &ls.DominantHand, &ls.Gender, &ls.FamilyHistory, &ls.Smoked, &ls.APOE4, &ls.PhysicalActivity,
		&ls.DepressionStatus, &ls.CognitiveTestScores, &ls.MedicationHistory, &ls.NutritionDiet, &ls.SleepQuality, &ls.ChronicHealthConditions, &ls.Education)
	if err != nil {
		fmt.Println("cant scan from db" + err.Error())
		http.Error(w, "failed to scan lifestyle", http.StatusInternalServerError)
		return
	}

	cumulativePrimary := "FALSE"
	cumulativeSecondary := "FALSE"
	cumulativeDegree := "FALSE"

	switch ls.Education {
	case "Primary":
		cumulativePrimary = "TRUE"
	case "Secondary":
		cumulativePrimary = "TRUE"
		cumulativeSecondary = "TRUE"
	case "Tertiary":
		cumulativePrimary = "TRUE"
		cumulativeSecondary = "TRUE"
		cumulativeDegree = "TRUE"
	default:

		fmt.Println("Education is wrong")
		http.Error(w, "Incorrect education", http.StatusInternalServerError)
		return
	}

	var lsAI LifestyleAIAnalyse

	lsAI.Diabetic = ls.Diabetic
	lsAI.AlcoholLevel = ls.AlcoholLevel
	lsAI.HeartRate = ls.HeartRate
	lsAI.BloodOxygen = ls.BloodOxygen
	lsAI.BodyTemperature = ls.BodyTemperature
	lsAI.Weight = ls.Weight
	lsAI.MRIDelay = ls.MRIDelay
	lsAI.Age = ls.Age
	lsAI.DominantHand = ls.DominantHand
	lsAI.Gender = ls.Gender
	lsAI.FamilyHistory = ls.FamilyHistory
	lsAI.Smoked = ls.Smoked
	lsAI.APOE4 = ls.APOE4
	lsAI.PhysicalActivity = ls.PhysicalActivity
	lsAI.DepressionStatus = ls.DepressionStatus
	lsAI.CognitiveTestScores = ls.CognitiveTestScores
	lsAI.MedicationHistory = ls.MedicationHistory
	lsAI.NutritionDiet = ls.NutritionDiet
	lsAI.SleepQuality = ls.SleepQuality
	lsAI.ChronicHealthConditions = ls.ChronicHealthConditions
	lsAI.CumulativePrimary = cumulativePrimary
	lsAI.CumulativeSecondary = cumulativeSecondary
	lsAI.CumulativeDegree = cumulativeDegree

	payload := AnswersAnalyse{
		Answers: lsAI,
	}

	jsonData, err := json.Marshal(payload)
	if err != nil {
		panic(err)
	}

	url := "http://api.magestle.dev/lifestyle"

	resp, err := http.Post(url, "application/json", bytes.NewBuffer(jsonData))
	if err != nil {
		http.Error(w, "Couldn't post to internal ai", http.StatusBadRequest)
	}
	defer resp.Body.Close()

	fmt.Println("Response Status:", resp.Status)

	var dementiaResult DementiaResult

	if err := json.NewDecoder(resp.Body).Decode(&dementiaResult); err != nil {
		fmt.Println("failed to payload" + err.Error())
		http.Error(w, "invalid payload", http.StatusBadRequest)
		return
	}

	if _, err := db.Exec(`
		UPDATE Lifestyle
		SET LifestyleStatus=$1
		WHERE patientID=$3
		`, dementiaResult.Classification+2, claims.UserID); err != nil {
		http.Error(w, "Failed to update lifestyle", 500)
		fmt.Printf("failed to update lifestyle")
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(ls)
}

// Review Lifestyle

func HandleDoctorReviewLifestyle(w http.ResponseWriter, r *http.Request) {
	var ls LifestyleInsert

	if err := json.NewDecoder(r.Body).Decode(&ls); err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	tx, err := db.Begin()
	if err != nil {
		http.Error(w, "Transaction start failed", http.StatusInternalServerError)
		return
	}
	defer tx.Rollback()

	lifestyleQuery := `
        UPDATE Lifestyle
        SET
            lifestyleStatus = 5,
            diabetic = $2,
            alcoholLevel = $3,
            heartRate = $4,
            bloodOxygen = $5,
            bodyTemperature = $6,
            weight = $7,
            mriDelay = $8,
            age = $9,
            dominantHand = $10,
            gender = $11,
            familyHistory = $12,
            smoked = $13,
            apoe = $14,
            physicalActivity = $15,
            depressionStatus = $16,
            cognitiveTestScores = $17,
            medicationHistory = $18,
            nutritionDiet = $19,
            sleepQuality = $20,
            chronicHealthConditions = $21,
            education = $22
        WHERE patientID = $1;
    `

	_, err = tx.Exec(
		lifestyleQuery,
		ls.PatientID,
		ls.Diabetic,
		ls.AlcoholLevel,
		ls.HeartRate,
		ls.BloodOxygen,
		ls.BodyTemperature,
		ls.Weight,
		ls.MRIDelay,
		ls.Age,
		ls.DominantHand,
		ls.Gender,
		ls.FamilyHistory,
		ls.Smoked,
		ls.APOE4,
		ls.PhysicalActivity,
		ls.DepressionStatus,
		ls.CognitiveTestScores,
		ls.MedicationHistory,
		ls.NutritionDiet,
		ls.SleepQuality,
		ls.ChronicHealthConditions,
		ls.Education,
	)
	if err != nil {
		http.Error(w, "Lifestyle update failed", http.StatusInternalServerError)
		return
	}

	if err := tx.Commit(); err != nil {
		http.Error(w, "Transaction commit failed", http.StatusInternalServerError)
		return
	}

	json.NewEncoder(w).Encode(map[string]any{
		"message": "success",
	})
}
