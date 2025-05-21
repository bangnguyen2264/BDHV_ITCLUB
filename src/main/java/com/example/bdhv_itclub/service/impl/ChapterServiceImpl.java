package com.example.bdhv_itclub.service.impl;


import com.example.bdhv_itclub.dto.reponse.ChapterDTO;
import com.example.bdhv_itclub.entity.Chapter;
import com.example.bdhv_itclub.entity.Courses;
import com.example.bdhv_itclub.exception.ConflictException;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.ChapterRepository;
import com.example.bdhv_itclub.repository.CoursesRepository;
import com.example.bdhv_itclub.service.ChapterService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Transactional
@Service
public class ChapterServiceImpl implements ChapterService {
    private final CoursesRepository coursesRepository;
    private final ChapterRepository chapterRepository;
    private final ModelMapper modelMapper;

    public ChapterServiceImpl(CoursesRepository coursesRepository, ChapterRepository chapterRepository, ModelMapper modelMapper) {
        this.coursesRepository = coursesRepository;
        this.chapterRepository = chapterRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ChapterDTO create(Integer courseId, ChapterDTO chapterDto) {
        Courses course = coursesRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Course ID không tồn tại"));

        Chapter chapterDuplicate = chapterRepository.findChapterByNameAndCourse(chapterDto.getName(), course);

        if (chapterDuplicate != null) {
            throw new ConflictException("Tên chương đã từng tồn tại trong khóa học ");
        }

        Chapter chapter = new Chapter();
        chapter.setName(chapterDto.getName());
        chapter.setOrders(chapterDto.getOrders());
        chapter.setCourse(course);

        Chapter savedChapter = chapterRepository.save(chapter);

        return modelMapper.map(savedChapter, ChapterDTO.class);
    }

    @Override
    public ChapterDTO update(Integer courseId, Integer chapterId, ChapterDTO chapterDto) {
        Courses course = coursesRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Course ID không tồn tại"));

        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new NotFoundException("Chapter ID không tồn tại"));

        // Lấy chapter theo tên và theo khoá học (chủ yếu để kiểm tra xem chapter muốn update có trùng với tên đã có chưa)
        Chapter chapterTemp = chapterRepository.findChapterByNameAndCourse(chapterDto.getName(), course);

        if (chapterTemp != null) {
            // Kiểm tra xem tên chương học đã có trong khoá học chưa
            if (!Objects.equals(chapter.getId(), chapterTemp.getId())) {
                throw new ConflictException("Tên chương này đã từng tồn tại trong khóa học ");
            }
        }

        chapter.setName(chapterDto.getName());

        Chapter savedChapter = chapterRepository.save(chapter);
        return modelMapper.map(savedChapter, ChapterDTO.class);
    }

    @Override
    public String delete(Integer chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new NotFoundException("Chapter ID không tồn tại"));

        chapterRepository.delete(chapter);
        return "Xóa chương thành công!";
    }
}
