package handlers

import (
	"encoding/json"
	"net/http"
	"strconv"

	"nms-server/server/internal/services"
)

// --------------------------------------------------------------
// ADMIN SIGNUP
// --------------------------------------------------------------

type SignupAdminRequest struct {
	Email     string `json:"email"`
	Password  string `json:"password"`
	Phone     string `json:"phone"`
	FirstName string `json:"firstName"`
	LastName  string `json:"lastName"`
}

func HandleSignupAdmin(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Invalid request method", http.StatusMethodNotAllowed)
		return
	}

	var req SignupAdminRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON body", http.StatusBadRequest)
		return
	}

	if !services.IsValidEmail(req.Email) {
		http.Error(w, "Invalid email format", http.StatusBadRequest)
		return
	}

	tx, err := db.Begin()
	if err != nil {
		http.Error(w, "Failed to start transaction", http.StatusInternalServerError)
		return
	}
	defer tx.Rollback()

	_, err = services.RegisterUser(db, tx, req.Email, req.Password, req.Phone, req.FirstName, req.LastName, "admin")
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
		"message": "Admin user created successfully",
	})
}

// --------------------------------------------------------------
// STRUCTS
// --------------------------------------------------------------

type AdminPatientInsert struct {
	Email     string `json:"email"`
	Password  string `json:"password"`
	FirstName string `json:"firstName"`
	LastName  string `json:"lastName"`
	Phone     string `json:"phone"`
	Eircode   string `json:"eircode"`
	ClinicID  int    `json:"clinicID"`
}

type AdminPatientUpdate struct {
	Email     string `json:"email,omitempty"`
	Password  string `json:"password,omitempty"`
	FirstName string `json:"firstName"`
	LastName  string `json:"lastName"`
	Phone     string `json:"phone"`
	Eircode   string `json:"eircode"`
	DoctorID  int    `json:"doctorID"`
}

type AdminPatientResponse struct {
	PatientID int    `json:"patientID"`
	Email     string `json:"email"`
	FirstName string `json:"firstName"`
	LastName  string `json:"lastName"`
	Phone     string `json:"phone"`
	Eircode   string `json:"eircode"`
	DoctorID  int    `json:"doctorID"`
}

type AdminDoctorInsert struct {
	Email     string   `json:"email"`
	Password  string   `json:"password"`
	FirstName string   `json:"firstName"`
	LastName  string   `json:"lastName"`
	Phone     string   `json:"phone"`
	DoctorNo  string   `json:"doctorNo"`
	Clinics   []string `json:"clinics"`
	Approved  bool     `json:"approved"`
}

type AdminDoctorUpdate struct {
	Email     string   `json:"email,omitempty"`
	Password  string   `json:"password,omitempty"`
	FirstName string   `json:"firstName"`
	LastName  string   `json:"lastName"`
	Phone     string   `json:"phone"`
	DoctorNo  string   `json:"doctorNo"`
	Clinics   []string `json:"clinics"`
	Approved  bool     `json:"approved"`
}

type AdminDoctorResponse struct {
	DoctorID  int      `json:"doctorID"`
	Email     string   `json:"email"`
	FirstName string   `json:"firstName"`
	LastName  string   `json:"lastName"`
	Phone     string   `json:"phone"`
	DoctorNo  string   `json:"doctorNo"`
	Clinics   []string `json:"clinics"`
	Approved  bool     `json:"approved"`
}

// --------------------------------------------------------------
// GET PATIENTS
// --------------------------------------------------------------

