# Getting Started
This repository contains the Spring Boot backend for the Exiger Supplier Portal. We created a [SpringBoot Starter Guide](https://docs.google.com/document/d/1ay7uyVYoCVsjc5VX_oeq7ehqm0PO-Tt5puqcqty7hDo/edit?tab=t.0#heading=h.sd82n2k14in4).

---

## Prerequisites
- Java SDK v17 or higher
  - Run `java -version` to check your system's Java SDK version
- Docker installed ([Docker Desktop](https://www.docker.com/products/docker-desktop/))
- Maven installed ([Maven Installation](https://maven.apache.org/install.html))
  - This is a build tool for Java projects. It manages compilation, testing, and dependencies.

## Environment Configuration
Before running the application, you need to set up your environment variables:

1. Copy the example environment file:
   ```bash
   cp .env.example .env
   ```

2. Edit `.env` with your actual values:
   - Replace `your-okta-client-id` with your Okta application client ID
   - Replace `your-okta-client-secret` with your Okta application client secret
   - Replace `your-domain.okta.com` with your actual Okta domain
   - Replace `your-api-token` with your API token
   - Replace `your-okta-management-api-token` with your Okta Management API token

**Note**: The `.env` file is gitignored for security - never commit your actual credentials.

---

## Running the Spring Boot server
### (***RECOMMENDED\***) Option 1: Run all docker containers (Backend, Frontend, Postgres)
1. Ensure you are in the project's root directory
2. `docker compose up --build` (`docker compose up` if you haven't made any changes to the code)
3. Navigate to `http://localhost:3000`
4. Stop the containers: `docker compose down`

### Option 2: Run Backend container only (might cause database connection errors)
1. Ensure you are in the project's root directory
2. Build the Docker Image: `docker build -t exiger-supplier-portal-backend ./backend`
3. Run the Docker Container: `docker run -d -p 8080:8080 --name backend exiger-supplier-portal-backend`
4. Navigate to `http://localhost:8080/` ("Hello World" should appear for now)
5. Stop the container: `docker stop backend` then `docker rm backend`

### Option 3: Run locally via Maven (might cause database connection errors)
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

## Connecting to PostgreSQL instance
NOTE: must be running backend and postgres containers (Option 1)
1. Start Docker containers: `docker compose up`
2. Connect via psql:`docker exec -it exiger-db psql -U exiger -d exigerdb`
3. You can:
   1. View tables with `\d`
   2. Write any SQL query
   3. Quit with `\q`
   4. More commands: [https://www.postgresql.org/docs/current/app-psql.html](https://www.postgresql.org/docs/current/app-psql.html)
