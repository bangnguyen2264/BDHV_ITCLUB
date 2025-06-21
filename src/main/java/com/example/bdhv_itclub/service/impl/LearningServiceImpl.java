package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.constant.LessonType;
import com.example.bdhv_itclub.dto.reponse.CourseChapterResponseForDetailPage;
import com.example.bdhv_itclub.dto.reponse.CourseResponseForLearningPage;
import com.example.bdhv_itclub.dto.reponse.CourseResponseForMyCoursesPage;
import com.example.bdhv_itclub.dto.reponse.LessonResponseForDetailPage;
import com.example.bdhv_itclub.entity.*;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.*;
import com.example.bdhv_itclub.service.LearningService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Transactional
@Service
public class LearningServiceImpl implements LearningService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CourseTrackingRepository courseTrackingRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository orderRepository;
    private final LessonRepository lessonRepository;
    private final VideoRepository videoRepository;

    public LearningServiceImpl(ModelMapper modelMapper, UserRepository userRepository, CourseTrackingRepository courseTrackingRepository, CourseRepository courseRepository, EnrollmentRepository orderRepository, LessonRepository lessonRepository, VideoRepository videoRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.courseTrackingRepository = courseTrackingRepository;
        this.courseRepository = courseRepository;
        this.orderRepository = orderRepository;
        this.lessonRepository = lessonRepository;
        this.videoRepository = videoRepository;
    }

    // Ok
    @Override
    public List<CourseResponseForMyCoursesPage> listAllCourseRegisteredByCustomer(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email không tồn tại"));
        List<Enrollment> orders = user.getOrders();

        return orders.stream().map(order -> {
            Course course = order.getCourse();
            List<CourseTracking> courseTrackings = courseTrackingRepository.findAllByCourseAndUser(course, user);

            // Tính tổng số bài học và bài học đã hoàn thành
            int totalLesson = courseTrackings.size();
            int totalLessonLearned = courseTrackings.stream().filter(CourseTracking::isCompleted).toList().size();
            int process = (totalLesson > 0) ? Math.round((float) totalLessonLearned * 100 / totalLesson) : 0;
            CourseResponseForMyCoursesPage myLearning = modelMapper.map(course, CourseResponseForMyCoursesPage.class);
            myLearning.setProcess(process);
            return myLearning;
        }).toList();
    }

    // Ok
    @Override
    public boolean isRegisterInThisCourse(String slug, String email) {
        if (email != null) {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại"));
            Course course = courseRepository.findBySlug(slug).orElseThrow(() -> new NotFoundException("Slug của khóa học không tồn tại"));
            return orderRepository.existsByCourseAndUser(course, user);
        }
        return false;
    }

    // Ok
    @Override
    public CourseResponseForLearningPage getCourseForLearningPage(String slug) {
        Course course = courseRepository.findBySlug(slug).orElseThrow(() -> new NotFoundException("Slug của khóa học không tồn tại"));
        CourseResponseForLearningPage courseResponseForLearningPage = modelMapper.map(course, CourseResponseForLearningPage.class);
        sortChapterAndLesson(courseResponseForLearningPage);
        return courseResponseForLearningPage;
    }

    // Ok
    private void sortChapterAndLesson(CourseResponseForLearningPage course) {
        int totalCourseLesson = 0;
        List<CourseChapterResponseForDetailPage> chapters = course.getCourseChapters().stream().map(chapter -> modelMapper.map(chapter, CourseChapterResponseForDetailPage.class)).sorted(Comparator.comparingInt(CourseChapterResponseForDetailPage::getChapterOrder)).toList();

        int i = 1;
        for (CourseChapterResponseForDetailPage chapter : chapters) {
            Duration chapterDuration = Duration.ZERO; // 👈 RESET mỗi chương

            List<LessonResponseForDetailPage> lessons = chapter.getLessons();
            lessons.sort(Comparator.comparingInt(LessonResponseForDetailPage::getLessonOrder));

            for (LessonResponseForDetailPage lesson : lessons) {
                if (lesson.getLessonType().equals(LessonType.VIDEO)) {
                    Lesson lessonInDB = lessonRepository.findById(lesson.getId()).get();
                    Video video = videoRepository.findById(lessonInDB.getVideo().getId()).get();
                    lesson.setDuration(video.getDuration());
                    chapterDuration = chapterDuration
                            .plusMinutes(video.getDuration().getMinute())
                            .plusSeconds(video.getDuration().getSecond());
                } else {
                    LocalTime time = LocalTime.of(0, 1, 0);
                    lesson.setDuration(time);
                    chapterDuration = chapterDuration.plusMinutes(1);
                }
                lesson.setLessonOrder(i++);
            }

            totalCourseLesson += lessons.size();
            chapter.setTotalLesson(lessons.size());

            long totalMinutes = chapterDuration.toMinutes();
            long seconds = chapterDuration.minusMinutes(totalMinutes).getSeconds();
            long hours = totalMinutes / 60;
            long minutes = totalMinutes % 60;

            LocalTime localTime = LocalTime.of((int) hours, (int) minutes, (int) seconds);
            chapter.setChapterDuration(localTime);
        }
        course.setTotalLesson(totalCourseLesson);
    }
}