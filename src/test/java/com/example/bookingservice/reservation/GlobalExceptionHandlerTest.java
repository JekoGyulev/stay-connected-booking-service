package com.example.bookingservice.reservation;

import com.example.bookingservice.exception.ResourceNotFound;
import com.example.bookingservice.reservation.service.ReservationService;
import com.example.bookingservice.web.controller.ReservationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.UUID;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ReservationController.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;


//    @Test
//    void whenResourceNotFound_thenReturnNotFoundStatusAndErrorResponse() throws Exception {
//        UUID userId =  UUID.randomUUID();
//
//        when(reservationService.getAllReservationsByUserId(userId))
//                .thenThrow(new ResourceNotFound("Reservations for user were not found"));
//
//        mockMvc.perform(get("/api/v1/reservations").param("userId",userId.toString()))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Reservations for user were not found"))
//                .andExpect(jsonPath("$.timestamp").exists());
//
//    }
//
//    @Test
//    void whenResourceHasBadParameter_thenReturnBadRequestStatusAndErrorResponse() throws Exception {
//        UUID userId =  UUID.randomUUID();
//
//        when(reservationService.getAllReservationsByUserId(userId))
//                .thenThrow(new RuntimeException("Bad Request"));
//
//        mockMvc.perform(get("/api/v1/reservations").param("userId",userId.toString()))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("Bad Request"))
//                .andExpect(jsonPath("$.timestamp").exists());
//    }













}
