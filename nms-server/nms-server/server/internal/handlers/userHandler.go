package handlers

import (
	"encoding/json"
	"net/http"
	"nms-server/server/internal/auth"
	"nms-server/server/internal/services"
	"time"
)

type LoginRequest struct {
	Email    string `json:"email"`
	Password string `json:"password"`
}

func HandleLogin(w http.ResponseWriter, r *http.Request) {
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

	var role string
	err = db.QueryRow("SELECT role FROM Account WHERE ID = $1", id).Scan(&role)
	if err != nil {
		http.Error(w, "Failed to retrieve user role", http.StatusInternalServerError)
		return
	}

	// Generate JWT token for the user
	token, err := auth.GenerateJWT(id, role)
	if err != nil {
		http.Error(w, "Failed to generate token", http.StatusInternalServerError)
		return
	}

	if role != "patient" {
		http.SetCookie(w, &http.Cookie{
			Name:     "auth_token",
			Value:    token,
			Path:     "/",
			HttpOnly: true,                               // prevent JS access
			Secure:   false,                              // HTTPS only
			SameSite: http.SameSiteLaxMode,               // safe default
			Expires:  time.Now().Add(7 * 24 * time.Hour), // 7 days
		})
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(map[string]any{
			"message": "login successful",
			"ID":      id,
		})
	} else {
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(map[string]any{
			"message": "login successful",
			"token":   token,
			"ID":      id,
		})
	}
}

func WebHandleMe(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		w.WriteHeader(http.StatusMethodNotAllowed)
		json.NewEncoder(w).Encode(map[string]string{"error": "Method not allowed"})
		return
	}

	cookie, err := r.Cookie("auth_token")
	if err != nil {
		w.WriteHeader(http.StatusUnauthorized)
		json.NewEncoder(w).Encode(map[string]string{"error": "No authentication cookie"})
		return
	}

	claims, err := auth.ValidateJWT(cookie.Value)
	if err != nil {
		w.WriteHeader(http.StatusUnauthorized)
		json.NewEncoder(w).Encode(map[string]string{"error": "Invalid or expired token"})
		return
	}

	// Convert float64 to int for userID
	json.NewEncoder(w).Encode(map[string]any{
		"userID": claims.UserID,
		"role":   claims.Role,
	})
}

func WebHandleLogout(w http.ResponseWriter, r *http.Request) {
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
