# Use Java 17 runtime
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Make Maven wrapper executable
RUN chmod +x mvnw

# Build the Spring Boot application
RUN ./mvnw clean package -DskipTests

# Run the Spring Boot application
CMD ["java", "-jar", "target/digital-banking-portal-0.0.1-SNAPSHOT.jar"]