# Build stage - using official Maven with Eclipse Temurin JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
# Cache dependencies
RUN mvn dependency:go-offline
COPY src /app/src
# Build the application
RUN mvn clean package -DskipTests

# Runtime stage - using Eclipse Temurin JDK 17
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
# Copy the built JAR
COPY --from=build /app/target/myproject-1.0-SNAPSHOT.jar /app/app.jar
# Copy config file
COPY config.json /app/config.json
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]