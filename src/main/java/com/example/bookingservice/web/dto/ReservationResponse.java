package com.example.bookingservice.web.dto;

import com.example.bookingservice.reservation.enums.ReservationStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class ReservationResponse {
    private UUID reservationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalPrice;
    private ReservationStatus status;
}
