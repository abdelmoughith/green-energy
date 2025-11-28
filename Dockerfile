# Use official OpenJDK 21 image
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy jar file (replace with your jar name)
COPY target/green-energy-0.0.1-SNAPSHOT.jar app.jar

# Expose port your app runs on
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
