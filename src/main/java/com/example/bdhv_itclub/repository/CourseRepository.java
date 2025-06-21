package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    boolean existsByTitle(String title);

    boolean existsBySlug(String slug);

    Page<Course> findAll(Pageable pageable);

    Optional<Course> findBySlug(String slug);

    @Query("SELECT c FROM Course c WHERE c.slug = ?1 AND c.isEnabled = true AND c.category.status IS NULL")
    Optional<Course> findEnabledAndCategoryActiveBySlug(String slug);

    Course findByTitleOrSlug(String title, String slug);

    @Query("select c from Course c where c.isEnabled = true and c.category.status is null")
    Page<Course> findEnabledExcludeDeletedCategory(Pageable pageable);

    @Query("select c from Course c where c.isEnabled = true and c.category.id = ?1 and c.category.status is null")
    Page<Course> findEnabledByCategoryIdExcludeDeletedCategory(Integer categoryId, Pageable pageable);

    @Query("select c from Course c where c.isEnabled = true and c.category.status is null and concat(c.title, ' ', c.category.name) like %?1% ")
    Page<Course> search(String keyword, Pageable pageable);

    @Modifying
    @Query("update Course c set c.isEnabled =?2 where c.id =?1")
    void switchEnabled(Integer courseId, boolean enabled);

    @Modifying
    @Query("update Course c set c.isFinished =?2 where c.id =?1")
    void switchFinished(Integer courseId, boolean isFinished);

    @Modifying
    @Query("update Course c set c.isPublished =?2, c.publishedAt = case " + "when ?2 = true then current_timestamp() else null end where c.id =?1")
    void switchPublished(Integer courseId, boolean isPublished);
}