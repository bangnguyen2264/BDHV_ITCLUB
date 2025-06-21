package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.CourseInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseInformationRepository extends JpaRepository<CourseInformation, Integer> {}