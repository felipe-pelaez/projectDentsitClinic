package com.example.dentistClinic.controller;

import com.example.dentistClinic.domain.Video;
import com.example.dentistClinic.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1:5173", allowCredentials = "true")
@RestController
@RequestMapping("/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping("/upload")
    public ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file, @RequestParam("name") String nombre) {
        try {
            videoService.save(file, nombre);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Video> findById(@PathVariable Long id) {
        Video video = videoService.findById(id);
        if (video != null) {
            return ResponseEntity.ok(video);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Video>> traerTodosVideos() {
        List<Video> videos = videoService.findAll();
        if (videos.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(videos);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        videoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
