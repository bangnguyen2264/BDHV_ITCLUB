package com.example.bdhv_itclub.service;



import com.example.bdhv_itclub.dto.reponse.NoteResponse;
import com.example.bdhv_itclub.dto.request.NoteRequest;

import java.util.List;

public interface NoteService {
    NoteResponse createNote(NoteRequest noteRequest);
    List<com.example.bdhv_itclub.dto.reponse.NoteResponse> getAll(Integer userId, Integer courseId);
    com.example.bdhv_itclub.dto.reponse.NoteResponse updateNote(Integer noteId, String content);
    String deleteNote(Integer noteId);
}
