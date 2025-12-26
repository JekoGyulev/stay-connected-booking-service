package com.example.bookingservice.aop;


import com.example.bookingservice.reservation.model.Reservation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {


    @AfterReturning(pointcut = "execution(* com.example.bookingservice.reservation.service.impl.ReservationServiceImpl.create(..))",
                    returning = "result")
    public void logCreationMethod(Reservation result) {
        log.info("Reservation created with id [{}] from user with id [{}]", result.getId(), result.getUserId());
    }










}
