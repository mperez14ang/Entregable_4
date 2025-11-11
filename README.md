# Mi Playlist Musical 🎵

Aplicación web para gestionar una playlist personal de videos musicales de YouTube. Desarrollada con Spring Boot, Thymeleaf y Bootstrap 5.

## Características

- ✅ **CRUD completo de videos**: Agregar, listar, eliminar videos
- ❤️ **Sistema de likes**: Dar "me gusta" a tus videos favoritos
- ⭐ **Marcadores de favoritos**: Marca videos como favoritos
- 🎬 **Videos embebidos**: Reproduce videos directamente desde la interfaz
- 📱 **Diseño responsive**: Interfaz moderna y adaptable a dispositivos móviles
- 🎨 **UI atractiva**: Diseño profesional con gradientes y animaciones

## Tecnologías Utilizadas

- **Backend**: Spring Boot 3.1.5
- **Frontend**: Thymeleaf + Bootstrap 5 + Bootstrap Icons
- **Testing**: JUnit 5 + Mockito
- **Build**: Maven
- **CI/CD**: Jenkins
- **Java**: JDK 17

## Requisitos Previos

- JDK 17 o superior
- Maven 3.6 o superior
- Git

## Instalación y Ejecución

### Opción 1: Usando scripts de deployment

#### En Mac/Linux:
```bash
./deploy-mac.sh
```

#### En Windows:
```batch
deploy-windows.bat
```

### Opción 2: Manual con Maven

1. **Clonar el repositorio**
```bash
git clone https://github.com/mperez14ang/Entregable_4.git
cd "Entregable 4"
```

2. **Compilar el proyecto**
```bash
mvn clean install
```

3. **Ejecutar tests**
```bash
mvn test
```

4. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

O ejecutar el JAR generado:
```bash
java -jar target/mi-playlist-1.0.0.jar
```

5. **Acceder a la aplicación**
```
http://localhost:8080
```

## Estructura del Proyecto

```
mi-playlist/
├── src/
│   ├── main/
│   │   ├── java/com/um/miplaylist/
│   │   │   ├── MiPlaylistApplication.java      # Clase principal
│   │   │   ├── controller/
│   │   │   │   └── HomeController.java          # Controlador principal
│   │   │   ├── model/
│   │   │   │   └── Video.java                   # Modelo de datos
│   │   │   └── service/
│   │   │       └── VideoService.java            # Lógica de negocio
│   │   └── resources/
│   │       ├── application.properties           # Configuración
│   │       └── templates/
│   │           └── index.html                   # Vista principal
│   └── test/
│       └── java/com/um/miplaylist/
│           ├── MiPlaylistApplicationTest.java   # Test de contexto
│           ├── controller/
│           │   └── HomeControllerTest.java      # Tests del controlador
│           └── service/
│               └── VideoServiceTest.java        # Tests del servicio
├── deploy-mac.sh                                # Script de deployment Mac
├── deploy-windows.bat                           # Script de deployment Windows
├── Jenkinsfile                                  # Pipeline CI/CD
├── pom.xml                                      # Configuración Maven
└── README.md                                    # Este archivo
```

## Uso de la Aplicación

### Agregar un Video

1. Completa el formulario en la parte superior:
   - **Nombre**: Título del video (ej: "Ed Sheeran - Shape of You")
   - **Link**: URL de YouTube (formatos soportados):
     - `https://www.youtube.com/watch?v=VIDEO_ID`
     - `https://youtu.be/VIDEO_ID`
     - `https://www.youtube.com/embed/VIDEO_ID`
2. Haz clic en "Agregar"

### Interactuar con Videos

- **❤️ Me gusta**: Incrementa el contador de likes
- **⭐ Favorito**: Marca/desmarca el video como favorito
- **🗑️ Eliminar**: Elimina el video de la playlist (requiere confirmación)

## Testing

El proyecto incluye tests completos:

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar solo tests del servicio
mvn test -Dtest=VideoServiceTest

# Ejecutar solo tests del controlador
mvn test -Dtest=HomeControllerTest

# Ver reporte de tests
mvn surefire-report:report
```

### Cobertura de Tests

- **VideoServiceTest**: 18 tests
  - Inicialización, agregar, eliminar, buscar
  - Incrementar likes, toggle favoritos
  - Listar todos y favoritos

- **HomeControllerTest**: 13 tests
  - Endpoint GET /
  - Endpoint POST /agregar (casos exitosos y de error)
  - Endpoints POST /eliminar, /like, /favorito

## CI/CD con Jenkins

### Configuración en Jenkins

1. **Crear nuevo Pipeline Job**
   - New Item → Pipeline → OK

2. **Configurar el Pipeline**
   - Definition: Pipeline script from SCM
   - SCM: Git
   - Repository URL: `https://github.com/mperez14ang/Entregable_4`
   - Branch: `*/master`
   - Script Path: `Jenkinsfile`

3. **Configurar Tools**
   - Maven: `Maven-3.9`
   - JDK: `JDK-17`

### Stages del Pipeline

1. **Checkout**: Obtiene el código del repositorio
2. **Build**: Compila el proyecto (`mvn clean compile`)
3. **Test**: Ejecuta los tests (`mvn test`)
4. **Package**: Empaqueta la aplicación (`mvn package`)
5. **Deploy**: Despliega la aplicación

## API Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Muestra la página principal con todos los videos |
| POST | `/agregar` | Agrega un nuevo video |
| POST | `/eliminar/{id}` | Elimina un video por ID |
| POST | `/like/{id}` | Incrementa likes de un video |
| POST | `/favorito/{id}` | Marca/desmarca como favorito |

## Modelo de Datos

### Video
```java
{
  "id": Long,           // ID único autogenerado
  "nombre": String,     // Título del video
  "link": String,       // URL de YouTube
  "likes": int,         // Contador de likes
  "favorito": boolean   // Estado de favorito
}
```

## Persistencia

La aplicación utiliza **almacenamiento en memoria** (ArrayList) con datos iniciales de ejemplo:
- The Weeknd - Blinding Lights
- Ed Sheeran - Shape of You
- Dua Lipa - Levitating

**Nota**: Los datos se reinician cada vez que se reinicia la aplicación.

## Solución de Problemas

### Puerto 8080 en uso
```bash
# Cambiar puerto en application.properties
server.port=8081
```

### Tests fallan
```bash
# Limpiar y recompilar
mvn clean install
```

### Error al ejecutar JAR
```bash
# Verificar versión de Java
java -version  # Debe ser JDK 17+
```

## Roadmap Futuro

- [ ] Persistencia con base de datos (H2/MySQL)
- [ ] Búsqueda y filtrado de videos
- [ ] Ordenamiento por likes/favoritos/fecha
- [ ] Autenticación de usuarios
- [ ] Múltiples playlists por usuario
- [ ] Modo oscuro

## Autor

Manuel Pérez - Universidad de Mendoza - Programación Avanzada

## Licencia

Este proyecto es parte de un trabajo académico para la materia de Programación Avanzada.

---

**Versión**: 1.0.0
**Última actualización**: Noviembre 2025