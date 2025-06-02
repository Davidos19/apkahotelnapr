
package org.example.apkahotels.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hotel_id")
    private Long hotelId;

    @Column(name = "room_id", nullable = true)
    private Long roomId;

    // DODAJ TO POLE
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = true, insertable = false, updatable = false)
    private Room room;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "check_in")
    private LocalDate checkIn;

    @Column(name = "check_out")
    private LocalDate checkOut;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "status")
    private String status = "CONFIRMED";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Konstruktory
    public Reservation() {
        this.createdAt = LocalDateTime.now();
    }

    public Reservation(Long hotelId, Long roomId, LocalDate checkIn, LocalDate checkOut, String username) {
        this.hotelId = hotelId;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.username = username;
        this.startDate = checkIn;
        this.endDate = checkOut;
        this.createdAt = LocalDateTime.now();
    }

    // METODA DO OBLICZANIA CENY
    public void calculateTotalPrice(Double roomPricePerNight) {
        if (checkIn != null && checkOut != null && roomPricePerNight != null) {
            long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
            this.totalPrice = nights * roomPricePerNight;
        }
    }

    // Gettery i Settery
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getHotelId() { return hotelId; }
    public void setHotelId(Long hotelId) { this.hotelId = hotelId; }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public Room getRoom() { return room; }
    public void setRoom(Room room) {
        this.room = room;
        if (room != null) {
            this.roomId = room.getId();
        }
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
        this.startDate = checkIn;
    }

    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
        this.endDate = checkOut;
    }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}