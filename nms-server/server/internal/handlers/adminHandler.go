package handlers

import (
	"encoding/json"
	"net/http"
	"strconv"

	"nms-server/server/internal/services"
)

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

	// Check if the email is valid
	if !services.IsValidEmail(req.Email) {
		http.Error(w, "Invalid email format", http.StatusBadRequest)
		return
	}

	// Start a transaction
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

	// Register the user
	_, err = services.RegisterUser(db, tx, req.Email, req.Password, req.Phone, req.FirstName, req.LastName, "admin")
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	// Commit the transaction
	if err := tx.Commit(); err != nil {
		http.Error(w, "Failed to commit transaction", http.StatusInternalServerError)
		return
	}

	// Respond with success
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)

	json.NewEncoder(w).Encode(map[string]any{
		"message": "Admin user created successfully",
	})
}

// -------------------------------------------------------------- GAVIN ADDED ----------------------------------------------------

// --------------------
// Structs
// --------------------

type AdminPatientInsert struct {
	Email     string `json:"email"`
	Password  string `json:"password"`
	PatientID int    `json:"patientID"`
	FirstName string `json:"firstName"`
	LastName  string `json:"lastName"`
	Phone     string `json:"phone"`
	Eircode   string `json:"eircode"`
	ClinicID  int    `json:"clinicID"`
}

type AdminPatientUpdate struct {
	Email     string `json:"email"`
	Password  string `json:"password"`
	PatientID int    `json:"patientID"`
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
	DoctorID  int      `json:"doctorID"`
	Email     string   `json:"email"`
	Password  string   `json:"password"`
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
	Password  string   `json:"password"`
	FirstName string   `json:"firstName"`
	LastName  string   `json:"lastName"`
	Phone     string   `json:"phone"`
	DoctorNo  string   `json:"doctorNo"`
	Clinics   []string `json:"clinics"`
	Approved  bool     `json:"approved"`
}

// --------------------
// GET Endpoints
// --------------------

func HandleAdminGetPatients(w http.ResponseWriter, r *http.Request) {
	rows, err := db.Query(`
        SELECT p.patientID, u.firstName, u.lastName, u.phone, p.eircode, p.doctorID
        FROM Patient p
        JOIN Users u ON p.patientID = u.userID
		JOIN Account a ON u.userID = a.ID
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

	json.NewEncoder(w).Encode(patients)
}

func HandleAdminGetDoctors(w http.ResponseWriter, r *http.Request) {
	rows, err := db.Query(`
        SELECT d.doctorID, a.email, u.firstName, u.lastName, u.phone, d.doctorNO, d.approved
        FROM Doctor d
        JOIN Users u ON d.doctorID = u.userID
		JOIN Account a ON u.userID = a.ID
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

		clinicRows, _ := db.Query(`
            SELECT name FROM Clinic
            JOIN DoctorEmployment ON Clinic.clinicID = DoctorEmployment.clinicID
            WHERE DoctorEmployment.doctorID = $1
        `, d.DoctorID)

		for clinicRows.Next() {
			var cname string
			clinicRows.Scan(&cname)
			d.Clinics = append(d.Clinics, cname)
		}
		clinicRows.Close()

		doctors = append(doctors, d)
	}

	json.NewEncoder(w).Encode(doctors)
}

// --------------------
// CREATE Endpoints
// --------------------

func HandleAdminCreateClinic(w http.ResponseWriter, r *http.Request) {
	var req Clinic
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", 400)
		return
	}

	err := db.QueryRow(`
        INSERT INTO Clinic (name, phone, county, eircode)
        VALUES ($1, $2, $3, $4)
        RETURNING clinicID
    `, req.Name, req.Phone, req.County, req.Eircode).Scan(&req.ClinicID)
	if err != nil {
		http.Error(w, "Failed to create clinic", 500)
		return
	}

	json.NewEncoder(w).Encode(req)
}

func HandleAdminCreatePatient(w http.ResponseWriter, r *http.Request) {
	var req AdminPatientInsert
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", 400)
		return
	}

	// Create account
	var newID int
	tx, err := db.Begin()
	err = tx.QueryRow(`
        INSERT INTO Account (email, password, role)
        VALUES ($1, $2, "patient")
        RETURNING ID
    `, req.Email, req.Password).Scan(&newID)
	if err != nil {
		http.Error(w, "Failed to create account", 500)
		return
	}

	// Create user
	_, err = tx.Exec(`
        INSERT INTO Users (userID, firstName, lastName, phone)
        VALUES ($1, $2, $3, $4)
    `, newID, req.FirstName, req.LastName, req.Phone)
	if err != nil {
		http.Error(w, "Failed to create user", 500)
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

	// Create patient
	_, err = tx.Exec(`
        INSERT INTO Patient (patientID, doctorID, eircode)
        VALUES ($1, $2, $3)
    `, newID, doctorID, req.Eircode)
	if err != nil {
		http.Error(w, "Failed to create patient", 500)
		return
	}

	if err := tx.Commit(); err != nil {
		http.Error(w, "Failed to commit transaction", http.StatusInternalServerError)
		return
	}

	req.PatientID = newID
	json.NewEncoder(w).Encode(req)
}