func HandleAdminGetPatients(w http.ResponseWriter, r *http.Request) {
	rows, err := db.Query(`
        SELECT p.patientID, a.email, u.firstName, u.lastName, u.phone, p.eircode, p.doctorID
        FROM Patient p
        JOIN Users u ON p.patientID = u.userID
        JOIN Account a ON a.ID = u.userID
    `)
	if err != nil {
		http.Error(w, "Failed to query patients", 500)
		return
	}
	defer rows.Close()

	var patients []AdminPatientResponse

	for rows.Next() {
		var p AdminPatientResponse
		if err := rows.Scan(&p.PatientID, &p.Email, &p.FirstName, &p.LastName, &p.Phone, &p.Eircode, &p.DoctorID); err != nil {
			http.Error(w, "Failed to scan patient", 500)
			return
		}
		patients = append(patients, p)
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(patients)
}

// --------------------------------------------------------------
// GET DOCTORS
// --------------------------------------------------------------

func HandleAdminGetDoctors(w http.ResponseWriter, r *http.Request) {
	rows, err := db.Query(`
        SELECT d.doctorID, a.email, u.firstName, u.lastName, u.phone, d.doctorNO, d.approved
        FROM Doctor d
        JOIN Users u ON d.doctorID = u.userID
        JOIN Account a ON a.ID = d.doctorID
    `)
	if err != nil {
		http.Error(w, "Failed to query doctors", 500)
		return
	}
	defer rows.Close()

	var doctors []AdminDoctorResponse

	for rows.Next() {
		var d AdminDoctorResponse
		if err := rows.Scan(&d.DoctorID, &d.Email, &d.FirstName, &d.LastName, &d.Phone, &d.DoctorNo, &d.Approved); err != nil {
			http.Error(w, "Failed to scan doctor", 500)
			return
		}

		// Fetch clinics
		clinicRows, err := db.Query(`
            SELECT c.name
            FROM Clinic c
            JOIN DoctorEmployment de ON de.clinicID = c.clinicID
            WHERE de.doctorID = $1
        `, d.DoctorID)
		if err != nil {
			http.Error(w, "Failed to load clinics", 500)
			return
		}

		for clinicRows.Next() {
			var cname string
			if err := clinicRows.Scan(&cname); err != nil {
				clinicRows.Close()
				http.Error(w, "Failed scanning clinic", 500)
				return
			}
			d.Clinics = append(d.Clinics, cname)
		}
		clinicRows.Close()

		doctors = append(doctors, d)
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(doctors)
}

// --------------------------------------------------------------
// CREATE CLINIC
// --------------------------------------------------------------

func HandleAdminCreateClinic(w http.ResponseWriter, r *http.Request) {
	var req Clinic

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", 400)
		return
	}

	if err := db.QueryRow(`
        INSERT INTO Clinic (name, phone, county, eircode)
        VALUES ($1, $2, $3, $4)
        RETURNING clinicID
    `, req.Name, req.Phone, req.County, req.Eircode).Scan(&req.ClinicID); err != nil {
		http.Error(w, "Failed to create clinic", 500)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(req)
}

// --------------------------------------------------------------
// CREATE PATIENT
// --------------------------------------------------------------

func HandleAdminCreatePatient(w http.ResponseWriter, r *http.Request) {
	var req AdminPatientInsert

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", 400)
		return
	}

	tx, err := db.Begin()
	if err != nil {
		http.Error(w, "Failed to begin transaction", 500)
		return
	}
	defer tx.Rollback()

	// Hashing password
	hashedPass, _ := services.HashPassword(req.Password)

	// Insert account
	var newID int
	if err := tx.QueryRow(`
        INSERT INTO Account (email, password, role)
        VALUES ($1, $2, 'patient')
        RETURNING ID
    `, req.Email, hashedPass).Scan(&newID); err != nil {
		http.Error(w, "Failed to create account", 500)
		return
	}

	// Insert Users
	if _, err := tx.Exec(`
        INSERT INTO Users (userID, firstName, lastName, phone)
        VALUES ($1, $2, $3, $4)
    `, newID, req.FirstName, req.LastName, req.Phone); err != nil {
		http.Error(w, "Failed to create user", 500)
		return
	}

	// Assign doctor
	var doctorID int
	if err := tx.QueryRow(`
        SELECT de.doctorID
        FROM DoctorEmployment de
        JOIN Doctor d ON d.doctorID = de.doctorID
        LEFT JOIN Patient p ON p.doctorID = d.doctorID
        WHERE de.clinicID = $1 AND d.approved = TRUE
        GROUP BY de.doctorID
        ORDER BY COUNT(p.patientID) ASC
        LIMIT 1
    `, req.ClinicID).Scan(&doctorID); err != nil {
		http.Error(w, "No available doctors", 400)
		return
	}

	if _, err := tx.Exec(`
        INSERT INTO Patient (patientID, doctorID, eircode)
        VALUES ($1, $2, $3)
    `, newID, doctorID, req.Eircode); err != nil {
		http.Error(w, "Failed to create patient", 500)
		return
	}

	if err := tx.Commit(); err != nil {
		http.Error(w, "Commit failed", 500)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]any{"success": true, "patientID": newID})
}

