package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.dto.reponse.ChapterReturnDetailResponse;
import com.example.bdhv_itclub.dto.reponse.CourseReturnLearningPageResponse;
import com.example.bdhv_itclub.dto.reponse.CourseReturnMyLearning;
import com.example.bdhv_itclub.dto.reponse.LessonReturnDetailResponse;
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
    private final TrackCourseRepository trackCourseRepository;
    private final CoursesRepository coursesRepository;
    private final OrderRepository orderRepository;
    private final LessonRepository lessonRepository;
    private final VideoRepository videoRepository;

    public LearningServiceImpl(ModelMapper modelMapper, UserRepository userRepository, TrackCourseRepository trackCourseRepository, CoursesRepository coursesRepository, OrderRepository orderRepository, LessonRepository lessonRepository, VideoRepository videoRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.trackCourseRepository = trackCourseRepository;
        this.coursesRepository = coursesRepository;
        this.orderRepository = orderRepository;
        this.lessonRepository = lessonRepository;
        this.videoRepository = videoRepository;
    }

    @Override
    public List<CourseReturnMyLearning> listAllCourseRegisteredByCustomer(String email) {
        // Lấy user
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email không tồn tại"));
        // Lấy danh sách order của user
        List<Order> listOrders = user.getListOrders();

        // Sử dụng Java 8 Streams để xử lý
        return listOrders.stream().map(order -> {
            Courses courses = order.getCourses();
            List<TrackCourse> listTrackCourses = trackCourseRepository.findAllByCoursesAndUser(courses, user);

            // Tính tổng số bài học và bài học đã hoàn thành
            int totalLesson = listTrackCourses.size();
            int totalLessonLearned = listTrackCourses.stream().filter(TrackCourse::isCompleted).toList().size();

            // Tính toán % tiến trình
            int process = (totalLesson > 0) ? Math.round((float) totalLessonLearned * 100 / totalLesson) : 0;

            CourseReturnMyLearning myLearning = modelMapper.map(courses, CourseReturnMyLearning.class);
            myLearning.setProcess(process);
            return myLearning;
        }).toList();
    }

    @Override
    public boolean isRegisterInThisCourse(String slug, String email) {
        if (email != null) {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại"));

            Courses courses = coursesRepository.findBySlug(slug).orElseThrow(() -> new NotFoundException("Course slug không tồn tại"));

            return orderRepository.existsOrderByCoursesAndUser(courses, user);

        }
        return false;
    }

    @Override
    public CourseReturnLearningPageResponse getCourseReturnLearningPage(String slug) {
        Courses coursesInDB = coursesRepository.findBySlug(slug).orElseThrow(() -> new NotFoundException("Course slug không tồn tại"));

        CourseReturnLearningPageResponse course = modelMapper.map(coursesInDB, CourseReturnLearningPageResponse.class);
        sortChapterAndLesson(course);
        return course;
    }

    // Hàm sắp xếp lại chapter và lesson theo order
    private void sortChapterAndLesson(CourseReturnLearningPageResponse course) {
        int totalLessonInCourse = 0;
        Duration durationInChapter = Duration.ZERO;

        // Lấy ra chapters của course (theo DTO)
        List<ChapterReturnDetailResponse> chapterList = course.getChapterList().stream().map(chapter -> modelMapper.map(chapter, ChapterReturnDetailResponse.class)).sorted(Comparator.comparingInt(ChapterReturnDetailResponse::getOrders)).toList();

        int i = 1;
        for (ChapterReturnDetailResponse chapter : chapterList) {
            // Lây ra lessons của chapter đó
            List<LessonReturnDetailResponse> listLesson = chapter.getLessonList();
            // Sort lại lesson theo order
            listLesson.sort(Comparator.comparingInt(LessonReturnDetailResponse::getOrders));

            for (LessonReturnDetailResponse lesson : listLesson) {
                // Nếu mà type lesson đó là dạng video
                if (lesson.getLessonType().equals(LessonType.VIDEO)) {
                    Lesson lessonInDB = lessonRepository.findById(lesson.getId()).get();
                    Video video = videoRepository.findById(lessonInDB.getVideo().getId()).get();
                    // Gán thời lượng là = thời lượng video
                    lesson.setDuration(video.getDuration());

                    // Tính toán thời lượng video
                    durationInChapter = durationInChapter.plus(Duration.ofMinutes(video.getDuration().getMinute()).plusSeconds(video.getDuration().getSecond()));
                } else {
                    // Nếu type lesson không phải là dạng video thì mặc định sẽ gán duration là 1 phút
                    LocalTime time = LocalTime.of(0, 1, 0);
                    lesson.setDuration(time);

                    durationInChapter = durationInChapter.plus(Duration.ofMinutes(1));
                }
                lesson.setOrders(i);
                ++i;
            }
            // Gán tổng số bài học của 1 chapter
            totalLessonInCourse += listLesson.size();
            chapter.setTotalLesson(listLesson.size());

            long hours = durationInChapter.toHours();
            long minutes = durationInChapter.toMinutes();
            long seconds = durationInChapter.minusMinutes(minutes).getSeconds();

            // Tạo LocalTime từ số phút và số giây
            LocalTime localTime = LocalTime.of((int) hours, (int) minutes, (int) seconds);
            chapter.setDurationChapter(localTime);
        }
        course.setTotalLesson(totalLessonInCourse);
    }
}
