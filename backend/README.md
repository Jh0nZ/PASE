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
│   └── api.md               # API documentation
├── .env                     # Environment variables
├── .gitignore               # Git ignore file
├── .air.toml                # Air configuration for hot reload
├── go.mod                   # Module definition
├── go.sum                   # Module checksums
└── README.md                # Project documentation
```

## Setup Instructions

### Prerequisites
1. **Install Go**
   ```sh
   winget install GoLang.Go -v 1.24.4
   ```

2. **Install Air for hot reload**
   ```sh
   go install github.com/air-verse/air@latest
   ```

### Installation
1. **Clone the repository**:
   ```sh
   git clone https://github.com/Jh0nZ/PASE.git
   cd backend
   ```

2. **Install dependencies**:
   ```sh
   go mod tidy
   ```

3. **Set up environment variables**:
   Create a `.env` file in the root directory and define any necessary environment variables.

### Running the Application

#### Development (with hot reload)
```sh
air
```

#### Production
```sh
go run cmd/server/main.go
```

### Access
The server will start on `http://localhost:8080`.

## Development Features
- **Hot Reload**: Air automatically rebuilds and restarts the server when files change
- **Live Development**: No need to manually restart the server during development
