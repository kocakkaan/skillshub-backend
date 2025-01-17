FROM maven:3.9.8-eclipse-temurin-21 AS build-stage
WORKDIR /app

COPY pom.xml .
COPY skillshub_api_definition ./skillshub_api_definition
COPY src ./src

RUN mvn clean package -DskipTests -Dspring-boot.run.profiles=prod

# Production stage
FROM eclipse-temurin:21-alpine AS production-stage
WORKDIR /app
COPY --from=build-stage /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]