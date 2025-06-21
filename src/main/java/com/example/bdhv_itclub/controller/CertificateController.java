package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.service.CertificateService;
import com.example.bdhv_itclub.utils.annotation.APIResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/certificate")
public class CertificateController {
    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN') or @certificateSecurity.isOwnerOfCertificate(#certificateId)")
    @GetMapping("/get/{id}")
    @APIResponseMessage("Lấy chứng chỉ theo mã chứng chỉ")
    public ResponseEntity<?> get(
            @PathVariable(value = "id") Integer certificateId
    ) {
        return ResponseEntity.ok(certificateService.getById(certificateId));
    }
}