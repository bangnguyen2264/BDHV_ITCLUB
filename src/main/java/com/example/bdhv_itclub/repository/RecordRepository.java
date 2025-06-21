package com.example.bdhv_itclub.repository;


import com.example.bdhv_itclub.entity.Contest;
import com.example.bdhv_itclub.entity.Records;
import com.example.bdhv_itclub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Records, Integer> {
    List<Records> findAllByUser(User user);
    List<Records> findAllByContest(Contest contest);
    List<Records> findAllByUserAndContest(User user, Contest contest);
    int countAllByContest(Contest contest);
}