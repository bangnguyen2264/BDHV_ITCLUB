package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.dto.request.LessonRequestInQuiz;
import com.example.bdhv_itclub.dto.request.QuizAnswerLearningRequest;
import com.example.bdhv_itclub.dto.request.QuizLearningRequest;
import com.example.bdhv_itclub.entity.Lesson;
import com.example.bdhv_itclub.entity.Quiz;
import com.example.bdhv_itclub.entity.QuizAnswer;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.LessonRepository;
import com.example.bdhv_itclub.repository.QuizAnswerRepository;
import com.example.bdhv_itclub.repository.QuizRepository;
import com.example.bdhv_itclub.service.QuizService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;
    private final LessonRepository lessonRepository;
    private final QuizAnswerRepository quizAnswerRepository;

    public QuizServiceImpl(QuizRepository quizRepository, LessonRepository lessonRepository, QuizAnswerRepository quizAnswerRepository) {
        this.quizRepository = quizRepository;
        this.lessonRepository = lessonRepository;
        this.quizAnswerRepository = quizAnswerRepository;
    }

    // Ok
    @Override
    public float gradeOfQuiz(LessonRequestInQuiz lessonRequestInQuiz) {
        Integer lessonId = lessonRequestInQuiz.getId();
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Mã bài học không tồn tại"));

        float totalQuizzes = lesson.getQuizzes().size();
        float totalCorrectQuizzes = 0;

        for (QuizLearningRequest quizLearningRequest : lessonRequestInQuiz.getQuizzes()) {
            Integer quizId = quizLearningRequest.getId();
            Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new NotFoundException("Mã câu hỏi không tồn tại"));

            if (quiz.getQuizType().toString().equals("ONE_CHOICE")) {
                Integer answerId = quizLearningRequest.getAnswers().get(0).getId();
                QuizAnswer quizAnswer = quizAnswerRepository.checkCorrectAnswer(answerId);
                if (quizAnswer != null) {
                    ++totalCorrectQuizzes;
                }
            } else if (quiz.getQuizType().toString().equals("PERFORATE")) {
                List<QuizAnswer> quizAnswers = quizAnswerRepository.listAllCorrectAnswer(quizId);
                String answerContent = quizLearningRequest.getAnswers().get(0).getPerforatedContent();
                for (QuizAnswer answer : quizAnswers) {
                    if (answerContent.equalsIgnoreCase(answer.getContent())) {
                        ++totalCorrectQuizzes;
                        break;
                    }
                }
            } else {
                List<QuizAnswer> quizAnswers = quizAnswerRepository.listAllCorrectAnswer(quizId);
                float totalCorrectAnswer = quizAnswers.size();
                float totalUserCorrectAnswer = 0.0f;
                for (QuizAnswerLearningRequest quizAnswerLearningRequest : quizLearningRequest.getAnswers()) {
                    QuizAnswer answer = quizAnswerRepository.checkCorrectAnswer(quizAnswerLearningRequest.getId());
                    if (answer != null) {
                        ++totalUserCorrectAnswer;
                    } else {
                        --totalUserCorrectAnswer;
                    }
                }
                if (totalUserCorrectAnswer < 0) {
                    totalUserCorrectAnswer = 0.0f;
                }
                float multipleChoiceQuizPercent = totalUserCorrectAnswer / totalCorrectAnswer;
                totalCorrectQuizzes += multipleChoiceQuizPercent;
            }
        }
        float grade = (totalCorrectQuizzes * 10) / totalQuizzes;
        return (float) (Math.round(grade * 100.0) / 100.0);
    }
}