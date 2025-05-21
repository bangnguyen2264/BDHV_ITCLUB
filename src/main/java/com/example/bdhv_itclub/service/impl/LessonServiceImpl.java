package com.example.bdhv_itclub.service.impl;


import com.example.bdhv_itclub.dto.reponse.AnswerDto;
import com.example.bdhv_itclub.dto.reponse.LessonResponse;
import com.example.bdhv_itclub.dto.request.LessonRequest;
import com.example.bdhv_itclub.dto.request.QuizRequest;
import com.example.bdhv_itclub.entity.*;
import com.example.bdhv_itclub.exception.ConflictException;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.*;
import com.example.bdhv_itclub.service.LessonService;
import com.example.bdhv_itclub.utils.UploadFile;
import com.example.bdhv_itclub.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class LessonServiceImpl implements LessonService {
    private final ModelMapper modelMapper;
    private final UploadFile uploadFile;
    private final LessonRepository lessonRepository;
    private final ChapterRepository chapterRepository;
    private final OrderRepository orderRepository;
    private final TrackCourseRepository trackCourseRepository;

    private final QuizRepository quizRepository;


    public LessonServiceImpl(ModelMapper modelMapper, UploadFile uploadFile, LessonRepository lessonRepository, ChapterRepository chapterRepository, OrderRepository orderRepository, TrackCourseRepository trackCourseRepository, QuizRepository quizRepository) {
        this.modelMapper = modelMapper;
        this.uploadFile = uploadFile;
        this.lessonRepository = lessonRepository;
        this.chapterRepository = chapterRepository;
        this.orderRepository = orderRepository;
        this.trackCourseRepository = trackCourseRepository;
        this.quizRepository = quizRepository;
    }

    @Override
    public Courses getCourse(Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson ID không tồn tại"));
        return lesson.getChapter().getCourse();
    }

    @Override
    public LessonResponse create(LessonRequest lessonRequest, Video video, TextLesson textLesson, QuizRequest[] quizRequest) {
        // Kiểm tra xem có tồn tại chapter đó không
        Chapter chapter = chapterRepository.findById(lessonRequest.getChapterId()).orElseThrow(() -> new NotFoundException("Chapter ID không tồn tại"));

        if (lessonRepository.existsLessonByNameAndChapter(lessonRequest.getName(), chapter)) {
            throw new ConflictException("Tên bài học đã từng tồn tại trong chương này!");
        }


        // Set các thông tin của lesson
        Lesson lesson = new Lesson();
        lesson.setName(lessonRequest.getName());
        lesson.setCreatedAt(Instant.now());
        lesson.setLessonType(LessonType.valueOf(lessonRequest.getLessonType()));
        lesson.setChapter(chapter);
        lesson.setVideo(video);
        lesson.setText(textLesson);
        lesson.setOrders(lessonRequest.getOrders());

        // Nếu lesson là bài quiz thì thực hiện chuyển đổi quizDTO thành entity để dễ xử lý
        if (lessonRequest.getLessonType().equals("QUIZ") && quizRequest != null) {
            for (QuizRequest quiz : quizRequest) {
                lesson.add(Utils.convertToQuizEntity(quiz));
            }
        }

        Lesson savedLesson = lessonRepository.save(lesson);

        Courses courses = savedLesson.getChapter().getCourse();
        // Chủ yếu để thêm track lesson cho người dùng nào mà đã mua khoá học này
        List<Order> listOrder = orderRepository.findAllByCourses(courses);
        if (!listOrder.isEmpty()) {
            for (Order order : listOrder) {
                TrackCourse trackCourse = new TrackCourse();
                trackCourse.setUser(order.getUser());
                trackCourse.setCourses(order.getCourses());
                trackCourse.setChapter(savedLesson.getChapter());
                trackCourse.setLesson(savedLesson);

                TrackCourse trackCourseCurrentLesson = trackCourseRepository.findTrackCoursesByCurrent(courses.getId(), order.getUser().getId());
                if (trackCourseCurrentLesson != null) {
                    int chapterCurrentLessIdOrder = trackCourseCurrentLesson.getChapter().getOrders();
                    int lessonCurrentLessIdOrder = trackCourseCurrentLesson.getLesson().getOrders();
                    if (chapterCurrentLessIdOrder > savedLesson.getChapter().getOrders()) {
                        trackCourse.setUnlock(true);
                    } else if (chapterCurrentLessIdOrder == savedLesson.getChapter().getOrders()) {
                        if (lessonCurrentLessIdOrder > savedLesson.getOrders()) {
                            trackCourse.setUnlock(true);
                        }
                    }
                } else {
                    trackCourse.setUnlock(true);
                    trackCourse.setCurrent(true);
                }
                trackCourseRepository.save(trackCourse);
            }
        }
        return convertToLessonResponse(savedLesson);
    }

    @Override
    public LessonResponse get(Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson ID không tồn tại"));

        return convertToLessonResponse(lesson);
    }

    @Override
    public LessonResponse update(Integer lessonId, LessonRequest lessonRequest, Video video, TextLesson textLesson, QuizRequest[] quizRequest) {
        Chapter chapter = chapterRepository.findById(lessonRequest.getChapterId()).orElseThrow(() -> new NotFoundException("Chapter ID không tồn tại"));

        Lesson lessonInDB = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson ID không tồn tại"));

        Lesson lessonTemp = lessonRepository.findLessonByNameAndChapter(lessonRequest.getName(), chapter);
        if (lessonTemp != null) {
            if (!Objects.equals(lessonInDB.getId(), lessonTemp.getId())) {
                throw new ConflictException("Tên bài học đã từng tồn tại trong chương này!");
            }
        }

        lessonInDB.setName(lessonRequest.getName());

        if (video != null && video.getId() != null) {
            lessonInDB.setVideo(video);
        }

        if (textLesson != null && textLesson.getId() != null) {
            lessonInDB.setText(textLesson);
        }

        if (lessonRequest.getLessonType().equals("QUIZ") && quizRequest != null) {
            List<Quiz> listQuizzes = new ArrayList<>();
            for (QuizRequest quizInList : quizRequest) {
                Quiz quiz = quizInList.getId() == null ? Utils.convertToQuizEntity(quizInList) : updateQuiz(quizInList, lessonInDB);
                quiz.setLesson(lessonInDB);
                listQuizzes.add(quiz);
            }

            lessonInDB.setQuizList(listQuizzes);
        }

        return convertToLessonResponse(lessonRepository.save(lessonInDB));
    }

    @Override
    public String delete(Integer lessonId) {
        Lesson lessonInDB = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson ID không tồn tại"));

        if (lessonInDB.getVideo() != null) {
            String url = lessonInDB.getVideo().getUrl();
            uploadFile.deleteVideoInCloudinary(url);
        }

        lessonRepository.delete(lessonInDB);
        return "Xóa bài học thành công";
    }

    private Quiz updateQuiz(QuizRequest quizRequest, Lesson lesson) {
        Quiz quizInDB = quizRepository.findById(quizRequest.getId()).orElseThrow(() -> new NotFoundException("Quiz ID không tồn tại"));

        Quiz checkQuizDuplicate = quizRepository.findQuizByQuestionAndLesson(quizRequest.getQuestion(), lesson);

        if (checkQuizDuplicate != null) {
            if (!Objects.equals(quizInDB.getId(), checkQuizDuplicate.getId())) {
                throw new ConflictException("Câu hỏi đã từng tồn tại trong bài học này!");
            }
        }

        quizInDB.setQuestion(quizRequest.getQuestion());
        quizInDB.setQuizType(QuizType.valueOf(quizRequest.getQuizType()));
        List<Answer> answerList = new ArrayList<>();

        for (AnswerDto dto : quizRequest.getAnswerList()) {
            Answer answer = null;
            if (dto.getId() != null) {
                answer = new Answer(dto.getId(), dto.getContent(), dto.isCorrect(), quizInDB);
            } else {
                answer = new Answer(dto.getContent(), dto.isCorrect(), quizInDB);
            }

            answerList.add(answer);
        }

        quizInDB.setAnswerList(answerList);

        return quizInDB;
    }

    private LessonResponse convertToLessonResponse(Lesson lesson) {
        LessonResponse response = modelMapper.map(lesson, LessonResponse.class);
        response.setChapterId(lesson.getChapter().getId());

        return response;
    }
}
