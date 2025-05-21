package com.example.bdhv_itclub.service;

import com.example.bdhv_itclub.dto.reponse.RecordResponse;
import com.example.bdhv_itclub.dto.reponse.RecordReturnInRank;
import com.example.bdhv_itclub.dto.reponse.RecordReturnToReview;
import com.example.bdhv_itclub.dto.request.RecordRequest;

import java.util.List;

public interface RecordService {
    List<RecordResponse> listAllRecord(Integer userId);
    List<RecordReturnInRank> ranking(Integer contestId);
    List<RecordResponse> listAllRecordByUserAndContest(Integer userId, Integer contestId);
    RecordResponse saveRecord(RecordRequest recordRequest);
    RecordReturnToReview review(Integer recordId);
}
