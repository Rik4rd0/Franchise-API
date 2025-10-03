# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Instalar Maven
RUN apk add --no-cache maven

# Copiar pom.xml
COPY pom.xml .

# Descargar dependencias
RUN mvn dependency:resolve

# Copiar código fuente
COPY src ./src

# Compilar aplicación
RUN mvn clean package -DskipTests

# Stage 2: Runtime (mantener igual)
FROM eclipse-temurin:21-jre-alpine AS runtime

RUN apk add --no-cache curl

RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

RUN chown -R appuser:appgroup /app

USER appuser

ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=docker

EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8081/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]