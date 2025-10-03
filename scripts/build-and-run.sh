#!/bin/bash

# Script completo: build y run
DOCKER_USERNAME="ricardo026"
IMAGE_NAME="franchise-api"
TAG="latest"

# Función para mostrar ayuda
show_help() {
    echo "Uso: $0 [dev|prod]"
    echo "  dev  - Ejecutar en modo desarrollo"
    echo "  prod - Ejecutar en modo producción"
    exit 1
}

# Verificar parámetro
if [ -z "$1" ]; then
    show_help
fi

MODE=$1

# Cargar variables del archivo .env
if [ -f .env ]; then
    export $(cat .env | grep -v '#' | xargs)
else
    echo "❌ Archivo .env no encontrado"
    exit 1
fi

echo "🔨 Construyendo imagen Docker..."

# Construir imagen
docker build -t ${DOCKER_USERNAME}/${IMAGE_NAME}:${TAG} .

if [ $? -ne 0 ]; then
    echo "❌ Error construyendo la imagen"
    exit 1
fi

echo "✅ Imagen construida exitosamente"

# Ejecutar según el modo
case $MODE in
    "dev")
        echo "🚀 Ejecutando en modo desarrollo..."

        docker stop franchise-api-dev 2>/dev/null || true
        docker rm franchise-api-dev 2>/dev/null || true

        docker run -d \
          --name franchise-api-dev \
          -p 8081:8081 \
          -e SPRING_PROFILES_ACTIVE=dev \
          -e DB_PASSWORD="${DB_PASSWORD}" \
          -e PORT=8081 \
          ${DOCKER_USERNAME}/${IMAGE_NAME}:${TAG}

        CONTAINER_NAME="franchise-api-dev"
        ;;
    "prod")
        echo "🚀 Ejecutando en modo producción..."

        docker stop franchise-api-prod 2>/dev/null || true
        docker rm franchise-api-prod 2>/dev/null || true

        docker run -d \
          --name franchise-api-prod \
          -p 8081:8081 \
          -e SPRING_PROFILES_ACTIVE=prod \
          -e DB_PASSWORD="${DB_PASSWORD}" \
          -e PORT=8081 \
          -e JAVA_OPTS="-Xmx512m -Xms256m -server" \
          --restart unless-stopped \
          ${DOCKER_USERNAME}/${IMAGE_NAME}:${TAG}

        CONTAINER_NAME="franchise-api-prod"
        ;;
    *)
        show_help
        ;;
esac

if [ $? -eq 0 ]; then
    echo "✅ Contenedor $CONTAINER_NAME ejecutándose en http://localhost:8081"
    echo "📊 Health check: http://localhost:8081/actuator/health"
    echo "🔍 Logs: docker logs -f $CONTAINER_NAME"
    echo "🛑 Para detener: docker stop $CONTAINER_NAME && docker rm $CONTAINER_NAME"
else
    echo "❌ Error ejecutando el contenedor"
    exit 1
fi