# ---- Stage 1: Build the application ----
FROM gradle:8.5-jdk17-alpine AS builder

# Set the working directory
WORKDIR /app

# Copy only build files first to leverage Docker cache
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy the source code
COPY src ./src

# Build the Spring Boot app
RUN ./gradlew bootJar --no-daemon

# ---- Stage 2: Run the application ----
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8085 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
