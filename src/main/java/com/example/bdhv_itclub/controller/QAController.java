package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.request.QARequest;
import com.example.bdhv_itclub.service.QAService;
import com.example.bdhv_itclub.utils.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qa")
public class QAController {
    private final QAService qaService;

    public QAController(QAService qaService) {
        this.qaService = qaService;
    }

    @GetMapping("/get-all")
    @ApiMessage("List all questions")
    public ResponseEntity<?> listAll(@RequestParam(value = "lesson") Integer lessonId){
        return ResponseEntity.ok(qaService.listAll(lessonId));
    }

    @GetMapping("/get-all-list")
    @ApiMessage("List all questions for admin and assistant")
    public ResponseEntity<?> listAllForAdmin(){
        return ResponseEntity.ok(qaService.listAllForAdmin());
    }

    @PostMapping("/create")
    @ApiMessage("Create a question")
    public ResponseEntity<?> create(@RequestBody @Valid QARequest qaRequest){
        return new ResponseEntity<>(qaService.createQA(qaRequest), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @ApiMessage("Update the question")
    public ResponseEntity<?> update(@PathVariable(value = "id") Integer qaId,
                                    @RequestParam(value = "content") String content){
        return ResponseEntity.ok(qaService.updateQA(qaId, content));
    }

    @DeleteMapping("/delete/{id}")
    @ApiMessage("Delete the question")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Integer qaId){
        return ResponseEntity.ok(qaService.deleteQA(qaId));
    }
}
