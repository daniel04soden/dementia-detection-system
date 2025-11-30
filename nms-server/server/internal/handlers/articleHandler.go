package handlers

import (
	"net/http"
)

func HandleGetDoctorNews(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	http.ServeFile(w, r, "./articles/doctor/article.json")
}

func HandleGetPatientNews(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	http.ServeFile(w, r, "./articles/patient/article.json")
}
