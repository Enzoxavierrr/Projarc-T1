# ─── Estágio 1: compilar e empacotar ───────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copia o pom.xml e baixa as dependências antes de copiar o código
# (aproveitamento de cache do Docker: só rebaixa deps se o pom mudar)
COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn package -DskipTests -q

# ─── Estágio 2: imagem final leve apenas com o JAR ─────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
