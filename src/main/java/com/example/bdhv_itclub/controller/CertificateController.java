package com.example.bdhv_itclub.controller;


import com.example.bdhv_itclub.service.CertificateService;
import com.example.bdhv_itclub.utils.ApiMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/certificate")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping("/get/{id}")
    @ApiMessage("Get the certificate by id")
    public ResponseEntity<?> get(@PathVariable(value = "id") Integer certificateId){
        return ResponseEntity.ok(certificateService.getById(certificateId));
    }
}