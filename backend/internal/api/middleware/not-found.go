package middleware

import (
	"github.com/gofiber/fiber/v2"
)

func NotFound() fiber.Handler {
	return func(c *fiber.Ctx) error {
		return c.Status(404).SendString("Page not found")
	}
}

