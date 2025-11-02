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

func RegisterUser(db *sql.DB, email, password string) error {
    if !IsValidEmail(email) {
        return errors.New("invalid email format")
    }

    var count int
    err := db.QueryRow("SELECT COUNT(*) FROM users WHERE email = $1", email).Scan(&count)
    if err != nil {
        return err
    }
    if count > 0 {
        return errors.New("email already taken")
    }

	hash, _ := HashPassword(password)

    _, err = db.Exec("INSERT INTO users (email, password) VALUES ($1, $2)", email, hash)
    return err
}

func LoginUser(db *sql.DB, email, password string) error {
	if !IsValidEmail(email) {
		return errors.New("invalid email format")
	}

	var storedHash string
	err := db.QueryRow("SELECT password FROM users WHERE email = $1", email).Scan(&storedHash)
	if err == sql.ErrNoRows {
		return errors.New("user not found")
	}
	if err != nil {
		return err
	}

	err = bcrypt.CompareHashAndPassword([]byte(storedHash), []byte(password))
	if err != nil {
		return errors.New("incorrect password")
	}

	return nil 
}
