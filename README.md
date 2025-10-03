# Franchise API

Una API REST desarrollada en Java con Spring Boot para la gestión de franquicias, sucursales y productos. Utiliza arquitectura hexagonal (Clean Architecture) y programación reactiva con WebFlux.

## 🚀 Características

- **Arquitectura Hexagonal**: Separación clara entre dominio, aplicación e infraestructura
- **Programación Reactiva**: Implementada con Spring WebFlux y MongoDB Reactive
- **Base de Datos**: MongoDB con Spring Data MongoDB Reactive  
- **Documentación API**: Swagger/OpenAPI 3.0
- **Testing**: Pruebas unitarias e integración con TestContainers
- **Containerización**: Docker y Docker Compose
- **Validación**: Bean Validation con anotaciones personalizadas

## 📋 Prerrequisitos

Para ejecutar este proyecto necesitas:

- **Java 21** o superior
- **Maven 3.6+**
- **Docker** y **Docker Compose** (para contenedores)
- **MongoDB** (si ejecutas localmente sin Docker)

## 🛠️ Instalación y Configuración

### Clonar el Repositorio

```bash
git clone <url-del-repositorio>
cd franchise-api
```

### Configuración Local

1. **Configurar MongoDB**: 
   - Instala MongoDB localmente o usa Docker
   - Actualiza `src/main/resources/application.properties` con tu URI de conexión

2. **Instalar Dependencias**:
```bash
mvn clean install
```

## ▶️ Ejecutar la Aplicación

### Opción 1: Ejecutar Localmente

```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar la aplicación
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8081`

### Opción 2: Usar Docker Compose (Recomendado)

```bash
# Construir y levantar todos los servicios
docker-compose up --build

# Para ejecutar en background
docker-compose up -d --build

# Para parar los servicios
docker-compose down

# Para parar y eliminar volúmenes
docker-compose down -v
```

#### Servicios Disponibles:

- **API**: `http://localhost:8081`
- **MongoDB**: `localhost:27017`
- **Mongo Express** (UI admin): `http://localhost:8082` (solo en perfil dev)

Para usar Mongo Express:
```bash
docker-compose --profile dev up
```

## 🧪 Ejecutar Pruebas

### Todas las Pruebas

```bash
mvn test
```

### Solo Pruebas Unitarias

```bash
mvn test -Dtest="**/*Test"
```

### Solo Pruebas de Integración

```bash
mvn test -Dtest="**/*IntegrationTest"
```

### Reporte de Cobertura

```bash
mvn jacoco:report
```

El reporte estará disponible en: `target/site/jacoco/index.html`

## 📚 Documentación de la API

### Swagger UI

Una vez que la aplicación esté ejecutándose, puedes acceder a la documentación interactiva:

- **Swagger UI**: `http://localhost:8081/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8081/v3/api-docs`

### Endpoints Principales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/v1/franchises` | Crear franquicia |
| POST | `/api/v1/franchises/{franchiseId}/branches` | Agregar sucursal |
| POST | `/api/v1/franchises/branches/{branchId}/products` | Agregar producto |
| GET | `/api/v1/franchises/{franchiseId}/top-stock-products` | Productos con más stock |
| PATCH | `/api/v1/franchises/{franchiseId}/name` | Actualizar nombre franquicia |
| PATCH | `/api/v1/franchises/branches/{branchId}/name` | Actualizar nombre sucursal |
| PATCH | `/api/v1/franchises/products/{productId}/name` | Actualizar nombre producto |
| PATCH | `/api/v1/franchises/products/{productId}/stock` | Actualizar stock producto |
| DELETE | `/api/v1/franchises/products/{productId}` | Eliminar producto |

### Ejemplos de Uso

#### Crear Franquicia

```bash
curl -X POST http://localhost:8081/api/v1/franchises \
  -H "Content-Type: application/json" \
  -d '{"name": "McDonald'\''s"}'
```

#### Agregar Sucursal

```bash
curl -X POST http://localhost:8081/api/v1/franchises/{franchiseId}/branches \
  -H "Content-Type: application/json" \
  -d '{"name": "Sucursal Centro"}'
```

## 🏗️ Estructura del Proyecto

