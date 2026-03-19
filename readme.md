# Employee Management System

A simple Employee Management System (EMS) used for development and learning Spring Boot features (JPA, Flyway, file uploads, OAuth2 resource server, etc.).

## Start the app (quickstart)

This document explains how to start the app in development using the provided Docker Compose (MySQL) or a standalone Docker container and how to run the Spring Boot application locally.

Prerequisites
- Java 17
- Gradle (or use the included Gradle wrapper)
- Docker and Docker Compose (if you want to run MySQL in a container)

Paths used by the application
- The app writes/reads uploaded files from the `uploads` directory at the project root. Default configured in `application.yml` as `app.upload-dir: uploads`.
- Development DB configuration is in `src/main/resources/application-dev.yml` and expects a MySQL database listening on `localhost:3307` by default (compose maps host 3307 -> container 3306).

Option A — Start MySQL with docker-compose (recommended for dev)
1. From project root run:

```powershell
docker compose up -d
```

2. This starts a MySQL container (image `mysql:8.0`) and creates a named volume `mysql_data` for persistence. The compose file maps host port `3307` -> container `3306` so it doesn't conflict with a local MySQL instance.

3. To stop and remove containers (but keep the volume):

```powershell
docker compose down
```

4. To stop and remove containers and the volume (destructively):

```powershell
docker compose down -v
```

If you prefer the DB files on a specific host folder, uncomment and set the bind-mount line in `docker-compose.yml` (Windows path example is already included as a comment).

Option B — Run MySQL with `docker run` and host-mounted data

This example maps container DB data to a host folder and exposes MySQL on port 3307:

```powershell
# create a folder for mysql data if you want host persistence
mkdir "D:\programming practice\springboot3\employee-mangement-service\mysql_data"

docker run --name some-mysql \
  -e MYSQL_ROOT_PASSWORD=password \
  -e MYSQL_DATABASE=employee-mangement \
  -e MYSQL_USER=employeemangement \
  -e MYSQL_PASSWORD=password \
  -p 3307:3306 \
  -v "D:\programming practice\springboot3\employee-mangement-service\mysql_data:/var/lib/mysql" \
  -d mysql:8.0
```

Notes:
- Use `-p 3307:3306` so this DB is available at `localhost:3307` (matches `application-dev.yml`).
- To completely remove the host-mounted DB folder, stop & remove container then delete the host folder. To remove a named Docker volume created by compose, use `docker volume rm <volume_name>` or `docker compose down -v`.

Start the Spring Boot app locally (development profile)

The project includes `application-dev.yml` which is configured for MySQL on `localhost:3307`.

Method 1 — Use the Gradle wrapper and set the profile via environment variable (PowerShell):

```powershell
$Env:SPRING_PROFILES_ACTIVE = 'dev'
.\gradlew bootRun
```

Method 2 — Pass the profile on the JVM command line:

```powershell
.\gradlew bootRun -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=dev"
```

Method 3 — Build a runnable jar and run it:

```powershell
.\gradlew bootJar
java -jar build\libs\*SNAPSHOT.jar --spring.profiles.active=dev
```

Environment variables and overrides
- The resource server (JWT) JWK URI is configured via `JWK_SET_URI` (defaults to `http://localhost:8080/realms/dev-apps-test/protocol/openid-connect/certs` in `application.yml`).
- Audience expected by the app is controlled by `OAUTH2_AUDIENCE`.
- Database credentials are in `application-dev.yml` but can be overridden with standard Spring Boot properties / env vars (e.g., `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`).

Uploads and file preview
- Uploaded files are saved in the `uploads` directory at the project root when running locally.
- If you run the app inside Docker, mount the `uploads` folder into the container to keep and access files from the host, for example:

```powershell
# Example when running the app in a container (not provided in this repo by default):
# -v "D:\programming practice\springboot3\employee-mangement-service\uploads:/app/uploads"
```

Troubleshooting / common issues
- Port 3306 already in use when starting MySQL container: either stop the local MySQL server or change the host port mapping to another port (this repo uses `3307` to avoid conflicts).
- Flyway validation errors on startup: in `application-dev.yml` the project sets `flyway.clean-on-validation-error: true` and `flyway.clean-disabled: false` for developer convenience. This will clean the DB on checksum mismatch — only use in development.
- If you see JWT errors like "Another algorithm expected, or no matching key(s) found", verify `JWK_SET_URI` points to a Keycloak/authorization server JWKS endpoint matching the token issuer and algorithms.
- If images are not served from the expected path, check `app.upload-dir` in `application.yml` and ensure your `serveFile` implementation resolves paths relative to the project root or the configured upload-dir.

Helpful Docker commands
- List volumes: `docker volume ls`
- Remove a named volume: `docker volume rm <volume_name>`
- Show running containers: `docker ps`
- Stop & remove a container: `docker rm -f <container>`

If you'd like, I can also add an example `Dockerfile` + `docker-compose` service for the application itself (to run the app in Docker with the uploads and DB volumes mounted).
