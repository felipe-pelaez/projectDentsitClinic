package com.example.dentistClinic.service;

import com.example.dentistClinic.domain.Video;
import com.example.dentistClinic.repository.VideoRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class VideoService {

    @Autowired
    private VideoRespository videoRepository;

    @Transactional

    public void save(MultipartFile file, String name) throws IOException {
        Video video = new Video();
        video.setName(name);
        video.setSize(file.getSize());
        videoRepository.save(video);

        File videoFile = new File("C:/Users/felip/Desktop/Proyectos/Proyecto Clinica Odontologica/Videos/" + video.getId() + ".mp4");
        try (OutputStream outputStream = new FileOutputStream(videoFile)) {
            byte[] bytes = file.getBytes();
            outputStream.write(bytes);
        }
    }

    @Transactional(readOnly = true)
    public Video findById(Long id) {
        return videoRepository.findById(id).orElse(null);
    }

    @Transactional
    public List<Video> findAll()
    {
        return videoRepository.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        videoRepository.deleteById(id);
    }

}

