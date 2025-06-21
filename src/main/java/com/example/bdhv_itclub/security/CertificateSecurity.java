package com.example.bdhv_itclub.security;

import com.example.bdhv_itclub.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("certificateSecurity")
public class CertificateSecurity {
    @Autowired
    private CertificateRepository certificateRepository;

    public boolean isOwnerOfCertificate(Integer certificateId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) return false;
        String email = authentication.getName();

        return certificateRepository
                .findById(certificateId)
                .map(certificate -> certificate.getUser().getEmail().equals(email))
                .orElse(false);
    }
}