package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.reponse.RecordResponse;
import com.example.bdhv_itclub.dto.request.RecordRequest;
import com.example.bdhv_itclub.service.RecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/record")
public class RecordController {
    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    // Ok
    @GetMapping("/list-all/user")
    public ResponseEntity<?> listAllByUser(
            Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(recordService.listAllRecordByUser(email));
    }

    // Ok
    @GetMapping("/list-all/user-contest")
    public ResponseEntity<?> listAllByUserAndContest(
            @RequestParam(value = "contest") Integer contestId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        List<RecordResponse> recordResponses = recordService.listAllRecordByUserAndContest(contestId, email);
        if (recordResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(recordResponses);
    }

    // Ok
    @PostMapping("/save")
    public ResponseEntity<?> save(
            @RequestBody @Valid RecordRequest recordRequest,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recordService.saveRecord(recordRequest, email));
    }

    // Ok
    @GetMapping("/review/{id}")
    public ResponseEntity<?> review(
            @PathVariable(value = "id") Integer recordId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(recordService.review(recordId, email));
    }
}