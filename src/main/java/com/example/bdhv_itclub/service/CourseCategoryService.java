package com.example.bdhv_itclub.service;


import com.example.bdhv_itclub.dto.request.CourseCategoryDTO;

import java.util.List;

public interface CourseCategoryService {
    CourseCategoryDTO get(Integer categoryId);
    List<CourseCategoryDTO> listAllCourseCategories(String keyword);
    List<CourseCategoryDTO> listAllDeletedCourseCategories(String keyword);
    CourseCategoryDTO create(CourseCategoryDTO categoryRequest);
    CourseCategoryDTO update(Integer categoryId, CourseCategoryDTO categoryRequest);
    String delete(Integer categoryId);
    CourseCategoryDTO restore(Integer categoryId);
}