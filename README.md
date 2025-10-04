# Franchise  Management API

> **Descripción breve:**  
> API RESTful para la gestión de franquicias, sucursales y productos. Permite crear, consultar y administrar franquicias, sucursales y productos asociados, así como consultar productos con mayor stock. Desarrollada con Spring Boot WebFlux y MongoDB, documentada con Swagger y lista para ejecutarse en Docker.

## 📝 Instrucciones para Evaluar la Solución

1. **Preparar el entorno**  
   - Clona el repositorio o descarga el proyecto.
   - El archivo `.env.example` contiene un ejemplo de las variables de entorno necesarias.
   - Copia el archivo `.env.example` como `.env` y colócalo en la raíz del proyecto.

2. **Ejecutar la aplicación en Docker**  
   - Asegúrate de tener Docker instalado.
   - Ejecuta el contenedor usando la imagen publicada en Docker Hub:  
     [`ricardo026/franchise-api`](https://hub.docker.com/r/ricardo026/franchise-api)
   - Usa el siguiente comando (ajusta las variables según el `.env`) que las encontrara en el archivo `.env.example`:

     ```bash
     docker run -d \
--name franchise-api \
-p 8081:8081 \
-e SPRING_PROFILES_ACTIVE=dev \
-e DB_PASSWORD:{ DB_PASSWORD }\
-e "MONGODB_URI=mongodb+srv://rickmartinezbanda_db_user:{ DB_PASSWORD }@franchise-api-dev.rz2dpuc.mongodb.net/franchise-api-db-dev?retryWrites=true&w=majority&appName=franchise-api-dev" \
ricardo026/franchise-api:latest
     ```

   - El puerto expuesto es **8081**.

3. **Probar la funcionalidad**  
   - Accede a la documentación interactiva en Swagger:  
     [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
   - Usa Swagger para guiarte y probar todos los endpoints y funcionalidades.

4. **Variables de entorno**  
   - La aplicación utiliza variables como:
     - `DB_PASSWORD`
     - `MONGODB_URI`
     - `SPRING_PROFILES_ACTIVE`
     - `PORT` (por defecto 8081)
   - Todas deben estar definidas en el archivo `.env` en la raíz.

5. **Scripts de ejecución**  
   - Puedes ejecutar los scripts incluidos en la carpeta `scripts` para facilitar la ambientación.

---

> **Nota:**  
> La imagen Docker está publicada en Docker Hub y lista para usarse.  
> Solo necesitas el archivo `.env` y seguir las instrucciones anteriores para levantar la API y evaluarla correctamente.

---

## 🚩 Presentación de la Prueba Técnica

Esta aplicación está **empaquetada en Docker** y lista para ejecutarse en cualquier entorno con Docker instalado. No necesitas instalar Java, Maven ni configurar nada adicional. Solo sigue los pasos y tendrás la API corriendo en segundos.

---

## 🐳 Ejecución Rápida con Docker

> **Puerto principal:** La API se expone por defecto en el puerto **8081**.

### 1. Prerrequisitos

- Tener **Docker** instalado ([Descargar Docker Desktop](https://www.docker.com/products/docker-desktop/))
- Obtener las credenciales de MongoDB Atlas (o usar las de prueba si están incluidas en `.env`)

### 2. Crear archivo `.env`
Crea un archivo `.env` en la raíz del proyecto con las siguientes variables (ajusta según tus credenciales o toma el .env.example):
```
DB_PASSWORD=tu_password_mongodb
MONGODB_URI=mongodb+srv://usuario:password@cluster.mongodb.net/franchise-api-db
```

#### Verificar estado

```bash
curl http://localhost:8081/actuator/health
```

#### Ver logs en tiempo real

```bash
docker logs -f franchise-api
```

#### Detener y eliminar el contenedor

```bash
docker stop franchise-api && docker rm franchise-api
```

---

## 🚀 Características

- **Arquitectura Hexagonal**: Separación clara entre dominio, aplicación e infraestructura
- **Programación Reactiva**: Implementada con Spring WebFlux y MongoDB Reactive
- **Base de Datos**: MongoDB con Spring Data MongoDB Reactive
- **Documentación API**: Swagger/OpenAPI 3.0
- **Testing**: Pruebas unitarias
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
git clone https://github.com/Rik4rd0/Franchise-API.git
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

## 📦 Empaquetado y entrega

La aplicación se **empaqueta usando Docker** y los scripts ubicados en la carpeta `scripts`.

- El archivo `.env` es **obligatorio** y debe contener las variables de entorno necesarias, especialmente `DB_PASSWORD`.
- Para construir y ejecutar la aplicación en modo desarrollo o producción, utiliza el script:
  ```bash
  ./scripts/build-and-run.sh [dev|prod]
  ```
- Para ejecutar rápidamente en modo local con MongoDB Atlas, usa:
  ```bash
  ./scripts/run-local.sh
  ```

---

## ⚙️ Variables de entorno

Las variables principales son:

- `DB_PASSWORD`: Contraseña de la base de datos MongoDB. Se toma del archivo `.env` y se pasa al contenedor Docker.
- `PORT`: Puerto en el que se expone la API (por defecto `8081`).
- `SPRING_PROFILES_ACTIVE`: Perfil de Spring Boot (`dev`, `prod`, etc.).

Ejemplo de archivo `.env`:
```
DB_PASSWORD=tu_password_mongodb
PORT=8081
SPRING_PROFILES_ACTIVE=prod
MONGODB_URI=mongodb+srv://usuario:password@cluster.mongodb.net/franchise-api-db
```

---

## 📂 Ubicación de los scripts y comandos de ejecución

Los scripts para ejecutar la aplicación se encuentran en la carpeta `scripts` en la raíz del proyecto.

### Comandos disponibles:

- **Ejecutar en modo desarrollo con Docker Compose:**
  ```bash
  ./scripts/docker-compose-run.sh dev
  ```

- **Ejecutar en modo producción con Docker Compose:**
  ```bash
  ./scripts/docker-compose-run.sh prod
  ```

- **Ejecutar localmente con MongoDB Atlas:**
  ```bash
  ./scripts/run-local.sh
  ```

- **Ejecutar en modo producción con MongoDB Atlas:**
  ```bash
  ./scripts/run-prod.sh
  ```

Asegúrate de tener el archivo `.env` correctamente configurado en la raíz antes de ejecutar cualquier script.

---

## 🛠️ Configuraciones de desarrollo y local

- Para desarrollo, se utiliza el perfil `dev` y el archivo `application-dev.properties`.
- Para ejecución local, usa el script `run-local.sh` y el perfil `dev`.
- El archivo `application.properties` define la configuración base y puede usarse para desarrollo local si no se especifica el perfil.

---

## 🗑️ Archivos de configuración no utilizados

Si existen archivos como `application-prod.properties` y no se usan, puedes eliminarlos para mantener el proyecto limpio.

---

## 🏃‍♂️ Scripts recomendados

- **Construir y ejecutar (dev o prod):**
  ```bash
  ./scripts/build-and-run.sh [dev|prod]
  ```
- **Ejecución rápida local con MongoDB Atlas:**
  ```bash
  ./scripts/run-local.sh
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

# Crear archivo .env (no incluido)
echo "MONGODB_URI=mongodb+srv://user:pass@cluster.mongodb.net/db" > .env
echo "SPRING_PROFILES_ACTIVE=prod" >> .env

# Usar con Docker Compose
docker-compose -f docker-compose.prod.yml --env-file .env up
```

### Health Checks

La aplicación incluye endpoints de salud:

- **Health**: `GET /actuator/health`
- **Info**: `GET /actuator/info`


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
`