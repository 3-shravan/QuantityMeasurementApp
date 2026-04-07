FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x mvnw
RUN ./mvnw -B -q -DskipTests dependency:go-offline

COPY src src
RUN ./mvnw -B -q -DskipTests clean package

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=prod

COPY --from=build /workspace/target/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]