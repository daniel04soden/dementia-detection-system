package handlers


type GenericPatient struct {
	ID int `"json:ID"`


}

// func PatientList ( w http.ResponseWriter, r *http.Request ) {


// 	rows, err := db.Query("SELECT * FROM Users WHERE doctorID == NULL")
// 	if err != nil {
// 		http.Error(w, "Can't fetch patients", http.StatusBadRequest)
// 	}
// 
// 
// }
