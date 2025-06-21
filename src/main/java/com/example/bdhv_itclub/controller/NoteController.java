package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.request.NoteRequest;
import com.example.bdhv_itclub.service.NoteService;
import com.example.bdhv_itclub.utils.annotation.APIResponseMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/note")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // Ok
    @GetMapping("/get-all")
    @APIResponseMessage("Liệt kê tất cả ghi chú")
    public ResponseEntity<?> list(
            @RequestParam(value = "course") Integer courseId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(noteService.getAllByEmail(email, courseId));
    }

    // Ok
    @PostMapping("/create")
    @APIResponseMessage("Thêm ghi chú")
    public ResponseEntity<?> create(
            @RequestBody @Valid NoteRequest noteRequest,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return new ResponseEntity<>(noteService.createNote(noteRequest, email), HttpStatus.CREATED);
    }

    // Ok
    @PutMapping("/update")
    public ResponseEntity<?> updateNote(
            @RequestParam("id") Integer noteId,
            @RequestParam("content") String content,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(noteService.updateNote(noteId, content, email));
    }

    // Ok
    @DeleteMapping("/delete/{id}")
    @APIResponseMessage("Xóa ghi chú")
    public ResponseEntity<?> delete(
            @PathVariable("id") Integer noteId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(noteService.deleteNote(noteId, email));
    }
}