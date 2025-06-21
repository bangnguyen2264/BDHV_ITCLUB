package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Integer> {
    boolean existsByTitle(String title);

    Page<Contest> findAll(Pageable pageable);

    Page<Contest> findAllByEnabledTrue(Pageable pageable);

    Contest findByTitle(String title);

    @Query("update Contest c set c.enabled = ?2 where c.id = ?1")
    @Modifying
    void switchEnabled(Integer contestId, boolean enabled);

    @Query("select c from Contest c where c.title like %?1% and c.enabled = true")
    List<Contest> search(String keyword);
}