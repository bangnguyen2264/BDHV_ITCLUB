package com.example.bdhv_itclub.service;

import com.example.bdhv_itclub.dto.reponse.PaymentResponse;
import com.example.bdhv_itclub.dto.request.PaymentRequest;
import com.example.bdhv_itclub.dto.request.TransactionRequest;

;

public interface PaymentService {
    PaymentResponse getPaymentInfo(PaymentRequest paymentRequest);
    boolean checkTransaction(TransactionRequest transactionRequest);
}