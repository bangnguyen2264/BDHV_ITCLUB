package com.example.bdhv_itclub.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankRequest {
    private String accountNo;
    private String currency;
    private String fromDate;
    private String keyword;
    private String maxAcentrysrno;
    private int pageNumber;
    private int pageSize;
    private String toDate;
}