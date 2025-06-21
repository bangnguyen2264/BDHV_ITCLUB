package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.dto.request.VideoDTO;
import com.example.bdhv_itclub.entity.Video;
import com.example.bdhv_itclub.exception.BadRequestException;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.VideoRepository;
import com.example.bdhv_itclub.service.VideoService;
import com.example.bdhv_itclub.utils.UploadFileUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;

@Transactional
@Service
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;
    private final UploadFileUtil uploadFile;

    public VideoServiceImpl(VideoRepository videoRepository, UploadFileUtil uploadFile) {
        this.videoRepository = videoRepository;
        this.uploadFile = uploadFile;
    }

    // Ok
    @Override
    public Video save(VideoDTO videoDTO, MultipartFile videoFile) {
        Video video = new Video();
        String url = uploadFile.uploadFileOnCloudinary(videoFile);
        video.setUrl(url);
        LocalTime duration = getVideoDuration(url);
        video.setDuration(duration);
        video.setDescription(videoDTO.getDescription());
        return videoRepository.save(video);
    }

    // Ok
    @Override
    public Video update(VideoDTO videoDTO, MultipartFile videoFile) {
        Video video = videoRepository.findById(videoDTO.getId()).orElseThrow(() -> new NotFoundException("Mã video không tồn tại"));
        if (videoFile != null) {
            uploadFile.deleteVideoInCloudinary(video.getUrl());
            String url = uploadFile.uploadFileOnCloudinary(videoFile);
            video.setUrl(url);
            LocalTime duration = getVideoDuration(url);
            video.setDuration(duration);
        }
        video.setDescription(videoDTO.getDescription());
        return videoRepository.save(video);
    }

    // Ok
    private LocalTime getVideoDuration(String url) {
        try {
            URL tempUrl = new URL(url);
            MultimediaObject multimediaObject = new MultimediaObject(tempUrl);
            MultimediaInfo multimediaInfo = multimediaObject.getInfo();
            long minutes = (multimediaInfo.getDuration() / 1000) / 60;
            long seconds = (multimediaInfo.getDuration() / 1000) % 60;
            return LocalTime.of(0, (int) minutes, (int) seconds);
        } catch (MalformedURLException | EncoderException e) {
            throw new BadRequestException("Đường dẫn video không hợp lệ");
        }
    }
}