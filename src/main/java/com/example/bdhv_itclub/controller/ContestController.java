package com.example.bdhv_itclub.controller;


import com.example.bdhv_itclub.dto.reponse.ContestResponse;
import com.example.bdhv_itclub.dto.reponse.RecordReturnInRank;
import com.example.bdhv_itclub.dto.request.ContestRequest;
import com.example.bdhv_itclub.service.ContestService;
import com.example.bdhv_itclub.service.RecordService;
import com.example.bdhv_itclub.utils.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contest")
public class ContestController {
    private final ContestService contestService;
    private final RecordService recordService;

    public ContestController(ContestService contestService, RecordService recordService) {
        this.contestService = contestService;
        this.recordService = recordService;
    }

    //api này dùng cho admin vs học viên.
    @GetMapping("/list-all")
    @ApiMessage("List all contests")
    public ResponseEntity<?> listAll() {
        List<ContestResponse> listContest = contestService.listAll();
        if (listContest.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listContest);
    }

    @GetMapping("/ranking/contest/{id}")
    @ApiMessage("List rank of contest")
    public ResponseEntity<?> rank(@PathVariable(value = "id") Integer contestId) {
        List<RecordReturnInRank> listRanks = recordService.ranking(contestId);
        if (listRanks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listRanks);
    }

    @GetMapping("/join/{contest_id}")
    public ResponseEntity<?> join(@PathVariable(value = "contest_id") Integer contestId) {
        return ResponseEntity.ok(contestService.joinTest(contestId));
    }

    @GetMapping("/search")
    @ApiMessage("Search contest")
    public ResponseEntity<?> search(@RequestParam(value = "keyword") String keyword) {
        List<ContestResponse> listContest = contestService.search(keyword);
        if (listContest.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listContest);
    }

    @PostMapping("/create")
    @ApiMessage("Create a contest")
    public ResponseEntity<ContestResponse> save(@RequestBody @Valid ContestRequest contestRequest) {
        return new ResponseEntity<>(contestService.save(contestRequest), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @ApiMessage("Update the contest")
    public ResponseEntity<ContestResponse> update(@PathVariable(value = "id") Integer contestId, @RequestBody @Valid ContestRequest contestRequest) {
        return ResponseEntity.ok(contestService.update(contestId, contestRequest));
    }

    @GetMapping("/get/{id}")
    @ApiMessage("Get the contest by id")
    public ResponseEntity<?> getInAdministration(@PathVariable(value = "id") Integer contestId){
        return ResponseEntity.ok(contestService.get(contestId));
    }

    @DeleteMapping("/delete/{id}")
    @ApiMessage("Delete the contest")
    public ResponseEntity<String> delete(@PathVariable(value = "id") Integer contestId) {
        return ResponseEntity.ok(contestService.delete(contestId));
    }

    @PostMapping("/switch-enabled")
    public ResponseEntity<?> switchEnabledOfContest(@RequestParam(value = "id") Integer contestId,
                                                    @RequestParam(value = "enabled") boolean enabled){
        return ResponseEntity.ok(contestService.switchEnabled(contestId, enabled));
    }
}
