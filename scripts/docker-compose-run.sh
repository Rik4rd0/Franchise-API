#!/bin/bash

# Script para usar Docker Compose
MODE=${1:-dev}

# Cargar variables del archivo .env
if [ -f .env ]; then
    export $(cat .env | grep -v '#' | xargs)
else
    echo "❌ Archivo .env no encontrado"
    exit 1
fi

echo "🚀 Ejecutando con Docker Compose en modo: $MODE"

case $MODE in
    "dev")
        echo "📋 Modo desarrollo..."
        docker-compose down
        docker-compose up --build -d

        if [ $? -eq 0 ]; then
            echo "✅ Aplicación ejecutándose en modo desarrollo"
            echo "📊 URL: http://localhost:8081"
            echo "🔍 Logs: docker-compose logs -f"
            echo "🛑 Para detener: docker-compose down"
        fi
        ;;
    "prod")
        echo "📋 Modo producción..."
        docker-compose -f docker-compose.prod.yml down
        docker-compose -f docker-compose.prod.yml up --build -d

        if [ $? -eq 0 ]; then
            echo "✅ Aplicación ejecutándose en modo producción"
            echo "📊 URL: http://localhost:8081"
            echo "🔍 Logs: docker-compose -f docker-compose.prod.yml logs -f"
            echo "🛑 Para detener: docker-compose -f docker-compose.prod.yml down"
        fi
        ;;
    *)
        echo "Uso: $0 [dev|prod]"
        exit 1
        ;;
esac