// --------------------------------------------------------------
// CREATE DOCTOR
// --------------------------------------------------------------

func HandleAdminCreateDoctor(w http.ResponseWriter, r *http.Request) {
	var req AdminDoctorInsert

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", 400)
		return
	}

	tx, err := db.Begin()
	if err != nil {
		http.Error(w, "Could not start transaction", 500)
		return
	}
	defer tx.Rollback()

	hashedPass, _ := services.HashPassword(req.Password)

	// Insert account
	var newID int
	if err := tx.QueryRow(`
        INSERT INTO Account (email, password, role)
        VALUES ($1, $2, 'doctor')
        RETURNING ID
    `, req.Email, hashedPass).Scan(&newID); err != nil {
		http.Error(w, "Failed to create account", 500)
		return
	}

	// Insert Users
	if _, err := tx.Exec(`
        INSERT INTO Users (userID, firstName, lastName, phone)
        VALUES ($1, $2, $3, $4)
    `, newID, req.FirstName, req.LastName, req.Phone); err != nil {
		http.Error(w, "Failed to create user", 500)
		return
	}

	// Insert doctor
	if _, err := tx.Exec(`
        INSERT INTO Doctor (doctorID, doctorNO, approved)
        VALUES ($1, $2, $3)
    `, newID, req.DoctorNo, req.Approved); err != nil {
		http.Error(w, "Failed to create doctor", 500)
		return
	}

	// Assign clinics
	for _, cname := range req.Clinics {
		var clinicID int
		if err := tx.QueryRow(`SELECT clinicID FROM Clinic WHERE name=$1`, cname).Scan(&clinicID); err != nil {
			http.Error(w, "Clinic not found: "+cname, 400)
			return
		}

		if _, err := tx.Exec(`
            INSERT INTO DoctorEmployment (doctorID, clinicID)
            VALUES ($1, $2)
        `, newID, clinicID); err != nil {
			http.Error(w, "Failed clinic assignment", 500)
			return
		}
	}

	if err := tx.Commit(); err != nil {
		http.Error(w, "Commit failed", 500)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]any{"success": true, "doctorID": newID})
}

// --------------------------------------------------------------
// DELETE CLINIC
// --------------------------------------------------------------

func HandleAdminDeleteClinic(w http.ResponseWriter, r *http.Request) {
	id := r.URL.Query().Get("id")

	if _, err := db.Exec(`DELETE FROM Clinic WHERE clinicID = $1`, id); err != nil {
		http.Error(w, "Failed to delete clinic", 500)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]string{"message": "Clinic deleted"})
}

// --------------------------------------------------------------
// DELETE USER (Account Cascades)
// --------------------------------------------------------------

func HandleAdminDeleteUser(w http.ResponseWriter, r *http.Request) {
	id := r.URL.Query().Get("id")

	if _, err := db.Exec(`DELETE FROM Account WHERE ID = $1`, id); err != nil {
		http.Error(w, "Failed to delete user", 500)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]string{"message": "User deleted"})
}

// --------------------------------------------------------------
// UPDATE CLINIC
// --------------------------------------------------------------

func HandleAdminUpdateClinic(w http.ResponseWriter, r *http.Request) {
	idStr := r.URL.Query().Get("id")
	id, _ := strconv.Atoi(idStr)

	var req Clinic
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid input", 400)
		return
	}

	if _, err := db.Exec(`
        UPDATE Clinic
        SET name=$1, phone=$2, county=$3, eircode=$4
        WHERE clinicID=$5
    `, req.Name, req.Phone, req.County, req.Eircode, id); err != nil {
		http.Error(w, "Failed to update clinic", 500)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(req)
}

// --------------------------------------------------------------
// UPDATE PATIENT
// --------------------------------------------------------------

