package handlers

import (
	"encoding/json"
	"net/http"
	"strconv"
)

type Clinic struct {
	ClinicID int    `json:"clinicID"`
	Name     string `json:"name"`
	Phone    string `json:"phone"`
	County   string `json:"county"`
	Eircode  string `json:"eircode"`
}

func HandleGetAllClinics(w http.ResponseWriter, r *http.Request) {
	rows, err := db.Query(`SELECT clinicID, name, phone, county, eircode FROM Clinic`)
	if err != nil {
		http.Error(w, "Failed to query clinics", http.StatusInternalServerError)
		return
	}
	var clinics []Clinic
	for rows.Next() {
		var c Clinic
		if err := rows.Scan(&c.ClinicID, &c.Name, &c.Phone, &c.County, &c.Eircode); err != nil {
			http.Error(w, "Failed to scan clinic", http.StatusInternalServerError)
			return
		}
		clinics = append(clinics, c)
	}
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(clinics)
}

func HandleGetClinic(w http.ResponseWriter, r *http.Request) {
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

	row := db.QueryRow(`SELECT clinicID, name, phone, county, eircode FROM Clinic WHERE clinicID = $1`, id)
	var c Clinic
	err = row.Scan(&c.ClinicID, &c.Name, &c.Phone, &c.County, &c.Eircode)
	if err != nil {
		http.Error(w, "Clinic not found", http.StatusNotFound)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(c)
}

func HandleGetCountyClinics(w http.ResponseWriter, r *http.Request) {
	county := r.URL.Query().Get("county")

	rows, err := db.Query(`SELECT clinicID, name, phone, county, eircode FROM Clinic WHERE county = $1`, county)
	if err != nil {
		http.Error(w, "Failed to query clinics", http.StatusInternalServerError)
		return
	}
	var clinics []Clinic
	for rows.Next() {
		var c Clinic
		if err := rows.Scan(&c.ClinicID, &c.Name, &c.Phone, &c.County, &c.Eircode); err != nil {
			http.Error(w, "Failed to scan clinic", http.StatusInternalServerError)
			return
		}
		clinics = append(clinics, c)
	}
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(clinics)
}

func HandleGetPatientClinic(w http.ResponseWriter, r *http.Request) {
	patientID := r.URL.Query().Get("id")
	if patientID == "" {
		http.Error(w, "id is required", http.StatusBadRequest)
		return
	}

	id, err := strconv.Atoi(patientID)
	if err != nil {
		http.Error(w, "invalid id", http.StatusBadRequest)
		return
	}

	var doctorID int
	err = db.QueryRow(`SELECT doctorID FROM Patient WHERE patientID = $1`, id).Scan(&doctorID)
	if err != nil {
		http.Error(w, "Patient not found or no associated doctor", http.StatusNotFound)
		return
	}

	var clinicID int
	err = db.QueryRow(`SELECT clinicID FROM DoctorEmployment WHERE doctorID = $1`, doctorID).Scan(&clinicID)
	if err != nil {
		http.Error(w, "Doctor not employed at any clinic", http.StatusNotFound)
		return
	}

	var clinic Clinic
	err = db.QueryRow(`SELECT clinicID, name, phone, county, eircode FROM Clinic WHERE clinicID = $1`, clinicID).Scan(&clinic.ClinicID, &clinic.Name, &clinic.Phone, &clinic.County, &clinic.Eircode)
	if err != nil {
		http.Error(w, "Clinic not found", http.StatusNotFound)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(clinic)
}
