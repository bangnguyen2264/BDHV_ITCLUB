package com.example.bdhv_itclub.service.impl;


import com.example.bdhv_itclub.dto.reponse.CertificateResponse;
import com.example.bdhv_itclub.entity.Certificate;
import com.example.bdhv_itclub.entity.Courses;
import com.example.bdhv_itclub.entity.User;
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

    @Override
    public CertificateResponse save(String email, Courses courses) {
        User user = userRepository.findByEmail(email).get();
        Certificate certificate = null;
        Certificate savedCertificate;
        if (courses.isFinished()) {
            certificate = new Certificate();
            certificate.setCourses(courses);
            certificate.setAchievedTime(Instant.now());
            certificate.setUser(user);

            savedCertificate = certificateRepository.save(certificate);
        } else {
            throw new BadRequestException("Khóa học nay chưa kết thúc. Vui lòng chờ cập nhật thêm bài học mới");
        }
        CertificateResponse response = modelMapper.map(savedCertificate, CertificateResponse.class);
        response.setStudentName(savedCertificate.getUser().getFullName());
        response.setTitleCourse(savedCertificate.getCourses().getTitle());
        return response;
    }

    @Override
    public CertificateResponse getById(Integer certificateId) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new NotFoundException("Certificate ID không tồn tại"));

        CertificateResponse response = modelMapper.map(certificate, CertificateResponse.class);
        response.setStudentName(certificate.getUser().getFullName());
        response.setTitleCourse(certificate.getCourses().getTitle());
        return response;
    }
}
