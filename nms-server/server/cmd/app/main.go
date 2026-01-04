package main

import (
	"database/sql"
	"fmt"
	"log"
	"net/http"
	"os"
	"path/filepath"
	"strings"
	"time"

	"nms-server/server/internal/handlers"

	_ "github.com/lib/pq"
)

func spaHandler(staticPath, indexPath string) http.Handler {
	fs := http.FileServer(http.Dir(staticPath))

	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if strings.HasPrefix(r.URL.Path, "/api/") {
			http.NotFound(w, r)
			return
		}

		path := filepath.Join(staticPath, r.URL.Path)

		if _, err := os.Stat(path); os.IsNotExist(err) {
			http.ServeFile(w, r, filepath.Join(staticPath, indexPath))
			return
		}

		fs.ServeHTTP(w, r)
	})
}

var (
	host     = os.Getenv("DB_HOST")
	port     = os.Getenv("DB_PORT")
	user     = os.Getenv("DB_USER")
	password = os.Getenv("DB_PASSWORD")
	dbname   = os.Getenv("DB_NAME")
)

var dbInstance *sql.DB

func main() {
	psqlInfo := fmt.Sprintf("host=%s port=%s user=%s "+
		"password=%s dbname=%s sslmode=disable",
		host, port, user, password, dbname)

	db, err := sql.Open("postgres", psqlInfo)
	if err != nil {
		panic(err)
	}

	for i := 0; i < 10; i++ {
		err = db.Ping()
		if err == nil {
			break
		}
		log.Println("Waiting for database...")
		time.Sleep(2 * time.Second)
	}
	if err != nil {
		panic(err)
	}

	dbInstance = db
	defer dbInstance.Close()

	handlers.Init(dbInstance)

	// ------------------------------------------------------------------------------------------------------
	//									HANDLERS
	// ------------------------------------------------------------------------------------------------------

	// Payment

	http.HandleFunc("POST /api/mobile/payment", handlers.HandleCreatePayment)
	http.HandleFunc("POST /api/stripe/webhook", handlers.HandleStripeWebhook)

	// news
	http.HandleFunc("GET /api/web/news", handlers.HandleGetDoctorNews)
	http.HandleFunc("GET /api/mobile/news", handlers.HandleGetPatientNews)

	// logging in and auth criteria
	http.HandleFunc("POST /api/login", handlers.HandleLogin)
	http.HandleFunc("GET /api/web/me", handlers.WebHandleMe)
	http.HandleFunc("POST /api/web/logout", handlers.WebHandleLogout)
	http.HandleFunc("GET /api/mobile/me", handlers.MobileHandleMe)

	// testing
	http.HandleFunc("GET /api/web/stageone/review", handlers.HandleGetTestStageOne)
	http.HandleFunc("GET /api/web/stagetwo/review", handlers.HandleGetTestStageTwo)

	http.HandleFunc("POST /api/mobile/stageone/insert", handlers.HandleInsertStageOne)
	http.HandleFunc("POST /api/mobile/stagetwo/insert", handlers.HandleInsertStageTwo)

	http.HandleFunc("POST /api/web/stageone/grade", handlers.HandleGradeStageOne)

	http.HandleFunc("POST /api/lifestyle/insert", handlers.HandleInsertLifestyle)
	http.HandleFunc("GET /api/lifestyle/review", handlers.HandleGetLifestyle)
	http.HandleFunc("POST /api/lifestyle/update", handlers.HandleDoctorReviewLifestyle)
	http.HandleFunc("POST /api/lifestyle/ai/review", handlers.HandleAIReviewLifestyle)
	http.HandleFunc("POST /api/lifestyle/confirm", handlers.HandleDoctorConfirmLifestyle)

	// testing - // AssemblyAI / Speech
	http.HandleFunc("POST /api/assembly/webhook", handlers.HandleAssemblyWebHook)
	http.HandleFunc("POST /api/mobile/uploadaudio", handlers.HandleUpload)

	// Reviews
	http.HandleFunc("POST /api/review/insert", handlers.HandleInsertReview)
	http.HandleFunc("POST /api/review/update", handlers.HandleUpdateReview)
	http.HandleFunc("POST /api/patient/reviews", handlers.HandleGetPatientReviews)
	http.HandleFunc("GET /api/reviews", handlers.HandleGetAllReviews)

	// Support Tickets
	http.HandleFunc("POST /api/ticket/insert", handlers.HandleInsertTicket)
	http.HandleFunc("POST /api/ticket/update", handlers.HandleUpdateTicket)
	http.HandleFunc("POST /api/patient/tickets", handlers.HandleGetPatientTickets)
	http.HandleFunc("GET /api/tickets", handlers.HandleGetAllTickets)

	// Patient
	http.HandleFunc("POST /api/mobile/signup", handlers.HandleSignupPatient)
	http.HandleFunc("GET /api/patients", handlers.HandleGetAllPatients)
	http.HandleFunc("GET /api/patient", handlers.HandleGetPatient)
	http.HandleFunc("GET /api/patient/results", handlers.HandleGetPatientTestStatus)
	http.HandleFunc("GET /api/patient/clinic", handlers.HandleGetPatientClinic)

	// Doctor
	http.HandleFunc("POST /api/web/signup", handlers.HandleSignupDoctor)
	http.HandleFunc("GET /api/doctor/tests", handlers.HandleGetDoctorTests)
	http.HandleFunc("GET /api/doctor/patients", handlers.HandleGetDoctorsPatients)

	// Clinics
	http.HandleFunc("GET /api/clinics", handlers.HandleGetAllClinics)
	http.HandleFunc("GET /api/clinic", handlers.HandleGetClinic)
	http.HandleFunc("GET /api/clinics/county", handlers.HandleGetCountyClinics)

	// Admin

	http.HandleFunc("POST /api/admin/signup", handlers.HandleSignupAdmin)
	http.HandleFunc("POST /api/admin/approve", handlers.HandleAdminApproveDoctor)
	http.HandleFunc("/api/admin/clinics", func(w http.ResponseWriter, r *http.Request) {
		switch r.Method {
		case http.MethodPost:
			handlers.HandleAdminCreateClinic(w, r)
		case http.MethodPut:
			handlers.HandleAdminUpdateClinic(w, r)
		case http.MethodDelete:
			handlers.HandleAdminDeleteClinic(w, r)
		default:
			http.Error(w, "Method Not Allowed", 405)
		}
	})
	http.HandleFunc("/api/admin/patients", func(w http.ResponseWriter, r *http.Request) {
		switch r.Method {
		case http.MethodGet:
			handlers.HandleAdminGetPatients(w, r)
		case http.MethodPost:
			handlers.HandleAdminCreatePatient(w, r)
		case http.MethodPut:
			handlers.HandleAdminUpdatePatient(w, r)
		case http.MethodDelete:
			handlers.HandleAdminDeleteUser(w, r)

		default:
			http.Error(w, "Method Not Allowed", 405)
		}
	})
	http.HandleFunc("/api/admin/doctors", func(w http.ResponseWriter, r *http.Request) {
		switch r.Method {
		case http.MethodGet:
			handlers.HandleAdminGetDoctors(w, r)
		case http.MethodPost:
			handlers.HandleAdminCreateDoctor(w, r)
		case http.MethodPut:
			handlers.HandleAdminUpdateDoctor(w, r)
		case http.MethodDelete:
			handlers.HandleAdminDeleteUser(w, r)

		default:
			http.Error(w, "Method Not Allowed", 405)
		}
	})

	// ------------------------------------------------------------------------------------------------------

	http.Handle("/", spaHandler("frontend", "index.html"))

	http.HandleFunc("/test", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("Hello, world!"))
	})

	//	http.HandleFunc("GET /api/testmodel", handlers.AiAnalyseHandler)

	port := ":8080"
	log.Printf("Server running at http://localhost%s/", port)
	if err := http.ListenAndServe(port, nil); err != nil {
		log.Fatal(err)
	}
}
