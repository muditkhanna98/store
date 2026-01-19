# Store API

A Spring Boot application for a simple store management system.

## Technologies Used

*   **Java 17**
*   **Spring Boot**
*   **Spring Data JPA**
*   **MySQL**
*   **Flyway** (Database Migration)
*   **Lombok**
*   **MapStruct**
*   **Thymeleaf**

## Features

*   **User Management**: Retrieve user details.
*   **Product Management**: Browse products, filter by category, and view product details.
*   **Cart Management**: Create shopping carts and add items to them.
*   **Database Integration**: Uses MySQL for data persistence with Flyway for schema management.

## Getting Started

### Prerequisites

*   Java 17 or higher
*   Maven
*   MySQL Server

### Configuration

1.  Clone the repository.
2.  Open `src/main/resources/application.yml`.
3.  Update the `spring.datasource` properties (url, username, password) to match your MySQL installation.

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/store_api?createDatabaseIfNotExist=true
    username: your_username
    password: your_password
```

### Build and Run

Run the application using Maven:

```bash
./mvnw spring-boot:run
```

The application will start on port 8080 (default).

## API Endpoints

### Products

*   `GET /products`: Get all products.
    *   Query Param: `categoryId` (optional) - Filter products by category ID.
*   `GET /products/{id}`: Get a specific product by ID.

### Users

*   `GET /users/`: Get all users.
*   `GET /users/{id}`: Get a specific user by ID.

### Carts

*   `POST /carts`: Create a new shopping cart.
*   `POST /carts/{cartId}/items`: Add an item to a specific cart.
    *   Body: `{"productId": <id>}`

### Other

*   `GET /`: Home page (Thymeleaf template).
*   `GET /hello`: Returns a simple JSON message.
