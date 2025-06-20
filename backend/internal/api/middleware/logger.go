package middleware

import (
	"github.com/gofiber/fiber/v2"
	"log"
	"time"
)

// LoggerMiddleware logs the details of each incoming request
func LoggerMiddleware(c *fiber.Ctx) error {
	start := time.Now()

	// Process the request
	err := c.Next()

	// Log the request details
	log.Printf("Request: %s %s | Duration: %s | Status: %d",
		c.Method(),
		c.Path(),
		time.Since(start),
		c.Response().StatusCode(),
	)

	return err
}