package com.example.bookingservice.web.dto;

import com.example.bookingservice.reservation.enums.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class ReservationResponse {

    @Schema(description = "reservation ID", example = "dd7de13a-f8e6-43a6-a939-6fbd7d57afcd")
    private UUID reservationId;
    @Schema(description = "start date", example = "12/05/2025")
    private LocalDate startDate;
    @Schema(description = "end date", example = "15/05/2025")
    private LocalDate endDate;
    @Schema(description = "total price", example = "150.00")
    private BigDecimal totalPrice;
    @Schema(description = "reservation status", example = "BOOKED")
    private ReservationStatus status;
    @Schema(description = "property ID", example = "0c927d84-6ef9-4bfb-82a2-8a95ba6ca1b7")
    private UUID propertyId;

}
