FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the Spring Boot JAR into the image
COPY SLTallyAutomation-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
