package com.example.bdhv_itclub.repository;
import com.example.bdhv_itclub.entity.Record;
import com.example.bdhv_itclub.entity.Contest;
import com.example.bdhv_itclub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {
    List<Record> findAllByUser(User user);
    List<Record> findAllByContest(Contest contest);
    int countAllByContest(Contest contest);
    List<Record> findAllByUserAndContest(User user, Contest contest);
}
