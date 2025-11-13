package main
import (
	"log"
	"os"
	"fmt"
	"net/http"
	"database/sql"
	"nms-server/server/internal/handlers"
    _ "github.com/lib/pq"

	
)
var (
  host     =  os.Getenv("DB_HOST")
  port     =  os.Getenv("DB_PORT")
  user     =  os.Getenv("DB_USER")
  password =  os.Getenv("DB_PASSWORD")
  dbname   = os.Getenv("DB_Name")
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

	err = db.Ping()
	if err != nil {
	  panic(err)
	}

	dbInstance = db
	defer dbInstance.Close()

	handlers.Init(dbInstance)

	// web
	http.HandleFunc("/api/web/signup", handlers.HandleSignupDoctor)
	http.HandleFunc("/api/web/login", handlers.HandleLoginDoctor)
	http.HandleFunc("/api/web/logout", handlers.HandleWebLogout)

	// mobile
	http.HandleFunc("api/mobile/login", handlers.HandleLoginPatient)
	http.HandleFunc("api/mobile/signup", handlers.HandleSignupPatient)

//	http.Handle("api/web/dashbord", handlers. )


// TODO
//	http.Handle("api/tests", )
//	http.Handle("api/approvtest", )
//	http.Handle("api/admin", )

	port := ":8000"
	log.Printf("Server running at http://localhost%s/", port)
	if err := http.ListenAndServe(port, nil); err != nil {
		log.Fatal(err)
	}
}


