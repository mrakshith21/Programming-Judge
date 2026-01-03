# Evaluator

This component is responsible for executing user submissions in a secure, containerized environment.

## Building Docker Images

The evaluator uses specific Docker images for different programming languages. These images must be built locally before running the evaluator.

### Python 3.13

To build the Python 3.13 image:

```bash
docker build -t judge-python:3.13 ./images/python3.13
```

### Java 21

To build the Java 21 image:

```bash
docker build -t judge-java:21 ./images/java21
```

## Running the Evaluator

Ensure you have Go installed and the Docker daemon is running.

```bash
go run .
```
