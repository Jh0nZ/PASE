package utils

import (
	"regexp"
)

// ValidateEmail checks if the provided email is valid.
func ValidateEmail(email string) bool {
	const emailRegex = `^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$`
	re := regexp.MustCompile(emailRegex)
	return re.MatchString(email)
}

// ValidateUsername checks if the provided username is valid.
func ValidateUsername(username string) bool {
	return len(username) >= 3 && len(username) <= 20
}

// ValidatePassword checks if the provided password meets the criteria.
func ValidatePassword(password string) bool {
	return len(password) >= 8
}