package com.example.bookingservice.web.mapper;

import com.example.bookingservice.reservation.model.Reservation;
import com.example.bookingservice.web.dto.ReservationResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static ReservationResponse fromReservation(Reservation reservation) {
        return ReservationResponse.builder()
                .reservationId(reservation.getId())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .totalPrice(reservation.getTotalPrice())
                .status(reservation.getStatus())
                .build();
    }

}
