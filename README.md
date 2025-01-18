# Library Management API

A mini Library Management RESTful API that allows users to manage books, borrow books and perform administrative tasks. It is built with Java 21, Spring Boot and PostgreSQL.

The application supports features like user registration, user login, book CRUD operations and borrowing books. It has authentication and authorization based on JWT and Spring Security

## Usage

Setup Database:
•	Create a PostgreSQL database (E.g: library_management_db).
•	Update essential configuration variable values in application.properties:

```
spring.application.name=Library Management API
server.port=

# Database connection
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=

# JWT
jwt.secret.key=
jwt.expiration.duration=
```

- execute mvn clean install
- execute mvn spring-boot:run