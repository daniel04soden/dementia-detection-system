package handlers

import (
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"os"
	"strconv"

	"github.com/stripe/stripe-go/v84"
	"github.com/stripe/stripe-go/v84/paymentlink"
	"github.com/stripe/stripe-go/v84/price"
	"github.com/stripe/stripe-go/v84/product"
	"github.com/stripe/stripe-go/v84/webhook"
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

	prod, err := product.New(&stripe.ProductParams{
		Name:        stripe.String("Dementia Detection System Premium Membership"),
		Description: stripe.String("Upgrading to premium allows for you to have your lifestyle questionare analysed by our specially trained AI model, to try detect if you have dementia. It also allows for you to conduct a speech test, where your speech will be analysed to see if you have dementia"),
		Metadata: map[string]string{
			"type": "premium",
		},
		Images: []*string{
			stripe.String("https://www.istockphoto.com/photo/cute-pig-leaning-on-railing-of-his-cot-gm140462837-3162997"),
		},
	})
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	priceObj, err := price.New(&stripe.PriceParams{
		Product:    stripe.String(prod.ID),
		UnitAmount: stripe.Int64(1000),
		Currency:   stripe.String(string(stripe.CurrencyEUR)),
	})
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	pl, err := paymentlink.New(&stripe.PaymentLinkParams{
		LineItems: []*stripe.PaymentLinkLineItemParams{
			{
				Price:    stripe.String(priceObj.ID),
				Quantity: stripe.Int64(1),
			},
		},
		Metadata: map[string]string{
			"patientID": strconv.Itoa(req.PatientID),
		},
	})
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	_, err = db.Exec(`
		INSERT INTO Payment (patientID, stripePaymentID, amount, status)
		VALUES ($1, $2, $3, 'pending')
		`, req.PatientID, pl.ID, 1000)
	if err != nil {
		http.Error(w, "Failed to add payment", http.StatusInternalServerError)
		return
	}

	fmt.Println(pl.URL)
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]string{
		"paymentURL": pl.URL,
	})
}

func HandleStripeWebhook(w http.ResponseWriter, r *http.Request) {
	payload, err := io.ReadAll(r.Body)
	if err != nil {
		fmt.Println("cant read body" + err.Error())
		http.Error(w, "cannot read body", http.StatusBadRequest)
		return
	}

	sig := r.Header.Get("Stripe-Signature")
	fmt.Println(sig)

	event, err := webhook.ConstructEvent(
		payload,
		sig,
		os.Getenv("WEBHOOK_KEY"),
	)
	if err != nil {

		fmt.Println("bad stripe sig" + err.Error())
		http.Error(w, "bad signature", http.StatusBadRequest)
		return
	}

	switch event.Type {
	case "checkout.session.completed":

		fmt.Println("does it even get here")
		var session stripe.CheckoutSession
		if err := json.Unmarshal(event.Data.Raw, &session); err != nil {

			fmt.Println("cant unmarshal session")
			w.WriteHeader(http.StatusOK)
			return
		}

		if session.PaymentStatus != stripe.CheckoutSessionPaymentStatusPaid {

			fmt.Println("not paid for")
			w.WriteHeader(http.StatusOK)
		}

		patientIDStr := session.Metadata["patientID"]
		patientID, err := strconv.Atoi(patientIDStr)
		if err != nil {

			fmt.Println("error converting" + err.Error())
			w.WriteHeader(http.StatusOK)
			return
		}

		if session.AmountTotal != 1000 {

			fmt.Println("wrong amount paid")
			w.WriteHeader(http.StatusOK)
			return
		}

		paymentIntentID := session.PaymentIntent.ID

		tx, err := db.Begin()
		if err != nil {
			w.WriteHeader(http.StatusOK)
			return
		}

		_, err = tx.Exec(`
			UPDATE Patient
			SET premium = true
			WHERE patientID = $1 AND premium = false
		`, patientID)
		if err != nil {
			fmt.Println(err.Error())
			return
		}

		_, err = tx.Exec(`
			UPDATE Payment
			SET status = 'succeeded'
			WHERE stripePaymentID = $1
		`, paymentIntentID)
		if err != nil {
			fmt.Println(err.Error())
			return
		}

		tx.Commit()
	}

	w.WriteHeader(http.StatusOK)
}
