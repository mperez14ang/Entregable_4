package com.um.miplaylist.service;

import com.um.miplaylist.model.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests completos para VideoService.
 * Prueba todas las operaciones: agregar, eliminar, buscar, likes y favoritos.
 */
class VideoServiceTest {

    private VideoService videoService;

    @BeforeEach
    void setUp() {
        videoService = new VideoService();
    }

    @Test
    void testInicializacionConVideosDeEjemplo() {
        // Verificar que se inicializa con videos de ejemplo
        List<Video> videos = videoService.listarTodos();
        assertNotNull(videos);
        assertEquals(3, videos.size(), "Debería inicializar con 3 videos de ejemplo");
    }

    @Test
    void testAgregarVideo() {
        int cantidadInicial = videoService.contarVideos();

        Video nuevoVideo = new Video(null, "Test Video", "https://www.youtube.com/watch?v=test123");
        Video videoAgregado = videoService.agregarVideo(nuevoVideo);

        assertNotNull(videoAgregado.getId(), "El video debería tener un ID asignado");
        assertEquals(cantidadInicial + 1, videoService.contarVideos(), "Debería aumentar la cantidad de videos");
        assertTrue(videoService.buscarPorId(videoAgregado.getId()).isPresent(), "El video debería existir en la lista");
    }

    @Test
    void testAgregarVideoConId() {
        Video video = new Video(999L, "Video con ID", "https://www.youtube.com/watch?v=test456");
        videoService.agregarVideo(video);

        Optional<Video> videoEncontrado = videoService.buscarPorId(999L);
        assertTrue(videoEncontrado.isPresent(), "El video debería encontrarse por su ID");
        assertEquals("Video con ID", videoEncontrado.get().getNombre());
    }

    @Test
    void testEliminarVideoExistente() {
        List<Video> videos = videoService.listarTodos();
        Long idPrimerVideo = videos.get(0).getId();
        int cantidadInicial = videos.size();

        boolean resultado = videoService.eliminarVideo(idPrimerVideo);

        assertTrue(resultado, "Debería retornar true al eliminar un video existente");
        assertEquals(cantidadInicial - 1, videoService.contarVideos(), "Debería disminuir la cantidad de videos");
        assertFalse(videoService.buscarPorId(idPrimerVideo).isPresent(), "El video no debería existir después de eliminarlo");
    }

    @Test
    void testEliminarVideoNoExistente() {
        boolean resultado = videoService.eliminarVideo(99999L);
        assertFalse(resultado, "Debería retornar false al intentar eliminar un video que no existe");
    }

    @Test
    void testListarTodos() {
        List<Video> videos = videoService.listarTodos();

        assertNotNull(videos);
        assertFalse(videos.isEmpty(), "La lista no debería estar vacía");
        assertEquals(3, videos.size(), "Debería tener 3 videos iniciales");
    }

    @Test
    void testBuscarPorIdExistente() {
        List<Video> videos = videoService.listarTodos();
        Long idExistente = videos.get(0).getId();

        Optional<Video> resultado = videoService.buscarPorId(idExistente);

        assertTrue(resultado.isPresent(), "Debería encontrar el video");
        assertEquals(idExistente, resultado.get().getId());
    }

    @Test
    void testBuscarPorIdNoExistente() {
        Optional<Video> resultado = videoService.buscarPorId(99999L);
        assertFalse(resultado.isPresent(), "No debería encontrar un video con ID inexistente");
    }

    @Test
    void testIncrementarLikes() {
        List<Video> videos = videoService.listarTodos();
        Video video = videos.get(0);
        Long videoId = video.getId();
        int likesIniciales = video.getLikes();

        boolean resultado = videoService.incrementarLikes(videoId);

        assertTrue(resultado, "Debería retornar true al incrementar likes");

        Optional<Video> videoActualizado = videoService.buscarPorId(videoId);
        assertTrue(videoActualizado.isPresent());
        assertEquals(likesIniciales + 1, videoActualizado.get().getLikes(), "Los likes deberían incrementarse en 1");
    }

