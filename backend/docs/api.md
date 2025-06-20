# API Documentation

## Overview
This document provides details about the REST API endpoints available in the application. The API is designed to be modular and scalable, allowing for future enhancements and integrations.

## Base URL
```
http://localhost:8080
```

## Endpoints

### Health Check
- **GET** `/health`
  - **Description**: Checks the health status of the API.
  - **Response**:
    - **200 OK**: Returns a simple status message.
      ```json
      {
        "status": "OK"
      }
      ```

### User Management

#### Create User
- **POST** `/users`
  - **Description**: Creates a new user.
  - **Request Body**:
    ```json
    {
      "username": "string",
      "email": "string",
      "password": "string"
    }
    ```
  - **Response**:
    - **201 Created**: Returns the created user object.
    - **400 Bad Request**: Returns validation errors.

#### Get User
- **GET** `/users/{id}`
  - **Description**: Retrieves a user by ID.
  - **Response**:
    - **200 OK**: Returns the user object.
    - **404 Not Found**: User not found.

#### Update User
- **PUT** `/users/{id}`
  - **Description**: Updates an existing user.
  - **Request Body**:
    ```json
    {
      "username": "string",
      "email": "string"
    }
    ```
  - **Response**:
    - **200 OK**: Returns the updated user object.
    - **400 Bad Request**: Returns validation errors.
    - **404 Not Found**: User not found.

### Product Management

#### Create Product
- **POST** `/products`
  - **Description**: Creates a new product.
  - **Request Body**:
    ```json
    {
      "name": "string",
      "description": "string",
      "price": "number"
    }
    ```
  - **Response**:
    - **201 Created**: Returns the created product object.
    - **400 Bad Request**: Returns validation errors.

#### Get Product
- **GET** `/products/{id}`
  - **Description**: Retrieves a product by ID.
  - **Response**:
    - **200 OK**: Returns the product object.
    - **404 Not Found**: Product not found.

#### Update Product
- **PUT** `/products/{id}`
  - **Description**: Updates an existing product.
  - **Request Body**:
    ```json
    {
      "name": "string",
      "description": "string",
      "price": "number"
    }
    ```
  - **Response**:
    - **200 OK**: Returns the updated product object.
    - **400 Bad Request**: Returns validation errors.
    - **404 Not Found**: Product not found.

## Error Responses
All endpoints will return standardized error responses in the following format:
```json
{
  "error": "string"
}
```

## Usage Examples
### Health Check Example
```bash
curl -X GET http://localhost:8080/health
```

### Create User Example
```bash
curl -X POST http://localhost:8080/users -H "Content-Type: application/json" -d '{"username": "john_doe", "email": "john@example.com", "password": "securepassword"}'
```

### Create Product Example
```bash
curl -X POST http://localhost:8080/products -H "Content-Type: application/json" -d '{"name": "Sample Product", "description": "This is a sample product.", "price": 19.99}'
```
