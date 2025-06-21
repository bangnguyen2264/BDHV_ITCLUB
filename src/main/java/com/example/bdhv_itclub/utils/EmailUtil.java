package com.example.bdhv_itclub.utils;

import com.example.bdhv_itclub.dto.request.FeedbackEmailDTO;
import com.example.bdhv_itclub.entity.Enrollment;
import com.example.bdhv_itclub.entity.User;
import com.example.bdhv_itclub.exception.BadRequestException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * Hỗ trợ gửi email dưới dạng HTML cho các mục đích khác nhau như:
 * thông báo người dùng, xác nhận đơn hàng, và phản hồi.
 * Các email được gửi bất đồng bộ (asynchronously) để không làm gián đoạn luồng chính của ứng dụng.
 * Nếu có lỗi trong quá trình gửi email, sẽ ném ra ngoại lệ tùy chỉnh để xử lý phù hợp.
 */
@Component
public class EmailUtil {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;

    public EmailUtil(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(String url, String subject, String content, User user) {
        String destinationAddress = user.getEmail();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        try {
            messageHelper.setFrom(email, "ITClub Forum");
            messageHelper.setTo(destinationAddress);
            messageHelper.setSubject(subject);

            content = content.replace("[[name]]", user.getFullName());
            content = content.replace("[[URL]]", url);
            messageHelper.setText(content, true);
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new BadRequestException("Không gửi được email");
        }
    }

    @Async
    public void sendOrderEmail(String subject, String content, Enrollment order) {
        String destinationAddress = order.getUser().getEmail();
        subject = subject.replace("[[orderId]]", order.getId().toString());

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        try {
            messageHelper.setFrom(email, "ITClub Forum");
            messageHelper.setTo(destinationAddress);
            messageHelper.setSubject(subject);

            content = content.replace("[[name]]", order.getUser().getFullName());
            content = content.replace("[[orderId]]", order.getId().toString());
            content = content.replace("[[orderTime]]", order.getEnrolledTime().toString());
            content = content.replace("[[courseName]]", order.getCourse().getTitle());

            messageHelper.setText(content, true);
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new BadRequestException("Không gửi được email");
        }
    }

    @Async
    public void sendFeedbackEmail(FeedbackEmailDTO sendEmail) {
        String destinationAddress = sendEmail.getToEmail();
        String subject = sendEmail.getSubject();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        try {
            messageHelper.setFrom(email, "ITClub Forum");
            messageHelper.setTo(destinationAddress);
            messageHelper.setSubject(subject);

            String content = sendEmail.getContent();

            messageHelper.setText(content, true);
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new BadRequestException("Không gửi được email");
        }
    }
}