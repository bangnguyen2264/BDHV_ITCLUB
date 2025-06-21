package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.constant.LessonType;
import com.example.bdhv_itclub.constant.QuizType;
import com.example.bdhv_itclub.dto.reponse.LessonResponse;
import com.example.bdhv_itclub.dto.request.LessonRequest;
import com.example.bdhv_itclub.dto.request.QuizAnswerDTO;
import com.example.bdhv_itclub.dto.request.QuizRequest;
import com.example.bdhv_itclub.entity.*;
import com.example.bdhv_itclub.exception.BadRequestException;
import com.example.bdhv_itclub.exception.ConflictException;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.*;
import com.example.bdhv_itclub.service.LessonService;

import com.example.bdhv_itclub.utils.GlobalUtil;
import com.example.bdhv_itclub.utils.UploadFileUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class LessonServiceImpl implements LessonService {
    private final ModelMapper modelMapper;
    private final UploadFileUtil uploadFile;
    private final LessonRepository lessonRepository;
    private final CourseChapterRepository courseChapterRepository;
    private final EnrollmentRepository orderRepository;
    private final CourseTrackingRepository courseTrackingRepository;
    private final QuizRepository quizRepository;
    private final QuizAnswerRepository quizAnswerRepository;

    public LessonServiceImpl(ModelMapper modelMapper, UploadFileUtil uploadFile, LessonRepository lessonRepository, CourseChapterRepository courseChapterRepository, EnrollmentRepository orderRepository, CourseTrackingRepository courseTrackingRepository, QuizRepository quizRepository, QuizAnswerRepository quizAnswerRepository) {
        this.modelMapper = modelMapper;
        this.uploadFile = uploadFile;
        this.lessonRepository = lessonRepository;
        this.courseChapterRepository = courseChapterRepository;
        this.orderRepository = orderRepository;
        this.courseTrackingRepository = courseTrackingRepository;
        this.quizRepository = quizRepository;
        this.quizAnswerRepository = quizAnswerRepository;
    }

    // Ok
    @Override
    public LessonResponse get(Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Mã bài học không tồn tại"));
        return convertToLessonResponse(lesson);
    }

    // Ok
    @Override
    public Course getCourse(Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Mã bài học không tồn tại"));
        return lesson.getChapter().getCourse();
    }

    // No Ok
    @Override
    public LessonResponse create(LessonRequest lessonRequest, Video video, LessonText lessonText, QuizRequest[] quizRequests) {
        CourseChapter courseChapter = courseChapterRepository.findById(lessonRequest.getChapterId()).orElseThrow(() -> new NotFoundException("Mã chương học không tồn tại"));
        if (lessonRepository.existsByNameAndChapter(lessonRequest.getName(), courseChapter)) {
            throw new ConflictException("Tên bài học đã từng tồn tại trong chương này");
        }
        // Khởi tạo bài học mới
        Lesson lesson = new Lesson();
        lesson.setName(lessonRequest.getName());
        lesson.setCreatedAt(Instant.now());
        lesson.setLessonType(LessonType.valueOf(lessonRequest.getLessonType()));
        lesson.setChapter(courseChapter);
        lesson.setVideo(video);
        lesson.setText(lessonText);
        lesson.setLessonOrder(lessonRequest.getLessonOrder());

        // Nếu là bài học là dạng bài kiểm tra, thêm các câu hỏi quiz
        if (lessonRequest.getLessonType().equals("QUIZ") && quizRequests != null) {
            for (QuizRequest quizRequest : quizRequests) {
                lesson.addAQuiz(GlobalUtil.convertToQuizEntity(quizRequest));
            }
        }
        Lesson savedLesson = lessonRepository.save(lesson);
        Course course = savedLesson.getChapter().getCourse();
        List<Enrollment> orders = orderRepository.findAllByCourse(course);

        // Tạo CourseTracking cho mỗi người học trong khóa
        if (!orders.isEmpty()) {
            for (Enrollment order : orders) {
                CourseTracking courseTracking = new CourseTracking();
                courseTracking.setUser(order.getUser());
                courseTracking.setCourse(order.getCourse());
                courseTracking.setChapter(savedLesson.getChapter());
                courseTracking.setLesson(savedLesson);

                // Lấy bài học hiện tại (được gắn "current") của người học đó trong khóa
                CourseTracking currentCourseTracking = courseTrackingRepository.findByCurrent(course.getId(), order.getUser().getId());
                if (currentCourseTracking != null) {
                    int currentChapterOrder = currentCourseTracking.getChapter().getChapterOrder();
                    int currentLessonOrder = currentCourseTracking.getLesson().getLessonOrder();
                    // Nếu chương hiện tại (current) lớn hơn chương bài học mới thêm vào -> mở khóa
                    if (currentChapterOrder > savedLesson.getChapter().getChapterOrder()) {
                        courseTracking.setUnlock(true);
                    }
                    // Nếu cùng chương, so sánh thứ tự bài học
                    else if (currentChapterOrder == savedLesson.getChapter().getChapterOrder()) {
                        if (currentLessonOrder > savedLesson.getLessonOrder()) {
                            courseTracking.setUnlock(true);
                        }
                    }
                } else {
                    // Nếu người học chưa có tracking nào => đánh dấu là mở khóa và hiện tại
                    courseTracking.setUnlock(true);
                    courseTracking.setCurrent(true);
                }
                courseTrackingRepository.save(courseTracking);
            }
        }
        return convertToLessonResponse(savedLesson);
    }

    // Ok
    @Override
    public LessonResponse update(Integer lessonId, LessonRequest lessonRequest, Video video, LessonText lessonText, QuizRequest[] quizRequests) {
        CourseChapter courseChapter = courseChapterRepository.findById(lessonRequest.getChapterId())
                .orElseThrow(() -> new NotFoundException("Mã chương học không tồn tại"));
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Mã bài học không tồn tại"));
        Lesson checkDuplicatedLesson = lessonRepository.findByNameAndChapter(lessonRequest.getName(), courseChapter);

        if (checkDuplicatedLesson != null && !Objects.equals(lesson.getId(), checkDuplicatedLesson.getId())) {
            throw new ConflictException("Tên bài học đã từng tồn tại trong chương này");
        }

        lesson.setName(lessonRequest.getName());

        if (video != null && video.getId() != null) {
            lesson.setVideo(video);
        }
        if (lessonText != null && lessonText.getId() != null) {
            lesson.setText(lessonText);
        }

        if (lessonRequest.getLessonType().equals("QUIZ") && quizRequests != null) {
            List<Quiz> quizzes = new ArrayList<>();
            for (QuizRequest quizRequest : quizRequests) {
                Quiz quiz = (quizRequest.getId() == null)
                        ? GlobalUtil.convertToQuizEntity(quizRequest)
                        : updateQuiz(quizRequest, lesson);
                quiz.setLesson(lesson);
                quizzes.add(quiz);
            }
            lesson.setQuizzes(quizzes);
        }

        return convertToLessonResponse(lessonRepository.save(lesson));
    }

    // No Ok
    @Override
    public String delete(Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Mã bài học không tồn tại"));
        if (lesson.getVideo() != null) {
            String url = lesson.getVideo().getUrl();
            uploadFile.deleteVideoInCloudinary(url);
        }
        lessonRepository.delete(lesson);
        return "Xóa bài học thành công";
    }

    // Ok
    private Quiz updateQuiz(QuizRequest quizRequest, Lesson lesson) {
        Quiz quiz = quizRepository.findById(quizRequest.getId())
                .orElseThrow(() -> new NotFoundException("Mã câu hỏi không tồn tại"));

        Quiz checkDuplicatedQuiz = quizRepository.findByQuestionAndLesson(quizRequest.getQuestion(), lesson);
        if (checkDuplicatedQuiz != null && !Objects.equals(quiz.getId(), checkDuplicatedQuiz.getId())) {
            throw new ConflictException("Câu hỏi đã từng tồn tại trong bài học này");
        }

        quiz.setQuestion(quizRequest.getQuestion());
        quiz.setQuizType(QuizType.valueOf(quizRequest.getQuizType()));

        // Danh sách ID từ client gửi lên
        Set<Integer> incomingIds = quizRequest.getAnswers().stream()
                .map(QuizAnswerDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Xoá các câu trả lời cũ không còn trong danh sách mới
        quiz.getAnswers().removeIf(ans -> ans.getId() != null && !incomingIds.contains(ans.getId()));

        List<QuizAnswer> updatedAnswers = new ArrayList<>();
        boolean hasCorrectAnswer = false;

        for (QuizAnswerDTO quizAnswerDTO : quizRequest.getAnswers()) {
            QuizAnswer quizAnswer;
            if (quizAnswerDTO.getId() != null) {
                quizAnswer = quizAnswerRepository.findById(quizAnswerDTO.getId())
                        .orElseThrow(() -> new NotFoundException("Câu trả lời không tồn tại"));
                quizAnswer.setContent(quizAnswerDTO.getContent());
                quizAnswer.setCorrect(quizAnswerDTO.isCorrect());
            } else {
                quizAnswer = new QuizAnswer(quizAnswerDTO.getContent(), quizAnswerDTO.isCorrect(), quiz);
            }

            quizAnswer.setQuiz(quiz);
            updatedAnswers.add(quizAnswer);
            if (quizAnswerDTO.isCorrect()) hasCorrectAnswer = true;
        }

        if (!hasCorrectAnswer) {
            throw new BadRequestException("Không có câu trả lời đúng trong danh sách câu trả lời");
        }

        quiz.setAnswers(updatedAnswers);
        return quiz;
    }

    // Ok
    private LessonResponse convertToLessonResponse(Lesson lesson) {
        LessonResponse lessonResponse = modelMapper.map(lesson, LessonResponse.class);
        lessonResponse.setChapterId(lesson.getChapter().getId());
        return lessonResponse;
    }
}