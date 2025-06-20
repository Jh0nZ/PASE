package services

import (
	"errors"
	"github.com/google/uuid"
	"github.com/Jh0nZ/PASE/backend/internal/models"
)

var users = make(map[uuid.UUID]models.User)

// CreateUserService creates a new user and returns the created user.
func CreateUserService(user models.User) (models.User, error) {
	if user.Name == "" || user.Email == "" {
		return models.User{}, errors.New("name and email are required")
	}
	user.ID = uuid.New()
	users[user.ID] = user
	return user, nil
}

// GetUserService retrieves a user by ID.
func GetUserService(id uuid.UUID) (models.User, error) {
	user, exists := users[id]
	if !exists {
		return models.User{}, errors.New("user not found")
	}
	return user, nil
}

// UpdateUserService updates an existing user.
func UpdateUserService(id uuid.UUID, updatedUser models.User) (models.User, error) {
	user, exists := users[id]
	if !exists {
		return models.User{}, errors.New("user not found")
	}
	if updatedUser.Name != "" {
		user.Name = updatedUser.Name
	}
	if updatedUser.Email != "" {
		user.Email = updatedUser.Email
	}
	users[id] = user
	return user, nil
}