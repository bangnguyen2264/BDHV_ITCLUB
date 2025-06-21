package com.example.bdhv_itclub.repository;


import com.example.bdhv_itclub.entity.Certificate;
import com.example.bdhv_itclub.entity.Course;
import com.example.bdhv_itclub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Integer> {
    Certificate findByUserAndCourse(User user, Course course);
}