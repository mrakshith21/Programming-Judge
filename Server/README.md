# Server

The Server is a Spring Boot application that serves as the backbone of the Programming Judge. It manages users, problems, and submissions, and coordinates with the Evaluator via RabbitMQ.

## Overview

- **Spring Boot**: Backend framework
- **Spring Security**: Authentication and Authorization (JWT)
- **Spring Data JPA**: Database interaction
- **Flyway**: Database migrations
- **RabbitMQ**: Message broker for submissions
- **PostgreSQL**: Primary database

## Prerequisites

- **Java**: 25 or higher
- **Maven**: 3.9 or higher
- **PostgreSQL**: [Download PostgreSQL](https://www.postgresql.org/download/)
- **RabbitMQ**: Running instance

## Infrastructure Setup

### RabbitMQ
You can run RabbitMQ using Docker:
```bash
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4-management
```

## Installation

1. Navigate to the Server directory.
2. Build the project:
```bash
mvn clean install
```

## Database Migrations

Flyway is used for database migrations. By default, Flyway runs before Hibernate initialization. To run migrations manually using Maven:
```bash
mvn flyway:migrate
```

## Run

1. Ensure PostgreSQL and RabbitMQ are running and configured in `application.properties`.
   Update the username and password if required.
2. Run the application with maven or Intellij Idea.
```bash
mvn spring-boot:run
```
3. The API will be available at `http://localhost:8080`.

## API Reference

### Users
- `POST /api/users/signup`: Register a new user.
- `POST /api/users/login`: Authenticate and get JWT token.
- `GET /api/users`: List all users.
- `GET /api/users/{username}`: Get user details.

### Problems
- `GET /api/problems`: List all problems (paginated).
- `GET /api/problems/{id}`: Get problem details.
- `POST /api/problems`: Create a new problem.
- `PUT /api/problems/{id}`: Update a problem.

### Submissions
- `GET /api/submissions`: List all submissions (paginated).
- `GET /api/submissions/{id}`: Get submission details.
- `GET /api/submissions/{id}/code`: Get submission source code.
- `POST /api/submissions`: Submit a solution.
- `PUT /api/submissions/{id}`: Update submission status (internal).
