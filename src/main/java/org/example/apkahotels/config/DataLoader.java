package org.example.apkahotels.config;

import org.example.apkahotels.models.*;
import org.example.apkahotels.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository; // ✅ DODAJ
    private final PasswordEncoder passwordEncoder; // ✅ DODAJ

    public DataLoader(HotelRepository hotelRepository,
                      RoomRepository roomRepository,
                      ReservationRepository reservationRepository,
                      ReviewRepository reviewRepository,
                      UserRepository userRepository,
                      PasswordEncoder passwordEncoder) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // ✅ TYLKO UŻYTKOWNICY - data.sql obsłuży resztę
        if (userRepository.count() == 0) {
            System.out.println("🔐 Tworzę użytkowników testowych...");

            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("Admin");
            admin.setLastName("Administrator");
            admin.setPhoneNumber("+48 123 456 789");
            admin.setRole(UserRole.ADMIN);
            admin.setActive(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());

            AppUser user = new AppUser();
            user.setUsername("user");
            user.setEmail("user@example.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setFirstName("Jan");
            user.setLastName("Kowalski");
            user.setPhoneNumber("+48 987 654 321");
            user.setRole(UserRole.CLIENT);
            user.setActive(true);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            userRepository.saveAll(List.of(admin, user));
            System.out.println("✅ Stworzono " + userRepository.count() + " użytkowników");
        }
    }
}
