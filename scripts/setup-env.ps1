# Script PowerShell para configurar variables de entorno para Franchise API
# Este script debe ser ejecutado antes de desplegar la aplicación en Windows

Write-Host "Configurando variables de entorno para Franchise API..." -ForegroundColor Green

# Variables de entorno requeridas
$env:MONGODB_URI = "mongodb+srv://rickmartinezbanda_db_user:5SedZCxKjx8mrhf4@franchise-api-dev.rz2dpuc.mongodb.net/Franchise-API-db?retryWrites=true&w=majority&appName=franchise-api-dev"
$env:SPRING_PROFILES_ACTIVE = "prod"
$env:PORT = "8081"

# Variables opcionales para optimización
$env:JAVA_OPTS = "-Xmx512m -Xms256m -server"

Write-Host "Variables de entorno configuradas:" -ForegroundColor Yellow
Write-Host "  - SPRING_PROFILES_ACTIVE: $env:SPRING_PROFILES_ACTIVE" -ForegroundColor White
Write-Host "  - PORT: $env:PORT" -ForegroundColor White
Write-Host "  - MONGODB_URI: [CONFIGURADO - CREDENCIALES OCULTAS]" -ForegroundColor White
Write-Host "  - JAVA_OPTS: $env:JAVA_OPTS" -ForegroundColor White

Write-Host "`nPara ejecutar la aplicación:" -ForegroundColor Green
Write-Host "  java `$env:JAVA_OPTS -jar target/franchise-api-*.jar" -ForegroundColor Cyan

# Opcional: Guardar variables en archivo .env para Docker
$envContent = @"
MONGODB_URI=mongodb+srv://rickmartinezbanda_db_user:5SedZCxKjx8mrhf4@franchise-api-dev.rz2dpuc.mongodb.net/Franchise-API-db?retryWrites=true&w=majority&appName=franchise-api-dev
SPRING_PROFILES_ACTIVE=prod
PORT=8081
JAVA_OPTS=-Xmx512m -Xms256m -server
"@

Set-Content -Path ".env" -Value $envContent -Encoding UTF8
Write-Host "`nArchivo .env creado para uso con Docker" -ForegroundColor Green
