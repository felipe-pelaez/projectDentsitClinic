package com.example.dentistClinic.VideoRequest;

import org.springframework.web.multipart.MultipartFile;

public class VideoRequest {
    private MultipartFile file;
    private String nombre;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
