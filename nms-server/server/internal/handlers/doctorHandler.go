package handlers

import (
	"database/sql"
	"encoding/json"
	"log"
	"net/http"
	"nms-server/server/internal/auth"
	"nms-server/server/internal/services"
	"strconv"
)

type SignupDoctorRequest struct {
	Email     string `json:"email"`
	Doctor    string `json:"doctorNO"`
	Password  string `json:"password"`
	Phone     string `json:"phone"`
	FirstName string `json:"firstName"`
	LastName  string `json:"lastName"`
	ClinicID  int    `json:"clinicID"`
}

func HandleSignupDoctor(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Invalid request method", http.StatusMethodNotAllowed)
		return
	}

	var req SignupDoctorRequest
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

	var id int
	id, err = services.RegisterUser(db, tx, req.Email, req.Password, req.Phone, req.FirstName, req.LastName, "doctor")
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	_, err = tx.Exec("INSERT INTO Doctor (doctorID, doctorNO) VALUES ($1, $2)", id, req.Doctor)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	_, err = tx.Exec("INSERT INTO DoctorEmployment (doctorID, clinicID) VALUES ($1, $2)", id, req.ClinicID)
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

type TestOverview struct {
	TestID         int    `json:"testID"`
	PatientID      int    `json:"patientID"`
	PatientName    string `json:"patientName"`
	TestDate       string `json:"testDate"`
	StageOneStatus int    `json:"stageOneStatus"`
	StageTwoStatus int    `json:"stageTwoStatus"`
}

func HandleGetDoctorTests(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		log.Println("[DEBUG] Method not allowed")
		return
	}

	cookie, err := r.Cookie("auth_token")
	if err != nil {
		http.Error(w, "No authentication cookie", http.StatusUnauthorized)
		log.Println("[DEBUG] No auth cookie")
		return
	}

	claims, err := auth.ValidateJWT(cookie.Value)
	if err != nil {
		http.Error(w, "Invalid token", http.StatusUnauthorized)
		return
	}

	query := `
        SELECT t.testID, t.patientID, u.firstName, u.lastName, t.stageOneStatus, t.stageTwoStatus, tso.testDate
        FROM Test t
        JOIN Patient p ON t.patientID = p.patientID
        JOIN Users u ON p.patientID = u.userID
        LEFT JOIN TestStageOne tso ON t.testID = tso.testID
        WHERE t.doctorID = $1
        ORDER BY tso.testDate DESC
    `

	rows, err := db.Query(query, claims.UserID)
	if err != nil {
		http.Error(w, "DB error: "+err.Error(), 500)
		return
	}
	defer rows.Close()

	var tests []TestOverview
	for rows.Next() {
		var t TestOverview
		var firstName, lastName sql.NullString
		var testDate sql.NullString

		if err := rows.Scan(
			&t.TestID,
			&t.PatientID,
			&firstName,
			&lastName,
			&t.StageOneStatus,
			&t.StageTwoStatus,
			&testDate,
		); err != nil {
			log.Println("[DEBUG] rows.Scan error:", err)
			continue
		}

		t.PatientName = ""
		if firstName.Valid {
			t.PatientName += firstName.String
		}
		if lastName.Valid {
			if t.PatientName != "" {
				t.PatientName += " "
			}
			t.PatientName += lastName.String
		}

		t.TestDate = ""
		if testDate.Valid {
			t.TestDate = testDate.String
		}

		tests = append(tests, t)
	}

	w.Header().Set("Content-Type", "application/json")
	if err := json.NewEncoder(w).Encode(tests); err != nil {
		log.Printf("[DEBUG] JSON encode error: %v\n", err)
	} else {
		log.Printf("[DEBUG] Successfully encoded %d tests\n", len(tests))
	}
}

func HandleGetDoctorsPatients(w http.ResponseWriter, r *http.Request) {
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

	rows, err := db.Query(`
		SELECT patientID, doctorID, firstName, lastName, phone, eircode 
		FROM Patient
		INNER JOIN Users ON Patient.patientID = Users.userID 
		WHERE doctorID = $1
		`, id)
	if err != nil {
		http.Error(w, "failed to query patients", http.StatusInternalServerError)
		return
	}
	defer rows.Close()

	var patients []Patient

	for rows.Next() {
		var p Patient
		if err := rows.Scan(&p.PatientID, &p.DoctorID, &p.FirstName, &p.LastName, &p.Phone, &p.Eircode); err != nil {
			http.Error(w, "failed to scan patient", http.StatusInternalServerError)
			return
		}
		patients = append(patients, p)
	}

	if err := rows.Err(); err != nil {
		http.Error(w, "error reading patients", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(patients)
}
