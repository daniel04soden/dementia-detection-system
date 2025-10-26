package model

type User struct {
	id string
	email string
	password string
}

type Patient struct {
	user User
	patient_id string
	first_name string
	last_name string
	phone_number string
	eircode string

}

type Doctor struct {
	doctor_id string
	firstName string
	lastName string
	phoneNumber string
	eircode string

}
