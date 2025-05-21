package com.example.bdhv_itclub.service.impl;


import com.example.bdhv_itclub.dto.reponse.QAResponse;
import com.example.bdhv_itclub.dto.request.QARequest;
import com.example.bdhv_itclub.entity.Lesson;
import com.example.bdhv_itclub.entity.QA;
import com.example.bdhv_itclub.entity.User;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.LessonRepository;
import com.example.bdhv_itclub.repository.QARepository;
import com.example.bdhv_itclub.repository.UserRepository;
import com.example.bdhv_itclub.service.QAService;
import com.example.bdhv_itclub.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Transactional
@Service
public class QAServiceImpl implements QAService {
    private final ModelMapper modelMapper;
    private final LessonRepository lessonRepository;
    private final QARepository qaRepository;
    private final UserRepository userRepository;

    public QAServiceImpl(ModelMapper modelMapper, LessonRepository lessonRepository, QARepository qaRepository, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.lessonRepository = lessonRepository;
        this.qaRepository = qaRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<QAResponse> listAll(Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson ID không tồn tại"));
        List<QA> listQAs = qaRepository.findAllByLesson(lesson.getId());

        return listQAs.stream().sorted(Comparator.comparing(QA::getCreatedAt).reversed()).map(this::convertToQAResponse).toList();
    }

    @Override
    public List<QAResponse> listAllForAdmin() {
        List<QA> listQAs = qaRepository.findAll();
        return listQAs.stream().sorted(Comparator.comparing(QA::getCreatedAt).reversed()).map(this::convertToQAResponse1).toList();
    }

    @Override
    public QAResponse createQA(QARequest qaRequest) {
        User user = userRepository.findById(qaRequest.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User ID không tồn tại"));

        Lesson lesson = lessonRepository.findById(qaRequest.getLessonId()).orElseThrow(() -> new NotFoundException("Lesson ID không tồn tại"));

        QA qa = new QA();
        if (qaRequest.getParentId() != null) {
            QA qaInDB = qaRepository.findById(qaRequest.getParentId()).orElseThrow(() -> new NotFoundException("QA ID không tồn tại"));
            qa.setParent(qaInDB);
        }
        qa.setContent(qaRequest.getContent());
        qa.setLesson(lesson);
        qa.setUser(user);
        qa.setCreatedAt(Instant.now());

        QA savedQA = qaRepository.save(qa);
        return convertToQAResponse(savedQA);
    }

    @Override
    public QAResponse updateQA(Integer qaId, String content) {
        QA qaInDB = qaRepository.findById(qaId).orElseThrow(() -> new NotFoundException("QA ID không tồn tại"));
        qaInDB.setContent(content);
        return convertToQAResponse(qaRepository.save(qaInDB));
    }

    @Override
    public String deleteQA(Integer qaId) {
        QA qaInDB = qaRepository.findById(qaId).orElseThrow(() -> new NotFoundException("QA ID không tồn tại"));
        qaRepository.delete(qaInDB);
        return "Xóa bình luận (hỏi đáp) thành công!";
    }

    //    Dành cho customer
    private QAResponse convertToQAResponse(QA qa) {
        QAResponse response = modelMapper.map(qa, QAResponse.class);
        response.setLessonId(qa.getLesson().getId());
        response.setUserId(qa.getUser().getId());
        response.setUsername(qa.getUser().getUsername());
        response.setPhotoUser(qa.getUser().getPhoto());
        response.setRoleName(qa.getUser().getRole().getName());
        response.setCreatedAtFormatted(Utils.formatDuration(Duration.between(qa.getCreatedAt(), Instant.now())));
        if (qa.getParent() != null) {
            response.setParentId(qa.getParent().getId());
        }

        List<QAResponse> childrenResponse = qa.getChildren().stream().sorted(Comparator.comparing(QA::getCreatedAt).reversed()).map(this::convertToQAResponse).toList();
        response.setChildren(childrenResponse);
        return response;
    }

    //    Dành cho admin và assistant
    private QAResponse convertToQAResponse1(QA qa) {
        QAResponse.CourseQA course = new QAResponse.CourseQA();
        course.setName(qa.getLesson().getChapter().getCourse().getTitle());

        QAResponse.LessonQA lesson = new QAResponse.LessonQA();
        lesson.setName(qa.getLesson().getName());

        QAResponse response = modelMapper.map(qa, QAResponse.class);
        response.setLessonId(qa.getLesson().getId());
        response.setUserId(qa.getUser().getId());
        response.setRoleName(qa.getUser().getRole().getName());
        response.setUsername(qa.getUser().getUsername());
        response.setPhotoUser(qa.getUser().getPhoto());
        response.setCreatedAtFormatted(Utils.formatDuration(Duration.between(qa.getCreatedAt(), Instant.now())));
        response.setCourse(course);
        response.setLesson(lesson);
        response.setChildren(null);
        return response;
    }
}
