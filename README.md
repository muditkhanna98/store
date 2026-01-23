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
*   **Spring Security** (for password encoding)

## Features

*   **Authentication**: User login functionality.
*   **User Management**: Retrieve user details.
*   **Product Management**: Browse products, filter by category, and view product details.
*   **Cart Management**: Create shopping carts, add items, update quantities, remove items, and clear carts.
*   **Database Integration**: Uses MySQL for data persistence with Flyway for schema management.
*   **Validation**: Global exception handling for validation errors.

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

### Authentication

*   `POST /auth/login`: Authenticate a user.
    *   Body: `{"email": "user@example.com", "password": "password"}`
    *   Returns: `200 OK` on success, `401 Unauthorized` on failure.

### Products

*   `GET /products`: Get all products.
    *   Query Param: `categoryId` (optional) - Filter products by category ID.
*   `GET /products/{id}`: Get a specific product by ID.

### Users

*   `GET /users/`: Get all users.
*   `GET /users/{id}`: Get a specific user by ID.

### Carts

*   `POST /carts`: Create a new shopping cart.
*   `GET /carts/{cartId}`: Get a specific cart by ID.
*   `POST /carts/{cartId}/items`: Add an item to a specific cart.
    *   Body: `{"productId": <id>}`
*   `PUT /carts/{cartId}/items/{productId}`: Update the quantity of an item in the cart.
    *   Body: `{"quantity": <new_quantity>}`
*   `DELETE /carts/{cartId}/items/{productId}`: Remove an item from the cart.
*   `DELETE /carts/{cartId}`: Clear all items from the cart.

### Other

*   `GET /`: Home page (Thymeleaf template).
*   `GET /hello`: Returns a simple JSON message.

## Error Handling

Validation errors are handled globally and return a `400 Bad Request` with a map of field names and error messages.
