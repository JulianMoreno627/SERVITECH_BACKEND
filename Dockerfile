# Build stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/servitech-backend-0.0.1-SNAPSHOT.jar app.jar

# Render asigna dinámicamente un puerto en la variable PORT
# Usamos shell form para que se expanda la variable de entorno
ENTRYPOINT java -jar app.jar --server.port=${PORT:-8080}
