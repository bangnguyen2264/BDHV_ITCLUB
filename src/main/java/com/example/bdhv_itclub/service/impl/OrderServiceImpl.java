package com.example.bdhv_itclub.service.impl;


import com.example.bdhv_itclub.dto.reponse.*;
import com.example.bdhv_itclub.entity.*;
import com.example.bdhv_itclub.repository.*;
import com.example.bdhv_itclub.service.OrderService;
import com.example.bdhv_itclub.service.UserService;
import com.example.bdhv_itclub.utils.EmailUtil;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseTrackingRepository courseTrackingRepository;
    private final EmailUtil emailUtil;

    public OrderServiceImpl(ModelMapper modelMapper, OrderRepository orderRepository, UserRepository userRepository, CourseRepository courseRepository, CourseTrackingRepository courseTrackingRepository, EmailUtil emailUtil, EnrollmentRepository enrollmentRepository) {
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.courseTrackingRepository = courseTrackingRepository;
        this.emailUtil = emailUtil;
    }

    // Ok
    @Override
    public List<CourseOrderResponse> getAll() {
        List<CourseOrder> orders = orderRepository.findAll();
        return orders.stream().map(this::convertToOrderResponse).toList();
    }

    // Ok
    @Override
    public List<CourseOrderResponse> getAllByUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Mã người dùng không tồn tại"));
        List<CourseOrder> orders = orderRepository.findAllByUser(user);
        return orders.stream().map(this::convertToOrderResponse).toList();
    }

    // Ok
    @Override
    public void createOrder(User user, Course course, int totalPrice) {

        // Thông tin đơn hàng
        CourseOrder order = new CourseOrder();
        order.setUser(user);
        order.setCreatedTime(Instant.now());
        order.setCourse(course);
        order.setTotalPrice(totalPrice);

        CourseOrder savedOrder = orderRepository.save(order);
        Enrollment enrollment = new Enrollment();
        enrollment.setId(savedOrder.getId());
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setEnrolledTime(Instant.now());
        enrollmentRepository.save(enrollment);


        emailUtil.sendOrderEmail("Xác nhận đơn hàng với mã: #[[orderId]]", """
            <div><div><div><b><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">Dear [[name]]</span></b><span style="font-size:
             12.0pt;font-family:&quot;Times New Roman&quot;,serif;mso-fareast-font-family:&quot;Times New Roman&quot;"><o:p></o:p></span></div>
            \s
             <div><b><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">This email is to confirm that you have
             successfully purchase. Please review the
             following order summary.</span></b><span style="font-size: 12pt;"><o:p></o:p></span></div>
            \s
             <div><span style="font-size: 12pt;">&nbsp;</span></div>
            \s
             <div><!--[if !supportLists]--><span style="font-size: 10pt; font-family: Symbol;">·<span style="font-variant-numeric: normal; font-variant-east-asian: normal; font-variant-alternates: normal; font-kerning: auto; font-optical-sizing: auto; font-feature-settings: normal; font-variation-settings: normal; font-variant-position: normal; font-stretch: normal; font-size: 7pt; line-height: normal; font-family: &quot;Times New Roman&quot;;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             </span></span><!--[endif]--><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">&nbsp;Order
             ID: [[orderId]]</span><span style="font-size: 12pt;"><o:p></o:p></span></div>
            \s
             <div><!--[if !supportLists]--><span style="font-size: 10pt; font-family: Symbol;">·<span style="font-variant-numeric: normal; font-variant-east-asian: normal; font-variant-alternates: normal; font-kerning: auto; font-optical-sizing: auto; font-feature-settings: normal; font-variation-settings: normal; font-variant-position: normal; font-stretch: normal; font-size: 7pt; line-height: normal; font-family: &quot;Times New Roman&quot;;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             </span></span><!--[endif]--><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">&nbsp;Order
             time: [[orderTime]]</span><span style="font-size: 12pt;"><o:p></o:p></span></div>
            \s
             <div><!--[if !supportLists]--><span style="font-size: 10pt; font-family: Symbol;">·<span style="font-variant-numeric: normal; font-variant-east-asian: normal; font-variant-alternates: normal; font-kerning: auto; font-optical-sizing: auto; font-feature-settings: normal; font-variation-settings: normal; font-variant-position: normal; font-stretch: normal; font-size: 7pt; line-height: normal; font-family: &quot;Times New Roman&quot;;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             </span></span><!--[endif]--><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">&nbsp;Course
             name: [[courseName]]</span><span style="font-size: 12pt;"><o:p></o:p></span></div>
            \s
             <div><!--[if !supportLists]--><span style="font-size: 10pt; font-family: Symbol;">·<span style="font-variant-numeric: normal; font-variant-east-asian: normal; font-variant-alternates: normal; font-kerning: auto; font-optical-sizing: auto; font-feature-settings: normal; font-variation-settings: normal; font-variant-position: normal; font-stretch: normal; font-size: 7pt; line-height: normal; font-family: &quot;Times New Roman&quot;;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             </span></span><!--[endif]--><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">&nbsp;Total:
             [[total]] VND</span><span style="font-size: 12pt;"><o:p></o:p></span></div>
            \s
             <div><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;"><br>
             <!--[if !supportLineBreakNewLine]--><br>
             <!--[endif]--></span><span style="font-size: 12pt;"><o:p></o:p></span></div>
            \s
             <div><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">You can now start studying this course. When you complete you will receive a certificate for this course.</span><span style="font-size: 12pt;"><o:p></o:p></span></div>
            \s
             <div><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;"><br>
             <!--[if !supportLineBreakNewLine]--><br>
             <!--[endif]--></span><span style="font-size: 12pt;"><o:p></o:p></span></div>
            \s
             <div><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">Thanks for purchasing products at ITClub Forum.</span><span style="font-size: 12pt;"><o:p></o:p></span></div>
            \s
             <div><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">The Tech Course Team.</span><span style="font-size: 12pt;"><o:p></o:p></span></div>
            \s
             <div><span style="font-size:15.0pt;
             font-family:&quot;Times New Roman&quot;,serif">&nbsp;</span></div></div></div>""", enrollment);

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
    }

    // Ok
    private CourseOrderResponse convertToOrderResponse(CourseOrder order) {
        CourseOrderResponse orderResponse = modelMapper.map(order, CourseOrderResponse.class);
        orderResponse.setCourseName(order.getCourse().getTitle());
        orderResponse.setCustomerName(order.getUser().getFullName());
        return orderResponse;
    }
}