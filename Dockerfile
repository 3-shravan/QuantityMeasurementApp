# ===============================
# BUILD STAGE
# ===============================
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copy only necessary files first (better caching)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x mvnw
RUN ./mvnw -B -q -e -DskipTests dependency:go-offline

# Copy source separately (cache optimization)
COPY src src

RUN ./mvnw -B -q -DskipTests clean package

# ===============================
# RUNTIME STAGE
# ===============================
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Default profile (can be overridden by env)
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS=""

# Copy jar
COPY --from=build /workspace/target/*.jar /app/app.jar

# Railway uses PORT env
EXPOSE 8080

# Run app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]