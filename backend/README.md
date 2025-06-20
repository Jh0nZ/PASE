# PASE Backend API

## Overview
This project is a RESTful API built with Go using the Fiber framework. It is designed to be modular and scalable, allowing for easy expansion in the future. The API currently does not connect to a database but provides a solid foundation for user and product management.

## Project Structure
```
backend
├── cmd
│   └── server
│       └── main.go          # Entry point of the application
├── internal
│   ├── api
│   │   ├── handlers         # Contains handler functions for API endpoints
│   │   ├── middleware       # Contains middleware for request processing
│   │   └── routes           # Defines the application routes
│   ├── config               # Configuration management
│   ├── models               # Data models for the application
│   ├── services             # Business logic for user and product management
│   └── utils                # Utility functions for validation and response handling
├── pkg
│   └── errors               # Custom error handling
├── docs
│   └── api.md              # API documentation
├── .env                     # Environment variables
├── .gitignore               # Git ignore file
├── go.mod                   # Module definition
├── go.sum                   # Module checksums
└── README.md                # Project documentation
```

## Setup Instructions
1. **Clone the repository**:
   ```
   git clone <repository-url>
   cd backend
   ```

2. **Install dependencies**:
   ```
   go mod tidy
   ```

3. **Set up environment variables**:
   Create a `.env` file in the root directory and define any necessary environment variables.

4. **Run the application**:
   ```
   go run cmd/server/main.go
   ```

5. **Access the API**:
   The server will start on `http://localhost:8080`. You can use tools like Postman or curl to interact with the API.

## Usage
- **Health Check**: Check the status of the API.
- **User Management**: Create, retrieve, and update user information.
- **Product Management**: Create, retrieve, and update product information.