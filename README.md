# Shortly - URL Shortening API

Shortly is a URL shortening service built with Java Spring Boot, Spring Security, and JWT Authentication. It provides a simple API to shorten long URLs and manage them using a secure, authenticated backend.

## Features

- **User Registration**: Create your account and manage your links
- **Authentication**: Secure access to your short links
- **URL Shortening**: Converts long URLs into shortened versions
- **View User's URLs**: Retrieve all URLs created by a user
- **Redirect Shortened URLs**: Access the original URL from a shortened URL

## Tech Stack

- Java Spring Boot
- Spring Security
- JWT (JSON Web Token) Authentication
- MySQL Database (or any other relational database for storage)

## API Endpoints

### Authentication

- **POST** `/api/public/auth/login`
    - Logs in a user with `username` and `password`.
    - **Request Body**:
      ```json
      {
        "username": "user",
        "password": "password"
      }
      ```
    - **Response**:
      ```json
      {
        "token": "jwt_token_here"
      }
      ```

- **POST** `/api/public/auth/register`
    - Registers a new user with `username`, `email`, and `password`.
    - **Request Body**:
      ```json
      {
        "username": "newuser",
        "email": "newuser@example.com",
        "password": "password"
      }
      ```
    - **Response**:
      ```json
      {
        "message": "User registered successfully"
      }
      ```

### URL Management

- **POST** `/api/urls/shorten`
    - Shortens a given long URL.
    - **Request Body**:
      ```json
      {
        "originalUrl": "https://www.example.com/long-url"
      }
      ```
    - **Response**:
      ```json
      {
        "shortUrl": "short.ly/abc123"
      }
      ```

- **GET** `/api/urls/myurls`
    - Retrieves all the URLs shortened by the authenticated user.
    - **Headers**: `Authorization: Bearer <jwt_token>`
    - **Response**:
      ```json
      [
        {
          "originalUrl": "https://www.example.com/long-url",
          "shortUrl": "short.ly/abc123",
          "createdDate": "2023-10-01T12:00:00",
          "clickCount": 5
        }
      ]
      ```

### URL Redirection

- **GET** `/r/{shortUrl}`
    - Redirects the user to the original long URL using the shortened URL.
    - Example:
        - If `shortUrl` is `abc123`, it will redirect the user to the original URL `https://www.example.com/long-url`.

## Running the Application

### Prerequisites

- Java 21 or higher
- Maven
- MySQL (or any other relational database)
- IDE (e.g., IntelliJ IDEA or VS Code) or terminal

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/shortly.git
cd shortly
```

### 2. Set Up the Database

- Create a database in MySQL (or another relational database).
- Update `application.properties` or `application.yml` with your database connection details:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/shortly
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
```

### 3. Build the Project

Run the following Maven command to build the project:

```bash
mvn clean install
```

### 4. Run the Application

Run the Spring Boot application using Maven:

```bash
mvn spring-boot:run
```

This will start the application on `http://localhost:8080`.

## Authentication

To access the **/api/urls/myurls** endpoint or other protected routes, you'll need to first log in to obtain a JWT token.

1. **Login**: Send a `POST` request to `/api/auth/login` with `username` and `password` to receive a JWT token.

2. **Use the Token**: For routes that require authentication (e.g., `/api/urls/myurls`), include the JWT token in the `Authorization` header:

```bash
Authorization: Bearer <jwt_token>
```

## Notes

- JWT tokens expire after a set period of time (can be configured in the backend).
- Make sure to replace `localhost:8080` with your backend server's actual URL if deploying to production.
