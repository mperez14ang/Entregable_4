package com.um.miplaylist.service;

import com.um.miplaylist.model.Video;
import com.um.miplaylist.repository.VideoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Servicio para gestionar la playlist de videos.
 * Utiliza VideoRepository para persistir datos en JSON.
 * Los cambios se guardan automáticamente después de cada operación.
 */
@Service
public class VideoService {

    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);

    private final List<Video> videos;
    private final AtomicLong idGenerator;
    private final VideoRepository repository;

    /**
     * Constructor que inicializa la lista cargando datos persistidos
     * o creando videos de ejemplo si es la primera ejecución.
     */
    @Autowired
    public VideoService(VideoRepository repository) {
        this.repository = repository;
        this.videos = new ArrayList<>();
        this.idGenerator = new AtomicLong(1);

        // Cargar datos persistidos o inicializar con ejemplos
        cargarDatos();
    }

    /**
     * Carga los datos desde el archivo JSON.
     * Si no existe el archivo, inicializa con videos de ejemplo.
     */
    private void cargarDatos() {
        try {
            if (repository.existeArchivo()) {
                List<Video> videosCargados = repository.cargar();
                if (!videosCargados.isEmpty()) {
                    videos.addAll(videosCargados);

                    // Ajustar el generador de IDs al máximo ID existente
                    long maxId = videos.stream()
                            .map(Video::getId)
                            .max(Long::compareTo)
                            .orElse(0L);
                    idGenerator.set(maxId + 1);

                    logger.info("Cargados {} videos desde {}", videos.size(), repository.getRutaArchivo());
                    return;
                }
            }

            // Si no hay datos, inicializar con ejemplos
            inicializarVideosDeEjemplo();
            guardarDatos();
            logger.info("Inicializados {} videos de ejemplo", videos.size());

        } catch (IOException e) {
            logger.error("Error al cargar datos, inicializando con ejemplos", e);
            inicializarVideosDeEjemplo();
        }
    }

    /**
     * Guarda los datos actuales en el archivo JSON.
     */
    private void guardarDatos() {
        try {
            repository.guardar(videos);
            logger.debug("Datos guardados exitosamente en {}", repository.getRutaArchivo());
        } catch (IOException e) {
            logger.error("Error al guardar datos", e);
        }
    }

    /**
     * Inicializa la playlist con videos musicales de ejemplo.
     */
    private void inicializarVideosDeEjemplo() {
        videos.clear();

        videos.add(new Video(
            idGenerator.getAndIncrement(),
            "The Weeknd - Blinding Lights",
            "https://www.youtube.com/watch?v=4NRXx6U8ABQ",
            42,
            true
        ));

        videos.add(new Video(
            idGenerator.getAndIncrement(),
            "Ed Sheeran - Shape of You",
            "https://www.youtube.com/watch?v=JGwWNGJdvx8",
            28,
            false
        ));

        videos.add(new Video(
            idGenerator.getAndIncrement(),
            "Dua Lipa - Levitating",
            "https://www.youtube.com/watch?v=TUVcZfQe-Kw",
            15,
            true
        ));
    }

    /**
     * Agrega un nuevo video a la playlist y guarda los cambios.
     *
     * @param video Video a agregar
     * @return El video agregado con su ID asignado
     */
    public Video agregarVideo(Video video) {
        if (video.getId() == null) {
            video.setId(idGenerator.getAndIncrement());
        }
        videos.add(video);
        guardarDatos();
        return video;
    }

    /**
     * Elimina un video de la playlist por su ID y guarda los cambios.
     *
     * @param id ID del video a eliminar
     * @return true si se eliminó, false si no se encontró
     */
    public boolean eliminarVideo(Long id) {
        boolean eliminado = videos.removeIf(video -> video.getId().equals(id));
        if (eliminado) {
            guardarDatos();
        }
        return eliminado;
    }

    /**
     * Obtiene todos los videos de la playlist.
     *
     * @return Lista de todos los videos
     */
    public List<Video> listarTodos() {
        return new ArrayList<>(videos);
    }

    /**
     * Busca un video por su ID.
     *
     * @param id ID del video a buscar
     * @return Optional con el video si se encuentra, vacío si no
     */
    public Optional<Video> buscarPorId(Long id) {
        return videos.stream()
                .filter(video -> video.getId().equals(id))
                .findFirst();
    }

    /**
     * Incrementa los likes de un video y guarda los cambios.
     *
     * @param id ID del video
     * @return true si se incrementó, false si no se encontró el video
     */
    public boolean incrementarLikes(Long id) {
        Optional<Video> videoOpt = buscarPorId(id);
        if (videoOpt.isPresent()) {
            videoOpt.get().incrementarLikes();
            guardarDatos();
            return true;
        }
        return false;
    }

    /**
     * Cambia el estado de favorito de un video y guarda los cambios.
     *
     * @param id ID del video
     * @return true si se cambió, false si no se encontró el video
     */
    public boolean toggleFavorito(Long id) {
        Optional<Video> videoOpt = buscarPorId(id);
        if (videoOpt.isPresent()) {
            videoOpt.get().toggleFavorito();
            guardarDatos();
            return true;
        }
        return false;
    }

    /**
     * Obtiene el número total de videos en la playlist.
     *
     * @return Cantidad de videos
     */
    public int contarVideos() {
        return videos.size();
    }

    /**
     * Obtiene todos los videos marcados como favoritos.
     *
     * @return Lista de videos favoritos
     */
    public List<Video> listarFavoritos() {
        return videos.stream()
                .filter(Video::isFavorito)
                .toList();
    }
}