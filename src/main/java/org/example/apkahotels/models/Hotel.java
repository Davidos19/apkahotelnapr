package org.example.apkahotels.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nazwa hotelu jest wymagana")
    @Size(min = 2, max = 100)
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Miasto jest wymagane")
    @Column(nullable = false)
    private String city;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String stars; // "3", "4", "5"
    private String address;
    private String phoneNumber;
    private String email;
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Konstruktory
    public Hotel() {}

    public Hotel(String name, String city, String description, String stars) {
        this.name = name;
        this.city = city;
        this.description = description;
        this.stars = stars;
        this.createdAt = LocalDateTime.now();
    }

    // Gettery i Settery
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    // ===== DODAJ TĘ METODĘ dla kompatybilności z widokami =====
    public String getLocation() {
        return this.city; // albo address jeśli wolisz
    }
    public void setLocation(String location) {
        this.city = location;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStars() { return stars; }
    public void setStars(String stars) { this.stars = stars; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // ===== DODAJ dla kompatybilności z widokami =====
    @Transient // nie zapisuj w bazie, to tylko helper
    private Integer availableRooms = 0;

    public Integer getAvailableRooms() {
        return availableRooms;
    }
    public void setAvailableRooms(Integer availableRooms) {
        this.availableRooms = availableRooms;
    }
    @Transient
    private int reservationCount = 0;

    public int getReservationCount() {
        return reservationCount;
    }

    public void setReservationCount(int reservationCount) {
        this.reservationCount = reservationCount;
    }
}