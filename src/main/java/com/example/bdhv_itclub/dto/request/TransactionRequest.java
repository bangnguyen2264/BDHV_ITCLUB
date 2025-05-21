package com.example.bdhv_itclub.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    public String email;
    public int courseId;
    public String description;
    public int totalPrice;
}
