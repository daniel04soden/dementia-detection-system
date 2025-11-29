package services

import (
	"database/sql"
	"errors"
	"regexp"

	"golang.org/x/crypto/bcrypt"
)

func IsValidEmail(email string) bool {
	re := regexp.MustCompile(`^[^@]+@[^@]+\.[^@]+$`)
	return re.MatchString(email)
}

func HashPassword(password string) (string, error) {
	bytes, err := bcrypt.GenerateFromPassword([]byte(password), 14)
	return string(bytes), err
}

func RegisterUser(db *sql.DB, tx *sql.Tx, email, password, phone, firstName, lastName string, role string) (int, error) {
	if !IsValidEmail(email) {
		return -1, errors.New("invalid email format")
	}

	var count int
	queryer := db.QueryRow
	if tx != nil {
		queryer = tx.QueryRow
	}

	err := queryer("SELECT COUNT(*) FROM Account WHERE email = $1", email).Scan(&count)
	if err != nil {
		return -1, err
	}
	if count > 0 {
		return -1, errors.New("email already taken")
	}

	hash, err := HashPassword(password)
	if err != nil {
		return -1, err
	}

	var accountID int
	if tx != nil {
		err = tx.QueryRow("INSERT INTO Account (email, password, role) VALUES ($1, $2, $3) RETURNING id", email, hash, role).Scan(&accountID)
		if err != nil {
			return -1, err
		}
		_, err = tx.Exec("INSERT INTO Users (userID, phone, firstName, lastName) VALUES ($1, $2, $3, $4)", accountID, phone, firstName, lastName)
		return accountID, err
	}

	return accountID, err
}

func LoginUser(db *sql.DB, email, password string) (int, error) {
	if !IsValidEmail(email) {
		return -1, errors.New("invalid email format")
	}

	var storedHash string
	var accountID int
	err := db.QueryRow("SELECT ID, password FROM Account WHERE email = $1", email).Scan(&accountID, &storedHash)
	if err == sql.ErrNoRows {
		return -1, errors.New("user not found")
	}
	if err != nil {
		return -1, err
	}

	err = bcrypt.CompareHashAndPassword([]byte(storedHash), []byte(password))
	if err != nil {
		return -1, errors.New("incorrect password")
	}

	return accountID, nil
}

func GetUserByID(db *sql.DB, id int) {

}
