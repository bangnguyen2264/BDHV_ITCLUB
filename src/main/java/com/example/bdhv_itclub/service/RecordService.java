package com.example.bdhv_itclub.service;


import com.example.bdhv_itclub.dto.reponse.RecordResponse;
import com.example.bdhv_itclub.dto.reponse.RecordResponseForLeaderboard;
import com.example.bdhv_itclub.dto.reponse.RecordResponseForReview;
import com.example.bdhv_itclub.dto.request.RecordRequest;

import java.util.List;

public interface RecordService {
    List<RecordResponse> listAllRecordByUser(String email);
    List<RecordResponse> listAllRecordByUserAndContest(Integer contestId, String email);
    RecordResponse saveRecord(RecordRequest recordRequest, String email);
    RecordResponseForReview review(Integer recordId, String email);
    List<RecordResponseForLeaderboard> ranking(Integer contestId);
}