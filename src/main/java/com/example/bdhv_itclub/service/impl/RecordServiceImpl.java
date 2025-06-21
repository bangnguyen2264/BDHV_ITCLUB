package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.dto.reponse.*;
import com.example.bdhv_itclub.dto.request.*;
import com.example.bdhv_itclub.entity.*;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.*;
import com.example.bdhv_itclub.service.RecordService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Transactional
@Service
public class RecordServiceImpl implements RecordService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RecordRepository recordRepository;
    private final ContestRepository contestRepository;
    private final QuizRepository quizRepository;
    private final QuizAnswerRepository quizAnswerRepository;
    private final RecordDetailRepository recordDetailRepository;

    public RecordServiceImpl(ModelMapper modelMapper, UserRepository userRepository, RecordRepository recordRepository, ContestRepository contestRepository, QuizRepository quizRepository, QuizAnswerRepository quizAnswerRepository, RecordDetailRepository recordDetailRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.recordRepository = recordRepository;
        this.contestRepository = contestRepository;
        this.quizRepository = quizRepository;
        this.quizAnswerRepository = quizAnswerRepository;
        this.recordDetailRepository = recordDetailRepository;
    }

    // Ok
    @Override
    public List<RecordResponse> listAllRecordByUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại"));
        List<Records> records = recordRepository.findAllByUser(user);
        return records.stream().map(this::convertToRecordResponse).toList();
    }

    // Ok
    @Override
    public List<RecordResponse> listAllRecordByUserAndContest(Integer contestId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email người dùng không tồn tại"));
        Contest contest = contestRepository.findById(contestId).orElseThrow(() -> new NotFoundException("Mã cuộc thi không tồn tại"));
        List<Records> records = recordRepository.findAllByUserAndContest(user, contest);
        return records.stream().map(this::convertToRecordResponse).toList();
    }

    // Ok
    @Override
    public RecordResponse saveRecord(RecordRequest recordRequest, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email người dùng không tồn tại"));
        Contest contest = contestRepository.findById(recordRequest.getContestId()).orElseThrow(() -> new NotFoundException("Mã cuộc thi không tồn tại"));

        Records record = new Records();
        record.setUser(user);
        record.setContest(contest);
        record.setPeriod(recordRequest.getPeriod());
        record.setJoinedAt(Instant.now());

        float totalQuizzes = contest.getQuizzes().size();
        float totalCorrectQuizzes = 0;

        for (QuizLearningRequest quizLearningRequest : recordRequest.getQuizzes()) {
            Integer quizId = quizLearningRequest.getId();
            Quiz quizInDB = quizRepository.findById(quizId).orElseThrow(() -> new NotFoundException("Mã câu hỏi không tồn tại"));

            Set<QuizAnswer> quizAnswers = new HashSet<>();
            String perforatedContent = null;

            switch (quizInDB.getQuizType()) {
                case ONE_CHOICE -> {
                    Integer answerId = quizLearningRequest.getAnswers().get(0).getId();
                    QuizAnswer answerByIDInDB = quizAnswerRepository.findById(answerId).orElseThrow(() -> new NotFoundException("Mã câu trả lời không tồn tại"));
                    QuizAnswer answer = quizAnswerRepository.checkCorrectAnswer(answerId);
                    // Nếu câu trả lời là đúng thì tăng số câu hỏi đúng + 1
                    if (answer != null) {
                        ++totalCorrectQuizzes;
                    }
                    // Thêm câu trả lời vào trong set
                    quizAnswers.add(answerByIDInDB);
                }
                case PERFORATE -> {
                    List<QuizAnswer> answers = quizAnswerRepository.listAllCorrectAnswer(quizId);
                    // Lấy câu trả lời của user
                    String answerContent = quizLearningRequest.getAnswers().get(0).getPerforatedContent();
                    for (QuizAnswer answer : answers) {
                        // Kiểm tra xem câu trả lời có đúng không
                        if (answerContent.equalsIgnoreCase(answer.getContent())) {
                            // Tăng số câu trả lời đúng
                            ++totalCorrectQuizzes;
                            break;
                        }
                    }

                    // Gán là đáp án của user
                    perforatedContent = answerContent;
                }
                case MULTIPLE_CHOICE -> {
                    List<QuizAnswer> answers = quizAnswerRepository.listAllCorrectAnswer(quizId);
                    // Số câu trả lời đúng của câu hỏi
                    float totalCorrectAnswerInList = answers.size();
                    // Số câu trả lời đúng của user
                    float totalCorrectAnswerInThere = 0.0f;

                    for (QuizAnswerLearningRequest answerLearningRequest : quizLearningRequest.getAnswers()) {
                        // Lấy ra id của câu trả lời
                        Integer answerId = answerLearningRequest.getId();
                        QuizAnswer answerByIDInDB = quizAnswerRepository.findById(answerId).orElseThrow(() -> new NotFoundException("Mã câu trả lời không tồn tại"));

                        // Lấy ra kiểm tra đáp án trong db (nếu có thì là đúng và ngược lại)
                        QuizAnswer answer = quizAnswerRepository.checkCorrectAnswer(answerId);

                        // Giả sử bạn chọn 3 đáp nhưng trong đó chỉ có 2 đáp đúng thôi thì => câu hỏi đó bạn đúng 1 đáp án
                        if (answer != null) {
                            ++totalCorrectAnswerInThere;
                        } else {
                            --totalCorrectAnswerInThere;
                        }
                        quizAnswers.add(answerByIDInDB);
                    }
                    if (totalCorrectAnswerInThere < 0) {
                        totalCorrectAnswerInThere = 0.0f;
                    }
                    float percentMultipleChoiceQuiz = totalCorrectAnswerInThere / totalCorrectAnswerInList;
                    totalCorrectQuizzes += percentMultipleChoiceQuiz;
                }
                default -> {
                }
            }
            record.addARecordDetail(quizInDB, quizAnswers, perforatedContent);
        }
        // Tính toán tổng điểm cho bài thi
        float grade = (totalCorrectQuizzes * 10) / totalQuizzes;
        grade = (float) (Math.round(grade * 100.0) / 100.0);

        record.setGrade(grade);
        record.setTotalCorrectAnswer(totalCorrectQuizzes);

        Records savedRecord = recordRepository.save(record);
        RecordResponse response = convertToRecordResponse(savedRecord);
        response.setTotalQuizzes(totalQuizzes);
        return response;
    }

    // No Ok: Vì vẫn lấy số lượng câu hỏi bên bài thi nên khi cập nhật số lượng câu hỏi bên bài thi thì khi xem lại bài thi sẽ bị bug
    @Override
    public RecordResponseForReview review(Integer recordId, String email) {
        Records recordInDB = recordRepository.findById(recordId).orElseThrow(() -> new NotFoundException("Record ID không tồn tại"));
        if (!recordInDB.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("Bạn không có quyền xem kết quả này");
        }

        // Mapper qua thông tin bài đã làm
        RecordResponseForReview reviewRecord = modelMapper.map(recordInDB, RecordResponseForReview.class);
        reviewRecord.setContestTitle(recordInDB.getContest().getTitle());

        // Danh sách câu hỏi đã làm
        List<QuizResponseForRecord> quizzesInRecord = new ArrayList<>();

        // Tổng số câu hỏi trong bài
        float totalQuizzes = recordInDB.getContest().getQuizzes().size();
        // Tổng số câu hỏi làm đúng
        float totalCorrectQuizzes = 0;
        int i = 0;

        // Lặp lại từng câu hỏi
        for (Quiz quiz : recordInDB.getContest().getQuizzes()) {
            Quiz quizInDB = quizRepository.findById(quiz.getId()).orElseThrow(() -> new NotFoundException("Mã câu hỏi không tồn tại"));

            QuizResponseForRecord quizInRecord = modelMapper.map(quizInDB, QuizResponseForRecord.class);
            quizInRecord.setOrder(++i);

            RecordDetail recordDetail = recordDetailRepository.findRecordDetailByRecordAndQuiz(recordInDB, quizInDB);

            if (recordDetail != null) {
                switch (quizInDB.getQuizType()) {
                    case ONE_CHOICE -> {
                        // Duyệt từng câu trả lời
                        for (QuizAnswer answerInSet : recordDetail.getAnswer()) {
                            quizInRecord.getAnswers().stream().filter(quizInStream -> quizInStream.getId().equals(answerInSet.getId())).forEach(quizInStream -> quizInStream.setAnswerOfCustomer(true));

                            // Kiểm tra xem đáp án đó có đúng không
                            QuizAnswer answer = quizAnswerRepository.checkCorrectAnswer(answerInSet.getId());
                            // Nếu đáp án có (nghĩa là đúng)
                            if (answer != null) {
                                // Tăng số câu hỏi đúng lên
                                ++totalCorrectQuizzes;
                                // Và gán câu hỏi đó là chính xác
                                quizInRecord.setCorrectForAnswer(true);
                            }
                        }
                    }
                    case PERFORATE -> {
                        // Lấy ra các câu trả lời đúng của câu hỏi đó
                        List<QuizAnswer> answers = quizAnswerRepository.listAllCorrectAnswer(quizInDB.getId());
                        // Lấy đáp án mà user trả lời
                        String answerContent = recordDetail.getPerforatedContent();
                        for (QuizAnswer answer : answers) {
                            // Kiểm tra xem có đúng không
                            if (answerContent.equalsIgnoreCase(answer.getContent())) {
                                // Gán câu hỏi đó là chính xác
                                quizInRecord.setCorrectForAnswer(true);
                                // Tăng số câu hỏi đúng lên
                                ++totalCorrectQuizzes;
                            }

                            quizInRecord.getAnswers().stream().filter(quizInStream -> quizInStream.getId().equals(answer.getId())).forEach(quizInStream -> quizInStream.setPerforatedContentOfCustomer(answerContent));
                        }
                    }
                    case MULTIPLE_CHOICE -> {
                        // Lấy ra các câu trả lời đúng của câu hỏi đó
                        List<QuizAnswer> answers = quizAnswerRepository.listAllCorrectAnswer(quizInDB.getId());
                        // Số câu hỏi trong bài thi
                        float totalCorrectAnswerInList = answers.size();
                        // Số câu hỏi đúng của user
                        float totalCorrectAnswerInThere = 0.0f;
                        // Lắp các câu trả lời của user
                        for (QuizAnswer answerInSet : recordDetail.getAnswer()) {
                            Integer answerId = answerInSet.getId();
                            // Kiểm tra xem câu trả lời có đúng không
                            QuizAnswer answer = quizAnswerRepository.checkCorrectAnswer(answerId);

                            quizInRecord.getAnswers().stream().filter(quizInStream -> quizInStream.getId().equals(answerInSet.getId())).forEach(quizInStream -> quizInStream.setAnswerOfCustomer(true));

                            // Nếu đúng
                            if (answer != null) {
                                // Tăng số câu hỏi đúng của user lên
                                ++totalCorrectAnswerInThere;
                            } else {
                                // Giảm số câu hỏi đúng của user lên
                                --totalCorrectAnswerInThere;
                            }

                        }

                        // Nếu số câu hỏi đúng mà < 0 -> thì cho là = 0
                        if (totalCorrectAnswerInThere < 0) {
                            totalCorrectAnswerInThere = 0.0f;
                        }

                        // Nếu các đáp án là đúng hết -> câu hỏi này đúng
                        if (totalCorrectAnswerInThere == totalCorrectAnswerInList) {
                            quizInRecord.setCorrectForAnswer(true);
                        }

                        // Tính số câu hỏi đúng
                        float percentMultipleChoiceQuiz = totalCorrectAnswerInThere / totalCorrectAnswerInList;
                        totalCorrectQuizzes += percentMultipleChoiceQuiz;
                    }
                    default -> {
                    }
                }
            }
            quizzesInRecord.add(quizInRecord);
        }
        // Tính điểm
        float grade = (totalCorrectQuizzes * 10) / totalQuizzes;
        grade = (float) (Math.round(grade * 100.0) / 100.0);

        reviewRecord.setGrade(grade);
        reviewRecord.setTotalQuizzes(totalQuizzes);
        reviewRecord.setTotalCorrectQuizzes(totalCorrectQuizzes);
        reviewRecord.setQuizzes(quizzesInRecord);

        return reviewRecord;
    }

    // Ok
    @Override
    public List<RecordResponseForLeaderboard> ranking(Integer contestId) {
        Contest contest = contestRepository.findById(contestId).orElseThrow(() -> new NotFoundException("Mã cuộc thi không tồn tại"));
        List<Records> records = recordRepository.findAllByContest(contest);

        // Lưu bản ghi tốt nhất (tham gia sớm nhất) của mỗi người dùng
        Map<Integer, Records> bestRecordByUser = new HashMap<>();
        for (Records record : records) {
            Integer userId = record.getUser().getId();
            bestRecordByUser.merge(userId, record, (oldRecord, newRecord) -> newRecord.getJoinedAt().isBefore(oldRecord.getJoinedAt()) ? newRecord : oldRecord);
        }

        // Sắp xếp theo điểm giảm dần, rồi kỳ học tăng dần, rồi thời gian tham gia tăng dần
        return bestRecordByUser.values().stream().sorted(Comparator.comparing(Records::getGrade).reversed().thenComparing(Records::getPeriod).thenComparing(Records::getJoinedAt)).map(this::convertToRank).toList();
    }

    // Ok
    private RecordResponseForLeaderboard convertToRank(Records record) {
        RecordResponseForLeaderboard rank = modelMapper.map(record, RecordResponseForLeaderboard.class);
        rank.setUsername(record.getUser().getUsername());
        rank.setUserAvatar(record.getUser().getPhoto());
        return rank;
    }

    // Ok
    private RecordResponse convertToRecordResponse(Records record) {
        RecordResponse recordResponse = modelMapper.map(record, RecordResponse.class);
        recordResponse.setUserId(record.getUser().getId());
        recordResponse.setUsername(record.getUser().getUsername());
        recordResponse.setContestId(record.getContest().getId());
        recordResponse.setContestTitle(record.getContest().getTitle());
        recordResponse.setTotalQuizzes(record.getContest().getQuizzes().size());
        recordResponse.setTotalCorrectQuizzes(record.getTotalCorrectAnswer());

        return recordResponse;
    }
}