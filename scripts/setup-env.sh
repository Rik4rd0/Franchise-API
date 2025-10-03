#!/bin/bash

# Script para configurar variables de entorno para la aplicación Franchise API
# Este script debe ser ejecutado antes de desplegar la aplicación

echo "Configurando variables de entorno para Franchise API..."

# Variables de entorno requeridas
export MONGODB_URI="mongodb+srv://rickmartinezbanda_db_user:5SedZCxKjx8mrhf4@franchise-api-dev.rz2dpuc.mongodb.net/Franchise-API-db?retryWrites=true&w=majority&appName=franchise-api-dev"
export SPRING_PROFILES_ACTIVE="prod"
export PORT="8081"

# Variables opcionales para optimización
export JAVA_OPTS="-Xmx512m -Xms256m -server"

echo "Variables de entorno configuradas:"
echo "  - SPRING_PROFILES_ACTIVE: $SPRING_PROFILES_ACTIVE"
echo "  - PORT: $PORT"
echo "  - MONGODB_URI: [CONFIGURADO - CREDENCIALES OCULTAS]"

echo "Para ejecutar la aplicación:"
echo "  java $JAVA_OPTS -jar target/franchise-api-*.jar"
