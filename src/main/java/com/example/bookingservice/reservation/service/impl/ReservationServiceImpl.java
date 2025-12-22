package com.example.bookingservice.reservation.service.impl;

import com.example.bookingservice.exception.ResourceNotFound;
import com.example.bookingservice.reservation.enums.ReservationStatus;
import com.example.bookingservice.reservation.model.Reservation;
import com.example.bookingservice.reservation.repository.ReservationRepository;
import com.example.bookingservice.reservation.service.ReservationService;
import com.example.bookingservice.web.dto.CreateReservationRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
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
    public Page<Reservation> getAllReservationsByUserId(UUID userId, int pageNumber, int pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<Reservation> reservationsByUser =
                this.reservationRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageRequest);

        if (reservationsByUser.isEmpty())
            throw new ResourceNotFound("Reservations for user with id [%s] were not found".formatted(userId));

        return  reservationsByUser;
    }

    @Override
    public Page<Reservation> getAllReservationsByUserIdAndStatus(UUID userId, String reservationStatus, int pageNumber, int pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        ReservationStatus status = ReservationStatus.valueOf(reservationStatus);

        return this.reservationRepository.findAllByStatusAndUserIdOrderByCreatedAtDesc(
                status,
                userId,
                pageRequest
        );
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

    @Override
    @Transactional
    public Reservation cancel(UUID id) {

        Optional<Reservation> optionalReservation = this.reservationRepository.findById(id);

        if (optionalReservation.isEmpty()) {
            throw new ResourceNotFound("Reservation with such id [%s] was not found".formatted(id));
        }

        Reservation reservation = optionalReservation.get();

        reservation.setStatus(ReservationStatus.CANCELLED);

        this.reservationRepository.save(reservation);

        log.info("Reservation cancelled with id: {}", reservation.getId());

        return reservation;
    }

    @Override
    public long getTotalCountReservations(String status) {

        if (status.equals("ALL")) {
            return this.reservationRepository.count();
        }

        return this.reservationRepository.countAllByStatus(ReservationStatus.valueOf(status));
    }

    @Override
    public BigDecimal calculatePercentage(ReservationStatus reservationStatus) {

        List<Reservation> allReservations = this.reservationRepository.findAll();

        if (allReservations.isEmpty()) return BigDecimal.ZERO;

        long countReservations = allReservations.size();

        BigDecimal percentage;

        if (reservationStatus == ReservationStatus.BOOKED) {
            long bookedReservationsCount = allReservations
                    .stream()
                    .filter(reservation -> reservation.getStatus() == ReservationStatus.BOOKED)
                    .count();

            return BigDecimal.valueOf(bookedReservationsCount)
                    .divide(BigDecimal.valueOf(countReservations), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }


        long cancelledReservationsCount = allReservations
                    .stream()
                    .filter(reservation -> reservation.getStatus() == ReservationStatus.CANCELLED)
                    .count();

        return BigDecimal.valueOf(cancelledReservationsCount)
                .divide(BigDecimal.valueOf(countReservations), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

    }


}

