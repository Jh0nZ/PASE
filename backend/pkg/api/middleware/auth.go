package middleware

import (
	"github.com/gofiber/fiber/v2"
	"log"
	"strings"
)

// AuthMiddleware checks for a valid authentication token in the request headers.
func AuthMiddleware() fiber.Handler {
	return func(c *fiber.Ctx) error {
		authHeader := c.Get("Authorization")
		if authHeader == "" {
			return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map{"error": "Authorization header is missing"})
		}

		// Assuming the token is a Bearer token
		token := strings.TrimPrefix(authHeader, "Bearer ")
		if token == "" {
			return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map{"error": "Invalid token"})
		}

		// Here you would typically validate the token (e.g., check its signature, expiration, etc.)
		// For now, we will just log the token and allow the request to proceed.
		log.Printf("Authenticated token: %s", token)

		return c.Next()
	}
}