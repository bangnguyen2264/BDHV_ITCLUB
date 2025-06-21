package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.dto.reponse.*;
import com.example.bdhv_itclub.dto.request.*;
import com.example.bdhv_itclub.entity.*;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.CourseRepository;
import com.example.bdhv_itclub.repository.UserRepository;
import com.example.bdhv_itclub.service.OrderService;
import com.example.bdhv_itclub.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PaymentServiceImpl implements PaymentService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final Payment payment;

    @Autowired
    public PaymentServiceImpl(CourseRepository courseRepository, UserRepository userRepository, OrderService orderService, Payment payment) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.orderService = orderService;
        this.payment = payment;
    }

    // Ok
    @Override
    public PaymentResponse getPaymentInfo(PaymentRequest paymentRequest) {
        Course course = courseRepository.findById(paymentRequest.getCourseId()).orElseThrow(() -> new NotFoundException("Mã khóa học không tồn tại"));
        User user = userRepository.findByEmail(paymentRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại"));
        String content = String.format("TC%02d%02d", user.getId(), course.getId());
        int amount = (int) (course.getPrice() - (course.getPrice() * course.getDiscount()));
        String qrCode = String.format("https://img.vietqr.io/image/970423-%s-qr_only.png?amount=%d&addInfo=%s&accountName=%s", payment.getBANK_NUMBER(), amount, content, payment.getACCOUNT_NAME());
        return PaymentResponse.builder().qrCode(qrCode).bankNumber(payment.getBANK_NUMBER()).content(content).accountName(payment.getACCOUNT_NAME()).bankBranch(payment.getBANK_BRANCH()).build();
    }

    // Ok
    @Override
    public boolean checkTransaction(TransactionRequest transactionRequest) {
        Course course = courseRepository.findById(transactionRequest.getCourseId()).orElseThrow(() -> new NotFoundException("Mã khóa học không tồn tại"));
        User user = userRepository.findByEmail(transactionRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại"));
        // Kiểm tra xem có giao dịch này trong lịch sử giao dịch của bank không
        BankTransactionInfo[] bankTransactionInfos = payment.getTransactions();
        for (BankTransactionInfo bankTransactionInfo : bankTransactionInfos) {
            if (Integer.parseInt(bankTransactionInfo.getAmount()) >= transactionRequest.getTotalPrice() && bankTransactionInfo.getDescription().contains(transactionRequest.getDescription())) {
                orderService.createOrder(user, course, transactionRequest.getTotalPrice());
                return true;
            }
        }
        return false;
    }
}