package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoursesRepository extends JpaRepository<Courses, Integer> {
    boolean existsCoursesByTitle(String title);

    boolean existsCoursesBySlug(String slug);

    List<Courses> findAllByCategoryId(int categoryId);

    Optional<Courses> findBySlug(String slug);

    Courses findByTitleOrSlug(String title, String slug);

    @Query("select c from Courses c where c.isEnabled = true and concat(c.title, ' ', c.category.name) like %?1% ")
    List<Courses> search(String keyword);

    @Modifying
    @Query("update Courses c set c.isEnabled =?2 where c.id =?1")
    void switchEnabled(Integer courseId, boolean enabled);

    @Modifying
    @Query("update Courses c set c.isFinished =?2 where c.id =?1")
    void switchFinished(Integer courseId, boolean isFinished);

    @Modifying
    @Query("update Courses c set c.isPublished =?2, c.publishedAt = case " + "when ?2 = true then current_timestamp() else null end where c.id =?1")
    void switchPublished(Integer courseId, boolean isPublished);


}
