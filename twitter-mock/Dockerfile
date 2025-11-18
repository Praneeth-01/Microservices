# Build stage
FROM maven:3.9.6-eclipse-temurin-21 as builder

# Set working directory
WORKDIR /app

# Copy only the files needed for downloading dependencies
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN mvn package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-jammy

# Set working directory
WORKDIR /app

# Create a non-root user and switch to it
RUN groupadd -r spring && useradd -r -g spring spring

# Create config directory and set permissions
RUN mkdir -p /app/config && \
    chown -R spring:spring /app

# Switch to non-root user
USER spring:spring

# Copy application.properties first to leverage Docker cache
COPY --from=builder --chown=spring:spring /app/src/main/resources/application.properties /app/config/application.properties

# Copy the built JAR from the builder stage
COPY --from=builder --chown=spring:spring /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Set the entrypoint to run the application with the config location
ENTRYPOINT ["java", \
  "-Dspring.config.location=classpath:/,file:/app/config/", \
  "-Xms256m", \
  "-Xmx512m", \
  "-jar", \
  "/app/app.jar"]