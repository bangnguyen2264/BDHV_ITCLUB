package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.dto.reponse.VideoDTO;
import com.example.bdhv_itclub.entity.Video;
import com.example.bdhv_itclub.exception.BadRequestException;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.VideoRepository;
import com.example.bdhv_itclub.service.VideoService;
import com.example.bdhv_itclub.utils.UploadFile;
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
    private final UploadFile uploadFile;

    public VideoServiceImpl(VideoRepository videoRepository, UploadFile uploadFile) {
        this.videoRepository = videoRepository;
        this.uploadFile = uploadFile;
    }

    @Override
    public Video save(VideoDTO videoDto, MultipartFile videoFile) {
        Video video = new Video();
        String url = uploadFile.uploadFileOnCloudinary(videoFile);
        video.setUrl(url);

        LocalTime duration = getDurationVideo(url);
        video.setDuration(duration);

        video.setDescription(videoDto.getDescription());

        return videoRepository.save(video);
    }

    @Override
    public Video update(VideoDTO videoDto, MultipartFile videoFile) {
        Video videoDB = videoRepository.findById(videoDto.getId()).orElseThrow(() -> new NotFoundException("Video ID không tồn tại"));
        if (videoFile != null) {
            uploadFile.deleteVideoInCloudinary(videoDB.getUrl());
            String url = uploadFile.uploadFileOnCloudinary(videoFile);
            videoDB.setUrl(url);

            LocalTime duration = getDurationVideo(url);
            videoDB.setDuration(duration);

        }
        videoDB.setDescription(videoDto.getDescription());
        return videoRepository.save(videoDB);
    }

    private LocalTime getDurationVideo(String url) {
        try {
            URL url1 = new URL(url);
            MultimediaObject multimediaObject = new MultimediaObject(url1);
            MultimediaInfo multimediaInfo = multimediaObject.getInfo();

            long minutes = (multimediaInfo.getDuration() / 1000) / 60;
            long seconds = (multimediaInfo.getDuration() / 1000) % 60;

            return LocalTime.of(0, (int) minutes, (int) seconds);
        } catch (MalformedURLException | EncoderException e) {
            throw new BadRequestException("Link video không hợp lệ");
        }
    }
}
