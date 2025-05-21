package com.example.bdhv_itclub.service.impl;


import com.example.bdhv_itclub.dto.reponse.AnswerDto;
import com.example.bdhv_itclub.dto.reponse.ContestResponse;
import com.example.bdhv_itclub.dto.reponse.ContestReturnInTest;
import com.example.bdhv_itclub.dto.reponse.QuizReturnLearningPage;
import com.example.bdhv_itclub.dto.request.ContestRequest;
import com.example.bdhv_itclub.dto.request.QuizRequest;
import com.example.bdhv_itclub.entity.Answer;
import com.example.bdhv_itclub.entity.Contest;
import com.example.bdhv_itclub.entity.Quiz;
import com.example.bdhv_itclub.entity.QuizType;
import com.example.bdhv_itclub.exception.ConflictException;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.ContestRepository;
import com.example.bdhv_itclub.repository.QuizRepository;
import com.example.bdhv_itclub.repository.RecordRepository;
import com.example.bdhv_itclub.service.ContestService;
import com.example.bdhv_itclub.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class ContestServiceImpl implements ContestService {
    private final ModelMapper modelMapper;
    private final ContestRepository contestRepository;
    private final RecordRepository recordRepository;

    private final QuizRepository quizRepository;

    public ContestServiceImpl(ModelMapper modelMapper, ContestRepository contestRepository, RecordRepository recordRepository, QuizRepository quizRepository) {
        this.modelMapper = modelMapper;
        this.contestRepository = contestRepository;
        this.recordRepository = recordRepository;
        this.quizRepository = quizRepository;
    }

    @Override
    public List<ContestResponse> listAll() {
        List<Contest> listContests = contestRepository.findAll();
        return listContests.stream().map(
                contest -> {
                    ContestResponse response = modelMapper.map(contest, ContestResponse.class);
                    response.setNumberQuestion(contest.getListQuizzes().size());
                    response.setListQuizzes(null);
                    int numberOfJoined = recordRepository.countAllByContest(contest);
                    response.setNumberOfJoined(numberOfJoined);
                    return response;
                }).toList();
    }

    @Override
    public ContestReturnInTest joinTest(Integer contestId) {
        Contest contestInDB = contestRepository.findById(contestId)
                .orElseThrow(() -> new NotFoundException("Contest ID không tồn tại"));

        ContestReturnInTest responseContest = modelMapper.map(contestInDB, ContestReturnInTest.class);
        List<QuizReturnLearningPage> quizReturnLearningPages = convertToQuizResponse(contestInDB.getListQuizzes());
        responseContest.setListQuizzes(quizReturnLearningPages);
        responseContest.setNumberQuestion(quizReturnLearningPages.size());
        return responseContest;
    }

    @Override
    public List<ContestResponse> search(String keyword) {
        List<Contest> listContests = contestRepository.search(keyword);
        return listContests.stream().map(
                contest -> {
                    ContestResponse response = modelMapper.map(contest, ContestResponse.class);
                    response.setNumberQuestion(contest.getListQuizzes().size());
                    response.setListQuizzes(null);
                    return response;
                }).toList();
    }

    @Override
    public ContestResponse save(ContestRequest contestRequest) {
        if(contestRepository.existsContestByTitle(contestRequest.getTitle())){
            throw new ConflictException( "Tên bài kiểm tra đã tồn tại!");
        }
        Contest contest = new Contest();
        contest.setTitle(contestRequest.getTitle());
        contest.setPeriod(contestRequest.getPeriod());
        contest.setEnabled(contestRequest.isEnabled());
        contest.setCreatedAt(Instant.now());
        for (QuizRequest quizRequest : contestRequest.getQuizList()){
            contest.add(Utils.convertToQuizEntity(quizRequest));
        }

        Contest savedContest = contestRepository.save(contest);
        return modelMapper.map(savedContest, ContestResponse.class);
    }

    @Override
    public ContestResponse update(Integer contestId, ContestRequest contestRequest) {
        Contest contestInDB = contestRepository.findById(contestId)
                .orElseThrow(() -> new NotFoundException("Contest ID không tồn tại"));

        Contest contestCheckDuplicate = contestRepository.findContestByTitle(contestRequest.getTitle());

        if(contestCheckDuplicate != null){
            if(!Objects.equals(contestInDB.getId(), contestCheckDuplicate.getId())){
                throw new ConflictException( "Tên bài kiểm tra đã tồn tại!");
            }
        }

        contestInDB.setTitle(contestRequest.getTitle());
        contestInDB.setPeriod(contestRequest.getPeriod());
        contestInDB.setEnabled(contestRequest.isEnabled());

        List<Quiz> listQuizzes = new ArrayList<>();
        for (QuizRequest quizRequest : contestRequest.getQuizList()){
            Quiz quiz = quizRequest.getId() == null ? Utils.convertToQuizEntity(quizRequest) : updateQuiz(quizRequest);
            quiz.setContest(contestInDB);
            listQuizzes.add(quiz);
        }
        contestInDB.setQuizList(listQuizzes);
        Contest savedContest = contestRepository.save(contestInDB);
        return modelMapper.map(savedContest, ContestResponse.class);
    }

    @Override
    public String delete(Integer contestId) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new NotFoundException("Contest ID không tồn tại"));

        contestRepository.delete(contest);
        return "Xóa bài thi thành công";
    }

    @Override
    public ContestResponse get(Integer contestId) {
        Contest contestInDB = contestRepository.findById(contestId)
                .orElseThrow(() -> new NotFoundException("Contest ID không tồn tại"));

        return modelMapper.map(contestInDB, ContestResponse.class);
    }

    @Override
    public String switchEnabled(Integer contestId, boolean enabled) {
        Contest contestInDB = contestRepository.findById(contestId)
                .orElseThrow(() -> new NotFoundException("Contest ID không tồn tại"));

        contestRepository.switchEnabled(contestInDB.getId(), enabled);
        return "Đổi trạng thái bài thi thành công";
    }

    private List<QuizReturnLearningPage> convertToQuizResponse(List<Quiz> quizzes) {
        List<QuizReturnLearningPage> listQuizzes = new ArrayList<>();
        int i = 0;
        for (Quiz quiz : quizzes) {
            QuizReturnLearningPage quizReturnLearningPage = modelMapper.map(quiz, QuizReturnLearningPage.class);
            if (quiz.getQuizType().toString().equals("PERFORATE")) {
                quizReturnLearningPage.setAnswerList(null);
            }
            quizReturnLearningPage.setOrder(++i);
            listQuizzes.add(quizReturnLearningPage);
        }
        return listQuizzes;
    }

    private Quiz updateQuiz(QuizRequest quizRequest) {
        Quiz quizInDB = quizRepository.findById(quizRequest.getId())
                .orElseThrow(() -> new NotFoundException("Quiz ID không tồn tại"));

        quizInDB.setQuestion(quizRequest.getQuestion());
        quizInDB.setQuizType(QuizType.valueOf(quizRequest.getQuizType()));
        List<Answer> answerList = new ArrayList<>();
        for(AnswerDto dto : quizRequest.getAnswerList()){
            Answer answer = null;
            if(dto.getId() != null){
                answer = new Answer(dto.getId(), dto.getContent(), dto.isCorrect(), quizInDB);
            }else{
                answer = new Answer(dto.getContent(), dto.isCorrect(), quizInDB);
            }
            answerList.add(answer);
        }

        quizInDB.setAnswerList(answerList);

        return quizInDB;
    }
}
