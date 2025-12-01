package com.example.bookingservice.reservation.service;

import com.example.bookingservice.reservation.model.Reservation;
import com.example.bookingservice.web.dto.CreateReservationRequest;

import java.util.List;
import java.util.UUID;

public interface ReservationService {

    List<Reservation> getAllReservationsByUserId(UUID userId);

    Reservation create(CreateReservationRequest createReservationRequest);

    Reservation cancel(UUID id);

    long getTotalCountReservations();

    // Check availability -> GET

    // Get total reservations a user has made
    // Get total booked reservations
    // Get total cancelled reservations
    // Get percentage booked reservations
}
