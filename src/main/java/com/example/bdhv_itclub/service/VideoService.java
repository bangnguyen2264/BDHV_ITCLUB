package com.example.bdhv_itclub.service;


import com.example.bdhv_itclub.dto.reponse.VideoDTO;
import com.example.bdhv_itclub.entity.Video;
import org.springframework.web.multipart.MultipartFile;

public interface VideoService {
    Video save(VideoDTO videoDto, MultipartFile videoFile);

    Video update(VideoDTO videoDto, MultipartFile videoFile);
}
