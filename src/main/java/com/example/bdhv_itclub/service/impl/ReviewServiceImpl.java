package com.example.bdhv_itclub.service.impl;


import com.example.bdhv_itclub.dto.reponse.ListReviewResponse;
import com.example.bdhv_itclub.dto.reponse.ReviewResponse;
import com.example.bdhv_itclub.dto.request.ReviewRequest;
import com.example.bdhv_itclub.entity.Courses;
import com.example.bdhv_itclub.entity.Review;
import com.example.bdhv_itclub.entity.User;
import com.example.bdhv_itclub.exception.ConflictException;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.CoursesRepository;
import com.example.bdhv_itclub.repository.OrderRepository;
import com.example.bdhv_itclub.repository.ReviewRepository;
import com.example.bdhv_itclub.repository.UserRepository;
import com.example.bdhv_itclub.service.ReviewService;
import com.example.bdhv_itclub.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Transactional
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ModelMapper modelMapper;
    private final CoursesRepository coursesRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public ReviewServiceImpl(ModelMapper modelMapper, CoursesRepository coursesRepository, ReviewRepository reviewRepository, UserRepository userRepository, OrderRepository orderRepository) {
        this.modelMapper = modelMapper;
        this.coursesRepository = coursesRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public ListReviewResponse listAllByCourse(Integer courseId) {
        Courses courses = coursesRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Course ID không tồn tại"));
        List<Review> listReview = reviewRepository.findByCourses(courses);
        return convertToListReviewResponse(listReview);
    }

    @Override
    public String checkCustomerToReviewed(Integer userId, Integer courseId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User ID không tồn tại"));

        Courses courses = coursesRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Course ID không tồn tại"));

        // Kiểm tra user xem đã mua khoá học này chưa
        if (orderRepository.existsOrderByCoursesAndUser(courses, user)) {
            // Kiểm tra xem user đã đánh giá khoá học này chưa
            if (reviewRepository.existsReviewByUserAndCourses(user, courses)) {
                throw new ConflictException("Bạn đã đánh giá khóa học này trước đó!");
            }
            return "Bạn vui lòng đánh giá khóa học này!";
        }
        throw new AccessDeniedException("Bạn chưa mua khoá học này!");
    }

    @Override
    public ReviewResponse createReview(ReviewRequest reviewRequest) {
        User user = userRepository.findById(reviewRequest.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User ID không tồn tại"));

        Courses courses = coursesRepository.findById(reviewRequest.getCourseId()).orElseThrow(() -> new NotFoundException("Courses ID không tồn tại"));

        if (!orderRepository.existsOrderByCoursesAndUser(courses, user)) {
            throw new AccessDeniedException("Tài khoản " + user.getUsername() + " chưa từng mua khóa học này!");
        }

        if (reviewRepository.existsReviewByUserAndCourses(user, courses)) {
            throw new ConflictException("Tài khoản " + user.getUsername() + " đã đánh giá khóa học này trước đó!");
        }

        Review review = new Review();
        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());
        review.setUser(user);
        review.setCourses(courses);
        review.setReviewTime(Instant.now());

        Review savedReview = reviewRepository.save(review);

        return convertToReviewResponse(savedReview);
    }

    @Override
    public String deleteReview(Integer reviewId) {
        Review reviewInDB = reviewRepository.findById(reviewId).orElseThrow(() -> new NotFoundException("Review ID không tồn tại"));

        reviewRepository.delete(reviewInDB);
        return "Xóa đánh giá thành công!";
    }

    @Override
    public ListReviewResponse listAll() {
        List<Review> listReviews = reviewRepository.findAll();
        return convertToListReviewResponse(listReviews);
    }

    // Convert danh sách review thành DTO
    private ListReviewResponse convertToListReviewResponse(List<Review> listReviews) {
        ListReviewResponse listReviewResponse = new ListReviewResponse();
        listReviewResponse.setListResponses(listReviews.stream().map(this::convertToReviewResponse).toList());
        int totalReview = listReviews.size();
        listReviewResponse.setTotalReview(totalReview);
        int totalRating = listReviews.stream().mapToInt(Review::getRating).sum();
        double averageReview = (double) totalRating / totalReview;
        averageReview = Math.round(averageReview * 10.0) / 10.0;
        listReviewResponse.setAverageReview(averageReview);
        return listReviewResponse;
    }

    // Convert review thành DTO
    private ReviewResponse convertToReviewResponse(Review savedReview) {
        ReviewResponse response = modelMapper.map(savedReview, ReviewResponse.class);
        response.setTimeFormatted(Utils.formatDuration(Duration.between(savedReview.getReviewTime(), Instant.now())));
        response.setUserId(savedReview.getUser().getId());
        response.setUsername(savedReview.getUser().getUsername());
        response.setPhotoUser(savedReview.getUser().getPhoto());
        response.setCourseId(savedReview.getCourses().getId());
        response.setTitleCourse(savedReview.getCourses().getTitle());
        return response;
    }


}
