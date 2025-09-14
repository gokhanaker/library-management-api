# Library Management API

A Library Management RESTful API that allows users to manage books, borrow books and return books. It is built with Java 17, Spring Boot and H2 in memory database.

## Features

- **User Management**: Registration, authentication, and role-based access control
- **Book Management**: Add, update, delete, and search books with pagination
- **Borrowing System**: Borrow and return books with availability tracking

## Technology Stack

- **Java Version**: 17
- **Backend**: Spring Boot 3.4.1
- **Database**: H2 (In-Memory)
- **Message Broker** Kafka
- **Build Tool**: Maven

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/gokhanaker/library-management-api
cd library-management-api
```

### 2. Build and Run the Application

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8090`

### 3. Start Kafka and Zookeeper (Required for Event Publishing)

Kafka is used for event-driven communication (e.g., publishing borrowing and returning book events). You can start Kafka and Zookeeper using Docker Compose:

```bash
docker-compose up -d
```

This will start both services and expose Kafka on port 9092. The application expects Kafka at `localhost:9092`.

### 4. Access Tools

- **API Documentation**: `http://localhost:8090/swagger-ui.html`
- **H2 Database Console**: `http://localhost:8090/h2-console`
  - JDBC URL: `jdbc:h2:mem:library_management_db`
  - Username: `librarian`
  - Password: `password`
- OpenAPI JSON: `http://localhost:8090/api-docs`

## API Endpoints

### Authentication

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token

### Books

- `GET /api/books` - Get all books (with pagination)
- `GET /api/books/{id}` - Get book by ID
- `GET /api/books/filter` - Filter books by criteria
- `POST /api/books/add` - Add a new book (Librarian/Admin only)
- `PUT /api/books/{id}` - Update book (Librarian/Admin only)
- `DELETE /api/books/{id}` - Delete book (Librarian/Admin only)

### Borrowings

- `POST /api/borrowings/borrow` - Borrow a book
- `POST /api/borrowings/return` - Return a book
- `GET /api/borrowings/user/{userId}` - Get user's borrowings
- `GET /api/borrowings/overdue` - Get overdue borrowings

## Monitoring

The application includes Spring Boot Actuator for monitoring:

- Health check: `http://localhost:8090/actuator/health`
- Metrics: `http://localhost:8090/actuator/metrics`
- Info: `http://localhost:8090/actuator/info`
