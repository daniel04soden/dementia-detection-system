package handlers

import (
	"database/sql"
	"encoding/json"
	"net/http"
	"strconv"
	"strings"

	"nms-server/server/internal/auth"
)

type LifestyleInsert struct {
	PatientID               int     `json:"patientID"`
	Status                  bool    `json:"status"`
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
		http.Error(w, "Invalid JSON body", http.StatusBadRequest)
		return
	}

	cumulativePrimary := "FALSE"
	cumulativeSecondary := "FALSE"
	cumulativeDegree := "FALSE"
	if req.Education == "Primary" {
		cumulativePrimary = "TRUE"
	}
	if req.Education == "Secondary" {
		cumulativePrimary = "TRUE"
		cumulativeSecondary = "TRUE"
	}
	if req.Education == "Third-level" {
		cumulativePrimary = "TRUE"
		cumulativeSecondary = "TRUE"
		cumulativeDegree = "TRUE"
	}

	var lifestyleStatus int
	if req.Status == false {
		lifestyleStatus = 1
	}
	if req.Status == true {
		lifestyleStatus = 2
	}

	err := db.QueryRow(`
		INSERT INTO Lifestyle (
			lifestyleStatus, patientID, diabetic, alcohollevel, heartrate, bloodoxygen, bodytemperature, 
			weight, mri_delay, age, dominanthand, gender, familyhistory, smoked, apoeε4, 
			physicalactivity, depressionstatus, cognitivetestscores, medicationhistory, 
			nutritiondiet, sleepquality, chronichealthconditions, education, 
			cumulativeprimary, cumulativesecondary, cumulativedegree, dementiastatus
		) 
		VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, 
			$15, $16, $17, $18, $19, $20, $21, $22, $23, $24, $25, $26, $27
		)`,
		lifestyleStatus, req.PatientID, req.Diabetic, req.AlcoholLevel, req.HeartRate, req.BloodOxygen,
		req.BodyTemperature, req.Weight, req.MRIDelay, req.Age, req.DominantHand,
		req.Gender, req.FamilyHistory, req.Smoked, req.APOEε4, req.PhysicalActivity,
		req.DepressionStatus, req.CognitiveTestScores, req.MedicationHistory, req.NutritionDiet,
		req.SleepQuality, req.ChronicHealthConditions, req.Education,
		cumulativePrimary,
		cumulativeSecondary,
		cumulativeDegree,
		"N/A",
	)
	if err != nil {
		http.Error(w, "invalid insert", http.StatusBadRequest)
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
	APOEε4                  int     `json:"apoe4"`
	PhysicalActivity        string  `json:"physicalActivity"`
	DepressionStatus        int     `json:"depressionStatus"`
	CognitiveTestScores     int     `json:"cognitiveTestScores"`
	MedicationHistory       int     `json:"medicationHistory"`
	NutritionDiet           string  `json:"nutritionDiet"`
	SleepQuality            int     `json:"sleepQuality"`
	ChronicHealthConditions string  `json:"chronicHealthConditions"`
	CumulativePrimary       string  `json:"cumulativePrimary"`
	CumulativeSecondary     string  `json:"cumulativeSecondary"`
	CumulativeDegree        string  `json:"cumulativeDegree"`
	DementiaStatus          string  `json:"dementiaStatus"`
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

	// Declare a struct to hold the result
	var lifestyle LifestyleResponse
	query := `
		SELECT 
			lifestyleID, lifestyleStatus, patientID, diabetic, alcohollevel, heartrate, bloodoxygen, 
			bodytemperature, weight, mri_delay, age, dominanthand, gender, familyhistory, smoked, apoeε4, 
			physicalactivity, depressionstatus, cognitivetestscores, medicationhistory, nutritiondiet, 
			sleepquality, chronichealthconditions, cumulativeprimary, cumulativesecondary, cumulativedegree, dementiastatus
		FROM Lifestyle 
		WHERE patientID = $1
	`

	// Perform the query
	err = db.QueryRow(query, id).Scan(
		&lifestyle.LifestyleID, &lifestyle.LifestyleStatus, &lifestyle.PatientID, &lifestyle.Diabetic,
		&lifestyle.AlcoholLevel, &lifestyle.HeartRate, &lifestyle.BloodOxygen, &lifestyle.BodyTemperature,
		&lifestyle.Weight, &lifestyle.MRIDelay, &lifestyle.Age, &lifestyle.DominantHand, &lifestyle.Gender,
		&lifestyle.FamilyHistory, &lifestyle.Smoked, &lifestyle.APOEε4, &lifestyle.PhysicalActivity,
		&lifestyle.DepressionStatus, &lifestyle.CognitiveTestScores, &lifestyle.MedicationHistory,
		&lifestyle.NutritionDiet, &lifestyle.SleepQuality, &lifestyle.ChronicHealthConditions,
		&lifestyle.CumulativePrimary, &lifestyle.CumulativeSecondary, &lifestyle.CumulativeDegree,
		&lifestyle.DementiaStatus,
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

type LifestyleAIAnalyse struct {
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
	CumulativePrimary       string  `json:"cumulativePrimary"`
	CumulativeSecondary     string  `json:"cumulativeSecondary"`
	CumulativeDegree        string  `json:"cumulativeDegree"`
	DementiaStatus          string  `json:"dementiaStatus"`
}

func HandleReviewLifestyle(w http.ResponseWriter, r *http.Request) {
	authHeader := r.Header.Get("Authorization")
	var token string
	token, ok := strings.CutPrefix(authHeader, "Bearer ")
	if !ok {
		http.Error(w, "Missing or invalid Authorization header", http.StatusUnauthorized)
		return
	}

	claims, err := auth.ValidateJWT(token)
	if err != nil {
		http.Error(w, "Invalid or expired token", http.StatusUnauthorized)
		return
	}

	row := db.QueryRow(`
		SELECT premium 
		FROM Patient
		WHERE patientID = $1
		`, claims.ID)

	var premium bool

	err = row.Scan(&premium)
	if err != nil {
		http.Error(w, "failed to scan patient", http.StatusInternalServerError)
		return
	}

	if !premium {
		http.Error(w, "Access not allowed", http.StatusUnauthorized)
	}

	var ls LifestyleAIAnalyse

	row = db.QueryRow(`
		SELECT diabetic, alcoholLevel,
		heartRate, bloodOxygen, bodyTemperature, weight, MRIDelay,
		age, dominantHand, gender, familyHistory, smoked, apoe4, physicalActivity,
		depressionStatus, cognitiveTestScores, medicationHistory, nutritionDiet,
		sleepQuality, cumulativePrimary, cumulativeSecondary, cumulativeDegree, dementiaStatus
		FROM Lifestyle 
		WHERE patientID = $1
		`, claims.ID)
	err = row.Scan(ls.Diabetic, ls.AlcoholLevel, ls.HeartRate, ls.BloodOxygen, ls.BodyTemperature, ls.Weight,
		ls.MRIDelay, ls.Age, ls.DominantHand, ls.Gender, ls.FamilyHistory, ls.Smoked, ls.APOE4, ls.PhysicalActivity,
		ls.DepressionStatus, ls.CognitiveTestScores, ls.MedicationHistory, ls.NutritionDiet, ls.SleepQuality,
		ls.CumulativePrimary, ls.CumulativeSecondary, ls.CumulativeDegree, ls.DementiaStatus)
	if err != nil {
		http.Error(w, "failed to scan lifestyle", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(ls)
}
