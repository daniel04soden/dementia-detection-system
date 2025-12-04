package handlers

import (
	"encoding/json"
	"net/http"
	"strconv"
	"time"
)

type TicketsInsert struct {
	Details int `json:"details"`
}

func HandleInsertTicket(w http.ResponseWriter, r *http.Request) {
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

	var req TicketsInsert
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON body", http.StatusBadRequest)
		return
	}

	date := time.Now().Format("02/01/2006")

	_, err = db.Exec(`
		INSERT INTO Tickets (
			dateOpened, dateUpdated, patientID, priority, status, workaround, solution, details
		) 
		VALUES ($1, $2, $3, $4, $5, $6, $7)
	`, date, "", id, 0, 1, "", "", req.Details)

	if err != nil {
		http.Error(w, "invalid insert", http.StatusBadRequest)
		return
	}

	json.NewEncoder(w).Encode(map[string]any{
		"message": "success",
	})
}

type TicketResponse struct {
	TicketID    int    `json:"ticketID"`
	DateOpened  string `json:"dateOpened"`
	DateUpdated string `json:"dateUpdated"`
	PatientID   int    `json:"patientID"`
	Priority    int    `json:"priority"`
	Status      int    `json:"status"`
	Workaround  string `json:"workaround"`
	Solution    string `json:"solution"`
	Details     string `json:"details"`
}

func HandleGetPatientTickets(w http.ResponseWriter, r *http.Request) {
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
		SELECT ticketID, date, patientID, priority, status, workaround, solution, details
		FROM Ticket
		WHERE patientID = $1
		`, id)
	if err != nil {
		http.Error(w, "Failed to query tickets", http.StatusInternalServerError)
		return
	}
	defer rows.Close()

	var tickets []TicketResponse

	for rows.Next() {
		var t TicketResponse
		if err := rows.Scan(&t.TicketID, &t.DateOpened, &t.DateUpdated, &t.PatientID, &t.Priority, &t.Status, &t.Workaround, &t.Solution, &t.Details); err != nil {
			http.Error(w, "Failed to scan ticket", http.StatusInternalServerError)
			return
		}
		tickets = append(tickets, t)
	}

	if err := rows.Err(); err != nil {
		http.Error(w, "Error reading ticket data", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(tickets)
}

func HandleGetAllTickets(w http.ResponseWriter, r *http.Request) {
	rows, err := db.Query(`
		SELECT ticketID, date, patientID, priority, status, workaround, solution, details
		FROM Ticket
		`)
	if err != nil {
		http.Error(w, "Failed to query tickets", http.StatusInternalServerError)
		return
	}
	defer rows.Close()

	var tickets []TicketResponse

	for rows.Next() {
		var t TicketResponse
		if err := rows.Scan(&t.TicketID, &t.DateOpened, &t.DateUpdated, &t.PatientID, &t.Priority, &t.Status, &t.Workaround, &t.Solution, &t.Details); err != nil {
			http.Error(w, "Failed to scan ticket", http.StatusInternalServerError)
			return
		}
		tickets = append(tickets, t)
	}

	if err := rows.Err(); err != nil {
		http.Error(w, "Error reading ticket data", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(tickets)
}

type TicketUpdate struct {
	Priority   int    `json:"priority"`
	Status     int    `json:"status"`
	Workaround string `json:"workaround"`
	Solution   string `json:"solution"`
}

func HandleUpdateTicket(w http.ResponseWriter, r *http.Request) {
	// Get the ID from the URL parameters
	id := r.URL.Query().Get("id")

	// Check if the ID is valid
	if id == "" {
		http.Error(w, "Invalid ID", http.StatusBadRequest)
		return
	}

	// Parse the request body as JSON
	var ticket TicketUpdate
	if err := json.NewDecoder(r.Body).Decode(&ticket); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	date := time.Now().Format("02/01/2006")

	// Update the ticket in the database
	_, err := db.Exec(`UPDATE Ticket SET dateUpdated = ?, priority = ?, status = ?, workaround = ?, solution = ? WHERE ticketID = ?`, date, ticket.Priority, ticket.Status, ticket.Workaround, ticket.Solution, id)
	if err != nil {
		http.Error(w, "Failed to update ticket", http.StatusInternalServerError)
		return
	}

	// Return the updated ticket as JSON
	json.NewEncoder(w).Encode(ticket)
}
