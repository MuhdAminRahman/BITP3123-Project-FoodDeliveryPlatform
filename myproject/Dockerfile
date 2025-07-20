# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copy both the JAR and config file to classpath
COPY --from=build /app/target/myproject-1.0-SNAPSHOT.jar ./app.jar
COPY config.json .

# Use environment variables for production (recommended)
ENV DATABASE_URL=${DATABASE_URL}
ENV DATABASE_USERNAME=${DATABASE_USERNAME}
ENV DATABASE_PASSWORD=${DATABASE_PASSWORD}
ENV JWT_SECRET=${JWT_SECRET}
ENV PASSWORD_PEPPER=${PASSWORD_PEPPER}

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]