func HandleAdminCreateDoctor(w http.ResponseWriter, r *http.Request) {
	var req AdminDoctorInsert
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", 400)
		return
	}

	// Create account
	var newID int
	tx, err := db.Begin()
	err = tx.QueryRow(`
        INSERT INTO Account (email, password, role)
        VALUES ($1, $2, "doctor")
        RETURNING ID
    `, req.Email, req.Password).Scan(&newID)
	if err != nil {
		http.Error(w, "Failed to create account", 500)
		return
	}

	// Create user
	_, err = db.Exec(`
        INSERT INTO Users (userID, firstName, lastName, phone)
        VALUES ($1, $2, $3, $4)
    `, newID, req.FirstName, req.LastName, req.Phone)
	if err != nil {
		http.Error(w, "Failed to create user", 500)
		return
	}

	// Create doctor
	_, err = db.Exec(`
        INSERT INTO Doctor (doctorID, doctorNO)
        VALUES ($1, $2)
    `, newID, req.DoctorNo)
	if err != nil {
		http.Error(w, "Failed to create doctor", 500)
		return
	}

	// Assign clinics
	for _, cname := range req.Clinics {
		var cid int
		db.QueryRow(`SELECT clinicID FROM Clinic WHERE name=$1`, cname).Scan(&cid)
		if cid != 0 {
			db.Exec(`
                INSERT INTO DoctorEmployment (doctorID, clinicID)
                VALUES ($1, $2)
            `, newID, cid)
		}
	}

	req.DoctorID = newID
	json.NewEncoder(w).Encode(req)
}

// --------------------
// DELETE Endpoints
// --------------------

func HandleAdminDeleteClinic(w http.ResponseWriter, r *http.Request) {
	id := r.URL.Query().Get("id")
	_, err := db.Exec(`DELETE FROM Clinic WHERE clinicID = $1`, id)
	if err != nil {
		http.Error(w, "Failed to delete clinic", 500)
		return
	}
	json.NewEncoder(w).Encode(map[string]string{"message": "Clinic deleted"})
}

func HandleAdminDeleteUser(w http.ResponseWriter, r *http.Request) {
	id := r.URL.Query().Get("id")
	_, err := db.Exec(`DELETE FROM Account WHERE ID = $1`, id)
	if err != nil {
		http.Error(w, "Failed to delete user", 500)
		return
	}
	json.NewEncoder(w).Encode(map[string]string{"message": "User deleted"})
}

// --------------------
// UPDATE Endpoints
// --------------------

func HandleAdminUpdateClinic(w http.ResponseWriter, r *http.Request) {
	// Get clinicID from query string (similar to how you did it in HandleGetClinic)
	idStr := r.URL.Query().Get("id")
	if idStr == "" {
		http.Error(w, "id is required", http.StatusBadRequest)
		return
	}

	clinicID, err := strconv.Atoi(idStr)
	if err != nil {
		http.Error(w, "invalid id", http.StatusBadRequest)
		return
	}

	// Decode the request body into the Clinic struct
	var req Clinic

	err = json.NewDecoder(r.Body).Decode(&req)
	if err != nil {
		http.Error(w, "Invalid input", http.StatusBadRequest)
		return
	}

	// Now update the clinic in the database
	_, err = db.Exec(`
        UPDATE Clinic
        SET name = $1, phone = $2, county = $3, eircode = $4, 
        WHERE clinicID = $5`,
		req.Name, req.Phone, req.County, req.Eircode, clinicID)
	if err != nil {
		http.Error(w, "Failed to update clinic", http.StatusInternalServerError)
		return
	}

	// Return the updated clinic data
	json.NewEncoder(w).Encode(req)
}

