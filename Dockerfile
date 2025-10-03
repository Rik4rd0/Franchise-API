# Dockerfile optimizado para aplicación Spring Boot
# Utiliza multi-stage build para optimizar el tamaño final

# Stage 1: Build
FROM openjdk:21-jdk-slim AS builder

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Hacer el script mvnw ejecutable
RUN chmod +x ./mvnw

# Descargar dependencias (se cachea si pom.xml no cambia)
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Construir la aplicación (omitir tests para construcción más rápida)
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:21-jre-slim AS runtime

# Instalar curl para health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Crear usuario no root por seguridad
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Establecer directorio de trabajo
WORKDIR /app

# Copiar JAR desde el stage de build
COPY --from=builder /app/target/franchise-api-*.jar app.jar

# Exponer puerto correcto (8081 según application.properties)
EXPOSE 8081

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8081/actuator/health || exit 1

# Comando de inicio optimizado
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
