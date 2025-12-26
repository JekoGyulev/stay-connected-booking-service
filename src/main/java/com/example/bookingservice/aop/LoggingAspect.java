package com.example.bookingservice.aop;


import com.example.bookingservice.reservation.model.Reservation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Aspect
@Component
@Slf4j
public class LoggingAspect {


    @AfterReturning(
            pointcut = "execution(* com.example.bookingservice.reservation.service.impl.ReservationServiceImpl.create(..))",
            returning = "result"
    )
    public void logCreationMethod(Reservation result) {
        log.info("Reservation created with id [{}] from user with id [{}]", result.getId(), result.getUserId());
    }


    @AfterReturning(
            pointcut = "execution(* com.example.bookingservice.reservation.service.impl.ReservationServiceImpl.cancel(..))",
            returning = "result"
    )
    public void logCancelMethod(Reservation result) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        log.info("Reservation with id [{}] from {} to {} was cancelled", result.getId(),
                    result.getStartDate().format(formatter), result.getEndDate().format(formatter));
    }










}
