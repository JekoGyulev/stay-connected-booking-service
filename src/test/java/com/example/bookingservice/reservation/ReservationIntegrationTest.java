package com.example.bookingservice.reservation;

import com.example.bookingservice.reservation.enums.ReservationStatus;
import com.example.bookingservice.reservation.model.Reservation;
import com.example.bookingservice.reservation.repository.ReservationRepository;
import com.example.bookingservice.web.dto.CreateReservationRequest;
import com.example.bookingservice.web.dto.ReservationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReservationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void whenCreateReservation_thenItIsSavedAndStatusIsBooked() throws Exception {

        CreateReservationRequest request = CreateReservationRequest.builder()
                .userId(UUID.randomUUID())
                .propertyId(UUID.randomUUID())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(2))
                .totalPrice(BigDecimal.valueOf(200))
                .build();


        String responseJson = mockMvc.perform(post("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()) // HTTP 201
                .andReturn()
                .getResponse()
                .getContentAsString();


        ReservationResponse response = objectMapper.readValue(responseJson, ReservationResponse.class);


        assertThat(response.getReservationId()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(ReservationStatus.BOOKED);


        Reservation savedReservation = reservationRepository.findById(response.getReservationId())
                .orElseThrow(() -> new AssertionError("Reservation not found in DB"));

        assertThat(savedReservation.getUserId()).isEqualTo(request.getUserId());
        assertThat(savedReservation.getPropertyId()).isEqualTo(request.getPropertyId());
        assertThat(savedReservation.getStartDate()).isEqualTo(request.getStartDate());
        assertThat(savedReservation.getEndDate()).isEqualTo(request.getEndDate());
        assertThat(savedReservation.getTotalPrice()).isEqualByComparingTo(request.getTotalPrice());
        assertThat(savedReservation.getStatus()).isEqualTo(ReservationStatus.BOOKED);
    }

}
