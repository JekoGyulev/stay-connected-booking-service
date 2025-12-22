package com.example.bookingservice.reservation;


import com.example.bookingservice.exception.ResourceNotFound;
import com.example.bookingservice.reservation.enums.ReservationStatus;
import com.example.bookingservice.reservation.model.Reservation;
import com.example.bookingservice.reservation.repository.ReservationRepository;
import com.example.bookingservice.reservation.service.impl.ReservationServiceImpl;
import com.example.bookingservice.web.dto.CreateReservationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceUTest {

    @Mock
    private ReservationRepository reservationRepository;
    @InjectMocks
    private ReservationServiceImpl reservationServiceImpl;

//    @Test
//    void whenThereAreReservationsForUser_thenReturnThem() {
//
//        UUID userId = UUID.randomUUID();
//
//        Reservation reservation = Reservation.builder().userId(userId).build();
//        Reservation reservation2 = Reservation.builder().userId(userId).build();
//
//        List<Reservation> reservations = Arrays.asList(reservation, reservation2);
//
//        when(reservationRepository.findAllByUserIdOrderByCreatedAtDesc(userId))
//                .thenReturn(reservations);
//
//        List<Reservation> result = reservationServiceImpl.getAllReservationsByUserId(userId);
//
//
//        assertEquals(reservations.size(), result.size());
//        assertArrayEquals(reservations.toArray(), result.toArray());
//        verify(reservationRepository).findAllByUserIdOrderByCreatedAtDesc(userId);
//    }

//    @Test
//    void whenThereAreNoReservationsForUser_thenThrowException() {
//        UUID userId = UUID.randomUUID();
//
//        when(reservationRepository.findAllByUserIdOrderByCreatedAtDesc(userId))
//                .thenReturn(Collections.emptyList());
//
//        assertThrows(ResourceNotFound.class, () -> reservationServiceImpl.getAllReservationsByUserId(userId));
//    }


    @Test
    void whenCreateReservation_thenSaveReservationToDatabase() {

        CreateReservationRequest dto = CreateReservationRequest.builder()
                .startDate(LocalDate.now())
                .totalPrice(BigDecimal.valueOf(240))
                .build();


        Reservation reservation = Reservation.builder()
                .startDate(LocalDate.now())
                .totalPrice(BigDecimal.valueOf(240))
                .build();


        when(reservationRepository.save(any())).thenReturn(reservation);

        Reservation result = reservationServiceImpl.create(dto);

        assertEquals(reservation.getTotalPrice(), result.getTotalPrice());
        assertEquals(reservation.getStartDate(), result.getStartDate());
        verify(reservationRepository).save(any());
    }


    @Test
    void whenThereAreReservations_andWantCountCancelledReservations_thenReturnCount() {

        Reservation reservation = Reservation.builder().status(ReservationStatus.CANCELLED).build();
        Reservation reservation2 = Reservation.builder().status(ReservationStatus.CANCELLED).build();
        Reservation reservation3 = Reservation.builder().status(ReservationStatus.BOOKED).build();

        long countCancelledReservations = 2;

        when(reservationRepository.countAllByStatus(ReservationStatus.CANCELLED))
                .thenReturn(countCancelledReservations);

        long result = reservationServiceImpl.getTotalCountReservations("CANCELLED");

        assertEquals(countCancelledReservations, result);
    }

    @Test
    void whenThereAreReservations_andStatusIsAll_thenReturnTotalCountReservations() {
        Reservation reservation2 = Reservation.builder().status(ReservationStatus.CANCELLED).build();
        Reservation reservation3 = Reservation.builder().status(ReservationStatus.BOOKED).build();

        long totalCount = 2;

        when(reservationRepository.count()).thenReturn(totalCount);


        long result = reservationServiceImpl.getTotalCountReservations("ALL");

        assertEquals(totalCount, result);
    }


    @Test
    void whenCancelReservation_andReservationDoesNotExist_thenThrowException() {

        UUID reservationId = UUID.randomUUID();

        Reservation reservation = Reservation.builder().id(reservationId).status(ReservationStatus.BOOKED).build();

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> reservationServiceImpl.cancel(reservationId));
    }

    @Test
    void whenCancelReservation_andThereIsReservation_thenCancelReservation() {
        UUID reservationId = UUID.randomUUID();

        Reservation reservation = Reservation.builder().id(reservationId).status(ReservationStatus.BOOKED).build();

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        Reservation result = reservationServiceImpl.cancel(reservationId);

        assertEquals(ReservationStatus.CANCELLED, result.getStatus());
        verify(reservationRepository).save(argThat(r -> r.getId().equals(reservationId) &&
                r.getStatus().equals(ReservationStatus.CANCELLED)));
    }

    @Test
    void whenCalculatePercentage_andThereIsEmptyListOfReservations_thenReturn0() {

        ReservationStatus status = ReservationStatus.BOOKED;

        when(reservationRepository.findAll()).thenReturn(Collections.emptyList());

        BigDecimal resultPercentage = reservationServiceImpl.calculatePercentage(status);

        assertEquals(BigDecimal.ZERO, resultPercentage);
    }

    @Test
    void whenCalculatePercentage_andThereAreReservationsWithStatusBooked_thenReturnPercentage() {

        Reservation reservation1 = Reservation.builder().status(ReservationStatus.BOOKED).build();
        Reservation reservation2 = Reservation.builder().status(ReservationStatus.BOOKED).build();


        long countBookedReservations = 2;
        long countReservations = 2;

        BigDecimal expectedPercentage = BigDecimal.valueOf(countBookedReservations)
                .divide(BigDecimal.valueOf(countReservations), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));


        ReservationStatus status = ReservationStatus.BOOKED;

        when(reservationRepository.findAll()).thenReturn(List.of(reservation1, reservation2));

        BigDecimal resultPercentage = reservationServiceImpl.calculatePercentage(status);

        assertEquals(expectedPercentage, resultPercentage);
    }

    @Test
    void whenCalculatePercentage_andThereAreReservationsWithStatusCancelled_thenReturnPercentage() {

        Reservation reservation1 = Reservation.builder().status(ReservationStatus.BOOKED).build();
        Reservation reservation2 = Reservation.builder().status(ReservationStatus.BOOKED).build();
        Reservation reservation3 = Reservation.builder().status(ReservationStatus.CANCELLED).build();


        long countCancelledReservations = 1;
        long countReservations = 3;

        BigDecimal expectedPercentage = BigDecimal.valueOf(countCancelledReservations)
                .divide(BigDecimal.valueOf(countReservations), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        ReservationStatus status = ReservationStatus.CANCELLED;

        when(reservationRepository.findAll()).thenReturn(List.of(reservation1,reservation2, reservation3));

        BigDecimal resultPercentage = reservationServiceImpl.calculatePercentage(status);

        assertEquals(expectedPercentage, resultPercentage);
    }
}
