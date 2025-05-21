package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.dto.reponse.ContestReportResponse;
import com.example.bdhv_itclub.dto.reponse.CountReportResponse;
import com.example.bdhv_itclub.dto.reponse.RevenueReportResponse;
import com.example.bdhv_itclub.entity.*;
import com.example.bdhv_itclub.entity.Record;
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
    private CategoryRepository categoryRepository;
    @Autowired
    private CoursesRepository coursesRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private RecordRepository recordRepository;

    private String PERIOD_TYPE = "DAY";

    @Override
    public CountReportResponse count() {
        return CountReportResponse.builder().totalUsers((int) userRepository.count()).totalCategories((int) categoryRepository.count()).totalCourses((int) coursesRepository.count()).totalBlogs((int) blogRepository.count()).totalQuizzes((int) contestRepository.count()).totalOrders((int) orderRepository.count()).totalIncomes(orderRepository.sumInCome()).totalReviews((int) reviewRepository.count()).build();
    }

    @Override
    public List<RevenueReportResponse> getRevenueReport(String period) {
        Instant dateNow = Instant.now();
        Instant dateBefore = getDateBefore(dateNow, period);

        List<Order> orders = orderRepository.findByOrderTimeBetween(dateBefore, dateNow);

        return createReportRevenueList(dateNow, dateBefore, orders);
    }

    @Override
    public List<RevenueReportResponse> getCategoryIncomeReport() {
        List<Category> categories = categoryRepository.findAll().stream().filter(category -> category.getStatus() != Status.DELETED).toList();
        List<Order> orders = orderRepository.findAllOrderCategory();
        List<RevenueReportResponse> responseList = new ArrayList<>();

        categories.forEach(category -> {
            RevenueReportResponse reportRevenueResponse = new RevenueReportResponse(category.getName());

            int totalPrice = orders.stream().filter(order -> order.getCourses().getCategory().getName().equals(category.getName())).mapToInt(Order::getTotalPrice).sum();
            reportRevenueResponse.setTotalIncome(totalPrice);

            responseList.add(reportRevenueResponse);
        });

        return responseList;
    }

    public List<RevenueReportResponse> getCourseIncomeReport(String period) {
        Instant dateNow = Instant.now();
        Instant dateBefore = getDateBefore(dateNow, period);

        List<Courses> courses = coursesRepository.findAll();
        List<Order> orders = orderRepository.findByOrderTimeBetween(dateBefore, dateNow);
        List<RevenueReportResponse> responseList = new ArrayList<>();

        courses.forEach(course -> {
            RevenueReportResponse reportRevenueResponse = new RevenueReportResponse(course.getTitle());

            int totalPrice =
                    orders.stream().filter(order -> order.getCourses().getTitle().equals(course.getTitle())).mapToInt(Order::getTotalPrice).sum();
            reportRevenueResponse.setTotalIncome(totalPrice);

            int orderCount =
                    orders.stream().filter(order -> order.getCourses().getTitle().equals(course.getTitle())).toList().size();
            reportRevenueResponse.setOrderCount(orderCount);

            responseList.add(reportRevenueResponse);
        });
        return responseList;
    }

    @Override
    public List<ContestReportResponse> getContestReport() {
        List<Contest> contests = contestRepository.findAll();
        List<Record> records = recordRepository.findAll();
        List<ContestReportResponse> contestReportResponses = new ArrayList<>();

        contests.forEach(contest -> {
            ContestReportResponse contestReportResponse = new ContestReportResponse(contest.getTitle());
            List<Record> filterRecord =
                    records.stream().filter(record -> record.getContest().getTitle().equals(contest.getTitle())).toList();
            double totalGrade = filterRecord.stream().mapToDouble(Record::getGrade).sum();
            float avgGrade = (float) (totalGrade / filterRecord.size());
            contestReportResponse.setAverageGrade(avgGrade);
            contestReportResponse.setNumberOfJoined(filterRecord.size());

            contestReportResponses.add(contestReportResponse);
        });

        return contestReportResponses;
    }

    private List<RevenueReportResponse> createReportRevenueList(Instant now, Instant before, List<Order> orders) {
        List<RevenueReportResponse> responseList = new ArrayList<>();

        // Chuyển đổi Instant thành LocalDateTime
        LocalDateTime startDate = LocalDateTime.ofInstant(before, ZoneId.systemDefault());
        LocalDateTime endDate = LocalDateTime.ofInstant(now, ZoneId.systemDefault());

        // Lặp cho đến khi đạt đến endDate
        while (startDate.isBefore(endDate)) {
            List<Order> filteredOrders = new ArrayList<>();
            RevenueReportResponse reportRevenueResponse = new RevenueReportResponse();
            LocalDate currentDate = startDate.toLocalDate();
            YearMonth currentMonth = YearMonth.from(startDate);

            if (PERIOD_TYPE.equals("DAY")) {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                // Tính tổng thu nhập và số đơn hàng cho ngày này
                filteredOrders = orders.stream().filter(order -> LocalDate.ofInstant(order.getCreatedTime(), ZoneId.systemDefault()).equals(currentDate)).toList();

                reportRevenueResponse.setIdentifier(startDate.format(dateFormat));

                startDate = startDate.plus(1, ChronoUnit.DAYS);
            } else if (PERIOD_TYPE.equals("MONTH")) {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-yyyy");
                // Lọc các đơn hàng theo tháng
                filteredOrders = orders.stream().filter(order -> YearMonth.from(LocalDate.ofInstant(order.getCreatedTime(), ZoneId.systemDefault())).equals(currentMonth)).toList();

                reportRevenueResponse.setIdentifier(startDate.format(dateFormat));

                startDate = startDate.plus(1, ChronoUnit.MONTHS).withDayOfMonth(1);
            }

            // Nếu có đơn hàng, cập nhật reportRevenueResponse
            if (!filteredOrders.isEmpty()) {
                int totalIncome = filteredOrders.stream().mapToInt(Order::getTotalPrice).sum();
                reportRevenueResponse.setTotalIncome(totalIncome);
                reportRevenueResponse.setOrderCount(filteredOrders.size());
            }

            responseList.add(reportRevenueResponse);
        }

        return responseList;
    }

    private Instant getDateBefore(Instant dateNow, String period) {

        switch (period) {
            case "last_14_days" -> {
                this.PERIOD_TYPE = "DAY";
                return dateNow.minus(Duration.ofDays(14));
            }
            case "last_year" -> {
                this.PERIOD_TYPE = "MONTH";
                return dateNow.minus(Duration.ofDays(365));
            }
            default -> {
                this.PERIOD_TYPE = "DAY";
                return dateNow.minus(Duration.ofDays(7));
            }
        }
    }

}
