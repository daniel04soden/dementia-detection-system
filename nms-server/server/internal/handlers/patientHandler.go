package handlers

import (
	"encoding/json"
	"net/http"
	"nms-server/server/internal/services"
	"strconv"
)

type SignupPatientRequest struct {
	Email     string `json:"email"`
	Eircode   string `json:"eircode"`
	Password  string `json:"password"`
	Phone     string `json:"phone"`
	FirstName string `json:"firstName"`
	LastName  string `json:"lastName"`
	ClinicID  int    `json:"clinicID"`
}

func HandleSignupPatient(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Invalid request method", http.StatusMethodNotAllowed)
		return
	}

	var req SignupPatientRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON body", http.StatusBadRequest)
		return
	}

	tx, err := db.Begin()
	if err != nil {
		http.Error(w, "Failed to start transaction", http.StatusInternalServerError)
		return
	}

	defer func() {
		if err != nil {
			tx.Rollback()
		}
	}()

	id, err := services.RegisterUser(db, tx, req.Email, req.Password, req.Phone, req.FirstName, req.LastName, "patient")
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	var doctorID int
	err = db.QueryRow(`
        SELECT de.doctorID
		FROM DoctorEmployment de
		LEFT JOIN Patient p ON p.doctorID = de.doctorID
		JOIN Doctor d ON d.doctorID = de.doctorID
		WHERE de.clinicID = $1 
		AND d.approved = TRUE
		GROUP BY de.doctorID
		ORDER BY COUNT(p.patientID) ASC
		LIMIT 1;
    `, req.ClinicID).Scan(&doctorID)

	if err != nil {
		http.Error(w, "No doctors found for the given clinic", http.StatusBadRequest)
		return
	}

	_, err = tx.Exec("INSERT INTO Patient (patientID, eircode, doctorID) VALUES ($1, $2, $3)", id, req.Eircode, doctorID)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	if err := tx.Commit(); err != nil {
		http.Error(w, "Failed to commit transaction", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)

	json.NewEncoder(w).Encode(map[string]any{
		"message": "success",
	})
}

type Patient struct {
	PatientID int    `json:"patientID"`
	DoctorID  int    `json:"doctorID"`
	FirstName string `json:"firstName"`
	LastName  string `json:"lastName"`
	Phone     string `json:"phone"`
	Eircode   string `json:"eircode"`
}

func HandleGetAllPatients(w http.ResponseWriter, r *http.Request) {
	rows, err := db.Query(`
		SELECT patientID, doctorID, firstName, lastName, phone, eircode 
		FROM Patient
		INNER JOIN Users ON Patient.patientID = Users.userID
		`)
	if err != nil {
		http.Error(w, "Failed to query patients", http.StatusInternalServerError)
		return
	}
	defer rows.Close()

	var patients []Patient

	for rows.Next() {
		var p Patient
		if err := rows.Scan(&p.PatientID, &p.DoctorID, &p.FirstName, &p.LastName, &p.Phone, &p.Eircode); err != nil {
			http.Error(w, "Failed to scan patient", http.StatusInternalServerError)
			return
		}
		patients = append(patients, p)
	}

	if err := rows.Err(); err != nil {
		http.Error(w, "Error reading patient data", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(patients)
}

func HandleGetPatient(w http.ResponseWriter, r *http.Request) {
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
		SELECT patientID, doctorID, firstName, lastName, phone, eircode 
		FROM Patient
		INNER JOIN Users ON Patient.patientID = Users.userID 
		WHERE patientID = $1
		`, id)

	var p Patient

	err = row.Scan(&p.PatientID, &p.DoctorID, &p.FirstName, &p.LastName, &p.Phone, &p.Eircode)
	if err != nil {
		http.Error(w, "failed to scan patient", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(p)
}
