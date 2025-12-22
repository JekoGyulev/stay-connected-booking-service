package com.example.bookingservice.web.controller;


import com.example.bookingservice.reservation.enums.ReservationStatus;
import com.example.bookingservice.reservation.model.Reservation;
import com.example.bookingservice.reservation.service.ReservationService;
import com.example.bookingservice.web.dto.CreateReservationRequest;

import com.example.bookingservice.web.dto.PageResponse;
import com.example.bookingservice.web.dto.ReservationResponse;
import com.example.bookingservice.web.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @GetMapping("/percentage")
    public ResponseEntity<BigDecimal> getCompletedBookingsPercentage(@RequestParam("status") String reservationStatus) {
        BigDecimal percentage = this.reservationService.calculatePercentage(ReservationStatus.valueOf(reservationStatus));

        return new ResponseEntity<>(percentage, HttpStatus.OK);
    }

    @GetMapping("/total")
    public ResponseEntity<Long>  getTotalCountReservations(@RequestParam(value = "status") String reservationStatus) {
        return ResponseEntity.ok(this.reservationService.getTotalCountReservations(reservationStatus));
    }


    @GetMapping
    public ResponseEntity<PageResponse<ReservationResponse>> getAllReservationsByUser(
                                                    @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                    @RequestParam(value = "pageSize", defaultValue = "3") int pageSize,
                                                    @RequestParam(value = "userId") UUID userId) {

        Page<Reservation> reservationsByUser = this.reservationService.getAllReservationsByUserId(userId, pageNumber, pageSize);

        List<ReservationResponse> reservationResponses = reservationsByUser.stream()
                .map(DtoMapper::fromReservation)
                .toList();

        PageResponse<ReservationResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(reservationResponses);
        pageResponse.setTotalPages(reservationsByUser.getTotalPages());
        pageResponse.setTotalElements(reservationsByUser.getTotalElements());
        pageResponse.setPageNumber(reservationsByUser.getNumber());
        pageResponse.setPageSize(reservationsByUser.getSize());


        return ResponseEntity.ok().body(pageResponse);
    }


    @GetMapping("/status")
    public ResponseEntity<PageResponse<ReservationResponse>> getAllReservationsByPropertyId(
                                        @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                        @RequestParam(value = "pageSize", defaultValue = "3") int pageSize,
                                        @RequestParam(value = "userId") UUID userId,
                                        @RequestParam(value = "reservationStatus") String reservationStatus) {


        Page<Reservation> reservationsPage = this.reservationService
                .getAllReservationsByUserIdAndStatus(userId, reservationStatus, pageNumber, pageSize);

        List<ReservationResponse> content = reservationsPage.stream()
                .map(DtoMapper::fromReservation)
                .toList();

        PageResponse<ReservationResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(content);
        pageResponse.setTotalPages(reservationsPage.getTotalPages());
        pageResponse.setTotalElements(reservationsPage.getTotalElements());
        pageResponse.setPageNumber(reservationsPage.getNumber());
        pageResponse.setPageSize(reservationsPage.getSize());


        return ResponseEntity.ok().body(pageResponse);
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

    @PutMapping("/cancellation")
    public ResponseEntity<ReservationResponse> cancelReservation(@RequestParam("reservationId") UUID reservationId) {

        Reservation reservation = this.reservationService.cancel(reservationId);

        ReservationResponse reservationResponse = DtoMapper.fromReservation(reservation);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reservationResponse);
    }


}
