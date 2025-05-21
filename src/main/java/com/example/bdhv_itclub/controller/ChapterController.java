package com.example.bdhv_itclub.controller;


import com.example.bdhv_itclub.dto.reponse.ChapterDTO;
import com.example.bdhv_itclub.service.ChapterService;
import com.example.bdhv_itclub.utils.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/chapter")
public class ChapterController {

    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @PostMapping("/create/{courseId}")
    @ApiMessage("Create chapter")
    public ResponseEntity<ChapterDTO> addChapter(@RequestBody @Valid ChapterDTO chapterDto, @PathVariable(value = "courseId") Integer courseId) {
        ChapterDTO response = chapterService.create(courseId, chapterDto);

        URI uri = URI.create("/api/chapters/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/update/{chapterId}/{courseId}")
    @ApiMessage("Update chapter")
    public ResponseEntity<ChapterDTO> updateChapter(@PathVariable(value = "courseId") Integer courseId, @PathVariable(value = "chapterId") Integer chapterId, @RequestBody @Valid ChapterDTO chapterDto) {
        return ResponseEntity.ok(chapterService.update(courseId, chapterId, chapterDto));
    }

    @DeleteMapping("/delete/{chapterId}")
    @ApiMessage("Delete chapter")
    public ResponseEntity<String> deleteChapter(@PathVariable(value = "chapterId") Integer chapterId) {
        return ResponseEntity.ok(chapterService.delete(chapterId));
    }
}
