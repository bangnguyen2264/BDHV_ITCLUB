package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.dto.request.CourseChapterDTO;
import com.example.bdhv_itclub.entity.Course;
import com.example.bdhv_itclub.entity.CourseChapter;
import com.example.bdhv_itclub.exception.ConflictException;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.CourseChapterRepository;
import com.example.bdhv_itclub.repository.CourseRepository;
import com.example.bdhv_itclub.service.CourseChapterService;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Transactional
@Service
public class CourseChapterServiceImpl implements CourseChapterService {
    private final CourseRepository courseRepository;
    private final CourseChapterRepository courseChapterRepository;
    private final ModelMapper modelMapper;

    public CourseChapterServiceImpl(CourseRepository courseRepository, CourseChapterRepository courseChapterRepository, ModelMapper modelMapper) {
        this.courseRepository = courseRepository;
        this.courseChapterRepository = courseChapterRepository;
        this.modelMapper = modelMapper;
    }

    // Ok
    @Override
    public CourseChapterDTO create(Integer courseId, CourseChapterDTO courseChapterDTO) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Mã khóa học không tồn tại"));
        // Kiểm tra trùng tên chương
        CourseChapter checkDuplicatedByName = courseChapterRepository.findByNameAndCourse(courseChapterDTO.getName(), course);
        if (checkDuplicatedByName != null) {
            throw new ConflictException("Tên chương đã từng tồn tại trong khóa học");
        }
        // Kiểm tra trùng chapterOrder trong cùng khóa học
        CourseChapter checkDuplicatedByOrder = courseChapterRepository.findByChapterOrderAndCourse(courseChapterDTO.getChapterOrder(), course);
        if (checkDuplicatedByOrder != null) {
            throw new ConflictException("Thứ tự chương đã tồn tại trong khóa học");
        }
        // Tạo mới chương học
        CourseChapter courseChapter = new CourseChapter();
        courseChapter.setName(courseChapterDTO.getName());
        courseChapter.setChapterOrder(courseChapterDTO.getChapterOrder());
        courseChapter.setCourse(course);

        CourseChapter savedCourseChapter = courseChapterRepository.save(courseChapter);
        return modelMapper.map(savedCourseChapter, CourseChapterDTO.class);
    }

    // Ok
    @Override
    public CourseChapterDTO update(Integer courseId, Integer chapterId, CourseChapterDTO courseChapterDTO) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Mã khóa học không tồn tại"));
        CourseChapter courseChapter = courseChapterRepository.findById(chapterId).orElseThrow(() -> new NotFoundException("Mã chương học không tồn tại"));
        CourseChapter tempCourseChapter = courseChapterRepository.findByNameAndCourse(courseChapterDTO.getName(), course);
        if (tempCourseChapter != null) {
            if (!Objects.equals(courseChapter.getId(), tempCourseChapter.getId())) {
                throw new ConflictException("Tên chương này đã từng tồn tại trong khóa học ");
            }
        }
        courseChapter.setName(courseChapterDTO.getName());
        CourseChapter savedCourseChapter = courseChapterRepository.save(courseChapter);
        return modelMapper.map(savedCourseChapter, CourseChapterDTO.class);
    }

    // Ok
    @Override
    public String delete(Integer chapterId) {
        CourseChapter courseChapter = courseChapterRepository.findById(chapterId).orElseThrow(() -> new NotFoundException("Mã chương học không tồn tại"));
        courseChapterRepository.delete(courseChapter);
        return "Xóa chương học thành công";
    }
}