package com.example.bookingservice.reservation;

import com.example.bookingservice.reservation.model.Reservation;
import com.example.bookingservice.web.dto.ReservationResponse;
import com.example.bookingservice.web.mapper.DtoMapper;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;



public class DtoMapperUnitTest {

    @Test
    void whenThereIsReservation_andWeCallDtoMapper_thenReturnReservationResponse() {

        UUID reservationId = UUID.randomUUID();

        LocalDate now = LocalDate.now();

        Reservation reservation = Reservation.builder()
                .id(reservationId)
                .totalPrice(BigDecimal.valueOf(250))
                .startDate(now)
                .endDate(now.plusDays(1))
                .build();

        ReservationResponse expected = ReservationResponse.builder()
                .reservationId(reservationId)
                .totalPrice(BigDecimal.valueOf(250))
                .startDate(now)
                .endDate(now.plusDays(1))
                .build();


        ReservationResponse result = DtoMapper.fromReservation(reservation);

        assertEquals(expected.getReservationId(), result.getReservationId());
        assertEquals(expected.getTotalPrice(), result.getTotalPrice());
        assertEquals(expected.getStartDate(), result.getStartDate());
        assertEquals(expected.getEndDate(), result.getEndDate());
    }

}
