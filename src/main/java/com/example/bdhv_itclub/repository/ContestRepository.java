package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Integer> {
    @Query("select c from Contest c where c.title like %?1% and c.enabled = true")
    List<Contest> search(String keyword);

    boolean existsContestByTitle(String title);

    Contest findContestByTitle(String title);

    @Query("update Contest c set c.enabled = ?2 where c.id = ?1")
    @Modifying
    void switchEnabled(Integer contestId, boolean enabled);
}
