package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Integer> {
    @Query("SELECT c FROM CourseCategory c WHERE c.id = ?1 AND c.status IS NULL")
    Optional<CourseCategory> findActiveById(Integer id);

    @Query("SELECT c FROM CourseCategory c WHERE c.name = ?1")
    Optional<CourseCategory> findByNameIgnoreStatus(String name);

    @Query("SELECT c FROM CourseCategory c WHERE c.slug = ?1")
    Optional<CourseCategory> findBySlugIgnoreStatus(String slug);


    @Query("select c from CourseCategory c where c.name like %?1%")
    List<CourseCategory> search(String keyword);

    @Modifying
    @Query("update CourseCategory c set c.status = 'DELETED' WHERE c.id = ?1")
    void deleteCategory(Integer id);
}