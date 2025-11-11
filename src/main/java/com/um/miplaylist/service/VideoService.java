package com.um.miplaylist.service;

import com.um.miplaylist.model.Video;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Servicio para gestionar la playlist de videos.
 * Utiliza almacenamiento en memoria (ArrayList) para persistencia simple.
 */
@Service
public class VideoService {

    private final List<Video> videos;
    private final AtomicLong idGenerator;

    /**
     * Constructor que inicializa la lista con videos de ejemplo.
     */
    public VideoService() {
        this.videos = new ArrayList<>();
        this.idGenerator = new AtomicLong(1);

        // Inicializar con videos de ejemplo (canciones populares)
        inicializarVideosDeEjemplo();
    }

    /**
     * Inicializa la playlist con videos musicales de ejemplo.
     */
    private void inicializarVideosDeEjemplo() {
        agregarVideo(new Video(
            idGenerator.getAndIncrement(),
            "The Weeknd - Blinding Lights",
            "https://www.youtube.com/watch?v=4NRXx6U8ABQ",
            42,
            true
        ));

        agregarVideo(new Video(
            idGenerator.getAndIncrement(),
            "Ed Sheeran - Shape of You",
            "https://www.youtube.com/watch?v=JGwWNGJdvx8",
            28,
            false
        ));

        agregarVideo(new Video(
            idGenerator.getAndIncrement(),
            "Dua Lipa - Levitating",
            "https://www.youtube.com/watch?v=TUVcZfQe-Kw",
            15,
            true
        ));
    }

    /**
     * Agrega un nuevo video a la playlist.
     *
     * @param video Video a agregar
     * @return El video agregado con su ID asignado
     */
    public Video agregarVideo(Video video) {
        if (video.getId() == null) {
            video.setId(idGenerator.getAndIncrement());
        }
        videos.add(video);
        return video;
    }

    /**
     * Elimina un video de la playlist por su ID.
     *
     * @param id ID del video a eliminar
     * @return true si se eliminó, false si no se encontró
     */
    public boolean eliminarVideo(Long id) {
        return videos.removeIf(video -> video.getId().equals(id));
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
     * Incrementa los likes de un video.
     *
     * @param id ID del video
     * @return true si se incrementó, false si no se encontró el video
     */
    public boolean incrementarLikes(Long id) {
        Optional<Video> videoOpt = buscarPorId(id);
        if (videoOpt.isPresent()) {
            videoOpt.get().incrementarLikes();
            return true;
        }
        return false;
    }

    /**
     * Cambia el estado de favorito de un video.
     *
     * @param id ID del video
     * @return true si se cambió, false si no se encontró el video
     */
    public boolean toggleFavorito(Long id) {
        Optional<Video> videoOpt = buscarPorId(id);
        if (videoOpt.isPresent()) {
            videoOpt.get().toggleFavorito();
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