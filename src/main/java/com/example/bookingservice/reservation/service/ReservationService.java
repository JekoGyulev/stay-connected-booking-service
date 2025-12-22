package com.example.bookingservice.reservation.service;

import com.example.bookingservice.reservation.enums.ReservationStatus;
import com.example.bookingservice.reservation.model.Reservation;
import com.example.bookingservice.web.dto.CreateReservationRequest;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.UUID;

public interface ReservationService {

    Page<Reservation> getAllReservationsByUserId(UUID userId, int pageNumber, int pageSize);

    Page<Reservation> getAllReservationsByUserIdAndStatus(UUID userId, String reservationStatus, int pageNumber, int pageSize);

    Reservation create(CreateReservationRequest createReservationRequest);

    Reservation cancel(UUID id);

    long getTotalCountReservations(String status);

    BigDecimal calculatePercentage(ReservationStatus reservationStatus);

    // Check availability -> GET

}
