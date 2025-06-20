package routes

import (
	"github.com/gofiber/fiber/v2"
	"github.com/Jh0nZ/PASE/backend/internal/api/handlers"
	"github.com/Jh0nZ/PASE/backend/internal/api/middleware"
)

func SetupRoutes(app *fiber.App) {
	//app.Use(middleware.CORSMiddleware())
	app.Use(middleware.LoggerMiddleware)

	app.Get("/health", handlers.HealthCheck)

	userGroup := app.Group("/users")
	userGroup.Post("/", handlers.CreateUser)
	userGroup.Get("/", handlers.ListUsers)
	userGroup.Get("/:id", handlers.GetUser)
	userGroup.Put("/:id", handlers.UpdateUser)

	productGroup := app.Group("/products")
	productGroup.Post("/", handlers.CreateProduct)
	productGroup.Get("/", handlers.ListProducts)
	productGroup.Get("/:id", handlers.GetProduct)
	productGroup.Put("/:id", handlers.UpdateProduct)

	app.Use(middleware.NotFound())
}