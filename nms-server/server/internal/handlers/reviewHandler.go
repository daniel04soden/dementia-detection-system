package handlers

import (
	"encoding/json"
	"net/http"
	"strconv"
	"time"
)

type ReviewInsert struct {
	Score    int    `json:"score"`
	Critique string `json:"critique"`
}

func HandleInsertReview(w http.ResponseWriter, r *http.Request) {
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

	var req ReviewInsert
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON body", http.StatusBadRequest)
		return
	}

	date := time.Now().Format("02/01/2006")

	_, err = db.Exec(`
		INSERT INTO Review (
			date, patientID, score, critique
		) 
		VALUES ($1, $2, $3, $4)
	`, date, id, req.Score, req.Critique)
	if err != nil {
		http.Error(w, "invalid insert", http.StatusBadRequest)
		return
	}

	json.NewEncoder(w).Encode(map[string]any{
		"message": "success",
	})
}

type ReviewResponse struct {
	ReviewID  int    `json:"reviewID"`
	Date      string `json:"date"`
	PatientID int    `json:"patientID"`
	Score     int    `json:"score"`
	Critique  string `json:"critique"`
}

func HandleGetPatientReviews(w http.ResponseWriter, r *http.Request) {
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

	rows, err := db.Query(`
		SELECT reviewID, date, patientID, score, critique
		FROM Review
		Where patientID = $1
		`, id)
	if err != nil {
		http.Error(w, "Failed to query reviews", http.StatusInternalServerError)
		return
	}
	defer rows.Close()

	var reviews []ReviewResponse

	for rows.Next() {
		var r ReviewResponse
		if err := rows.Scan(&r.ReviewID, &r.Date, &r.PatientID, &r.Score, &r.Critique); err != nil {
			http.Error(w, "Failed to scan lifestyle", http.StatusInternalServerError)
			return
		}
		reviews = append(reviews, r)
	}

	if err := rows.Err(); err != nil {
		http.Error(w, "Error reading lifestyle data", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(reviews)
}

func HandleGetAllReviews(w http.ResponseWriter, r *http.Request) {
	rows, err := db.Query(`
		SELECT reviewID, date, patientID, score, critique
		FROM Review
		`)
	if err != nil {
		http.Error(w, "Failed to query reviews", http.StatusInternalServerError)
		return
	}
	defer rows.Close()

	var reviews []ReviewResponse

	for rows.Next() {
		var r ReviewResponse
		if err := rows.Scan(&r.ReviewID, &r.Date, &r.PatientID, &r.Score, &r.Critique); err != nil {
			http.Error(w, "Failed to scan lifestyle", http.StatusInternalServerError)
			return
		}
		reviews = append(reviews, r)
	}

	if err := rows.Err(); err != nil {
		http.Error(w, "Error reading lifestyle data", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(reviews)
}

type ReviewUpdate struct {
	Answer string `json:"answer"`
}

func HandleUpdateReview(w http.ResponseWriter, r *http.Request) {
	id := r.URL.Query().Get("id")

	if id == "" {
		http.Error(w, "Invalid ID", http.StatusBadRequest)
		return
	}

	var review ReviewUpdate
	if err := json.NewDecoder(r.Body).Decode(&review); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	_, err := db.Exec(`UPDATE Review SET answer = ? WHERE ticketID = ?`, review.Answer, id)
	if err != nil {
		http.Error(w, "Failed to update review", http.StatusInternalServerError)
		return
	}

	json.NewEncoder(w).Encode(review)
}
