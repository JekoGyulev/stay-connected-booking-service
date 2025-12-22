package com.example.bookingservice.reservation.repository;
import com.example.bookingservice.reservation.enums.ReservationStatus;
import com.example.bookingservice.reservation.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,UUID> {

    Page<Reservation> findAllByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    Page<Reservation> findAllByStatusAndUserIdOrderByCreatedAtDesc(ReservationStatus status, UUID userId, Pageable pageable);

    long countAllByStatus(ReservationStatus reservationStatus);
}
