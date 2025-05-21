package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsCategoriesByName(String name);

    boolean existsCategoriesBySlug(String slug);

    @Query("select c from Category c where c.name like %?1%")
    List<Category> search(String keyword);

    Category findByNameOrSlug(String name, String slug);

    @Modifying
    @Query("update Category c set c.status = 'DELETED' WHERE c.id = ?1")
    void deleteCategory(Integer id);
}
