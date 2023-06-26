package com.example.dentistClinic.repository;

import com.example.dentistClinic.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRespository extends JpaRepository<Video,Long> {
}
