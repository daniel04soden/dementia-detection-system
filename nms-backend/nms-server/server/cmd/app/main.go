
package main
import (
	"log"
	"net/http"
	"os"
	"path/filepath"
	"server/internal/handlers"
)

func main() {
	distDir := "../../../../nms-web/my-react-app/dist"

	absDist, err := filepath.Abs(distDir)
	if err != nil {
		log.Fatal("Failed to resolve dist folder:", err)
	}

	if _, err := os.Stat(absDist); os.IsNotExist(err) {
		log.Fatalf("Dist folder not found at %s", absDist)
	}

	fs := http.FileServer(http.Dir(absDist))

	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		path := filepath.Join(absDist, r.URL.Path)
		if _, err := os.Stat(path); err == nil {
			fs.ServeHTTP(w, r)
			return
		}
		http.ServeFile(w, r, filepath.Join(absDist, "index.html"))
	})

	http.Handle("api/signup", handlers.handleSignup)
	http.Handle("api/signup", handlers.handleLogin)
// TODO
//	http.Handle("api/tests", )
//	http.Handle("api/approvtest", )
//	http.Handle("api/admin", )

	port := ":8000"
	log.Printf("Server running at http://localhost%s/", port)
	log.Printf("Serving files from %s", absDist)
	if err := http.ListenAndServe(port, nil); err != nil {
		log.Fatal(err)
	}
}


