package com.um.miplaylist.controller;

import com.um.miplaylist.model.Video;
import com.um.miplaylist.service.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests completos para HomeController.
 * Prueba todos los endpoints: GET /, POST /agregar, POST /eliminar, POST /like, POST /favorito.
 */
@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VideoService videoService;

    @Test
    void testHomePage() throws Exception {
        // Preparar datos de prueba
        List<Video> videosSimulados = Arrays.asList(
            new Video(1L, "Video 1", "https://www.youtube.com/watch?v=abc123", 10, true),
            new Video(2L, "Video 2", "https://www.youtube.com/watch?v=def456", 5, false)
        );

        when(videoService.listarTodos()).thenReturn(videosSimulados);
        when(videoService.contarVideos()).thenReturn(2);
        when(videoService.listarFavoritos()).thenReturn(Arrays.asList(videosSimulados.get(0)));

        // Ejecutar y verificar
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("videos"))
                .andExpect(model().attributeExists("totalVideos"))
                .andExpect(model().attributeExists("videosFavoritos"))
                .andExpect(model().attributeExists("nuevoVideo"))
                .andExpect(model().attribute("totalVideos", 2))
                .andExpect(model().attribute("videosFavoritos", 1));

        verify(videoService, times(1)).listarTodos();
        verify(videoService, times(1)).contarVideos();
        verify(videoService, times(1)).listarFavoritos();
    }

    @Test
    void testAgregarVideoExitoso() throws Exception {
        Video videoNuevo = new Video(null, "Nuevo Video", "https://www.youtube.com/watch?v=test123");
        when(videoService.agregarVideo(any(Video.class))).thenReturn(videoNuevo);

        mockMvc.perform(post("/agregar")
                .param("nombre", "Nuevo Video")
                .param("link", "https://www.youtube.com/watch?v=test123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("success"));

        verify(videoService, times(1)).agregarVideo(any(Video.class));
    }

    @Test
    void testAgregarVideoSinNombre() throws Exception {
        mockMvc.perform(post("/agregar")
                .param("nombre", "")
                .param("link", "https://www.youtube.com/watch?v=test123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("error"));

        verify(videoService, never()).agregarVideo(any(Video.class));
    }

    @Test
    void testAgregarVideoSinLink() throws Exception {
        mockMvc.perform(post("/agregar")
                .param("nombre", "Video sin link")
                .param("link", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("error"));

        verify(videoService, never()).agregarVideo(any(Video.class));
    }

    @Test
    void testAgregarVideoConLinkInvalido() throws Exception {
        mockMvc.perform(post("/agregar")
                .param("nombre", "Video con link inválido")
                .param("link", "https://www.example.com/not-youtube"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("error"));

        verify(videoService, never()).agregarVideo(any(Video.class));
    }

    @Test
    void testEliminarVideoExistente() throws Exception {
        when(videoService.eliminarVideo(1L)).thenReturn(true);

        mockMvc.perform(post("/eliminar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("success"));

        verify(videoService, times(1)).eliminarVideo(1L);
    }

    @Test
    void testEliminarVideoNoExistente() throws Exception {
        when(videoService.eliminarVideo(999L)).thenReturn(false);

        mockMvc.perform(post("/eliminar/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("error"));

        verify(videoService, times(1)).eliminarVideo(999L);
    }

    @Test
    void testDarLikeExitoso() throws Exception {
        when(videoService.incrementarLikes(1L)).thenReturn(true);

        mockMvc.perform(post("/like/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("success"));

        verify(videoService, times(1)).incrementarLikes(1L);
    }

    @Test
    void testDarLikeVideoNoExistente() throws Exception {
        when(videoService.incrementarLikes(999L)).thenReturn(false);

        mockMvc.perform(post("/like/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("error"));

        verify(videoService, times(1)).incrementarLikes(999L);
    }

    @Test
    void testToggleFavoritoExitoso() throws Exception {
        when(videoService.toggleFavorito(1L)).thenReturn(true);

        mockMvc.perform(post("/favorito/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("success"));

        verify(videoService, times(1)).toggleFavorito(1L);
    }

    @Test
    void testToggleFavoritoVideoNoExistente() throws Exception {
        when(videoService.toggleFavorito(999L)).thenReturn(false);

        mockMvc.perform(post("/favorito/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("error"));

        verify(videoService, times(1)).toggleFavorito(999L);
    }

    @Test
    void testAgregarVideoConLinkYouTubeFormatoCorto() throws Exception {
        Video videoNuevo = new Video(null, "Video YouTube corto", "https://youtu.be/abc123");
        when(videoService.agregarVideo(any(Video.class))).thenReturn(videoNuevo);

        mockMvc.perform(post("/agregar")
                .param("nombre", "Video YouTube corto")
                .param("link", "https://youtu.be/abc123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("success"));

        verify(videoService, times(1)).agregarVideo(any(Video.class));
    }

    @Test
    void testAgregarVideoConLinkYouTubeFormatoEmbed() throws Exception {
        Video videoNuevo = new Video(null, "Video YouTube embed", "https://www.youtube.com/embed/xyz789");
        when(videoService.agregarVideo(any(Video.class))).thenReturn(videoNuevo);

        mockMvc.perform(post("/agregar")
                .param("nombre", "Video YouTube embed")
                .param("link", "https://www.youtube.com/embed/xyz789"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("success"));

        verify(videoService, times(1)).agregarVideo(any(Video.class));
    }
}