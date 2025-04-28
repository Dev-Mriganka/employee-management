# Employee Management System

A Spring Boot-based application for managing employees and departments in an organization. This system provides a comprehensive solution for human resources management with secure authentication, role-based access control, and RESTful APIs.

## Table of Contents
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Authentication](#authentication)
- [Database](#database)
- [Security](#security)
- [Testing](#testing)

## Features

- User authentication and authorization with JWT
- Role-based access control (Admin and Employee roles)
- Employee management (CRUD operations)
- Department management (CRUD operations)
- Employee-department assignments
- Address management for employees
- Detailed employee profiles
- Search and filter capabilities
- API documentation with Swagger/OpenAPI

## Technology Stack

- **Java 17**
- **Spring Boot 3.x**
- **Spring Security** with JWT authentication
- **Spring Data JPA** for data persistence
- **H2 Database** for development and testing
- **MapStruct** for object mapping
- **Lombok** for reducing boilerplate code
- **Swagger/OpenAPI** for API documentation
- **JUnit 5** for testing

## Project Structure

```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/platformcommons/employeemanagementsystem/
│   │   │       ├── config/            # Configuration classes
│   │   │       ├── controller/        # REST controllers
│   │   │       ├── dto/               # Data transfer objects
│   │   │       ├── entity/            # JPA entities
│   │   │       ├── exception/         # Custom exceptions and handlers
│   │   │       ├── mapper/            # MapStruct mappers
│   │   │       ├── repository/        # JPA repositories
│   │   │       ├── security/          # Security configuration and JWT utils
│   │   │       ├── service/           # Business logic services
│   │   │       └── EmployeeManagementSystemApplication.java  # Main class
│   │   └── resources/
│   │       └── application.properties  # Application configuration
│   └── test/
│       └── java/
│           └── com/platformcommons/employeemanagementsystem/
│               └── ... # Test classes
```

## Getting Started

### Prerequisites

- Java JDK 17 or higher
- Maven 3.6 or higher
- Git

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Dev-Mriganka/employee-management.git
   cd employee-management
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

### Running the Application

1. Run the application:
   ```bash
   mvn spring-boot:run
   ```

2. The application will be available at:
   ```
   http://localhost:8080/api
   ```

3. Access H2 console (for development database):
   ```
   http://localhost:8080/api/h2-console
   ```
    - JDBC URL: `jdbc:h2:mem:employeedb`
    - Username: `sa`
    - Password: leave empty

## API Documentation

API documentation is available through Swagger UI:
```
http://localhost:8080/api/swagger-ui.html
```

## Authentication

The system has two types of authentication:

1. **Admin Authentication**: Username and password-based
   ```
   POST /api/auth/admin/login
   ```

2. **Employee Authentication**: Employee code and date of birth-based
   ```
   POST /api/auth/employee/login
   ```

Both authentication methods return a JWT token that should be included in the Authorization header for subsequent requests:
```
Authorization: Bearer <token>
```

## Database

The application uses H2 an in-memory database for development and testing. In a production environment, you would configure a persistent database like PostgreSQL or MySQL.

Database schema is automatically created based on JPA entities using the following property:
```
spring.jpa.hibernate.ddl-auto=update
```

## Security

Security features include:

- JWT-based authentication
- Role-based authorization (ADMIN and EMPLOYEE roles)
- Method-level security using `@PreAuthorize` annotations
- Password encryption using BCrypt
- CSRF protection disabled for REST API
- Session management set to STATELESS

## Core Entities

1. **Employee**
    - Basic information (name, DOB, gender, etc.)
    - Contact details (email, mobile, emergency contact)
    - Multiple addresses (permanent, residential, correspondence)
    - Can belong to multiple departments

2. **Department**
    - Department information (name, description, type)
    - Responsibilities
    - Can have multiple employees

3. **Address**
    - Address details (street, city, state, country, postal code)
    - Address type (permanent, residential, correspondence)

## Usage Examples

### Creating a New Employee

```bash
curl -X POST "http://localhost:8080/api/employee" \
  -H "Authorization: Bearer your-token" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "dateOfBirth": "1990-01-01",
    "gender": "MALE",
    "employeeCode": "EMP001",
    "email": "john.doe@example.com",
    "mobileNumber": "1234567890",
    "emergencyContact": "0987654321",
    "addresses": [
      {
        "type": "PERMANENT",
        "streetAddress": "123 Main St",
        "city": "New York",
        "state": "NY",
        "country": "USA",
        "postalCode": "10001"
      }
    ]
  }'
```

### Creating a New Department

```bash
curl -X POST "http://localhost:8080/api/department" \
  -H "Authorization: Bearer your-token" \
  -H "Content-Type: application/json" \
  -d '{
    "departmentName": "Engineering",
    "description": "Software Engineering Department",
    "departmentType": "TECHNICAL",
    "responsibilities": "Developing and maintaining software applications"
  }'
```

### Assigning Department to Employee

```bash
curl -X PUT "http://localhost:8080/api/employee/1/department/1" \
  -H "Authorization: Bearer your-token"
```

## Testing

The project includes unit tests and integration tests. To run the tests:

```bash
mvn test
```

## License

[MIT License](LICENSE)