package org.example.apkahotels.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReservationValidationService {

    public void validateReservationDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data przyjazdu nie może być w przeszłości");
        }
        if (checkOut.isBefore(checkIn.plusDays(1))) {
            throw new IllegalArgumentException("Pobyt musi trwać przynajmniej 1 noc");
        }
    }

    public void validateMaxStayDuration(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn.until(checkOut).getDays() > 30) {
            throw new IllegalArgumentException("Maksymalny pobyt to 30 dni");
        }
    }
}