```
franchise-api/
├── src/
│   ├── main/
│   │   ├── java/com/franchise/api/
│   │   │   ├── application/           # Casos de uso y DTOs
│   │   │   │   ├── service/          # Casos de uso
│   │   │   │   └── usecase/          # Commands y Responses
│   │   │   ├── domain/               # Lógica de negocio
│   │   │   │   ├── entity/           # Entidades de dominio
│   │   │   │   ├── port/             # Interfaces de repositorios
│   │   │   │   ├── vo/               # Value Objects
│   │   │   │   └── exception/        # Excepciones de dominio
│   │   │   ├── infrastructure/       # Adaptadores e infraestructura
│   │   │   │   ├── adapter/          # Adaptadores de persistencia
│   │   │   │   ├── config/           # Configuraciones
│   │   │   │   ├── controller/       # Controladores REST
│   │   │   │   └── mapper/           # Mappers de dominio
│   │   │   └── FranchiseApiApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                         # Pruebas unitarias e integración
├── docker/
│   └── mongo-init.js                # Script de inicialización MongoDB
├── docker-compose.yml               # Orquestación de contenedores
├── Dockerfile                       # Imagen de la aplicación
└── pom.xml                         # Dependencias Maven
```

## 🔧 Configuraciones Adicionales

### Perfiles de Spring

- **default**: Configuración para desarrollo local
- **docker**: Configuración para contenedores
- **test**: Configuración para pruebas

### Variables de Entorno

| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `MONGODB_URI` | URI de conexión MongoDB completa | `mongodb://localhost:27017/franchise-api-db` |
| `SPRING_PROFILES_ACTIVE` | Perfil activo (`default`, `docker`, `prod`) | `default` |
| `JAVA_OPTS` | Opciones JVM | `-Xmx512m -Xms256m` |
| `PORT` | Puerto del servidor | `8081` |

#### Configuración Segura de Credenciales

**Para desarrollo local:**
```bash
# Linux/Mac
source scripts/setup-env.sh

# Windows PowerShell
.\scripts\setup-env.ps1
```

**Para Docker con archivo .env:**
```bash
# Crear archivo .env (no incluir en Git)
echo "MONGODB_URI=mongodb+srv://user:pass@cluster.mongodb.net/db" > .env
echo "SPRING_PROFILES_ACTIVE=prod" >> .env

# Usar con Docker Compose
docker-compose -f docker-compose.prod.yml --env-file .env up
```

**Para servicios en la nube:**
- AWS: Usar AWS Systems Manager Parameter Store o AWS Secrets Manager
- Azure: Usar Azure Key Vault
- Google Cloud: Usar Secret Manager
- Heroku: Configurar variables en el dashboard

### Health Checks

La aplicación incluye endpoints de salud:

- **Health**: `GET /actuator/health`
- **Info**: `GET /actuator/info`

## 🐳 Docker

### Construir Imagen

```bash
docker build -t franchise-api .
```

### Ejecutar Contenedor

```bash
docker run -p 8081:8081 \
  -e SPRING_DATA_MONGODB_URI=mongodb://host.docker.internal:27017/franchise-api-db \
  franchise-api
```

### Docker Compose Comandos Útiles

```bash
# Ver logs de un servicio específico
docker-compose logs -f app

# Ejecutar comando en contenedor
docker-compose exec app bash

# Escalar servicios
docker-compose up --scale app=2

# Reconstruir imagen específica
docker-compose build app
```

### Despliegue en Producción

#### Opción 1: Usando Variables de Entorno

```bash
# 1. Configurar variables de entorno
export MONGODB_URI="tu-mongodb-atlas-uri-completo"
export SPRING_PROFILES_ACTIVE="prod"

# 2. Compilar aplicación
mvn clean package -DskipTests

# 3. Ejecutar
java -jar target/franchise-api-*.jar
```

#### Opción 2: Docker con Archivo .env

```bash
# 1. Crear archivo .env (NO subir a Git)
cat > .env << EOF
MONGODB_URI=mongodb+srv://user:pass@cluster.mongodb.net/db
SPRING_PROFILES_ACTIVE=prod
PORT=8081
JAVA_OPTS=-Xmx1g -Xms512m -server
EOF

# 2. Desplegar con Docker Compose
docker-compose -f docker-compose.prod.yml --env-file .env up -d
```

