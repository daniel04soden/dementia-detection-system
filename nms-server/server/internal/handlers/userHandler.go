
package handlers 

import (
	"net/http"
	"encoding/json"
	"nms-server/server/internal/services"
	"nms-server/server/internal/auth"
	"time"
)

type SignupDoctorRequest struct {
	Email     string `json:"email"`
	Doctor    string `json:"doctor"`
	Password  string `json:"password"`
	Phone     string `json:"phone"`
	FirstName string `json:"first_name"`
	LastName  string `json:"last_name"`
	Clinic    string `json:"clinic"`
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

	err, id := services.RegisterUser(db, req.Email, req.Password, req.Phone, req.FirstName, req.LastName)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	var clinicID int
	err = db.QueryRow("SELECT clinicID FROM Clinic WHERE name == ($1)", req.Clinic).Scan(&clinicID)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	_, err = db.Exec("INSERT INTO Doctor (id, doctor) VALUES ($1, $2)",id, req.Doctor)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	_, err = db.Exec("INSERT INTO DoctorEmployment (id, clinicID) VALUES ($1, $2)",id, clinicID)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}


	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)

	json.NewEncoder(w).Encode(map[string]any{
		"message":  "success",
		"doctorID": id,
	})
}



type SignupPatientRequest struct {
	Email     string `json:"email"`
	Eircode   string `json:"eircode"`
	Password  string `json:"password"`
	Phone     string `json:"phone"`
	FirstName string `json:"first_name"`
	LastName  string `json:"last_name"`
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

	err, id := services.RegisterUser(db, req.Email, req.Password, req.Phone, req.FirstName, req.LastName)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	_, err = db.Exec("INSERT INTO Patient (id, eircode,) VALUES ($1, $2)",id, req.Eircode)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}



	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)

	json.NewEncoder(w).Encode(map[string]any{
		"message":  "success",
		"patientID": id,
		"patientName": req.FirstName,
	})
}

type LoginRequest struct {
	Email     string `json:"email"`
	Password  string `json:"password"`
}



func HandleLoginDoctor (w http.ResponseWriter, r *http.Request) {

	if r.Method != http.MethodPost {
		http.Error(w, "Invalid request method", http.StatusMethodNotAllowed)
		return
	}

	var req LoginRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON body", http.StatusBadRequest)
		return
	}

	defer r.Body.Close()

	id, err := services.LoginUser(db, req.Email, req.Password)
	if err != nil {
		http.Error(w, err.Error(), http.StatusUnauthorized)
		return
	}

	token, err := auth.GenerateJWT(id, "false")
	if err != nil {
		http.Error(w, "Failed to generate token", http.StatusInternalServerError)
		return
	}

	http.SetCookie(w, &http.Cookie{
		Name:     "auth_token",
		Value:    token,
		Path:     "/",
		HttpOnly: true,               // prevent JS access
		Secure:   false,               // HTTPS only
		SameSite: http.SameSiteLaxMode, // safe default
		Expires:  time.Now().Add(7 * 24 * time.Hour), // 7 days
	})

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]string{
		"message": "login successful",
	})

}


func HandleLoginPatient(w http.ResponseWriter, r *http.Request) {
    if r.Method != http.MethodPost {
        http.Error(w, "Invalid request method", http.StatusMethodNotAllowed)
        return
    }

    var req LoginRequest
    if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
        http.Error(w, "Invalid JSON body", http.StatusBadRequest)
        return
    }
    defer r.Body.Close()

    userID, err := services.LoginUser(db, req.Email, req.Password)
    if err != nil {
        http.Error(w, "Invalid email or password", http.StatusUnauthorized)
        return
    }

    token, err := auth.GenerateJWT(userID, "patient")
    if err != nil {
        http.Error(w, "Failed to generate token", http.StatusInternalServerError)
        return
    }

    w.Header().Set("Content-Type", "application/json")
    json.NewEncoder(w).Encode(map[string]string{
        "message": "login successful",
        "token":   token,
    })
}

func HandleWebLogout(w http.ResponseWriter, r *http.Request) {
	http.SetCookie(w, &http.Cookie{
		Name:     "auth_token",
		Value:    "",
		Path:     "/",
		Expires:  time.Now().Add(-time.Hour),
		HttpOnly: true,
		Secure:   true,
		SameSite: http.SameSiteLaxMode,
	})
	w.WriteHeader(http.StatusOK)
}

