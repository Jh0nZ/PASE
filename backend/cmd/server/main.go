package main

import (
	"github.com/gofiber/fiber/v2"
	"github.com/joho/godotenv"
	"log"
	"github.com/Jh0nZ/PASE/backend/internal/api/routes"
)

func main() {
	_ = godotenv.Load()

	app := fiber.New()

	routes.SetupRoutes(app)

	log.Printf("Servidor iniciado en puerto %s", "8080")
	log.Fatal(app.Listen(":8080"))
}