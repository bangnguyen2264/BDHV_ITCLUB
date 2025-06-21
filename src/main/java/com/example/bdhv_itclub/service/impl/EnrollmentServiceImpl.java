package com.example.bdhv_itclub.service.impl;


import com.example.bdhv_itclub.dto.reponse.EnrollmentResponse;
import com.example.bdhv_itclub.entity.*;
import com.example.bdhv_itclub.repository.*;
import com.example.bdhv_itclub.service.EnrollmentService;
import com.example.bdhv_itclub.utils.EmailUtil;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    private final ModelMapper modelMapper;
    private final EnrollmentRepository orderRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseTrackingRepository courseTrackingRepository;
    private final EmailUtil emailUtil;

    public EnrollmentServiceImpl(ModelMapper modelMapper, EnrollmentRepository orderRepository, UserRepository userRepository, CourseRepository courseRepository, CourseTrackingRepository courseTrackingRepository, EmailUtil emailUtil) {
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.courseTrackingRepository = courseTrackingRepository;
        this.emailUtil = emailUtil;
    }

    // Ok
    @Override
    public List<EnrollmentResponse> getAll() {
        List<Enrollment> orders = orderRepository.findAll();
        return orders.stream().map(this::convertToOrderResponse).toList();
    }

    // Ok
    @Override
    public List<EnrollmentResponse> getAllByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email người dùng không tồn tại"));
        List<Enrollment> orders = orderRepository.findAllByUser(user);
        return orders.stream().map(this::convertToOrderResponse).toList();
    }

    // Ok
    @Override
    public EnrollmentResponse createEnrollment(Integer courseId, String email) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new UsernameNotFoundException("Mã khóa học không tồn tại"));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại"));
        Optional<Enrollment> existingEnrollment = orderRepository.findByUserAndCourse(user, course);
        if (existingEnrollment.isPresent()) {
            throw new IllegalStateException("Bạn đã đăng ký khóa học này rồi");
        }
        Enrollment order = new Enrollment();
        order.setUser(user);
        order.setEnrolledTime(Instant.now());
        order.setCourse(course);

        Enrollment savedOrder = orderRepository.save(order);

        int totalStudent = course.getStudentCount();
        course.setStudentCount(totalStudent + 1);
        courseRepository.save(course);

        for (CourseChapter chapter : course.getCourseChapters()) {
            for (Lesson lesson : chapter.getLessons()) {
                CourseTracking courseTracking = new CourseTracking();
                courseTracking.setCourse(course);
                courseTracking.setChapter(chapter);
                courseTracking.setLesson(lesson);
                courseTracking.setUser(user);
                courseTracking.setCompleted(false);
                if (chapter.getChapterOrder() == 0 && lesson.getLessonOrder() == 1) {
                    courseTracking.setUnlock(true);
                    courseTracking.setCurrent(true);
                }
                courseTrackingRepository.save(courseTracking);
            }
        }
        return convertToOrderResponse(savedOrder);
    }

    // Ok
    private EnrollmentResponse convertToOrderResponse(Enrollment order) {
        EnrollmentResponse orderResponse = modelMapper.map(order, EnrollmentResponse.class);
        orderResponse.setCourseName(order.getCourse().getTitle());
        orderResponse.setCustomerName(order.getUser().getFullName());
        return orderResponse;
    }
}