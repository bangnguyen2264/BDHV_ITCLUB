package com.example.bdhv_itclub.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankTransactionInfo {
    private String id;
    private String arrangementId;
    private String reference;
    private String description;
    private String category;
    private String bookingDate;
    private String valueDate;
    private String amount;
    private String currency;
    private String creditDebitIndicator;
    private String runningBalance;
}
