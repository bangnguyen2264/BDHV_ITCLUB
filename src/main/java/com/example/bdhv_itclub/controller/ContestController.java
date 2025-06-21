package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.reponse.ContestResponse;
import com.example.bdhv_itclub.dto.reponse.RecordResponseForLeaderboard;
import com.example.bdhv_itclub.dto.request.ContestRequest;
import com.example.bdhv_itclub.service.ContestService;
import com.example.bdhv_itclub.service.RecordService;
import com.example.bdhv_itclub.utils.annotation.APIResponseMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contest")
public class ContestController {
    private final ContestService contestService;
    private final RecordService recordService;

    public ContestController(ContestService contestService, RecordService recordService) {
        this.contestService = contestService;
        this.recordService = recordService;
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list-all")
    @APIResponseMessage("Liệt kê danh sách tất cả cuộc thi (kể cả cuộc thi chưa mở)")
    public ResponseEntity<?> listAllContest(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        Page<ContestResponse> contests = contestService.listAllContests(page, size, sortBy, sortDir);
        if (contests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contests);
    }

    // Ok
    @GetMapping("/list-all/enabled")
    @APIResponseMessage("Liệt kê danh sách tất cả cuộc thi đã mở")
    public ResponseEntity<?> listAllEnabledContest(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        Page<ContestResponse> contests = contestService.listAllEnabledContests(page, size, sortBy, sortDir);
        if (contests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contests);
    }

    // Ok
    @GetMapping("/ranking/contest/{id}")
    @APIResponseMessage("Liệt kê xếp hạng của cuộc thi")
    public ResponseEntity<?> rank(
            @PathVariable(value = "id") Integer contestId
    ) {
        List<RecordResponseForLeaderboard> ranks = recordService.ranking(contestId);
        if (ranks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ranks);
    }

    // Ok (Lấy bộ đề của cuộc thi đó (không có đáp án))
    @GetMapping("/join/{contestId}")
    public ResponseEntity<?> join(
            @PathVariable(value = "contestId") Integer contestId
    ) {
        return ResponseEntity.ok(contestService.joinTest(contestId));
    }

    // Ok
    @GetMapping("/search")
    @APIResponseMessage("Tìm kiếm cuộc thi")
    public ResponseEntity<?> search(
            @RequestParam(value = "keyword") String keyword
    ) {
        List<ContestResponse> contests = contestService.search(keyword);
        if (contests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contests);
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @APIResponseMessage("Tạo cuộc thi mới")
    public ResponseEntity<ContestResponse> save(
            @RequestBody @Valid ContestRequest contestRequest
    ) {
        return new ResponseEntity<>(contestService.save(contestRequest), HttpStatus.CREATED);
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    @APIResponseMessage("Cập nhật cuộc thi")
    public ResponseEntity<ContestResponse> update(
            @PathVariable(value = "id") Integer contestId,
            @RequestBody @Valid ContestRequest contestRequest
    ) {
        return ResponseEntity.ok(contestService.update(contestId, contestRequest));
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get/{id}")
    @APIResponseMessage("Lấy cuộc thi theo mã cuộc thi (có đáp án)")
    public ResponseEntity<?> getInAdministration(
            @PathVariable(value = "id") Integer contestId
    ) {
        return ResponseEntity.ok(contestService.get(contestId));
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    @APIResponseMessage("Xóa cuộc thi")
    public ResponseEntity<String> delete(
            @PathVariable(value = "id") Integer contestId
    ) {
        return ResponseEntity.ok(contestService.delete(contestId));
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/switch-enabled")
    public ResponseEntity<?> switchEnabledOfContest(
            @RequestParam(value = "id") Integer contestId,
            @RequestParam(value = "enabled") boolean enabled
    ) {
        return ResponseEntity.ok(contestService.switchEnabled(contestId, enabled));
    }
}