    @Test
    void testIncrementarLikesMultiplesVeces() {
        List<Video> videos = videoService.listarTodos();
        Video video = videos.get(0);
        Long videoId = video.getId();
        int likesIniciales = video.getLikes();

        videoService.incrementarLikes(videoId);
        videoService.incrementarLikes(videoId);
        videoService.incrementarLikes(videoId);

        Optional<Video> videoActualizado = videoService.buscarPorId(videoId);
        assertTrue(videoActualizado.isPresent());
        assertEquals(likesIniciales + 3, videoActualizado.get().getLikes(), "Los likes deberían incrementarse en 3");
    }

    @Test
    void testIncrementarLikesVideoNoExistente() {
        boolean resultado = videoService.incrementarLikes(99999L);
        assertFalse(resultado, "Debería retornar false al intentar dar like a un video inexistente");
    }

    @Test
    void testToggleFavoritoDeActivoAInactivo() {
        // Buscar un video que esté marcado como favorito
        List<Video> videos = videoService.listarTodos();
        Video videoFavorito = videos.stream()
                .filter(Video::isFavorito)
                .findFirst()
                .orElse(null);

        assertNotNull(videoFavorito, "Debería haber al menos un video favorito");

        Long videoId = videoFavorito.getId();
        boolean estadoInicial = videoFavorito.isFavorito();

        videoService.toggleFavorito(videoId);

        Optional<Video> videoActualizado = videoService.buscarPorId(videoId);
        assertTrue(videoActualizado.isPresent());
        assertEquals(!estadoInicial, videoActualizado.get().isFavorito(), "El estado de favorito debería cambiar");
    }

    @Test
    void testToggleFavoritoDeInactivoAActivo() {
        // Buscar un video que NO esté marcado como favorito
        List<Video> videos = videoService.listarTodos();
        Video videoNoFavorito = videos.stream()
                .filter(v -> !v.isFavorito())
                .findFirst()
                .orElse(null);

        assertNotNull(videoNoFavorito, "Debería haber al menos un video no favorito");

        Long videoId = videoNoFavorito.getId();
        assertFalse(videoNoFavorito.isFavorito(), "El video no debería ser favorito inicialmente");

        videoService.toggleFavorito(videoId);

        Optional<Video> videoActualizado = videoService.buscarPorId(videoId);
        assertTrue(videoActualizado.isPresent());
        assertTrue(videoActualizado.get().isFavorito(), "El video debería ser favorito después del toggle");
    }

    @Test
    void testToggleFavoritoVideoNoExistente() {
        boolean resultado = videoService.toggleFavorito(99999L);
        assertFalse(resultado, "Debería retornar false al intentar cambiar favorito de un video inexistente");
    }

    @Test
    void testContarVideos() {
        int cantidad = videoService.contarVideos();
        assertEquals(3, cantidad, "Debería contar correctamente los videos");

        videoService.agregarVideo(new Video(null, "Nuevo", "https://www.youtube.com/watch?v=nuevo"));
        assertEquals(4, videoService.contarVideos(), "Debería actualizar el conteo después de agregar");
    }

    @Test
    void testListarFavoritos() {
        List<Video> favoritos = videoService.listarFavoritos();

        assertNotNull(favoritos);
        assertTrue(favoritos.size() > 0, "Debería haber al menos un favorito en los datos iniciales");

        // Verificar que todos los videos en la lista son favoritos
        for (Video video : favoritos) {
            assertTrue(video.isFavorito(), "Todos los videos en la lista deberían ser favoritos");
        }
    }

    @Test
    void testListarFavoritosConCambios() {
        int favoritosIniciales = videoService.listarFavoritos().size();

        // Agregar un nuevo video y marcarlo como favorito
        Video nuevoVideo = new Video(null, "Nuevo Favorito", "https://www.youtube.com/watch?v=fav123");
        Video videoAgregado = videoService.agregarVideo(nuevoVideo);
        videoService.toggleFavorito(videoAgregado.getId());

        List<Video> favoritos = videoService.listarFavoritos();
        assertEquals(favoritosIniciales + 1, favoritos.size(), "Debería aumentar la cantidad de favoritos");
    }
}