package com.example.bdhv_itclub.service;


import com.example.bdhv_itclub.dto.reponse.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAll(String keyword);

    CategoryDTO get(Integer categoryId);

    CategoryDTO create(CategoryDTO categoryRequest);

    CategoryDTO update(Integer categoryId, CategoryDTO categoryRequest);

    String delete(Integer categoryId);
}