func HandleAdminUpdatePatient(w http.ResponseWriter, r *http.Request) {
	idStr := r.URL.Query().Get("id")
	patientID, _ := strconv.Atoi(idStr)

	var req AdminPatientUpdate
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", 400)
		return
	}

	tx, err := db.Begin()
	if err != nil {
		http.Error(w, "Failed to start transaction", 500)
		return
	}
	defer tx.Rollback()

	// Update Users
	if _, err := tx.Exec(`
        UPDATE Users
        SET firstName=$1, lastName=$2, phone=$3
        WHERE userID=$4
    `, req.FirstName, req.LastName, req.Phone, patientID); err != nil {
		http.Error(w, "Failed user update", 500)
		return
	}

	// Update Patient
	if _, err := tx.Exec(`
        UPDATE Patient
        SET doctorID=$1, eircode=$2
        WHERE patientID=$3
    `, req.DoctorID, req.Eircode, patientID); err != nil {
		http.Error(w, "Failed patient update", 500)
		return
	}

	// Update email if supplied
	if req.Email != "" {
		if _, err := tx.Exec(`
            UPDATE Account SET email=$1 WHERE ID=$2
        `, req.Email, patientID); err != nil {
			http.Error(w, "Failed email update", 500)
			return
		}
	}

	// Update password if supplied
	if req.Password != "" {
		hashed, _ := services.HashPassword(req.Password)
		if _, err := tx.Exec(`
            UPDATE Account SET password=$1 WHERE ID=$2
        `, hashed, patientID); err != nil {
			http.Error(w, "Failed password update", 500)
			return
		}
	}

	if err := tx.Commit(); err != nil {
		http.Error(w, "Commit failed", 500)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]any{"success": true})
}

// --------------------------------------------------------------
// UPDATE DOCTOR
// --------------------------------------------------------------

func HandleAdminUpdateDoctor(w http.ResponseWriter, r *http.Request) {
	idStr := r.URL.Query().Get("id")
	doctorID, _ := strconv.Atoi(idStr)

	var req AdminDoctorUpdate
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", 400)
		return
	}

	if len(req.Clinics) == 0 {
		http.Error(w, "Doctor must have at least one clinic", 400)
		return
	}

	tx, err := db.Begin()
	if err != nil {
		http.Error(w, "Failed to start transaction", 500)
		return
	}
	defer tx.Rollback()

	// Optional email update
	if req.Email != "" {
		if _, err := tx.Exec(`
            UPDATE Account SET email=$1 WHERE ID=$2
        `, req.Email, doctorID); err != nil {
			http.Error(w, "Email update failed", 500)
			return
		}
	}

	// Optional password update
	if req.Password != "" {
		hashed, _ := services.HashPassword(req.Password)
		if _, err := tx.Exec(`
            UPDATE Account SET password=$1 WHERE ID=$2
        `, hashed, doctorID); err != nil {
			http.Error(w, "Password update failed", 500)
			return
		}
	}

	// Users update
	if _, err := tx.Exec(`
        UPDATE Users
        SET firstName=$1, lastName=$2, phone=$3
        WHERE userID=$4
    `, req.FirstName, req.LastName, req.Phone, doctorID); err != nil {
		http.Error(w, "Users update failed", 500)
		return
	}

	// Doctor update
	if _, err := tx.Exec(`
        UPDATE Doctor
        SET doctorNO=$1, approved=$2
        WHERE doctorID=$3
    `, req.DoctorNo, req.Approved, doctorID); err != nil {
		http.Error(w, "Doctor update failed", 500)
		return
	}

	// Clear clinics
	if _, err := tx.Exec(`DELETE FROM DoctorEmployment WHERE doctorID=$1`, doctorID); err != nil {
		http.Error(w, "Failed clearing clinics", 500)
		return
	}

	// Reassign clinics
	for _, cname := range req.Clinics {
		var clinicID int
		if err := tx.QueryRow(`SELECT clinicID FROM Clinic WHERE name=$1`, cname).Scan(&clinicID); err != nil {
			http.Error(w, "Clinic not found: "+cname, 400)
			return
		}
		if _, err := tx.Exec(`
            INSERT INTO DoctorEmployment (doctorID, clinicID)
            VALUES ($1, $2)
        `, doctorID, clinicID); err != nil {
			http.Error(w, "Failed assigning clinic", 500)
			return
		}
	}

	if err := tx.Commit(); err != nil {
		http.Error(w, "Commit failed", 500)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]any{"success": true})
}

// --------------------------------------------------------------
// APPROVE DOCTOR
// --------------------------------------------------------------

func HandleAdminApproveDoctor(w http.ResponseWriter, r *http.Request) {
	idStr := r.URL.Query().Get("id")
	doctorID, _ := strconv.Atoi(idStr)

	if _, err := db.Exec(`
        UPDATE Doctor SET approved = TRUE WHERE doctorID = $1
    `, doctorID); err != nil {
		http.Error(w, "Approve failed", 500)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]any{"success": true})
}
