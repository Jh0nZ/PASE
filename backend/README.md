# PASE Backend API

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
1. **Install go**
   ```sh
   winget install GoLang.Go -v 1.24.4
   ```

2. **Clone the repository**:
   ```
   git clone <repository-url>
   cd backend
   ```

3. **Install dependencies**:
   ```
   go mod tidy
   ```

4. **Set up environment variables**:
   Create a `.env` file in the root directory and define any necessary environment variables.

5. **Run the application**:
   ```
   go run cmd/server/main.go
   ```

6. **Access the API**:
   The server will start on `http://localhost:8080`.
