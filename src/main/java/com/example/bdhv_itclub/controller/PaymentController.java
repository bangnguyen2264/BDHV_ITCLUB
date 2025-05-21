package com.example.bdhv_itclub.controller;


import com.example.bdhv_itclub.dto.request.PaymentRequest;
import com.example.bdhv_itclub.dto.request.TransactionRequest;
import com.example.bdhv_itclub.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/get-info")
    public ResponseEntity<?> getPaymentInfo(@RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.ok(paymentService.getPaymentInfo(paymentRequest));
    }
    @PostMapping("/check-transaction")
    public ResponseEntity<?> checkTransaction(@RequestBody TransactionRequest transactionRequest) {
        return ResponseEntity.ok(paymentService.checkTransaction(transactionRequest));
    }
}
