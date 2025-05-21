package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.reponse.RecordResponse;
import com.example.bdhv_itclub.dto.request.RecordRequest;
import com.example.bdhv_itclub.service.RecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/record")
public class RecordController {
    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody @Valid RecordRequest recordRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(recordService.saveRecord(recordRequest));
    }

    @GetMapping("/list-all/user")
    public ResponseEntity<?> listAllByUser(@RequestParam(value = "id") Integer userId) {
        List<RecordResponse> listRecords = recordService.listAllRecord(userId);
        if (listRecords.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listRecords);
    }

    @GetMapping("/list-all/user-contest")
    public ResponseEntity<?> listAllByUserAndContest(@RequestParam(value = "user") Integer userId,
                                                     @RequestParam(value = "contest") Integer contestId){
        List<RecordResponse> listRecords = recordService.listAllRecordByUserAndContest(userId, contestId);
        if(listRecords.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listRecords);
    }

    @GetMapping("/review/{id}")
    public ResponseEntity<?> review(@PathVariable(value = "id") Integer recordId){
        return ResponseEntity.ok(recordService.review(recordId));
    }
}
