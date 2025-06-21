package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.constant.CommonStatus;
import com.example.bdhv_itclub.dto.reponse.ReportContestResponse;
import com.example.bdhv_itclub.dto.reponse.ReportCountResponse;
import com.example.bdhv_itclub.dto.reponse.ReportRevenueResponse;
import com.example.bdhv_itclub.entity.*;
import com.example.bdhv_itclub.repository.*;
import com.example.bdhv_itclub.service.ReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseCategoryRepository courseCategoryRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private RecordRepository recordRepository;

    private String PERIOD_TYPE = "DAY";

    // Ok
    @Override
    public ReportCountResponse count() {
        return ReportCountResponse.builder()
                .totalUsers((int) userRepository.count())
                .totalCategories((int) courseCategoryRepository.count())
                .totalCourses((int) courseRepository.count())
                .totalBlogs((int) blogRepository.count())
                .totalQuizzes((int) contestRepository.count())
                .totalEnrollments((int) enrollmentRepository.count())
                .totalReviews((int) reviewRepository.count())
                .build();
    }

    // Ok
    @Override
    public List<ReportRevenueResponse> getRevenueReport(String period) {
        Instant dateNow = Instant.now();
        Instant dateBefore = getDateBefore(dateNow, period);
        List<Enrollment> enrollments = enrollmentRepository.findByEnrolledTimeBetween(dateBefore, dateNow);
        return createReportRevenueList(dateNow, dateBefore, enrollments);
    }

    // Ok
    @Override
    public List<ReportRevenueResponse> getCategoryIncomeReport() {
        List<CourseCategory> courseCategories = courseCategoryRepository.findAll().stream().filter(category -> category.getStatus() != CommonStatus.DELETED).toList();
        List<Enrollment> enrollments = enrollmentRepository.findAllOrderCategory();
        List<ReportRevenueResponse> reportRevenueResponses = new ArrayList<>();

        courseCategories.forEach(category -> {
            ReportRevenueResponse reportRevenueResponse = new ReportRevenueResponse(category.getName());
//            int totalPrice = enrollments.stream().filter(order -> order.getCourse().getCategory().getName().equals(category.getName())).mapToInt(Enrollment::getTotalPrice).sum();
//            reportRevenueResponse.setTotalIncome(totalPrice);
            reportRevenueResponses.add(reportRevenueResponse);
        });
        return reportRevenueResponses;
    }

    // Ok
    public List<ReportRevenueResponse> getCourseIncomeReport(String period) {
        Instant dateNow = Instant.now();
        Instant dateBefore = getDateBefore(dateNow, period);

        List<Course> courses = courseRepository.findAll();
        List<Enrollment> enrollments = enrollmentRepository.findByEnrolledTimeBetween(dateBefore, dateNow);
        List<ReportRevenueResponse> reportRevenueResponses = new ArrayList<>();

        courses.forEach(course -> {
            ReportRevenueResponse reportRevenueResponse = new ReportRevenueResponse(course.getTitle());
//            int totalPrice = enrollments.stream().filter(order -> order.getCourse().getTitle().equals(course.getTitle())).mapToInt(Enrollment::getTotalPrice).sum();
//            reportRevenueResponse.setTotalIncome(totalPrice);
            int enrollmentCount = enrollments.stream().filter(order -> order.getCourse().getTitle().equals(course.getTitle())).toList().size();
            reportRevenueResponse.setEnrollmentCount(enrollmentCount);
            reportRevenueResponses.add(reportRevenueResponse);
        });
        return reportRevenueResponses;
    }

    // Ok
    @Override
    public List<ReportContestResponse> getContestReport() {
        List<Contest> contests = contestRepository.findAll();
        List<Record> records = recordRepository.findAll();
        List<ReportContestResponse> reportContestResponses = new ArrayList<>();

        contests.forEach(contest -> {
            ReportContestResponse reportContestResponse = new ReportContestResponse(contest.getTitle());
            List<Record> filteredRecord = records.stream().filter(record -> record.getContest().getTitle().equals(contest.getTitle())).toList();
            double totalGrade = filteredRecord.stream().mapToDouble(Record::getGrade).sum();
            float averageGrade = (float) (totalGrade / filteredRecord.size());
            reportContestResponse.setAverageGrade(averageGrade);
            reportContestResponse.setNumberOfJoined(filteredRecord.size());
            reportContestResponses.add(reportContestResponse);
        });
        return reportContestResponses;
    }

    // Ok
    private List<ReportRevenueResponse> createReportRevenueList(Instant now, Instant before, List<Enrollment> enrollments) {
        List<ReportRevenueResponse> reportRevenueResponses = new ArrayList<>();

        // Chuyển đổi Instant thành LocalDateTime
        LocalDateTime startDate = LocalDateTime.ofInstant(before, ZoneId.systemDefault());
        LocalDateTime endDate = LocalDateTime.ofInstant(now, ZoneId.systemDefault());

        // Lặp cho đến khi đạt đến endDate
        while (startDate.isBefore(endDate)) {
            List<Enrollment> filteredEnrollments = new ArrayList<>();
            ReportRevenueResponse reportRevenueResponse = new ReportRevenueResponse();
            LocalDate currentDate = startDate.toLocalDate();
            YearMonth currentMonth = YearMonth.from(startDate);

            if (PERIOD_TYPE.equals("DAY")) {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                // Tính tổng thu nhập và số đơn hàng cho ngày này
                filteredEnrollments = enrollments.stream().filter(order -> LocalDate.ofInstant(order.getEnrolledTime(), ZoneId.systemDefault()).equals(currentDate)).toList();
                reportRevenueResponse.setIdentifier(startDate.format(dateFormat));
                startDate = startDate.plus(1, ChronoUnit.DAYS);
            } else if (PERIOD_TYPE.equals("MONTH")) {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-yyyy");
                // Lọc các đơn hàng theo tháng
                filteredEnrollments = enrollments.stream().filter(order -> YearMonth.from(LocalDate.ofInstant(order.getEnrolledTime(), ZoneId.systemDefault())).equals(currentMonth)).toList();
                reportRevenueResponse.setIdentifier(startDate.format(dateFormat));
                startDate = startDate.plus(1, ChronoUnit.MONTHS).withDayOfMonth(1);
            }

            // Nếu có đơn hàng, cập nhật reportRevenueResponse
            if (!filteredEnrollments.isEmpty()) {
//                int totalIncome = filteredEnrollments.stream().mapToInt(Enrollment::getTotalPrice).sum();
//                reportRevenueResponse.setTotalIncome(totalIncome);
                reportRevenueResponse.setEnrollmentCount(filteredEnrollments.size());
            }
            reportRevenueResponses.add(reportRevenueResponse);
        }
        return reportRevenueResponses;
    }

    // Ok
    private Instant getDateBefore(Instant currentDay, String period) {
        switch (period) {
            case "last_14_days" -> {
                this.PERIOD_TYPE = "DAY";
                return currentDay.minus(Duration.ofDays(14));
            }
            case "last_year" -> {
                this.PERIOD_TYPE = "MONTH";
                return currentDay.minus(Duration.ofDays(365));
            }
            default -> {
                this.PERIOD_TYPE = "DAY";
                return currentDay.minus(Duration.ofDays(7));
            }
        }
    }
}