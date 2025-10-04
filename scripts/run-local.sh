#!/bin/bash

DOCKER_USERNAME="ricardo026"
IMAGE_NAME="franchise-api"
TAG="latest"

# Cargar variables del archivo .env
if [ -f .env ]; then
    export $(cat .env | grep -v '#' | xargs)
else
    echo "❌ Archivo .env no encontrado"
    exit 1
fi

echo "🚀 Ejecutando contenedor localmente con MongoDB Atlas..."

# Detener y eliminar contenedor existente si existe
docker stop franchise-api-local 2>/dev/null || true
docker rm franchise-api-local 2>/dev/null || true

# Ejecutar nuevo contenedor
docker run -d \
  --name franchise-api-local \
  -p 8081:8081 \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e DB_PASSWORD="${DB_PASSWORD}" \
  -e PORT=8081 \
  ${DOCKER_USERNAME}/${IMAGE_NAME}:${TAG}

if [ $? -eq 0 ]; then
    echo "✅ Contenedor ejecutándose en http://localhost:8081"
    echo "📊 Health check: http://localhost:8081/actuator/health"
    echo "🔍 Logs: docker logs -f franchise-api-local"
    echo "🛑 Para detener: docker stop franchise-api-local && docker rm franchise-api-local"

    # Mostrar logs iniciales
    echo ""
    echo "📋 Mostrando logs iniciales..."
    sleep 3
    docker logs franchise-api-local
else
    echo "❌ Error ejecutando el contenedor"
    exit 1
fi
