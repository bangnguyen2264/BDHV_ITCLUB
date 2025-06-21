package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.dto.request.LessonTextDTO;
import com.example.bdhv_itclub.entity.LessonText;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.LessonTextRepository;
import com.example.bdhv_itclub.service.LessonTextService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class LessonTextServiceImpl implements LessonTextService {

    private final ModelMapper modelMapper;
    private final LessonTextRepository textLessonRepository;

    public LessonTextServiceImpl(ModelMapper modelMapper, LessonTextRepository textLessonRepository) {
        this.modelMapper = modelMapper;
        this.textLessonRepository = textLessonRepository;
    }

    @Override
    public LessonText create(LessonTextDTO textLessonDto) {
        LessonText lesson = modelMapper.map(textLessonDto, LessonText.class);
        return textLessonRepository.save(lesson);
    }

    @Override
    public LessonText update(LessonTextDTO textLessonDto) {
        LessonText textLessonInDB = textLessonRepository.findById(textLessonDto.getId()).orElseThrow(() -> new NotFoundException("Text lesson ID không hợp lệ"));

        textLessonInDB.setContent(textLessonDto.getContent());
        return textLessonRepository.save(textLessonInDB);
    }
}
