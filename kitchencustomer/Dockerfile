# Fase de Build usando Maven
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Fase de Execução usando OpenJDK
FROM openjdk:17-jdk-slim
WORKDIR /app
# Usando o nome correto do JAR gerado na fase de build
COPY --from=build /app/target/kitchencustomer-0.0.1-SNAPSHOT.jar kitchencustomer.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "kitchencustomer.jar"]
