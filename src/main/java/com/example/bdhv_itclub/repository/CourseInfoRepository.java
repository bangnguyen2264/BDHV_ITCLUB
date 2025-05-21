package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.CourseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseInfoRepository extends JpaRepository<CourseInfo, Integer> {

}
