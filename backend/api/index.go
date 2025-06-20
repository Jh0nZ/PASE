package handler

import (
	"net/http"
	"os"

	"github.com/Jh0nZ/PASE/backend/pkg/api/routes"
	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/adaptor"
)

func Handler(w http.ResponseWriter, r *http.Request) {
	// Configurar variables de entorno para Vercel
	if os.Getenv("VERCEL") == "" {
		os.Setenv("VERCEL", "1")
	}

	app := fiber.New(fiber.Config{
		AppName: "PASE API",
	})

	routes.SetupRoutes(app)
	adaptor.FiberApp(app)(w, r)
}
