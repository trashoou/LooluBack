FROM maven:3.8.1-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY src ./src

RUN mvn dependency:go-offline

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]