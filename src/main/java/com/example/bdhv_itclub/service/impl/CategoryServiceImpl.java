package com.example.bdhv_itclub.service.impl;


import com.example.bdhv_itclub.dto.reponse.CategoryDTO;
import com.example.bdhv_itclub.entity.Category;
import com.example.bdhv_itclub.exception.ConflictException;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.CategoryRepository;
import com.example.bdhv_itclub.service.CategoryService;
import com.example.bdhv_itclub.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(ModelMapper modelMapper, CategoryRepository categoryRepository) {
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDTO> getAll(String keyword) {
        List<Category> categories = null;
        if (!keyword.trim().equals("")) {
            categories = categoryRepository.search(keyword);
        } else {
            categories = categoryRepository.findAll();
        }
        // Status mà là null nghĩa là nó chưa bị xoá nên render ra giao diện
        return categories.stream().filter(category -> category.getStatus() == null).map(category -> {
            // Mapper về dạng CategoryEntity -> CategoryDTO
            return modelMapper.map(category, CategoryDTO.class);
        }).toList();
    }

    @Override
    public CategoryDTO get(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category ID không tồn tại"));

        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO create(CategoryDTO categoryRequest) {
        // Kiểm tra xem đã tồn tại tên category đó chưa
        if (categoryRepository.existsCategoriesByName(categoryRequest.getName())) {
            throw new ConflictException("Tên danh mục khóa học đã tồn tại!");
        }

        // Kiểm tra xem đã tồn tại slug đó chưa
        if (categoryRepository.existsCategoriesBySlug(categoryRequest.getSlug())) {
            throw new ConflictException("Slug của danh mục khóa học đã từng tồn tại!");
        }

        Category category = modelMapper.map(categoryRequest, Category.class);

        // tạo slug theo tên (VD: khoá học java => khoa-hoc-java)
        String slug = Utils.removeVietnameseAccents(category.getName());

        category.setSlug(slug);

        Category savedCategory = categoryRepository.save(category);

        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO update(Integer categoryId, CategoryDTO categoryRequest) {
        Category categoryInDB = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category ID không tồn tại"));

        Category category = categoryRepository.findByNameOrSlug(categoryRequest.getName(), categoryRequest.getSlug());

        if (category != null) {
            if (!Objects.equals(category.getId(), categoryInDB.getId())) {
                throw new ConflictException("Tên/Slug của danh mục khóa học đã từng tồn tại!");
            }
        }

        String name = categoryRequest.getName();
        categoryInDB.setName(name);
        categoryInDB.setSlug(Utils.removeVietnameseAccents(name));
        Category updatedCategory = categoryRepository.save(categoryInDB);
        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }

    @Override
    public String delete(Integer categoryId) {
        Category categoryInDB = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category ID không tồn tại"));

        categoryRepository.deleteCategory(categoryInDB.getId());
        return "Xóa danh mục thành công";
    }
}
