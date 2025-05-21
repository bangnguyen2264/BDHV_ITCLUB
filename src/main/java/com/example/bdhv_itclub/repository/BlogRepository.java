package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.Blog;
import com.example.bdhv_itclub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
    List<Blog> findAllByUser(User user);
    Optional<Blog> findBySlug(String slug);
    boolean existsBlogByTitle(String title);
    boolean existsBlogBySlug(String slug);
    Blog findBlogByTitleOrSlug(String title, String slug);
    @Query("select b from Blog b where b.title like %?1%")
    List<Blog> search(String keyword);
}