func HandleAdminUpdatePatient(w http.ResponseWriter, r *http.Request) {
	// Get patientID from query string
	idStr := r.URL.Query().Get("id")
	if idStr == "" {
		http.Error(w, "id is required", http.StatusBadRequest)
		return
	}

	patientID, err := strconv.Atoi(idStr)
	if err != nil {
		http.Error(w, "invalid id", http.StatusBadRequest)
		return
	}

	// Decode the request body into AdminPatient struct
	var req AdminPatientUpdate
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON in request body", http.StatusBadRequest)
		return
	}

	tx, err := db.Begin()
	// Update Users table for the patient
	_, err = tx.Exec(`
        UPDATE Users
        SET firstName=$1, lastName=$2, phone=$3
        WHERE userID=$4`, req.FirstName, req.LastName, req.Phone, patientID)
	if err != nil {
		http.Error(w, "Failed to update user data", http.StatusInternalServerError)
		return
	}

	// Update Patient table with doctorID and eircodetx
	_, err = tx.Exec(`
        UPDATE Patient
        SET doctorID=$1, eircode=$2
        WHERE patientID=$3`, req.DoctorID, req.Eircode, patientID)
	if err != nil {
		http.Error(w, "Failed to update patient data", http.StatusInternalServerError)
		return
	}

	if err := tx.Commit(); err != nil {
		http.Error(w, "Failed to commit transaction", http.StatusInternalServerError)
		return
	}

	// Respond with updated patient data
	json.NewEncoder(w).Encode(req)
}

func HandleAdminUpdateDoctor(w http.ResponseWriter, r *http.Request) {
	// Get doctorID from query string
	idStr := r.URL.Query().Get("id")
	if idStr == "" {
		http.Error(w, "id is required", http.StatusBadRequest)
		return
	}

	doctorID, err := strconv.Atoi(idStr)
	if err != nil {
		http.Error(w, "invalid id", http.StatusBadRequest)
		return
	}

	// Decode the request body into AdminDoctor struct
	var req AdminDoctorInsert
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON in request body", http.StatusBadRequest)
		return
	}

	// Update Users table for the doctor
	tx, err := db.Begin()

	_, err = tx.Exec(`
        UPDATE Account
        SET email=$1
        WHERE ID=$2`, req.Email, req.DoctorID)
	if err != nil {
		http.Error(w, "Failed to update user data", http.StatusInternalServerError)
		return
	}

	_, err = tx.Exec(`
        UPDATE Users
        SET firstName=$1, lastName=$2, phone=$3
        WHERE userID=$4`, req.FirstName, req.LastName, req.Phone, doctorID)
	if err != nil {
		http.Error(w, "Failed to update user data", http.StatusInternalServerError)
		return
	}

	// Update Doctor table with doctor number
	_, err = tx.Exec(`
        UPDATE Doctor
        SET doctorNO=$1, approved=$2
        WHERE doctorID=$2`, req.DoctorNo, doctorID, req.Approved)
	if err != nil {
		http.Error(w, "Failed to update doctor data", http.StatusInternalServerError)
		return
	}

	// Delete old clinic assignments for the doctor
	_, err = tx.Exec(`
        DELETE FROM DoctorEmployment WHERE doctorID=$1`, doctorID)
	if err != nil {
		http.Error(w, "Failed to remove old clinic assignments", http.StatusInternalServerError)
		return
	}

	// Assign doctor to new clinics (if any)
	for _, cname := range req.Clinics {
		var clinicID int
		// Find clinic by name
		err := tx.QueryRow(`SELECT clinicID FROM Clinic WHERE name=$1`, cname).Scan(&clinicID)
		if err != nil || clinicID == 0 {
			http.Error(w, "Failed to find clinic: "+cname, http.StatusInternalServerError)
			return
		}

		// Insert new doctor-clinic association
		_, err = tx.Exec(`
                INSERT INTO DoctorEmployment(doctorID, clinicID)
                VALUES ($1, $2)`, doctorID, clinicID)
		if err != nil {
			http.Error(w, "Failed to assign doctor to clinic", http.StatusInternalServerError)
			return
		}
	}

	if err := tx.Commit(); err != nil {
		http.Error(w, "Failed to commit transaction", http.StatusInternalServerError)
		return
	}

	// Respond with updated doctor data
	json.NewEncoder(w).Encode(req)
}

func HandleAdminApproveDoctor(w http.ResponseWriter, r *http.Request) {
	// Get doctorID from query string
	idStr := r.URL.Query().Get("id")
	if idStr == "" {
		http.Error(w, "id is required", http.StatusBadRequest)
		return
	}

	doctorID, err := strconv.Atoi(idStr)
	if err != nil {
		http.Error(w, "invalid id", http.StatusBadRequest)
		return
	}

	_, err = db.Exec(`
				UPDATE Doctor SET
					approved=$1,
				WHERE doctorID=$2
			`, true, doctorID)
	if err != nil {
		http.Error(w, "DB update error: "+err.Error(), http.StatusInternalServerError)
		return
	}
}
