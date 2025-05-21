package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.reponse.CategoryDTO;
import com.example.bdhv_itclub.service.CategoryService;
import com.example.bdhv_itclub.utils.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list-all")
    @ApiMessage("List all categories")
    public ResponseEntity<List<CategoryDTO>> listAllCategories(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {
        List<CategoryDTO> categoryDTOS = categoryService.getAll(keyword);
        if (categoryDTOS.size() == 0) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categoryDTOS);
    }

    @GetMapping("/get/{id}")
    @ApiMessage("Get the category by id")
    public ResponseEntity<CategoryDTO> get(@PathVariable(value = "id") Integer categoryId) {
        return ResponseEntity.ok(categoryService.get(categoryId));
    }

    @PostMapping("/create")
    @ApiMessage("Create a category")
    public ResponseEntity<CategoryDTO> add(@RequestBody @Valid CategoryDTO categoryRequest) {
        CategoryDTO savedCategory = categoryService.create(categoryRequest);
        URI uri = URI.create("/api/categories/" + savedCategory.getId());

        return ResponseEntity.created(uri).body(savedCategory);
    }

    @PutMapping("/update/{id}")
    @ApiMessage("Update the category")
    public ResponseEntity<CategoryDTO> updatedCategory(@PathVariable(value = "id") Integer categoryId, @RequestBody @Valid CategoryDTO categoryRequest) {
        return ResponseEntity.ok(categoryService.update(categoryId, categoryRequest));
    }

    @DeleteMapping("/delete/{id}")
    @ApiMessage("Delete the category")
    public ResponseEntity<String> deleteCategory(@PathVariable(value = "id") Integer categoryId) {
        return ResponseEntity.ok(categoryService.delete(categoryId));
    }

}
