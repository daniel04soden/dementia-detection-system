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

func RegisterUser(db *sql.DB, email, password string, phone string, firstName string, lastName string) (error, int){
    if !IsValidEmail(email) {
        return errors.New("invalid email format"), -1
    }

    var count int
    err := db.QueryRow("SELECT COUNT(*) FROM users WHERE email = $1", email).Scan(&count)
    if err != nil {
        return err, -1
    }
    if count > 0 {
        return errors.New("email already taken"), -1
    }

	hash, _ := HashPassword(password)

	var accountID int
	err = db.QueryRow("Insert INTO Account (email, password) VALUES ($1, $2) RETURNING id", email, hash).Scan(&accountID)
	if err != nil {
		return err, -1
	}

    _, err = db.Exec("INSERT INTO Account (id, email, password) VALUES ($1, $2, $3)", accountID, email, hash)
	if err != nil {
		return err, -1
	}
    _, err = db.Exec("INSERT INTO users (id, phone, firstName, lastName) VALUES ($1, $2, $3, $4)", accountID, phone, firstName, lastName)
    return err, 1
}

func LoginUser(db *sql.DB, email, password string) (int, error) {
	if !IsValidEmail(email) {
		return -1, errors.New("invalid email format")
	}

	var storedHash string; var accountID int;
	err := db.QueryRow("SELECT userID, password FROM Users WHERE email = $1", email).Scan(&accountID, &storedHash)
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
