package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.dto.reponse.ReviewListResponse;
import com.example.bdhv_itclub.dto.reponse.ReviewResponse;
import com.example.bdhv_itclub.dto.request.ReviewRequest;
import com.example.bdhv_itclub.entity.Course;
import com.example.bdhv_itclub.entity.Review;
import com.example.bdhv_itclub.entity.User;
import com.example.bdhv_itclub.exception.ConflictException;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.CourseRepository;
import com.example.bdhv_itclub.repository.EnrollmentRepository;
import com.example.bdhv_itclub.repository.ReviewRepository;
import com.example.bdhv_itclub.repository.UserRepository;
import com.example.bdhv_itclub.service.ReviewService;
import com.example.bdhv_itclub.utils.GlobalUtil;
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
    private final CourseRepository courseRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository orderRepository;

    public ReviewServiceImpl(ModelMapper modelMapper, CourseRepository courseRepository, ReviewRepository reviewRepository, UserRepository userRepository, EnrollmentRepository orderRepository) {
        this.modelMapper = modelMapper;
        this.courseRepository = courseRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    // Ok
    @Override
    public ReviewListResponse listAll() {
        List<Review> reviews = reviewRepository.findAll();
        return convertToReviewListResponse(reviews);
    }

    // Ok
    @Override
    public ReviewListResponse listAllByCourse(Integer courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Mã khóa học không tồn tại"));
        List<Review> reviews = reviewRepository.findByCourse(course);
        return convertToReviewListResponse(reviews);
    }

    // Ok
    @Override
    public ReviewResponse createReview(ReviewRequest reviewRequest, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Tài khoản không tồn tại"));
        Course course = courseRepository.findById(reviewRequest.getCourseId()).orElseThrow(() -> new NotFoundException("Mã khóa học không tồn tại"));
        if (!orderRepository.existsByCourseAndUser(course, user)) {
            throw new AccessDeniedException("Tài khoản " + user.getUsername() + " chưa từng mua khóa học này");
        }
        if (reviewRepository.existsReviewByUserAndCourse(user, course)) {
            throw new AccessDeniedException("Tài khoản " + user.getUsername() + " đã đánh giá khóa học này trước đó");
        }
        Review review = new Review();
        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());
        review.setUser(user);
        review.setCourse(course);
        review.setReviewTime(Instant.now());

        Review savedReview = reviewRepository.save(review);
        return convertToReviewResponse(savedReview);
    }

    // Ok
    @Override
    public String deleteReview(Integer reviewId, String email) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new NotFoundException("Mã đánh giá không tồn tại"));
        User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Tài khoản không tồn tại"));

        // Nếu không phải chủ sở hữu đánh giá và không phải admin
        boolean isOwner = review.getUser().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole().getName().equalsIgnoreCase("ROLE_ADMIN");

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Bạn không có quyền xóa đánh giá này");
        }
        reviewRepository.delete(review);
        return "Xóa đánh giá thành công";
    }

    // Ok
    @Override
    public String checkReviewedByCourse(Integer userId, Integer courseId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Mã người dùng không tồn tại"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Mã khóa học không tồn tại"));

        // Kiểm tra user xem đã mua khoá học này chưa
        if (orderRepository.existsByCourseAndUser(course, user)) {
            if (reviewRepository.existsReviewByUserAndCourse(user, course)) {
                throw new ConflictException("Bạn đã đánh giá khóa học này trước đó");
            }
            return "Bạn vui lòng đánh giá khóa học này";
        }
        throw new AccessDeniedException("Bạn chưa mua khoá học này");
    }

    // Ok
    private ReviewListResponse convertToReviewListResponse(List<Review> reviews) {
        ReviewListResponse reviewsResponse = new ReviewListResponse();
        reviewsResponse.setReviewResponses(reviews.stream().map(this::convertToReviewResponse).toList());
        int totalReview = reviews.size();
        reviewsResponse.setTotalReview(totalReview);
        int totalRating = reviews.stream().mapToInt(Review::getRating).sum();
        double averageReview = (double) totalRating / totalReview;
        averageReview = Math.round(averageReview * 10.0) / 10.0;
        reviewsResponse.setAverageReview(averageReview);
        return reviewsResponse;
    }

    // Ok
    private ReviewResponse convertToReviewResponse(Review review) {
        ReviewResponse reviewResponse = modelMapper.map(review, ReviewResponse.class);
        reviewResponse.setFormattedTime(GlobalUtil.convertDurationToString(Duration.between(review.getReviewTime(), Instant.now())));
        reviewResponse.setUserId(review.getUser().getId());
        reviewResponse.setUsername(review.getUser().getUsername());
        reviewResponse.setUserPhoto(review.getUser().getPhoto());
        reviewResponse.setCourseId(review.getCourse().getId());
        reviewResponse.setCourseTitle(review.getCourse().getTitle());
        return reviewResponse;
    }
}