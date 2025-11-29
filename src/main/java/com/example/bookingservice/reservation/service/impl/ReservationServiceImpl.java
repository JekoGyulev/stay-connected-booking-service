package com.example.bookingservice.reservation.service.impl;

import com.example.bookingservice.reservation.enums.ReservationStatus;
import com.example.bookingservice.reservation.model.Reservation;
import com.example.bookingservice.reservation.repository.ReservationRepository;
import com.example.bookingservice.reservation.service.ReservationService;
import com.example.bookingservice.web.dto.CreateReservationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    @Override
    public List<Reservation> getAllReservationsByUserId(UUID userId) {
        return this.reservationRepository.findAllByUserId(userId);
    }

    @Override
    public Reservation create(CreateReservationRequest createReservationRequest) {

        Reservation reservation = Reservation.builder()
                .userId(createReservationRequest.getUserId())
                .propertyId(createReservationRequest.getPropertyId())
                .startDate(createReservationRequest.getStartDate())
                .endDate(createReservationRequest.getEndDate())
                .totalPrice(createReservationRequest.getTotalPrice())
                .status(ReservationStatus.BOOKED)
                .build();

        this.reservationRepository.save(reservation);

        log.info("Reservation created with id: {}", reservation.getId());

        return reservation;
    }

    // Have at least 1 logging message -> log.info("Successfully done {your operation}")
}
