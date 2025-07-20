# Build stage
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
# This layer caches the dependencies
RUN mvn dependency:go-offline
COPY src /app/src
# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copy the built JAR from the build stage
COPY --from=build /app/target/myproject-1.0-SNAPSHOT.jar /app/app.jar
# Copy config file
COPY config.json /app/config.json
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]