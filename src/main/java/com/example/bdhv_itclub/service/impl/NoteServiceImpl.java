package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.dto.reponse.*;
import com.example.bdhv_itclub.dto.request.*;
import com.example.bdhv_itclub.entity.*;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.*;
import com.example.bdhv_itclub.service.NoteService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
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
    private final CourseRepository courseRepository;

    public NoteServiceImpl(ModelMapper modelMapper, LessonRepository lessonRepository, UserRepository userRepository, NoteRepository noteRepository, CourseRepository courseRepository) {
        this.modelMapper = modelMapper;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
        this.courseRepository = courseRepository;
    }

    // Ok
    @Override
    public NoteResponse createNote(NoteRequest noteRequest, String email) {
        Lesson lesson = lessonRepository.findById(noteRequest.getLessonId()).orElseThrow(() -> new NotFoundException("Mã bài học không tồn tại"));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email người dùng không tồn tại"));
        Note note = new Note();
        note.setContent(noteRequest.getContent());
        note.setUser(user);
        note.setLesson(lesson);
        note.setCreatedAt(Instant.now());
        note.setRecordedTime(noteRequest.getRecordedTime());
        Note savedNote = noteRepository.save(note);
        return convertToNoteResponse(savedNote);
    }

    // Ok
    @Override
    public List<NoteResponse> getAllByEmail(String email, Integer courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Mã khóa học không tồn tại"));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email));
        List<Note> notes = noteRepository.listAllByCoursesIdAndUserId(course.getId(), user.getId());
        return notes.stream().map(this::convertToNoteResponse).toList();
    }

    // Ok
    @Override
    public NoteResponse updateNote(Integer noteId, String content, String email) {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new NotFoundException("Ghi chú không tồn tại"));
        // Kiểm tra quyền sở hữu
        if (!note.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("Bạn không có quyền sửa ghi chú này");
        }

        note.setContent(content);
        return convertToNoteResponse(noteRepository.save(note));
    }

    // Ok
    @Override
    public String deleteNote(Integer noteId, String email) {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new NotFoundException("Ghi chú không tồn tại"));
        if (!note.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("Bạn không có quyền xóa ghi chú này");
        }
        noteRepository.delete(note);
        return "Xóa ghi chú thành công";
    }

    // Ok
    private NoteResponse convertToNoteResponse(Note note) {
        NoteResponse noteResponse = modelMapper.map(note, NoteResponse.class);
        noteResponse.setLessonId(note.getLesson().getId());
        noteResponse.setLessonTitle(note.getLesson().getName());
        noteResponse.setChapterTitle(note.getLesson().getChapter().getName());
        return noteResponse;
    }
}