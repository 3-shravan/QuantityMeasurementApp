FROM maven:3.9.4-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ ./src/

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=builder /app/target/auth-service-1.0.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8082

