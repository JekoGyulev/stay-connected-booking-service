package com.example.bookingservice.reservation.service;

import com.example.bookingservice.reservation.enums.ReservationStatus;
import com.example.bookingservice.reservation.model.Reservation;
import com.example.bookingservice.web.dto.CreateReservationRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ReservationService {

    List<Reservation> getAllReservationsByUserId(UUID userId);

    Reservation create(CreateReservationRequest createReservationRequest);

    Reservation cancel(UUID id);

    long getTotalCountReservations();

    BigDecimal calculatePercentage(ReservationStatus reservationStatus);

    // Check availability -> GET

    // Get total booked reservations
    // Get total cancelled reservations
}
