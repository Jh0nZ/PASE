package utils

import (
	"github.com/gofiber/fiber/v2"
)

// APIResponse defines the structure for standard API responses
type APIResponse struct {
	Status  string      `json:"status"`
	Message string      `json:"message"`
	Data    interface{} `json:"data,omitempty"`
	Error   string      `json:"error,omitempty"`
}

// SendResponse sends a standardized API response
func SendResponse(c *fiber.Ctx, status int, message string, data interface{}) error {
	response := APIResponse{
		Status:  "success",
		Message: message,
		Data:    data,
	}
	return c.Status(status).JSON(response)
}

// SendError sends a standardized error response
func SendError(c *fiber.Ctx, status int, message string) error {
	response := APIResponse{
		Status: "error",
		Error:  message,
	}
	return c.Status(status).JSON(response)
}