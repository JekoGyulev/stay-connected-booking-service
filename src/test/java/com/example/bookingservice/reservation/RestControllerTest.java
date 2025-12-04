package com.example.bookingservice.reservation;

import com.example.bookingservice.reservation.enums.ReservationStatus;
import com.example.bookingservice.reservation.model.Reservation;
import com.example.bookingservice.reservation.service.ReservationService;
import com.example.bookingservice.web.controller.ReservationController;
import com.example.bookingservice.web.dto.CreateReservationRequest;
import com.example.bookingservice.web.dto.ReservationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ReservationController.class)
public class RestControllerTest {

    @MockitoBean
    private ReservationService reservationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenThereAreReservationsInTheDatabase_andStatusIsAll_thenReturnTotalCountReservations_andStatusOk () throws Exception {

        String status = "ALL";

        Reservation reservation1 = Reservation.builder()
                .status(ReservationStatus.CANCELLED)
                .build();

        Reservation reservation2 = Reservation.builder()
                .status(ReservationStatus.BOOKED)
                .build();

        when(reservationService.getTotalCountReservations(status))
                .thenReturn(2L);

        mockMvc.perform(get("/api/v1/reservations/total").param("status", status))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(2));

        verify(reservationService).getTotalCountReservations(status);

    }

    @Test
    void whenThereAreReservationsForUser_thenReturnResponseAndStatusOk() throws Exception {

        UUID userId = UUID.randomUUID();

        Reservation reservation = Reservation
                .builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .build();

        Reservation reservation2 = Reservation
                .builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .build();

        when(reservationService.getAllReservationsByUserId(userId))
                .thenReturn(List.of(reservation, reservation2));


        mockMvc.perform(get("/api/v1/reservations").param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reservationId").value(reservation.getId().toString()))
                .andExpect(jsonPath("$[1].reservationId").value(reservation2.getId().toString()));

        verify(reservationService).getAllReservationsByUserId(userId);

    }

    @Test
    void whenThereAreBookedReservations_andSendRequestToCalculateAveragePercentageBookedOnes_thenReturnPercentage() throws Exception {

        Reservation reservation1 = Reservation.builder()
                .status(ReservationStatus.CANCELLED)
                .build();

        Reservation reservation2 = Reservation.builder()
                .status(ReservationStatus.BOOKED)
                .build();

        Reservation reservation3 = Reservation.builder()
                .status(ReservationStatus.CANCELLED)
                .build();

        Reservation reservation4 = Reservation.builder()
                .status(ReservationStatus.BOOKED)
                .build();


        when(reservationService.calculatePercentage(ReservationStatus.BOOKED))
                .thenReturn(BigDecimal.valueOf(50.00));

        mockMvc.perform(get("/api/v1/reservations/percentage").param("status", String.valueOf(ReservationStatus.BOOKED)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(50.00));

        verify(reservationService).calculatePercentage(ReservationStatus.BOOKED);
    }

    @Test
    void whenCreateReservation_andSendRequest_thenCreateAndReturnResponse() throws Exception {

        UUID userId = UUID.randomUUID();
        UUID propertyId = UUID.randomUUID();


        CreateReservationRequest request = CreateReservationRequest
                .builder()
                .userId(userId)
                .propertyId(propertyId)
                .totalPrice(BigDecimal.valueOf(50))
                .build();


        Reservation reservation = Reservation.builder()
                        .id(UUID.randomUUID())
                        .propertyId(propertyId)
                        .totalPrice(BigDecimal.valueOf(50))
                        .status(ReservationStatus.BOOKED)
                        .build();

        when(reservationService.create(request))
                .thenReturn(reservation);


        mockMvc.perform(post("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(ReservationStatus.BOOKED.toString()))
                .andExpect(jsonPath("$.propertyId").value(reservation.getPropertyId().toString()))
                .andExpect(jsonPath("$.totalPrice").value(reservation.getTotalPrice()))
                .andExpect(header().string("Location", "/api/v1/reservations/" + reservation.getId()));

        verify(reservationService).create(request);
    }

    @Test
    void whenCancelReservation_andSendRequest_thenCancelReservationAndReturnResponse() throws Exception {

        UUID reservationId = UUID.randomUUID();

        Reservation reservation = Reservation
                .builder()
                .id(reservationId)
                .totalPrice(BigDecimal.valueOf(50))
                .status(ReservationStatus.CANCELLED)
                .build();

        when(reservationService.cancel(reservationId))
                .thenReturn(reservation);


        mockMvc.perform(put("/api/v1/reservations/cancellation").param("reservationId", String.valueOf(reservationId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ReservationStatus.CANCELLED.toString()))
                .andExpect(jsonPath("$.reservationId").value(reservation.getId().toString()))
                .andExpect(jsonPath("$.totalPrice").value(reservation.getTotalPrice()));

        verify(reservationService).cancel(reservationId);
    }

}
