package com.example.bdhv_itclub.service.impl;


import com.example.bdhv_itclub.dto.reponse.NoteResponse;
import com.example.bdhv_itclub.dto.request.NoteRequest;
import com.example.bdhv_itclub.entity.Courses;
import com.example.bdhv_itclub.entity.Lesson;
import com.example.bdhv_itclub.entity.Note;
import com.example.bdhv_itclub.entity.User;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.CoursesRepository;
import com.example.bdhv_itclub.repository.LessonRepository;
import com.example.bdhv_itclub.repository.NoteRepository;
import com.example.bdhv_itclub.repository.UserRepository;
import com.example.bdhv_itclub.service.NoteService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class NoteServiceImpl implements NoteService {
    private final ModelMapper modelMapper;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final NoteRepository noteRepository;
    private final CoursesRepository coursesRepository;

    public NoteServiceImpl(ModelMapper modelMapper, LessonRepository lessonRepository, UserRepository userRepository, NoteRepository noteRepository, CoursesRepository coursesRepository) {
        this.modelMapper = modelMapper;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
        this.coursesRepository = coursesRepository;
    }

    @Override
    public NoteResponse createNote(NoteRequest noteRequest) {
        Lesson lesson = lessonRepository.findById(noteRequest.getLessonId()).orElseThrow(() -> new NotFoundException("Lesson ID không tồn tại"));

        User user = userRepository.findById(noteRequest.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User ID không tồn tại"));

        Note note = new Note();
        note.setContent(noteRequest.getContent());
        note.setUser(user);
        note.setLesson(lesson);
        note.setCreatedAt(Instant.now());
        note.setCurrentTime(noteRequest.getCurrentTime());

        Note savedNote = noteRepository.save(note);
        return convertToNoteResponse(savedNote);
    }

    @Override
    public List<NoteResponse> getAll(Integer userId, Integer courseId) {
        Courses courses = coursesRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Courses ID không tồn tại"));

        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User ID không tồn tại"));

        List<Note> listNotes = noteRepository.listNoteByCoursesAndUser(courses.getId(), user.getId());
        return listNotes.stream().map(this::convertToNoteResponse).toList();
    }

    @Override
    public NoteResponse updateNote(Integer noteId, String content) {
        Note noteInDB = noteRepository.findById(noteId).orElseThrow(() -> new NotFoundException("Note không tồn tại"));
        noteInDB.setContent(content);
        return convertToNoteResponse(noteRepository.save(noteInDB));
    }

    @Override
    public String deleteNote(Integer noteId) {
        Note noteInDB = noteRepository.findById(noteId).orElseThrow(() -> new NotFoundException("Note không tồn tại"));

        noteRepository.delete(noteInDB);
        return "Xóa ghi chú thành công";
    }

    private NoteResponse convertToNoteResponse(Note note) {
        NoteResponse noteResponse = modelMapper.map(note, NoteResponse.class);
        noteResponse.setLessonId(note.getLesson().getId());
        noteResponse.setTitleLesson(note.getLesson().getName());
        noteResponse.setTitleChapter(note.getLesson().getChapter().getName());

        return noteResponse;
    }
}
