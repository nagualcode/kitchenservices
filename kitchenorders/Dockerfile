# Usar uma imagem base do JDK para compilar o projeto
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Usar uma imagem base mais leve para o runtime
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/kitchenorders-0.0.1-SNAPSHOT.jar kitchenorders.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "kitchenorders.jar"]
