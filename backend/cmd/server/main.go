package main

import (
	"net/http"
	"os"
	"log"
	"github.com/Jh0nZ/PASE/backend/internal/api/routes"
	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/adaptor"
	"github.com/joho/godotenv"
)

func main() {
	// Cargar variables de entorno solo en desarrollo
	if os.Getenv("VERCEL") == "" {
		_ = godotenv.Load()
	}

	app := createApp()

	// Determinar si estamos en Vercel
	if os.Getenv("VERCEL") != "" {
		// En Vercel, usar el handler HTTP
		return
	}

	// En desarrollo local
	port := os.Getenv("PORT")
	if port == "" {
		port = "8080"
	}

	log.Printf("🚀 Servidor iniciado en puerto %s", port)
	log.Fatal(app.Listen(":" + port))
}

func createApp() *fiber.App {
	app := fiber.New(fiber.Config{
		AppName: "PASE API",
	})

	routes.SetupRoutes(app)
	return app
}

// Handler para Vercel
func Handler(w http.ResponseWriter, r *http.Request) {
	app := createApp()
	adaptor.FiberApp(app)(w, r)
}

// VercelHandler es el punto de entrada para Vercel
func VercelHandler(w http.ResponseWriter, r *http.Request) {
	Handler(w, r)
}
