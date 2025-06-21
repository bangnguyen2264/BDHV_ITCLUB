package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.dto.reponse.CertificateResponse;
import com.example.bdhv_itclub.entity.*;
import com.example.bdhv_itclub.exception.BadRequestException;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.CertificateRepository;
import com.example.bdhv_itclub.repository.UserRepository;
import com.example.bdhv_itclub.service.CertificateService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Transactional
@Service
public class CertificateServiceImpl implements CertificateService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CertificateRepository certificateRepository;

    public CertificateServiceImpl(ModelMapper modelMapper, UserRepository userRepository, CertificateRepository certificateRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.certificateRepository = certificateRepository;
    }

    // Ok
    @Override
    public CertificateResponse save(String email, Course course) {
        User user = userRepository.findByEmail(email).get();
        Certificate certificate = null;
        Certificate savedCertificate;
        if (course.isFinished()) {
            certificate = new Certificate();
            certificate.setCourse(course);
            certificate.setAchievedTime(Instant.now());
            certificate.setUser(user);
            savedCertificate = certificateRepository.save(certificate);
        } else {
            throw new BadRequestException("Khóa học này chưa kết thúc. Vui lòng chờ cập nhật thêm bài học mới");
        }
        CertificateResponse certificateResponse = modelMapper.map(savedCertificate, CertificateResponse.class);
        certificateResponse.setStudentName(savedCertificate.getUser().getFullName());
        certificateResponse.setCourseTitle(savedCertificate.getCourse().getTitle());
        return certificateResponse;
    }

    // Ok
    @Override
    public CertificateResponse getById(Integer certificateId) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new NotFoundException("Mã chứng chỉ không tồn tại"));
        CertificateResponse certificateResponse = modelMapper.map(certificate, CertificateResponse.class);
        certificateResponse.setStudentName(certificate.getUser().getFullName());
        certificateResponse.setCourseTitle(certificate.getCourse().getTitle());
        return certificateResponse;
    }
}