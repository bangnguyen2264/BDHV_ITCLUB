package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.dto.reponse.AnswerLearningRequest;
import com.example.bdhv_itclub.dto.request.LessonRequestInQuiz;
import com.example.bdhv_itclub.dto.request.QuizLearningRequest;
import com.example.bdhv_itclub.entity.Answer;
import com.example.bdhv_itclub.entity.Lesson;
import com.example.bdhv_itclub.entity.Quiz;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.AnswerQuizRepository;
import com.example.bdhv_itclub.repository.LessonRepository;
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
    private final AnswerQuizRepository answerQuizRepository;

    public QuizServiceImpl(QuizRepository quizRepository, LessonRepository lessonRepository, AnswerQuizRepository answerQuizRepository) {
        this.quizRepository = quizRepository;
        this.lessonRepository = lessonRepository;
        this.answerQuizRepository = answerQuizRepository;
    }

    @Override
    public float gradeOfQuiz(LessonRequestInQuiz lessonRequestInQuiz) {
        Integer lessonId = lessonRequestInQuiz.getId();
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Lesson ID không tồn tại"));

        float totalQuizzes = lesson.getQuizList().size();
        float correctTotalQuizzes = 0;

        for (QuizLearningRequest quizLearningRequest : lessonRequestInQuiz.getListQuizzes()){
            Integer quizId = quizLearningRequest.getId();
            Quiz quizInDB = quizRepository.findById(quizId)
                    .orElseThrow(() -> new NotFoundException("Quiz ID không tồn tại"));

            if(quizInDB.getQuizType().toString().equals("ONE_CHOICE")){
                Integer answerId = quizLearningRequest.getListAnswers().get(0).getId();
                Answer answer = answerQuizRepository.checkAnswerInCorrect(answerId);
                if(answer != null) {
                    ++correctTotalQuizzes;
                }
            }else if(quizInDB.getQuizType().toString().equals("PERFORATE")){
                List<Answer> listAnswers = answerQuizRepository.listAllAnswerIsCorrect(quizId);
                // Lấy câu trả lời của user
                String contentAnswer = quizLearningRequest.getListAnswers().get(0).getContentPerforate();
                for (Answer answer : listAnswers){
                    // Kiểm tra xem câu trả lời có đúng không
                    if(contentAnswer.equalsIgnoreCase(answer.getContent())){
                        // Tăng số câu trả lời đúng
                        ++correctTotalQuizzes;
                        break;
                    }
                }
            }else{
                List<Answer> listAnswers = answerQuizRepository.listAllAnswerIsCorrect(quizId);
                float totalAnswerCorrectInList = listAnswers.size();
                float totalAnswerCorrectInThere = 0.0f;
                for (AnswerLearningRequest answerLearningRequest : quizLearningRequest.getListAnswers()){
                    Answer answer = answerQuizRepository.checkAnswerInCorrect(answerLearningRequest.getId());
                    if(answer != null){
                        ++totalAnswerCorrectInThere;
                    }else{
                        --totalAnswerCorrectInThere;
                    }
                }
                if(totalAnswerCorrectInThere < 0){
                    totalAnswerCorrectInThere = 0.0f;
                }
                float percentMultipleChoiceQuiz = totalAnswerCorrectInThere / totalAnswerCorrectInList;
                correctTotalQuizzes += percentMultipleChoiceQuiz;
            }
        }
        float grade = (correctTotalQuizzes * 10) / totalQuizzes;
        return (float) (Math.round(grade * 100.0) / 100.0);
    }
}
