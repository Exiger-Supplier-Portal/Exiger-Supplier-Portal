# Getting Started
This repository contains the Spring Boot backend for the Exiger Supplier Portal. We created a [SpringBoot Starter Guide](https://docs.google.com/document/d/1ay7uyVYoCVsjc5VX_oeq7ehqm0PO-Tt5puqcqty7hDo/edit?tab=t.0#heading=h.sd82n2k14in4).

---

## Prerequisites
- Java SDK v17 or higher
  - Run `java -version` to check your system's Java SDK version
- Docker installed ([Docker Desktop](https://www.docker.com/products/docker-desktop/))
- Maven installed ([Maven Installation](https://maven.apache.org/install.html))
  - This is a build tool for Java projects. It manages compilation, testing, and dependencies.
---

## Running the Spring Boot server
### Option 1: Running Via Docker Container
1. Ensure you are in the project's root directory
2. Build the Docker Image: `docker build -t exiger-supplier-portal-backend ./backend`
3. Run the Docker Container: `docker run -d -p 8080:8080 --name backend exiger-supplier-portal-backend`
4. Navigate to `http://localhost:8080/` ("Hello World" should appear for now)
5. Stop the container: `docker stop backend` then `docker rm backend`

### Option 2: Running locally
#### Run in development
1. `mvn spring-boot:run`
2. Navigate to `http://localhost:8080/`

#### OR build and run an executable (.jar)
1. `mvn package`
2. `java -jar target/exiger-supplier-portal-VERSION.jar`
3. Navigate to `http://localhost:8080/`

---

## Running Tests

### Setup Test Configuration
Before running tests, create the test configuration file:
```bash
cp src/test/resources/application-test.properties.example src/test/resources/application-test.properties
```

### Run Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=RelationshipControllerTest
```

**Note**: `application-test.properties` is gitignored for security - use the example template to create your own.


