# Getting Started

This repository contains the Spring Boot backend for the Exiger Supplier Portal.

---

## Prerequisites

- Docker installed ([Docker Desktop](https://www.docker.com/products/docker-desktop/))
- PostgreSQL database running (can be local or in Docker)
- Maven installed (for local build, optional if using Docker multi-stage build)

---

## Running locally
Development
1. `mvn spring-boot:run`
2. Navigate to `http://localhost:8080/`

Executable
1. `mvn package`
2. `java -jar target/exiger-supplier-portal-VERSION.jar`
3. Navigate to `http://localhost:8080/`

## Running Via Docker Container
1. Ensure you are in the project's root directory
2. Build the Docker Image: `docker build -t exiger-supplier-portal-backend ./backend`
3. Run the Docker Container: `docker run -d -p 8080:8080 --name backend exiger-supplier-portal-backend`
4. TODO: update with arguments for postgreSQL environmental variables
5. Navigate to `http://localhost:8080/`
6. Stop: `docker stop backend` then `docker rm backend`
