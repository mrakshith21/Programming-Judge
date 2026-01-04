# Evaluator

The Evaluator is a Go-based service responsible for executing user submissions in a secure, containerized environment. It listens for submission tasks from RabbitMQ, runs the code against test cases stored in the shared storage, and updates the results in the database.

## Prerequisites

- **Go**: 1.25 or higher
- **Docker**: Installed and running
- **RabbitMQ**: Accessible instance
- **PostgreSQL**: Accessible instance

## Installation

1. Clone the repository and navigate to the Evaluator directory.
2. Install Go dependencies:
```bash
go mod download
```

## Building Docker Images

The evaluator uses specific Docker images for different programming languages. These images must be built locally before running the evaluator.

### Python 3.13
```bash
docker build -t judge-python:3.13 ./images/python3.13
```

### Java 21
```bash
docker build -t judge-java:21 ./images/java21
```

## Run

1. Ensure Docker, RabbitMQ, and PostgreSQL are running.
2. Start the evaluator:
```bash
go run .
```
