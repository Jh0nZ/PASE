package handlers

import (
	"github.com/gofiber/fiber/v2"
	"github.com/google/uuid"
	"github.com/Jh0nZ/PASE/backend/internal/models"
	"net/http"
)

// CreateUser handles the creation of a new user
func CreateUser(c *fiber.Ctx) error {
	var user models.User
	if err := c.BodyParser(&user); err != nil {
		return c.Status(http.StatusBadRequest).JSON(fiber.Map{"error": "Invalid input"})
	}
	user.ID = uuid.New()
	return c.Status(http.StatusCreated).JSON(user)
}

// GetUser handles fetching a user by ID
func GetUser(c *fiber.Ctx) error {
	id := c.Params("id")
	// Here you would typically fetch the user from a database or service
	user := models.User{ID: uuid.MustParse(id), Name: "John Doe", Email: "john@example.com"}
	return c.Status(http.StatusOK).JSON(user)
}

// UpdateUser handles updating an existing user
func UpdateUser(c *fiber.Ctx) error {
	id := c.Params("id")
	var user models.User
	if err := c.BodyParser(&user); err != nil {
		return c.Status(http.StatusBadRequest).JSON(fiber.Map{"error": "Invalid input"})
	}
	user.ID = uuid.MustParse(id)
	return c.Status(http.StatusOK).JSON(user)
}

// ListUsers handles fetching all users
func ListUsers(c *fiber.Ctx) error {
	users := []models.User{
		{ID: uuid.New(), Name: "John Doe", Email: "test@test.com"},
		{ID: uuid.New(), Name: "Jane Smith", Email: "jane@example.com"},
	}
	return c.Status(http.StatusOK).JSON(users)
}
