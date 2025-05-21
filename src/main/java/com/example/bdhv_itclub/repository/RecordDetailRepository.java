package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.Quiz;
import com.example.bdhv_itclub.entity.Record;
import com.example.bdhv_itclub.entity.RecordDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordDetailRepository extends JpaRepository<RecordDetail, Integer> {
    RecordDetail findRecordDetailByRecordAndQuiz(Record record, Quiz quiz);
}
