# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Instalar Maven
RUN apk add --no-cache maven

# Copiar archivos de Maven primero (para cachear dependencias)
COPY pom.xml .
COPY .mvn .mvn 2>/dev/null || true

# Descargar dependencias (esta capa se cachea)
RUN mvn dependency:go-offline -B -q || mvn dependency:resolve -B -q

# Copiar código fuente
COPY src ./src

# Compilar aplicación
RUN mvn clean package -DskipTests -B -q

# Stage 2: Runtime optimizado para Render
FROM eclipse-temurin:21-jre-alpine AS runtime

# Instalar curl para health checks
RUN apk add --no-cache curl tzdata

# Crear usuario no-root
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app

# Copiar JAR desde builder
COPY --from=builder /app/target/*.jar app.jar

# Cambiar ownership
RUN chown -R appuser:appgroup /app

USER appuser

# Variables de entorno optimizadas para Render
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseContainerSupport -XX:+UnlockExperimentalVMOptions -XX:+UseZGC"

EXPOSE ${PORT:-8081}

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:${PORT:-8081}/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8081} -jar app.jar"]