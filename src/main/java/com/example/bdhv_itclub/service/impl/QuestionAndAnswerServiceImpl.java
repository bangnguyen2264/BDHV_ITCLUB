package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.dto.reponse.*;
import com.example.bdhv_itclub.dto.request.*;
import com.example.bdhv_itclub.entity.*;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.LessonRepository;
import com.example.bdhv_itclub.repository.QuestionAndAnswerRepository;
import com.example.bdhv_itclub.repository.UserRepository;
import com.example.bdhv_itclub.service.QuestionAndAnswerService;
import com.example.bdhv_itclub.utils.GlobalUtil;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Transactional
@Service
public class QuestionAndAnswerServiceImpl implements QuestionAndAnswerService {
    private final ModelMapper modelMapper;
    private final LessonRepository lessonRepository;
    private final QuestionAndAnswerRepository questionAndAnswerRepository;
    private final UserRepository userRepository;

    public QuestionAndAnswerServiceImpl(ModelMapper modelMapper, LessonRepository lessonRepository, QuestionAndAnswerRepository questionAndAnswerRepository, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.lessonRepository = lessonRepository;
        this.questionAndAnswerRepository = questionAndAnswerRepository;
        this.userRepository = userRepository;
    }

    // Ok
    @Override
    public List<QuestionAndAnswerResponse> listAll(Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Mã bài học không tồn tại"));
        List<QuestionAndAnswer> questionAndAnswers = questionAndAnswerRepository.findAllByLesson(lesson.getId());
        return questionAndAnswers.stream().sorted(Comparator.comparing(QuestionAndAnswer::getCreatedAt).reversed()).map(this::convertToQuestionAndAnswerResponse).toList();
    }

    // Ok
    @Override
    public List<QuestionAndAnswerResponse> listAllForAdmin() {
        List<QuestionAndAnswer> questionAndAnswers = questionAndAnswerRepository.findAll();
        return questionAndAnswers.stream().sorted(Comparator.comparing(QuestionAndAnswer::getCreatedAt).reversed()).map(this::convertToQuestionAndAnswerResponseForAdmin).toList();
    }

    // Ok
    @Override
    public QuestionAndAnswerResponse createQuestionAndAnswer(QuestionAndAnswerRequest questionAndAnswerRequest, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại"));
        Lesson lesson = lessonRepository.findById(questionAndAnswerRequest.getLessonId()).orElseThrow(() -> new NotFoundException("Mã bài học không tồn tại"));
        QuestionAndAnswer newQuestionAndAnswer = new QuestionAndAnswer();
        if (questionAndAnswerRequest.getParentId() != null) {
            QuestionAndAnswer questionAndAnswer = questionAndAnswerRepository.findById(questionAndAnswerRequest.getParentId()).orElseThrow(() -> new NotFoundException("Mã câu hỏi đáp không tồn tại"));
            newQuestionAndAnswer.setParent(questionAndAnswer);
        }
        newQuestionAndAnswer.setContent(questionAndAnswerRequest.getContent());
        newQuestionAndAnswer.setLesson(lesson);
        newQuestionAndAnswer.setUser(user);
        newQuestionAndAnswer.setCreatedAt(Instant.now());
        QuestionAndAnswer savedQuestionAndAnswer = questionAndAnswerRepository.save(newQuestionAndAnswer);
        return convertToQuestionAndAnswerResponse(savedQuestionAndAnswer);
    }

    // Ok
    @Override
    public QuestionAndAnswerResponse updateQuestionAndAnswer(Integer questionAndAnswerId, String content, String email) {
        QuestionAndAnswer questionAndAnswer = questionAndAnswerRepository.findById(questionAndAnswerId).orElseThrow(() -> new NotFoundException("Mã câu hỏi đáp không tồn tại"));
        questionAndAnswer.setContent(content);
        if (!questionAndAnswer.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("Bạn không có quyền chỉnh sửa câu hỏi đáp này");
        }
        return convertToQuestionAndAnswerResponse(questionAndAnswerRepository.save(questionAndAnswer));
    }

