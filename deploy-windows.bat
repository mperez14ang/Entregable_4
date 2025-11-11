@echo off
REM ###########################################################################
REM Script de Deployment para Windows - Mi Playlist Musical
REM Este script compila el proyecto y ejecuta la aplicación Spring Boot
REM ###########################################################################

echo ==================================================
echo   Mi Playlist Musical - Deployment Script (Windows)
echo ==================================================
echo.

REM Verificar que Maven está instalado
echo [1/3] Verificando Maven...
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven no esta instalado
    echo Por favor instala Maven desde: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)
mvn -version | findstr /C:"Apache Maven"
echo.

REM Limpiar y compilar el proyecto
echo [2/3] Compilando el proyecto...
call mvn clean package -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: La compilacion fallo
    pause
    exit /b 1
)
echo Compilacion exitosa
echo.

REM Ejecutar la aplicación
echo [3/3] Iniciando la aplicacion...
echo La aplicacion estara disponible en: http://localhost:8080
echo Presiona Ctrl+C para detener la aplicacion
echo.

java -jar target\mi-playlist-1.0.0.jar

pause