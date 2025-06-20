package main

import (
	"os"
	"github.com/Jh0nZ/PASE/backend/internal/api/routes"
	"github.com/gofiber/fiber/v2"
	"github.com/joho/godotenv"
	"log"
)

func main() {
	_ = godotenv.Load()

	app := fiber.New(fiber.Config{
		AppName: "PASE API",
	})

	routes.SetupRoutes(app)

	port := os.Getenv("PORT")
    if port == "" {
        port = "8080"
    }
	
    log.Printf("🚀 Servidor iniciado en puerto %s", port)
    log.Fatal(app.Listen(":" + port))
}
