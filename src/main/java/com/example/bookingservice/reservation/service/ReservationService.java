package com.example.bookingservice.reservation.service;

import com.example.bookingservice.reservation.model.Reservation;

import java.util.List;
import java.util.UUID;

public interface ReservationService {

    List<Reservation> getAllReservationsByUserId(UUID userId);

    // Create Reservation -> POST
    // Cancel Reservation -> PATCH


    // Get total reservations made in the app
    // Get total reservations a user has made
    // Get total booked reservations
    // Get total cancelled reservations
    // Get percentage booked reservations
}