#### Opción 3: Heroku

```bash
# 1. Instalar Heroku CLI y hacer login
heroku login

# 2. Crear aplicación
heroku create tu-franchise-api

# 3. Configurar variables de entorno
heroku config:set MONGODB_URI="tu-mongodb-atlas-uri"
heroku config:set SPRING_PROFILES_ACTIVE="prod"

# 4. Desplegar
git push heroku main
```

## ☁️ Despliegue en la Nube

### GitHub Container Registry

La aplicación se empaqueta automáticamente en Docker y se sube a GitHub Container Registry mediante GitHub Actions.

```bash
# La imagen estará disponible en:
# ghcr.io/tu-usuario/franchise-api:latest
```

### AWS ECS (Elastic Container Service)

```bash
# 1. Configurar AWS CLI
aws configure

# 2. Crear parámetros en Systems Manager
aws ssm put-parameter \
    --name "/franchise-api/mongodb-uri" \
    --value "tu-mongodb-atlas-uri" \
    --type "SecureString"

# 3. Ejecutar script de despliegue
chmod +x deploy/aws-ecs-deploy.sh
./deploy/aws-ecs-deploy.sh
```

### Kubernetes (GKE, EKS, AKS)

```bash
# 1. Configurar kubectl
kubectl config current-context

# 2. Crear namespace
kubectl create namespace franchise-api

# 3. Actualizar secret con tu MongoDB URI
kubectl create secret generic franchise-api-secrets \
    --from-literal=mongodb-uri="tu-mongodb-atlas-uri" \
    --namespace franchise-api

# 4. Desplegar aplicación
kubectl apply -f deploy/kubernetes/deployment.yaml -n franchise-api

# 5. Verificar despliegue
kubectl get pods -n franchise-api
kubectl get services -n franchise-api
```

### Google Cloud Run

```bash
# 1. Configurar gcloud
gcloud auth login
gcloud config set project tu-proyecto-id

# 2. Desplegar desde imagen
gcloud run deploy franchise-api \
    --image ghcr.io/tu-usuario/franchise-api:latest \
    --platform managed \
    --region us-central1 \
    --allow-unauthenticated \
    --port 8081 \
    --set-env-vars SPRING_PROFILES_ACTIVE=prod \
    --set-env-vars MONGODB_URI="tu-mongodb-atlas-uri"
```

### Azure Container Instances

```bash
# 1. Crear grupo de recursos
az group create --name franchise-api-rg --location eastus

# 2. Desplegar contenedor
az container create \
    --resource-group franchise-api-rg \
    --name franchise-api \
    --image ghcr.io/tu-usuario/franchise-api:latest \
    --dns-name-label franchise-api-unique \
    --ports 8081 \
    --environment-variables SPRING_PROFILES_ACTIVE=prod \
    --secure-environment-variables MONGODB_URI="tu-mongodb-atlas-uri"
```

## 📊 Monitoreo y Logs

### Logs de la Aplicación

```bash
# Con Docker Compose
docker-compose logs -f app

# En desarrollo local
tail -f logs/application.log
```

### MongoDB Logs

```bash
docker-compose logs -f mongodb
```

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver `LICENSE` para más detalles.

## 🆘 Solución de Problemas

### Error de Conexión a MongoDB

```bash
# Verificar que MongoDB esté ejecutándose
docker-compose ps

# Reiniciar servicios
docker-compose restart mongodb
```

### Puerto en Uso

```bash
# Verificar qué proceso usa el puerto 8081
netstat -tulpn | grep :8081

# Usar puerto diferente
docker-compose up -p 8082:8081
```

### Problemas de Memoria

```bash
# Aumentar memoria en docker-compose.yml
environment:
  JAVA_OPTS: "-Xmx1g -Xms512m"
```

## 📞 Soporte

Para reportar bugs o solicitar nuevas características, por favor crea un issue en el repositorio.

## 🔗 Enlaces Útiles

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring WebFlux](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
- [MongoDB Documentation](https://docs.mongodb.com/)
- [Docker Documentation](https://docs.docker.com/)
- [Swagger/OpenAPI](https://swagger.io/specification/)
