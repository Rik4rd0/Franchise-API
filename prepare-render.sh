#!/bin/bash

echo "🔧 Preparando proyecto para Render..."

# Verificar que los archivos necesarios existen
if [ ! -f "pom.xml" ]; then
    echo "❌ Error: pom.xml no encontrado"
    exit 1
fi

echo "🧪 Ejecutando tests locales..."
mvn test -q

if [ $? -ne 0 ]; then
    echo "❌ Tests fallaron. Corrígelos antes de desplegar."
    exit 1
fi

echo "🔨 Probando build local..."
mvn clean package -DskipTests -q

if [ $? -ne 0 ]; then
    echo "❌ Build falló. Revisa tu configuración."
    exit 1
fi

echo "✅ Proyecto listo para Render!"
echo "📝 Próximos pasos:"
echo "   1. Haz push a GitHub"
echo "   2. Conecta tu repo en render.com"
echo "   3. Render detectará automáticamente la configuración"