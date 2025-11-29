package com.example.bookingservice.web.controller;


import com.example.bookingservice.reservation.model.Reservation;
import com.example.bookingservice.reservation.service.ReservationService;
import com.example.bookingservice.web.dto.CreateReservationRequest;
import com.example.bookingservice.web.dto.ReservationResponse;
import com.example.bookingservice.web.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservationsByUser(@RequestParam(value = "userId") UUID userId) {

        List<Reservation> reservationsByUser = this.reservationService.getAllReservationsByUserId(userId);

        if (reservationsByUser.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ReservationResponse> reservationResponses = reservationsByUser.stream()
                .map(DtoMapper::fromReservation)
                .toList();

        return ResponseEntity.ok().body(reservationResponses);
    }


    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody CreateReservationRequest createReservationRequest) {

        Reservation reservation = this.reservationService.create(createReservationRequest);

        ReservationResponse response = DtoMapper.fromReservation(reservation);

        URI location = URI.create("/api/v1/reservations/" + reservation.getId());

        return ResponseEntity
                .created(location)
                .body(response);
    }


}
