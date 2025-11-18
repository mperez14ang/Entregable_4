package com.um.miplaylist.service;

import com.um.miplaylist.model.Video;
import com.um.miplaylist.repository.VideoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.function.Consumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;


@Service
public class VideoService {

    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);

    private final List<Video> videos;
    private final AtomicLong idGenerator;
    private final VideoRepository repository;


    @Autowired
    public VideoService(VideoRepository repository) {
        this.repository = repository;
        this.videos = new ArrayList<>();
        this.idGenerator = new AtomicLong(1);

        // Cargar datos persistidos o inicializar con ejemplos
        cargarDatos();
    }

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

    private void guardarDatos() {
        try {
            repository.guardar(videos);
            logger.debug("Datos guardados exitosamente en {}", repository.getRutaArchivo());
        } catch (IOException e) {
            logger.error("Error al guardar datos", e);
        }
    }

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


    public Video agregarVideo(Video video) {
        if (video.getId() == null) {
            video.setId(idGenerator.getAndIncrement());
        }
        videos.add(video);
        guardarDatos();
        return video;
    }


    public boolean eliminarVideo(Long id) {
        boolean eliminado = videos.removeIf(video -> video.getId().equals(id));
        if (eliminado) {
            guardarDatos();
        }
        return eliminado;
    }

    public List<Video> listarTodos() {
        return new ArrayList<>(videos);
    }


    public Optional<Video> buscarPorId(Long id) {
        return videos.stream()
                .filter(video -> video.getId().equals(id))
                .findFirst();
    }


//    public boolean incrementarLikes(Long id) {
//        Optional<Video> videoOpt = buscarPorId(id);
//        if (videoOpt.isPresent()) {
//            videoOpt.get().incrementarLikes();
//            guardarDatos();
//            return true;
//        }
//        return false;
//    }
//
//
//    public boolean toggleFavorito(Long id) {
//        Optional<Video> videoOpt = buscarPorId(id);
//        if (videoOpt.isPresent()) {
//            videoOpt.get().toggleFavorito();
//            guardarDatos();
//            return true;
//        }
//        return false;
//    }

    private boolean ejecutarOperacionSobreVideo(Long id, Consumer<Video> operacion) {
        Optional<Video> videoOpt = buscarPorId(id);
        if (videoOpt.isPresent()) {
            operacion.accept(videoOpt.get());
            guardarDatos();
            return true;
        }
        return false;
    }

    public boolean incrementarLikes(Long id) {
        return ejecutarOperacionSobreVideo(id, Video::incrementarLikes);
    }

    public boolean toggleFavorito(Long id) {
        return ejecutarOperacionSobreVideo(id, Video::toggleFavorito);
    }

    public int contarVideos() {
        return videos.size();
    }


    public List<Video> listarFavoritos() {
        return videos.stream()
                .filter(Video::isFavorito)
                .toList();
    }
}