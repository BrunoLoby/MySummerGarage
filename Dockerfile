# syntax=docker/dockerfile:1

###############################
# Stage 1 - Build
###############################
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Baixa as dependências primeiro (cache de camada)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Compila e empacota (skip dos testes no build da imagem)
COPY src ./src
RUN mvn -B clean package -DskipTests

###############################
# Stage 2 - Runtime
###############################
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Usuário não-root
RUN groupadd --system spring && useradd --system --gid spring spring
USER spring:spring

COPY --from=build /app/target/MySummerGarage-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
