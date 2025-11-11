package com.um.miplaylist.controller;

import com.um.miplaylist.model.Video;
import com.um.miplaylist.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador principal para gestionar la playlist de videos musicales.
 * Maneja todas las operaciones CRUD: listar, agregar, eliminar, likes y favoritos.
 */
@Controller
public class HomeController {

    private final VideoService videoService;

    @Autowired
    public HomeController(VideoService videoService) {
        this.videoService = videoService;
    }

    /**
     * Muestra la página principal con todos los videos de la playlist.
     *
     * @param model Modelo para pasar datos a la vista
     * @return Nombre de la vista index.html
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("videos", videoService.listarTodos());
        model.addAttribute("totalVideos", videoService.contarVideos());
        model.addAttribute("videosFavoritos", videoService.listarFavoritos().size());
        model.addAttribute("nuevoVideo", new Video());
        return "index";
    }

    /**
     * Agrega un nuevo video a la playlist.
     *
     * @param video Video a agregar (nombre y link)
     * @param redirectAttributes Atributos para mensaje flash
     * @return Redirección a la página principal
     */
    @PostMapping("/agregar")
    public String agregarVideo(@ModelAttribute Video video, RedirectAttributes redirectAttributes) {
        try {
            if (video.getNombre() == null || video.getNombre().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "El nombre del video es obligatorio");
                return "redirect:/";
            }

            if (video.getLink() == null || video.getLink().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "El link del video es obligatorio");
                return "redirect:/";
            }

            // Verificar que el link sea válido de YouTube
            Video videoTemp = new Video(null, "", video.getLink());
            if (videoTemp.extraerYouTubeId() == null) {
                redirectAttributes.addFlashAttribute("error", "El link debe ser un video válido de YouTube");
                return "redirect:/";
            }

            videoService.agregarVideo(video);
            redirectAttributes.addFlashAttribute("success", "Video agregado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al agregar el video: " + e.getMessage());
        }

        return "redirect:/";
    }

    /**
     * Elimina un video de la playlist.
     *
     * @param id ID del video a eliminar
     * @param redirectAttributes Atributos para mensaje flash
     * @return Redirección a la página principal
     */
    @PostMapping("/eliminar/{id}")
    public String eliminarVideo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (videoService.eliminarVideo(id)) {
            redirectAttributes.addFlashAttribute("success", "Video eliminado exitosamente");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se encontró el video a eliminar");
        }
        return "redirect:/";
    }

    /**
     * Incrementa los likes de un video.
     *
     * @param id ID del video
     * @param redirectAttributes Atributos para mensaje flash
     * @return Redirección a la página principal
     */
    @PostMapping("/like/{id}")
    public String darLike(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (videoService.incrementarLikes(id)) {
            redirectAttributes.addFlashAttribute("success", "Like agregado");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se encontró el video");
        }
        return "redirect:/";
    }

    /**
     * Marca o desmarca un video como favorito.
     *
     * @param id ID del video
     * @param redirectAttributes Atributos para mensaje flash
     * @return Redirección a la página principal
     */
    @PostMapping("/favorito/{id}")
    public String toggleFavorito(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (videoService.toggleFavorito(id)) {
            redirectAttributes.addFlashAttribute("success", "Estado de favorito actualizado");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se encontró el video");
        }
        return "redirect:/";
    }
}