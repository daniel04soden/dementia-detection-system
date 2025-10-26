
package main

import (
	"database/sql"
	"fmt"
	"net/http"
	"server/internal/services"
)

var db *sql.DB

func handleSignup(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Invalid request method", http.StatusMethodNotAllowed)
		return
	}

	if err := r.ParseForm(); err != nil {
		http.Error(w, "Unable to parse form", http.StatusBadRequest)
		return
	}

	email := r.FormValue("email")
	password := r.FormValue("password")
	doctorRegNo := r.FormValue("doctor_reg")
	clinic := r.FormValue("clinic")

	err := services.RegisterUser(db, email, password)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	fmt.Fprintf(w, "Signup successful for %s (RegNo: %s, Clinic: %s)", email, doctorRegNo, clinic)
}

func handleLogin(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Invalid request method", http.StatusMethodNotAllowed)
		return
	}

	if err := r.ParseForm(); err != nil {
		http.Error(w, "Unable to parse form", http.StatusBadRequest)
		return
	}

	email := r.FormValue("email")
	password := r.FormValue("password")

	err := services.LoginUser(db, email, password)
	if err != nil {
		http.Error(w, err.Error(), http.StatusUnauthorized)
		return
	}

	fmt.Fprintf(w, "Login successful! Welcome, %s", email)
}

