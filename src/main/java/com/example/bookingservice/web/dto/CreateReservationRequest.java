package com.example.bookingservice.web.dto;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class CreateReservationRequest {

    private UUID userId;
    private UUID propertyId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalPrice;

}
