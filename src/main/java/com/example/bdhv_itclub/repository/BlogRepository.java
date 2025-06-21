package com.example.bdhv_itclub.repository;


import com.example.bdhv_itclub.entity.Blog;
import com.example.bdhv_itclub.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
    boolean existsByTitle(String title);

    boolean existsBySlug(String slug);

    Optional<Blog> findByIdAndIsApprovedTrue(Integer id);

    Optional<Blog> findBySlugAndIsApprovedTrue(String slug);

    Blog findByTitleOrSlug(String title, String slug);

    Page<Blog> findAll(Pageable pageable);

    Page<Blog> findAllByIsApprovedTrue(Pageable pageable);

    Page<Blog> findAllByUserAndIsApprovedTrue(User user, Pageable pageable);

    @Query("SELECT b FROM Blog b WHERE b.isApproved = true AND b.title LIKE %?1%")
    Page<Blog> search(String keyword, Pageable pageable);

    @Modifying
    @Query("update Blog b set b.isApproved =?2 where b.id =?1")
    void switchApproved(Integer blogId, boolean isApproved);
}