

#!/bin/bash

###############################################################################
# Script de Deployment para Mac/Linux - Mi Playlist Musical
# Este script compila el proyecto y ejecuta la aplicación Spring Boot
###############################################################################

echo "=================================================="
echo "  Mi Playlist Musical - Deployment Script (Mac)"
echo "=================================================="
echo ""

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Verificar que Maven está instalado
echo -e "${YELLOW}[1/3] Verificando Maven...${NC}"
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}ERROR: Maven no está instalado${NC}"
    echo "Por favor instala Maven: brew install maven"
    exit 1
fi
echo -e "${GREEN}✓ Maven encontrado: $(mvn -version | head -n 1)${NC}"
echo ""

# Limpiar y compilar el proyecto
echo -e "${YELLOW}[2/3] Compilando el proyecto...${NC}"
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo -e "${RED}ERROR: La compilación falló${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Compilación exitosa${NC}"
echo ""

# Ejecutar la aplicación
echo -e "${YELLOW}[3/3] Iniciando la aplicación...${NC}"
echo -e "${GREEN}La aplicación estará disponible en: http://localhost:8080${NC}"
echo -e "${YELLOW}Presiona Ctrl+C para detener la aplicación${NC}"
echo ""

java -jar target/mi-playlist-1.0.0.jar