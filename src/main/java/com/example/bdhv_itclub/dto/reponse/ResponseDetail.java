package com.example.bdhv_itclub.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDetail<T> {
    private int status;
    private String error;
    private Object message;
    private T data;
}
