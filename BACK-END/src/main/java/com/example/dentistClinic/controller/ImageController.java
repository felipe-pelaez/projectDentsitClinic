package com.example.dentistClinic.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://127.0.0.1:5173", "http://127.0.0.1:5174" }, allowCredentials = "true")
@RestController
@RequestMapping("/images")
public class ImageController {


    @PostMapping()
    public ResponseEntity<String> addImages(@RequestParam("file") MultipartFile file) {
        String imagePath = "C:/Users/felip/Desktop/Proyectos/Proyecto Clinica Odontologica/images/";
        String uniqueFilename = UUID.randomUUID().toString() + ".png";
        String imageFilePath = imagePath + uniqueFilename;
        String imageFilePath2 = "http://localhost:8000/images/" + uniqueFilename;

        try (OutputStream outputStream = new FileOutputStream(imageFilePath)) {
            byte[] bytes = file.getBytes();
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(imageFilePath2);
        return ResponseEntity.ok(imageFilePath2);
    }

}
