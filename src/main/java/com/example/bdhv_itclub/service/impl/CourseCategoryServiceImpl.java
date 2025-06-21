package com.example.bdhv_itclub.service.impl;


import com.example.bdhv_itclub.constant.CommonStatus;
import com.example.bdhv_itclub.dto.request.CourseCategoryDTO;
import com.example.bdhv_itclub.entity.CourseCategory;
import com.example.bdhv_itclub.exception.BadRequestException;
import com.example.bdhv_itclub.exception.ConflictException;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.CourseCategoryRepository;
import com.example.bdhv_itclub.service.CourseCategoryService;
import com.example.bdhv_itclub.utils.GlobalUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {
    private final ModelMapper modelMapper;
    private final CourseCategoryRepository categoryRepository;

    public CourseCategoryServiceImpl(ModelMapper modelMapper, CourseCategoryRepository categoryRepository) {
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }

    // Ok
    @Override
    public CourseCategoryDTO get(Integer categoryId) {
        CourseCategory courseCategory = categoryRepository.findActiveById(categoryId).orElseThrow(() -> new NotFoundException("Mã danh mục khóa học không tồn tại hoặc đã bị ẩn/xóa"));
        return modelMapper.map(courseCategory, CourseCategoryDTO.class);
    }

    // Ok
    @Override
    public List<CourseCategoryDTO> listAllCourseCategories(String keyword) {
        List<CourseCategory> courseCategories;
        if (keyword != null && !keyword.isBlank()) {
            courseCategories = categoryRepository.search(keyword);
        } else {
            courseCategories = categoryRepository.findAll();
        }
        // Chỉ lọc những category có status == null rồi map sang DTO
        return courseCategories.stream()
                .filter(category -> category.getStatus() == null)
                .map(category -> modelMapper.map(category, CourseCategoryDTO.class))
                .toList();
    }

    // Ok
    @Override
    public CourseCategoryDTO create(CourseCategoryDTO categoryRequest) {
        String slug = GlobalUtil.convertToSlug(categoryRequest.getName());

        // 1. Kiểm tra theo tên
        Optional<CourseCategory> existingByName = categoryRepository.findByNameIgnoreStatus(categoryRequest.getName());
        if (existingByName.isPresent()) {
            CourseCategory existing = existingByName.get();
            if (CommonStatus.DELETED.equals(existing.getStatus())) {
                // Không ghi đè → thông báo tồn tại ở trạng thái DELETED
                throw new ConflictException(
                        "Tên danh mục đã từng tồn tại và đang ở trạng thái đã xóa. Vui lòng khôi phục danh mục này."
                );
            } else {
                throw new ConflictException("Tên danh mục khóa học đã tồn tại");
            }
        }

        // 2. Kiểm tra slug
        Optional<CourseCategory> existingBySlug = categoryRepository.findBySlugIgnoreStatus(slug);
        if (existingBySlug.isPresent()) {
            CourseCategory existing = existingBySlug.get();
            if (CommonStatus.DELETED.equals(existing.getStatus())) {
                throw new ConflictException(
                        "Slug '" + slug + "' đã từng tồn tại cho một danh mục đã bị xóa. Vui lòng khôi phục danh mục cũ."
                );
            } else {
                throw new ConflictException("Slug của danh mục khóa học đã tồn tại");
            }
        }

        // 3. Thêm mới hoàn toàn
        CourseCategory courseCategory = modelMapper.map(categoryRequest, CourseCategory.class);
        courseCategory.setSlug(slug);
        CourseCategory saved = categoryRepository.save(courseCategory);
        return modelMapper.map(saved, CourseCategoryDTO.class);
    }

    // Ok
    @Override
    public CourseCategoryDTO update(Integer categoryId, CourseCategoryDTO categoryRequest) {
        CourseCategory courseCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Mã danh mục khóa học không tồn tại"));

        String newName = categoryRequest.getName();
        String newSlug = GlobalUtil.convertToSlug(newName);

        // Kiểm tra tên trùng (trừ chính nó)
        Optional<CourseCategory> existingByName = categoryRepository.findByNameIgnoreStatus(newName);
        if (existingByName.isPresent() && !existingByName.get().getId().equals(categoryId)) {
            throw new NotFoundException("Tên danh mục khóa học đã tồn tại");
        }

        // Kiểm tra slug trùng (trừ chính nó)
        Optional<CourseCategory> existingBySlug = categoryRepository.findBySlugIgnoreStatus(newSlug);
        if (existingBySlug.isPresent() && !existingBySlug.get().getId().equals(categoryId)) {
            throw new ConflictException("Slug của danh mục khóa học đã tồn tại");
        }

        courseCategory.setName(newName);
        courseCategory.setSlug(newSlug);

        CourseCategory updatedCategory = categoryRepository.save(courseCategory);
        return modelMapper.map(updatedCategory, CourseCategoryDTO.class);
    }

    // Ok
    @Override
    public String delete(Integer categoryId) {
        CourseCategory courseCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Mã danh mục khóa học không tồn tại"));
        categoryRepository.deleteCategory(courseCategory.getId());
        return "Xóa danh mục khóa học thành công";
    }

    // Ok
    @Override
    public CourseCategoryDTO restore(Integer categoryId) {
        CourseCategory courseCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục"));
        if (!CommonStatus.DELETED.equals(courseCategory.getStatus())) {
            throw new BadRequestException("Danh mục không ở trạng thái đã xóa");
        }
        courseCategory.setStatus(null);
        CourseCategory restored = categoryRepository.save(courseCategory);
        return modelMapper.map(restored, CourseCategoryDTO.class);
    }
}