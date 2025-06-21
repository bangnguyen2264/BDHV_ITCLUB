package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.dto.reponse.CourseTrackingResponse;
import com.example.bdhv_itclub.dto.reponse.LessonResponseForLearningPage;
import com.example.bdhv_itclub.dto.reponse.QuizResponseForLearningPage;
import com.example.bdhv_itclub.dto.request.CourseRegisteredInformation;
import com.example.bdhv_itclub.entity.*;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.*;
import com.example.bdhv_itclub.service.CourseTrackingService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CourseTrackingServiceImpl implements CourseTrackingService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CertificateRepository certificateRepository;
    private final CourseTrackingRepository courseTrackingRepository;
    private final LessonRepository lessonRepository;

    public CourseTrackingServiceImpl(ModelMapper modelMapper, UserRepository userRepository, CourseRepository courseRepository, CertificateRepository certificateRepository, CourseTrackingRepository courseTrackingRepository, LessonRepository lessonRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.certificateRepository = certificateRepository;
        this.courseTrackingRepository = courseTrackingRepository;
        this.lessonRepository = lessonRepository;
    }

    // Ok
    @Override
    public CourseRegisteredInformation listAll(String email, String slug) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại"));
        Course course = courseRepository.findBySlug(slug).orElseThrow(() -> new NotFoundException("Slug của khoá học không tồn tại"));

        Integer certificateId = null;
        Certificate certificate = certificateRepository.findByUserAndCourse(user, course);
        if (certificate != null) {
            certificateId = certificate.getId();
        }
        int averageAchieved = 0;
        int totalLessonLearned = 0;
        int totalLesson = 0;

        List<CourseTracking> courseTrackings = sortCourseTracking(course, user);
        List<CourseTrackingResponse> courseTrackingResponses = new ArrayList<>();
        for (CourseTracking courseTracking : courseTrackings) {
            courseTrackingResponses.add(convertToCourseTrackingResponse(courseTracking));
            if (courseTracking.isCompleted()) {
                ++totalLessonLearned;
            }
            ++totalLesson;
        }
        if (totalLesson > 0) {
            float percent = (float) (totalLessonLearned * 100) / totalLesson;
            averageAchieved = Math.round(percent);
        }
        return new CourseRegisteredInformation(courseTrackingResponses, averageAchieved, totalLessonLearned, certificateId);
    }

    // Ok
    @Override
    public LessonResponseForLearningPage getLesson(Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Mã bài học không tồn tại"));
        LessonResponseForLearningPage lessonResponseForLearningPage = modelMapper.map(lesson, LessonResponseForLearningPage.class);
        if (lessonResponseForLearningPage.getLessonType().toString().equals("QUIZ")) {
            int i = 0;
            for (QuizResponseForLearningPage quiz : lessonResponseForLearningPage.getQuizzes()) {
                quiz.setOrder(++i);
                if (quiz.getQuizType().toString().equals("PERFORATE")) {
                    quiz.setAnswers(null);
                }
            }
        }
        return lessonResponseForLearningPage;
    }

    // Ok
    @Override
    public Integer confirmLearnedLesson(String email, Integer previousLessonId) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại"));
        Lesson lesson = lessonRepository.findById(previousLessonId).orElseThrow(() -> new NotFoundException("Mã bài học không tồn tại"));

        CourseTracking previousCourseTracking = courseTrackingRepository.findByLessonAndUser(lesson, user);
        // Kiểm tra xem bài học đã được mở khóa chưa
        if (!previousCourseTracking.isUnlock()) {
            throw new AccessDeniedException("Bài học này chưa được mở khóa");
        } else {
            // Đánh dấu bài học trước đó là đã học, và biến isCurrent sẽ thuộc về bài học hiện tại
            courseTrackingRepository.updatePreviousCourseTrackingLesson(user.getId(), previousLessonId);
            Course course = courseTrackingRepository.findCourseTrackingByLessonAndUser(lesson, user).getCourse();
            List<CourseTracking> courseTrackings = sortCourseTracking(course, user);
            Optional<Integer> nextLessonId = courseTrackings.stream().filter(track -> track.getLesson().getId().equals(previousLessonId)).map(track -> {
                int index = courseTrackings.indexOf(track);
                if (index != -1 && index < courseTrackings.size() - 1) {
                    return courseTrackings.get(index + 1).getLesson().getId(); // Lấy ID bài tiếp theo
                } else {
                    return -1;
                }
            }).findFirst();
            // Nếu có bài tiếp theo thì kiểm tra và mở khóa nếu cần
            if (nextLessonId.get() != -1) {
                Lesson nextLesson = lessonRepository.findById(nextLessonId.get()).orElseThrow(() -> new NotFoundException("Mã bài học không tồn tại"));
                CourseTracking nextCourseTracking = courseTrackingRepository.findCourseTrackingByLessonAndUser(nextLesson, user);
                if (!nextCourseTracking.isUnlock()) {
                    courseTrackingRepository.updateNextCourseTrackingLesson(user.getId(), nextLessonId.get());
                }
            }
            return nextLessonId.get();
        }
    }

    // Ok
    List<CourseTracking> sortCourseTracking(Course course, User user) {
        List<CourseTracking> courseTrackings = new ArrayList<>();
        for (CourseChapter courseChapter : course.getCourseChapters()) {
            List<CourseTracking> courseTrackingByChapters = courseTrackingRepository.findByCourseAndChapterAndUser(course, courseChapter, user);
            courseTrackingByChapters.sort(Comparator.comparingInt(track -> track.getLesson().getLessonOrder()));
            courseTrackings.addAll(courseTrackingByChapters);
        }
        return courseTrackings;
    }

    // Ok
    public CourseTrackingResponse convertToCourseTrackingResponse(CourseTracking courseTracking) {
        CourseTrackingResponse courseTrackingResponse = modelMapper.map(courseTracking, CourseTrackingResponse.class);
        courseTrackingResponse.setLessonId(courseTracking.getLesson().getId());
        return courseTrackingResponse;
    }
}