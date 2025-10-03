#!/bin/bash

# Script de despliegue para AWS ECS
# Ejecutar después de que la imagen Docker esté en GitHub Container Registry

set -e

# Variables de configuración
CLUSTER_NAME="franchise-api-cluster"
SERVICE_NAME="franchise-api-service"
TASK_DEFINITION_NAME="franchise-api-task"
IMAGE_URI="ghcr.io/tu-usuario/franchise-api:latest"
REGION="us-east-1"

echo "🚀 Iniciando despliegue en AWS ECS..."

# 1. Verificar que AWS CLI esté configurado
if ! aws sts get-caller-identity > /dev/null; then
    echo "❌ AWS CLI no está configurado. Ejecuta 'aws configure' primero."
    exit 1
fi

# 2. Crear definición de tarea
echo "📝 Creando definición de tarea..."
cat > task-definition.json << EOF
{
  "family": "${TASK_DEFINITION_NAME}",
  "networkMode": "awsvpc",
  "requiresCompatibilities": ["FARGATE"],
  "cpu": "512",
  "memory": "1024",
  "executionRoleArn": "arn:aws:iam::$(aws sts get-caller-identity --query Account --output text):role/ecsTaskExecutionRole",
  "taskRoleArn": "arn:aws:iam::$(aws sts get-caller-identity --query Account --output text):role/ecsTaskRole",
  "containerDefinitions": [
    {
      "name": "franchise-api",
      "image": "${IMAGE_URI}",
      "portMappings": [
        {
          "containerPort": 8081,
          "protocol": "tcp"
        }
      ],
      "environment": [
        {
          "name": "SPRING_PROFILES_ACTIVE",
          "value": "prod"
        }
      ],
      "secrets": [
        {
          "name": "MONGODB_URI",
          "valueFrom": "arn:aws:ssm:${REGION}:$(aws sts get-caller-identity --query Account --output text):parameter/franchise-api/mongodb-uri"
        }
      ],
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-group": "/ecs/franchise-api",
          "awslogs-region": "${REGION}",
          "awslogs-stream-prefix": "ecs"
        }
      },
      "healthCheck": {
        "command": [
          "CMD-SHELL",
          "curl -f http://localhost:8081/actuator/health || exit 1"
        ],
        "interval": 30,
        "timeout": 5,
        "retries": 3,
        "startPeriod": 60
      }
    }
  ]
}
EOF

# 3. Registrar nueva definición de tarea
echo "📋 Registrando nueva definición de tarea..."
TASK_DEFINITION_ARN=$(aws ecs register-task-definition \
    --cli-input-json file://task-definition.json \
    --region ${REGION} \
    --query 'taskDefinition.taskDefinitionArn' \
    --output text)

echo "✅ Definición de tarea registrada: ${TASK_DEFINITION_ARN}"

# 4. Actualizar servicio
echo "🔄 Actualizando servicio ECS..."
aws ecs update-service \
    --cluster ${CLUSTER_NAME} \
    --service ${SERVICE_NAME} \
    --task-definition ${TASK_DEFINITION_ARN} \
    --region ${REGION}

# 5. Esperar a que el despliegue termine
echo "⏳ Esperando que el despliegue termine..."
aws ecs wait services-stable \
    --cluster ${CLUSTER_NAME} \
    --services ${SERVICE_NAME} \
    --region ${REGION}

# 6. Obtener URL del Load Balancer
ALB_DNS=$(aws elbv2 describe-load-balancers \
    --names franchise-api-alb \
    --region ${REGION} \
    --query 'LoadBalancers[0].DNSName' \
    --output text 2>/dev/null || echo "No configurado")

echo "🎉 Despliegue completado exitosamente!"
echo "📍 URL de la API: http://${ALB_DNS}"
echo "🏥 Health Check: http://${ALB_DNS}/actuator/health"
echo "📚 Swagger UI: http://${ALB_DNS}/swagger-ui.html"

# Limpiar archivos temporales
rm -f task-definition.json

echo "✨ Script completado."
