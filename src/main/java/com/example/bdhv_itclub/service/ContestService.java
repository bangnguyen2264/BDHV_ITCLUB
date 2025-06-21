package com.example.bdhv_itclub.service;

import com.example.bdhv_itclub.dto.reponse.ContestResponse;
import com.example.bdhv_itclub.dto.reponse.ContestResponseForTest;
import com.example.bdhv_itclub.dto.request.ContestRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ContestService {
    ContestResponse get(Integer contestId);
    Page<ContestResponse> listAllContests(int page, int size, String sortBy, String direction);
    Page<ContestResponse> listAllEnabledContests(int page, int size, String sortBy, String direction);
    ContestResponse save(ContestRequest contestRequest);
    ContestResponse update(Integer contestId, ContestRequest contestRequest);
    String delete(Integer contestId);
    String switchEnabled(Integer contestId, boolean enabled);
    ContestResponseForTest joinTest(Integer contestId);
    List<ContestResponse> search(String keyword);
}