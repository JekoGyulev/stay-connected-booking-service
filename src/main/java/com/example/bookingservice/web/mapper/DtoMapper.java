package com.example.bookingservice.web.mapper;

import com.example.bookingservice.reservation.enums.ReservationStatus;
import com.example.bookingservice.reservation.model.Reservation;
import com.example.bookingservice.web.dto.BookingDatesResponse;
import com.example.bookingservice.web.dto.ReservationResponse;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class DtoMapper {

    public static ReservationResponse fromReservation(Reservation reservation) {
        return ReservationResponse.builder()
                .reservationId(reservation.getId())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .totalPrice(reservation.getTotalPrice())
                .status(reservation.getStatus())
                .propertyId(reservation.getPropertyId())
                .build();
    }

    public static List<BookingDatesResponse> fromReservations(List<Reservation> reservations) {
        return reservations.stream()
                .map(reservation -> BookingDatesResponse.builder().checkIn(reservation.getStartDate()).checkOut(reservation.getEndDate()).build())
                .toList();
    }

}