    // Ok
    @Override
    public String deleteQuestionAndAnswer(Integer questionAndAnswerId, String email) {
        QuestionAndAnswer questionAndAnswer = questionAndAnswerRepository.findById(questionAndAnswerId).orElseThrow(() -> new NotFoundException("Mã câu hỏi đáp không tồn tại"));
        // Kiểm tra người dùng hiện tại có phải là người tạo hay không
        if (!questionAndAnswer.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("Bạn không có quyền xóa câu hỏi đáp này");
        }
        questionAndAnswerRepository.delete(questionAndAnswer);
        return "Xóa hỏi đáp thành công";
    }

    // Ok
    private QuestionAndAnswerResponse convertToQuestionAndAnswerResponse(QuestionAndAnswer questionAndAnswer) {
        QuestionAndAnswerResponse questionAndAnswerResponse = modelMapper.map(questionAndAnswer, QuestionAndAnswerResponse.class);
        questionAndAnswerResponse.setLessonId(questionAndAnswer.getLesson().getId());
        questionAndAnswerResponse.setUserId(questionAndAnswer.getUser().getId());
        questionAndAnswerResponse.setUsername(questionAndAnswer.getUser().getUsername());
        questionAndAnswerResponse.setUserPhoto(questionAndAnswer.getUser().getPhoto());
        questionAndAnswerResponse.setRoleName(questionAndAnswer.getUser().getRole().getName());
        questionAndAnswerResponse.setCreatedAtFormatted(GlobalUtil.convertDurationToString(Duration.between(questionAndAnswer.getCreatedAt(), Instant.now())));
        if (questionAndAnswer.getParent() != null) {
            questionAndAnswerResponse.setParentId(questionAndAnswer.getParent().getId());
        }
        List<QuestionAndAnswerResponse> childrenQuestionAndAnswerResponse = questionAndAnswer.getChildren().stream().sorted(Comparator.comparing(QuestionAndAnswer::getCreatedAt).reversed()).map(this::convertToQuestionAndAnswerResponse).toList();
        questionAndAnswerResponse.setChildren(childrenQuestionAndAnswerResponse);
        return questionAndAnswerResponse;
    }

    // Ok
    private QuestionAndAnswerResponse convertToQuestionAndAnswerResponseForAdmin(QuestionAndAnswer questionAndAnswer) {
        QuestionAndAnswerResponse.QuestionAndAnswerCourse course = new QuestionAndAnswerResponse.QuestionAndAnswerCourse();
        course.setName(questionAndAnswer.getLesson().getChapter().getCourse().getTitle());

        QuestionAndAnswerResponse.QuestionAndAnswerLesson lesson = new QuestionAndAnswerResponse.QuestionAndAnswerLesson();
        lesson.setName(questionAndAnswer.getLesson().getName());

        QuestionAndAnswerResponse questionAndAnswerResponse = modelMapper.map(questionAndAnswer, QuestionAndAnswerResponse.class);
        questionAndAnswerResponse.setLessonId(questionAndAnswer.getLesson().getId());
        questionAndAnswerResponse.setUserId(questionAndAnswer.getUser().getId());
        questionAndAnswerResponse.setRoleName(questionAndAnswer.getUser().getRole().getName());
        questionAndAnswerResponse.setUsername(questionAndAnswer.getUser().getUsername());
        questionAndAnswerResponse.setUserPhoto(questionAndAnswer.getUser().getPhoto());
        questionAndAnswerResponse.setCreatedAtFormatted(GlobalUtil.convertDurationToString(Duration.between(questionAndAnswer.getCreatedAt(), Instant.now())));
        questionAndAnswerResponse.setCourse(course);
        questionAndAnswerResponse.setLesson(lesson);
        questionAndAnswerResponse.setChildren(null);
        return questionAndAnswerResponse;
    }
}