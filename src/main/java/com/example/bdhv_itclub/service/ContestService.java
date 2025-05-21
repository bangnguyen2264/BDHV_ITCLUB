package com.example.bdhv_itclub.service;



import com.example.bdhv_itclub.dto.reponse.ContestResponse;
import com.example.bdhv_itclub.dto.reponse.ContestReturnInTest;
import com.example.bdhv_itclub.dto.request.ContestRequest;

import java.util.List;

public interface ContestService {
    List<ContestResponse> listAll();

    ContestReturnInTest joinTest(Integer contestId);

    List<ContestResponse> search(String keyword);

    ContestResponse save(ContestRequest contestRequest);

    ContestResponse update(Integer contestId, ContestRequest contestRequest);

    String delete(Integer contestId);

    ContestResponse get(Integer contestId);

    String switchEnabled(Integer contestId, boolean enabled);
}
