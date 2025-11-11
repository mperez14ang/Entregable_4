package com.um.miplaylist.model;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Modelo que representa un video musical de YouTube en la playlist.
 * Contiene información básica del video, likes y estado de favorito.
 */
public class Video {

    private Long id;
    private String nombre;
    private String link;
    private int likes;
    private boolean favorito;

    // Constructor vacío
    public Video() {
    }

    // Constructor completo
    public Video(Long id, String nombre, String link, int likes, boolean favorito) {
        this.id = id;
        this.nombre = nombre;
        this.link = link;
        this.likes = likes;
        this.favorito = favorito;
    }

    // Constructor sin likes y favorito (valores por defecto)
    public Video(Long id, String nombre, String link) {
        this.id = id;
        this.nombre = nombre;
        this.link = link;
        this.likes = 0;
        this.favorito = false;
    }

    /**
     * Extrae el ID de YouTube del link proporcionado.
     * Soporta formatos:
     * - https://www.youtube.com/watch?v=VIDEO_ID
     * - https://youtu.be/VIDEO_ID
     * - https://www.youtube.com/embed/VIDEO_ID
     *
     * @return El ID del video de YouTube, o null si no se puede extraer
     */
    public String extraerYouTubeId() {
        if (link == null || link.isEmpty()) {
            return null;
        }

        // Patrón para varios formatos de YouTube
        String[] patterns = {
            "(?<=watch\\?v=)[^#\\&\\?]*",  // youtube.com/watch?v=VIDEO_ID
            "(?<=youtu.be/)[^#\\&\\?]*",    // youtu.be/VIDEO_ID
            "(?<=embed/)[^#\\&\\?]*"        // youtube.com/embed/VIDEO_ID
        };

        for (String pattern : patterns) {
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(link);
            if (matcher.find()) {
                return matcher.group();
            }
        }

        return null;
    }

    /**
     * Incrementa el contador de likes en 1.
     */
    public void incrementarLikes() {
        this.likes++;
    }

    /**
     * Cambia el estado de favorito (de true a false o viceversa).
     */
    public void toggleFavorito() {
        this.favorito = !this.favorito;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(id, video.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", link='" + link + '\'' +
                ", likes=" + likes +
                ", favorito=" + favorito +
                '}';
    }
}