package handlers

import (
	"encoding/json"
	"io"
	"net/http"
	"os"
	"strconv"

	"github.com/stripe/stripe-go/v78"
	"github.com/stripe/stripe-go/v78/paymentintent"
	"github.com/stripe/stripe-go/v78/webhook"
)

type PatientPaymentRequest struct {
	PatientID int `json:"patientID"`
}

func HandleCreatePayment(w http.ResponseWriter, r *http.Request) {
	stripe.Key = os.Getenv("STRIPE_KEY")
	var req PatientPaymentRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "invalid request", http.StatusBadRequest)
		return
	}
	params := &stripe.PaymentIntentParams{
		Amount:   stripe.Int64(1000),
		Currency: stripe.String(string(stripe.CurrencyUSD)),
		Metadata: map[string]string{
			"patientID": strconv.Itoa(req.PatientID),
			"type":      "premium",
		},
		AutomaticPaymentMethods: &stripe.PaymentIntentAutomaticPaymentMethodsParams{
			Enabled: stripe.Bool(true),
		},
	}

	intent, err := paymentintent.New(params)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	db.Exec(`
		INSERT INTO Payments(patientID, stripeIntentID, amount, status)
		VALUES ($1, $2, $3)
		`, req.PatientID, intent.ID, 1000)

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]string{
		"clientSecret": intent.ClientSecret,
	})
}

func HandleStripeWebhook(w http.ResponseWriter, r *http.Request) {
	payload, err := io.ReadAll(r.Body)
	if err != nil {
		http.Error(w, "cannot read body", http.StatusBadRequest)
		return
	}

	sig := r.Header.Get("Stripe-Signature")

	event, err := webhook.ConstructEvent(
		payload,
		sig,
		os.Getenv("WEBHOOK_KEY"),
	)
	if err != nil {
		http.Error(w, "bad signature", 400)
		return
	}

	if event.Type == "payment_intent.succeeded" {
		var intent stripe.PaymentIntent
		json.Unmarshal(event.Data.Raw, &intent)

		patientID, err := strconv.Atoi(intent.Metadata["patientID"])
		if err != nil {
			w.WriteHeader(200)
			return
		}

		if intent.Amount != 1000 {
			w.WriteHeader(200)
			return
		}

		tx, _ := db.Begin()
		tx.Exec(`UPDATE patients SET premium = true WHERE patient_id=$1 AND premium=false`, patientID)
		tx.Exec(`UPDATE payments SET status='succeeded' WHERE stripe_intent_id=$1`, intent.ID)
		tx.Commit()
	}

	w.WriteHeader(http.StatusOK)
}
