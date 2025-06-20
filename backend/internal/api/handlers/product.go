package handlers

import (
	"github.com/gofiber/fiber/v2"
	"github.com/google/uuid"
	"github.com/Jh0nZ/PASE/backend/internal/models"
	"net/http"
)

// CreateProduct handles the creation of a new product
func CreateProduct(c *fiber.Ctx) error {
	var product models.Product
	if err := c.BodyParser(&product); err != nil {
		return c.Status(http.StatusBadRequest).JSON(fiber.Map{"error": "Invalid input"})
	}
	product.ID = uuid.New()
	return c.Status(http.StatusCreated).JSON(product)
}

// GetProduct handles fetching a product by ID
func GetProduct(c *fiber.Ctx) error {
	id := c.Params("id")
	// Here you would typically fetch the product from a data source
	// For now, we return a mock product
	product := models.Product{ID: uuid.MustParse(id), Name: "Sample Product", Price: 10.0}
	return c.JSON(product)
}

// UpdateProduct handles updating an existing product
func UpdateProduct(c *fiber.Ctx) error {
	id := c.Params("id")
	var product models.Product
	if err := c.BodyParser(&product); err != nil {
		return c.Status(http.StatusBadRequest).JSON(fiber.Map{"error": "Invalid input"})
	}
	product.ID = uuid.MustParse(id)
	return c.JSON(product)
}

// ListProducts handles fetching all products
func ListProducts(c *fiber.Ctx) error {
	products := []models.Product{
		{ID: uuid.New(), Name: "Product 1", Price: 19.99},
		{ID: uuid.New(), Name: "Product 2", Price: 29.99},
	}
	return c.JSON(products)
}
