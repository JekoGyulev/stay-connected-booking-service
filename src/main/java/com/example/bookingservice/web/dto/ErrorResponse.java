package com.example.bookingservice.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String message;
}
