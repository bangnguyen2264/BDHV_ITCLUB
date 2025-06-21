package com.example.bdhv_itclub.utils;


import com.example.bdhv_itclub.constant.QuizType;
import com.example.bdhv_itclub.dto.request.QuizAnswerDTO;
import com.example.bdhv_itclub.dto.request.QuizRequest;
import com.example.bdhv_itclub.entity.Quiz;
import com.example.bdhv_itclub.exception.BadRequestException;
import org.springframework.http.HttpStatus;

import java.time.Duration;

/**
 * Lớp tiện ích chứa các phương thức tĩnh dùng chung trong toàn bộ ứng dụng,
 * bao gồm các chức năng như chuyển đổi thời gian sang chuỗi mô tả, tạo slug từ chuỗi,
 * và chuyển đổi DTO sang entity cho Quiz.
 */
public class GlobalUtil {
    public static String convertDurationToString(Duration duration) {
        long seconds = duration.getSeconds();
        if (seconds < 60) {
            return seconds + " giây trước";
        } else if (seconds < 3600) {
            return (seconds / 60) + " phút trước";
        } else if (seconds < 86400) {
            return (seconds / 3600) + " giờ trước";
        } else if (seconds < 2592000) {
            return (seconds / 86400) + " ngày trước";
        } else if (seconds < 31536000) {
            return (seconds / 2592000) + " tháng trước";
        } else {
            return (seconds / 31536000) + " năm trước";
        }
    }

    public static String convertToSlug(String stringToConvert) {
        String result = stringToConvert;

        result = result.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a");
        result = result.replaceAll("[èéẹẻẽêềếệểễ]", "e");
        result = result.replaceAll("[ìíịỉĩ]", "i");
        result = result.replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o");
        result = result.replaceAll("[ùúụủũưừứựửữ]", "u");
        result = result.replaceAll("[ỳýỵỷỹ]", "y");
        result = result.replaceAll("đ", "d");

        result = result.replaceAll("[^a-zA-Z0-9\\s]", "");

        String slug = result.trim().toLowerCase();
        slug = slug.replaceAll("\\s+", "-");
        return slug;
    }

    public static Quiz convertToQuizEntity(QuizRequest quizRequest) {
        Quiz quiz = new Quiz();
        quiz.setQuestion(quizRequest.getQuestion());
        quiz.setQuizType(QuizType.valueOf(quizRequest.getQuizType()));

        boolean flag = false;

        for (QuizAnswerDTO answerDto : quizRequest.getAnswers()) {
            if (answerDto.isCorrect()) {
                flag = true;
            }
            quiz.addAnAnswer(answerDto.getContent(), answerDto.isCorrect());
        }

        if (!flag) {
            throw new BadRequestException("Không có câu trả lời đúng trong danh sách câu trả lời");
        }
        return quiz;
    }
}