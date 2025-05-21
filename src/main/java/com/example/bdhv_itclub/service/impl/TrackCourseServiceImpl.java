package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.dto.reponse.InfoCourseRegistered;
import com.example.bdhv_itclub.dto.reponse.LessonReturnLearningResponse;
import com.example.bdhv_itclub.dto.reponse.QuizReturnLearningPage;
import com.example.bdhv_itclub.dto.reponse.TrackCourseResponse;
import com.example.bdhv_itclub.entity.*;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.*;
import com.example.bdhv_itclub.service.TrackCourseService;
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
public class TrackCourseServiceImpl implements TrackCourseService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CoursesRepository coursesRepository;
    private final CertificateRepository certificateRepository;
    private final TrackCourseRepository trackCourseRepository;
    private final LessonRepository lessonRepository;

    public TrackCourseServiceImpl(ModelMapper modelMapper, UserRepository userRepository, CoursesRepository coursesRepository, CertificateRepository certificateRepository, TrackCourseRepository trackCourseRepository, LessonRepository lessonRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.coursesRepository = coursesRepository;
        this.certificateRepository = certificateRepository;
        this.trackCourseRepository = trackCourseRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public InfoCourseRegistered listTrackCourse(String email, String slug) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại"));

        Courses courses = coursesRepository.findBySlug(slug).orElseThrow(() -> new NotFoundException("Course slug không tồn tại"));

        Integer certificateId = null;
        Certificate certificate = certificateRepository.findByUserAndCourses(user, courses);
        if (certificate != null) {
            certificateId = certificate.getId();
        }

        int averageAchieved = 0;
        int totalLessonLearned = 0;
        int totalLesson = 0;

        List<TrackCourse> listTrackCourses = sortTrackCourse(courses, user);
        List<TrackCourseResponse> listTrackCoursesResponse = new ArrayList<>();
        for (TrackCourse trackCourse : listTrackCourses) {
            listTrackCoursesResponse.add(convertToTrackCourseResponse(trackCourse));
            if (trackCourse.isCompleted()) {
                ++totalLessonLearned;
            }
            ++totalLesson;
        }
        float percent = (float) (totalLessonLearned * 100) / totalLesson;
        averageAchieved = Math.round(percent);
        return new InfoCourseRegistered(listTrackCoursesResponse, averageAchieved, totalLessonLearned, certificateId);
    }

    @Override
    public LessonReturnLearningResponse getLesson(Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson ID không tồn tại"));

        LessonReturnLearningResponse lessonReturn = modelMapper.map(lesson, LessonReturnLearningResponse.class);
        if (lessonReturn.getLessonType().toString().equals("QUIZ")) {
            int i = 0;
            for (QuizReturnLearningPage quiz : lessonReturn.getQuizList()) {
                quiz.setOrder(++i);
                if (quiz.getQuizType().toString().equals("PERFORATE")) {
                    quiz.setAnswerList(null);
                }
            }
        }
        return lessonReturn;
    }

    @Override
    public Integer confirmLessonLearned(String email, Integer lessonIdPre) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại"));
        Lesson lesson = lessonRepository.findById(lessonIdPre).orElseThrow(() -> new NotFoundException("Lesson ID không tồn tại"));

        // Lấy ra bài học của user
        TrackCourse trackCoursePre = trackCourseRepository.findByLessonAndUser(lesson, user);

        // Nếu bài học đó chưa được mở
        if (!trackCoursePre.isUnlock()) {
            throw new AccessDeniedException("Bài học này chưa được mở khóa");
        } else {
            // Cập nhật lại trạng thái của bài đang học
            trackCourseRepository.updateTrackCourseLessonPre(user.getId(), lessonIdPre);

            // Tìm kiếm khoá học
            Courses courses = trackCourseRepository.findTrackCourseByLessonAndUser(lesson, user).getCourses();
            List<TrackCourse> listTrackCourses = sortTrackCourse(courses, user);
            // Tìm ra xem bài học nào là bài học tiếp theo
            Optional<Integer> lessonIdNext = listTrackCourses.stream().filter(track -> track.getLesson().getId().equals(lessonIdPre)).map(track -> {
                int index = listTrackCourses.indexOf(track);
                if (index != -1 && index < listTrackCourses.size() - 1) {
                    return listTrackCourses.get(index + 1).getLesson().getId();
                } else {
                    return -1;
                }
            }).findFirst();

            if (lessonIdNext.get() != -1) {
                // Lấy ra bài học
                Lesson lessonNext = lessonRepository.findById(lessonIdNext.get()).orElseThrow(() -> new NotFoundException("Lesson ID không tồn tại"));

                // Lấy ra bài học của user
                TrackCourse trackCourseNext = trackCourseRepository.findTrackCourseByLessonAndUser(lessonNext, user);
                if (!trackCourseNext.isUnlock()) {
                    // Cập nhật bài học (bài học tiếp theo sẽ được mở khoá)
                    trackCourseRepository.updateTrackCourseLessonNext(user.getId(), lessonIdNext.get());
                }
            }
            return lessonIdNext.get();
        }
    }

    List<TrackCourse> sortTrackCourse(Courses courses, User user) {
        List<TrackCourse> listTrackCourses = new ArrayList<>();
        for (Chapter chapter : courses.getChapterList()) {
            List<TrackCourse> listTrackByChapter = trackCourseRepository.findTrackCourseByCoursesAndChapterAndUser(courses, chapter, user);
            listTrackByChapter.sort(Comparator.comparingInt(track -> track.getLesson().getOrders()));
            listTrackCourses.addAll(listTrackByChapter);
        }
        return listTrackCourses;
    }

    public TrackCourseResponse convertToTrackCourseResponse(TrackCourse trackCourse) {
        TrackCourseResponse response = modelMapper.map(trackCourse, TrackCourseResponse.class);
        response.setLessonId(trackCourse.getLesson().getId());
        return response;
    }
}
