package org.example.apkahotels.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long hotelId;

    @Column(nullable = false)
    private String roomType;   // np. "Standard", "Deluxe", "Suite"

    @Column(nullable = false)
    private int capacity;      // Liczba osób

    @Column(nullable = false)
    private double price;      // Cena za noc

    private String imageUrl;   // URL do zdjęcia pokoju

    @Column(nullable = false)
    private String roomNumber; // np. "101", "202A"

    // 1) publiczny konstruktor bez-arg (JPA)
    public Room() {
    }

    // 2) konstruktor ze wszystkimi polami
    public Room(Long id,
                Long hotelId,
                String roomType,
                int capacity,
                double price,
                String imageUrl,
                String roomNumber) {
        this.id = id;
        this.hotelId = hotelId;
        this.roomType = roomType;
        this.capacity = capacity;
        this.price = price;
        this.imageUrl = imageUrl;
        this.roomNumber = roomNumber;
    }

    // --- gettery i settery ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}
