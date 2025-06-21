package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ErrorDetailsResponse {
    @JsonFormat(pattern = "HH:mm dd-MM-yyyy")
    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}