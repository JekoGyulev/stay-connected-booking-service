package com.example.bookingservice.web.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDatesResponse {

    private LocalDate checkIn;
    private LocalDate checkOut;

}
