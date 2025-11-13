package handlers

import (
	"encoding/json"
	"net/http"
)

type LifestyleTestRequest struct {
    Gender      int     `json:"gender"`
    Age         int     `json:"age"`
    DHand       int     `json:"dHand"`
    Weight      float64 `json:"weight"`
    AvgTemp     float64 `json:"avgTemp"`
    RestingHR   int     `json:"restingHR"`
    OxLv        int     `json:"oxLv"`
    History     bool    `json:"history"`
    Smoke       bool    `json:"smoke"`
    Apoe        bool    `json:"apoe"`
    ActivityLv  string  `json:"activityLv"`
    Depressed   bool    `json:"depressed"`
    Diet        string  `json:"diet"`
    GoodSleep   bool    `json:"goodSleep"`
    Edu         string  `json:"edu"`
}


func HandleReciveLifestyleTest(w http.ResponseWriter, r *http.Request) {

	if r.Method != http.MethodPost {
		http.Error(w, "Invalid request method", http.StatusMethodNotAllowed)
		return
	}

	var req LifestyleTestRequest
    if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
        http.Error(w, "Invalid JSON body", http.StatusBadRequest)
    		return
    }

	_, err := db.Exec("INSERT INTO Lifestyle (id, doctor) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15)",
	req.Gender, req.Age, req.DHand,
	req.Weight, req.AvgTemp, req.RestingHR, req.OxLv, req.History, req.Smoke, req.Apoe, req.ActivityLv, req.Depressed, 
	req.Diet, req.GoodSleep, req.Edu)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}


}

