package org.example.apkahotels.controllers;

import org.example.apkahotels.models.Reservation;
import org.example.apkahotels.services.ReservationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
public class ReservationCalendarController {

    private final ReservationService reservationService;

    public ReservationCalendarController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/calendar")
    public List<ReservationEvent> getReservationEvents(@RequestParam(required = false) Long hotelId) {
        List<Reservation> reservations;

        if (hotelId != null) {
            reservations = reservationService.getReservationsByHotelId(hotelId);
        } else {
            reservations = reservationService.getAllReservations();
        }

        return reservations.stream()
                .map(reservation -> new ReservationEvent(
                        reservation.getId(),
                        "Pokój " + (reservation.getRoomId() != null ? reservation.getRoomId() : "N/A")
                                + " - " + reservation.getUsername(),
                        reservation.getCheckIn(),
                        reservation.getCheckOut()
                ))
                .collect(Collectors.toList());
    }
}

class ReservationEvent {
    private final Long id;
    private final String title;
    private final String start;
    private final String end;

    public ReservationEvent(Long id, String title, LocalDate checkIn, LocalDate checkOut) {
        this.id = id;
        this.title = title;
        this.start = checkIn.toString();
        this.end = checkOut.toString();
    }

    // Gettery (bez Lomboka)
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getStart() { return start; }
    public String getEnd() { return end; }
}