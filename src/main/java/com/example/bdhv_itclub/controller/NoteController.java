package com.example.bdhv_itclub.controller;


import com.example.bdhv_itclub.dto.request.NoteRequest;
import com.example.bdhv_itclub.service.NoteService;
import com.example.bdhv_itclub.utils.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/create")
    @ApiMessage("Create a note")
    public ResponseEntity<?> create(@RequestBody @Valid NoteRequest noteRequest){
        return new ResponseEntity<>(noteService.createNote(noteRequest), HttpStatus.CREATED);
    }

    @GetMapping("/get-all")
    @ApiMessage("List all notes")
    public ResponseEntity<?> list(@RequestParam(value = "course") Integer courseId,
                                  @RequestParam(value = "user") Integer userId){
        return ResponseEntity.ok(noteService.getAll(userId, courseId));
    }

    @PutMapping("/update/{id}")
    @ApiMessage("Update the note")
    public ResponseEntity<?> update(@PathVariable(value = "id") Integer noteId,
                                    @RequestParam(value = "content") String content){
        return ResponseEntity.ok(noteService.updateNote(noteId, content));
    }

    @DeleteMapping("/delete/{id}")
    @ApiMessage("Delete the note")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Integer noteId){
        return ResponseEntity.ok(noteService.deleteNote(noteId));
    }
}
