package com.example.bdhv_itclub.service;



import com.example.bdhv_itclub.dto.reponse.NoteResponse;
import com.example.bdhv_itclub.dto.request.NoteRequest;

import java.util.List;

public interface NoteService {
    List<NoteResponse> getAllByEmail(String email, Integer courseId);
    NoteResponse createNote(NoteRequest noteRequest, String email);
    NoteResponse updateNote(Integer noteId, String content, String email);
    String deleteNote(Integer noteId, String email);
}