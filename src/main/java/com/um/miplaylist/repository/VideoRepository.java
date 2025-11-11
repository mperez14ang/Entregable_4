package com.um.miplaylist.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.um.miplaylist.model.Video;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para persistir videos en archivo JSON.
 * Guarda y carga automáticamente la playlist desde playlist.json
 */
@Repository
public class VideoRepository {

    private static final String FILE_PATH = "playlist.json";
    private final ObjectMapper objectMapper;
    private final File dataFile;

    public VideoRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.dataFile = new File(FILE_PATH);
    }

    /**
     * Guarda la lista de videos en el archivo JSON.
     *
     * @param videos Lista de videos a guardar
     * @throws IOException Si hay error al escribir el archivo
     */
    public void guardar(List<Video> videos) throws IOException {
        objectMapper.writeValue(dataFile, videos);
    }

    /**
     * Carga la lista de videos desde el archivo JSON.
     * Si el archivo no existe, retorna una lista vacía.
     *
     * @return Lista de videos cargada desde el archivo
     * @throws IOException Si hay error al leer el archivo
     */
    public List<Video> cargar() throws IOException {
        if (!dataFile.exists()) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(dataFile, new TypeReference<List<Video>>() {});
        } catch (IOException e) {
            // Si el archivo está corrupto o vacío, retornar lista vacía
            return new ArrayList<>();
        }
    }

    /**
     * Verifica si existe el archivo de datos.
     *
     * @return true si existe el archivo, false si no
     */
    public boolean existeArchivo() {
        return dataFile.exists();
    }

    /**
     * Elimina el archivo de datos (útil para tests).
     *
     * @return true si se eliminó, false si no existía
     */
    public boolean eliminarArchivo() {
        if (dataFile.exists()) {
            return dataFile.delete();
        }
        return false;
    }

    /**
     * Obtiene la ruta del archivo de datos.
     *
     * @return Ruta del archivo
     */
    public String getRutaArchivo() {
        return dataFile.getAbsolutePath();
    }
}