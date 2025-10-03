# Etapa de construcción
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

RUN apk add --no-cache maven

COPY pom.xml .

# Si NO usas Maven Wrapper (.mvn), elimina la siguiente línea:
# COPY .mvn .mvn/

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

# Etapa de runtime
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

RUN addgroup -g 1001 -S appuser && \
    adduser -S -D -H -u 1001 -h /app -s /sbin/nologin -G appuser appuser

RUN apk add --no-cache curl

COPY --from=builder /app/target/*.jar app.jar

RUN chown appuser:appuser app.jar

USER appuser

EXPOSE 8082

ENV JAVA_OPTS="-Xmx512m -Xms256m"

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8081/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]