package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.constant.QuizType;
import com.example.bdhv_itclub.dto.reponse.ContestResponse;
import com.example.bdhv_itclub.dto.reponse.ContestResponseForTest;
import com.example.bdhv_itclub.dto.reponse.QuizResponseForLearningPage;
import com.example.bdhv_itclub.dto.request.ContestRequest;
import com.example.bdhv_itclub.dto.request.QuizAnswerDTO;
import com.example.bdhv_itclub.dto.request.QuizRequest;
import com.example.bdhv_itclub.entity.Contest;
import com.example.bdhv_itclub.entity.Quiz;
import com.example.bdhv_itclub.entity.QuizAnswer;
import com.example.bdhv_itclub.exception.ConflictException;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.ContestRepository;
import com.example.bdhv_itclub.repository.QuizRepository;
import com.example.bdhv_itclub.repository.RecordRepository;
import com.example.bdhv_itclub.service.ContestService;

import com.example.bdhv_itclub.utils.GlobalUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // Ok
    @Override
    public ContestResponse get(Integer contestId) {
        Contest contest = contestRepository.findById(contestId).orElseThrow(() -> new NotFoundException("Mã cuộc thi không tồn tại"));
        return modelMapper.map(contest, ContestResponse.class);
    }

    // Ok
    @Override
    public Page<ContestResponse> listAllContests(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException e) {
            sortDirection = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<Contest> contests = contestRepository.findAll(pageable);
        return contests.map(contest -> {
            ContestResponse contestResponse = modelMapper.map(contest, ContestResponse.class);
            contestResponse.setNumberOfQuestion(contest.getQuizzes().size());
            contestResponse.setQuizzes(null);
            int numberOfJoined = recordRepository.countAllByContest(contest);
            contestResponse.setNumberOfJoined(numberOfJoined);
            return contestResponse;
        });
    }

    // Ok
    @Override
    public Page<ContestResponse> listAllEnabledContests(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException e) {
            sortDirection = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<Contest> contests = contestRepository.findAllByEnabledTrue(pageable);
        return contests.map(contest -> {
            ContestResponse contestResponse = modelMapper.map(contest, ContestResponse.class);
            contestResponse.setNumberOfQuestion(contest.getQuizzes().size());
            contestResponse.setQuizzes(null);
            int numberOfJoined = recordRepository.countAllByContest(contest);
            contestResponse.setNumberOfJoined(numberOfJoined);
            return contestResponse;
        });
    }

    // Ok
    @Override
    public ContestResponse save(ContestRequest contestRequest) {
        if (contestRepository.existsByTitle(contestRequest.getTitle())) {
            throw new ConflictException( "Tên bài kiểm tra đã tồn tại");
        }
        Contest contest = new Contest();
        contest.setTitle(contestRequest.getTitle());
        contest.setPeriod(contestRequest.getPeriod());
        contest.setEnabled(contestRequest.isEnabled());
        contest.setCreatedAt(Instant.now());
        for (QuizRequest quizRequest : contestRequest.getQuizzes()) {
            contest.addAQuiz(GlobalUtil.convertToQuizEntity(quizRequest));
        }
        Contest savedContest = contestRepository.save(contest);
        return modelMapper.map(savedContest, ContestResponse.class);
    }

    // Ok
    @Override
    public ContestResponse update(Integer contestId, ContestRequest contestRequest) {
        Contest contest = contestRepository.findById(contestId).orElseThrow(() -> new NotFoundException("Mã cuộc thi không tồn tại"));
        Contest checkDuplicatedContest = contestRepository.findByTitle(contestRequest.getTitle());

        if (checkDuplicatedContest != null) {
            if (!Objects.equals(contest.getId(), checkDuplicatedContest.getId())) {
                throw new ConflictException( "Tên bài kiểm tra đã tồn tại");
            }
        }
        contest.setTitle(contestRequest.getTitle());
        contest.setPeriod(contestRequest.getPeriod());
        contest.setEnabled(contestRequest.isEnabled());

        List<Quiz> quizzes = new ArrayList<>();
        for (QuizRequest quizRequest : contestRequest.getQuizzes()) {
            Quiz quiz = quizRequest.getId() == null ? GlobalUtil.convertToQuizEntity(quizRequest) : updateQuiz(quizRequest);
            quiz.setContest(contest);
            quizzes.add(quiz);
        }
        contest.setQuizzes(quizzes);
        Contest savedContest = contestRepository.save(contest);
        return modelMapper.map(savedContest, ContestResponse.class);
    }

    // Ok
    @Override
    public String delete(Integer contestId) {
        Contest contest = contestRepository.findById(contestId).orElseThrow(() -> new NotFoundException("Mã cuộc thi không tồn tại"));
        contestRepository.delete(contest);
        return "Xóa cuộc thi thành công";
    }

    // Ok
    @Override
    public String switchEnabled(Integer contestId, boolean enabled) {
        Contest contest = contestRepository.findById(contestId).orElseThrow(() -> new NotFoundException("Mã cuộc thi không tồn tại"));
        contestRepository.switchEnabled(contest.getId(), enabled);
        return "Cuộc thi đã được chuyển đổi trạng thái";
    }

    // Ok
    @Override
    public ContestResponseForTest joinTest(Integer contestId) {
        Contest contest = contestRepository.findById(contestId).orElseThrow(() -> new NotFoundException("Mã cuộc thi không tồn tại"));
        ContestResponseForTest contestResponseForTest = modelMapper.map(contest, ContestResponseForTest.class);
        List<QuizResponseForLearningPage> quizResponseForLearningPage = convertToQuizResponse(contest.getQuizzes());
        contestResponseForTest.setQuizzes(quizResponseForLearningPage);
        contestResponseForTest.setNumberOfQuestion(quizResponseForLearningPage.size());
        return contestResponseForTest;
    }

    // Ok
    @Override
    public List<ContestResponse> search(String keyword) {
        List<Contest> contests = contestRepository.search(keyword);
        return contests.stream().map(contest -> {
            ContestResponse contestResponse = modelMapper.map(contest, ContestResponse.class);
            contestResponse.setNumberOfQuestion(contest.getQuizzes().size());
            contestResponse.setQuizzes(null);
            return contestResponse;
        }).toList();
    }

    // Ok
    private List<QuizResponseForLearningPage> convertToQuizResponse(List<Quiz> quizzes) {
        List<QuizResponseForLearningPage> quizResponseForLearningPages = new ArrayList<>();
        int i = 0;
        for (Quiz quiz : quizzes) {
            QuizResponseForLearningPage quizResponseForLearningPage = modelMapper.map(quiz, QuizResponseForLearningPage.class);
            if (quiz.getQuizType().toString().equals("PERFORATE")) {
                quizResponseForLearningPage.setAnswers(null);
            }
            quizResponseForLearningPage.setOrder(++i);
            quizResponseForLearningPages.add(quizResponseForLearningPage);
        }
        return quizResponseForLearningPages;
    }

    // Ok
    private Quiz updateQuiz(QuizRequest quizRequest) {
        Quiz quiz = quizRepository.findById(quizRequest.getId()).orElseThrow(() -> new NotFoundException("Mã câu hỏi không tồn tại"));
        quiz.setQuestion(quizRequest.getQuestion());
        quiz.setQuizType(QuizType.valueOf(quizRequest.getQuizType()));

        List<QuizAnswer> quizAnswers = new ArrayList<>();
        for (QuizAnswerDTO quizAnswerDTO : quizRequest.getAnswers()) {
            QuizAnswer quizAnswer = null;
            if (quizAnswerDTO.getId() != null) {
                quizAnswer = new QuizAnswer(quizAnswerDTO.getId(), quizAnswerDTO.getContent(), quizAnswerDTO.isCorrect(), quiz);
            } else{
                quizAnswer = new QuizAnswer(quizAnswerDTO.getContent(), quizAnswerDTO.isCorrect(), quiz);
            }
            quizAnswers.add(quizAnswer);
        }
        quiz.setAnswers(quizAnswers);
        return quiz;
    }
}