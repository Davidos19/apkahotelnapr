package org.example.apkahotels.config;

import org.example.apkahotels.models.*;
import org.example.apkahotels.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;

    public DataLoader(HotelRepository hotelRepository,
                      RoomRepository roomRepository,
                      ReservationRepository reservationRepository,
                      ReviewRepository reviewRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.reviewRepository = reviewRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        if (hotelRepository.count() > 0) {
            System.out.println("Dane testowe już istnieją, pomijam ładowanie...");
            return;
        }

        System.out.println("🏨 Ładuję dane testowe...");

        // Hotele
        Hotel hotel1 = new Hotel("Grand Hotel Warszawa", "Warszawa", "Luksusowy hotel w centrum", "5");
        List<Hotel> savedHotels = hotelRepository.saveAll(List.of(hotel1));

        // DODAJ WIĘCEJ POKOI TEGO SAMEGO TYPU
        List<Room> rooms = new ArrayList<>();

        // Standard 2-osobowe (10 pokoi)
        for (int i = 1; i <= 10; i++) {
            Room room = new Room();
            room.setHotelId(savedHotels.get(0).getId());
            room.setRoomType("Standard");
            room.setCapacity(2);
            room.setPrice(299.0);
            room.setImageUrl("standard.jpg");
            room.setRoomNumber("1" + String.format("%02d", i));
            rooms.add(room);
        }

        // Deluxe 2-osobowe (5 pokoi)
        for (int i = 1; i <= 5; i++) {
            Room room = new Room();
            room.setHotelId(savedHotels.get(0).getId());
            room.setRoomType("Deluxe");
            room.setCapacity(2);
            room.setPrice(499.0);
            room.setImageUrl("deluxe.jpg");
            room.setRoomNumber("2" + String.format("%02d", i));
            rooms.add(room);
        }

        // Suite 4-osobowe (3 pokoje)
        for (int i = 1; i <= 3; i++) {
            Room room = new Room();
            room.setHotelId(savedHotels.get(0).getId());
            room.setRoomType("Suite");
            room.setCapacity(4);
            room.setPrice(799.0);
            room.setImageUrl("suite.jpg");
            room.setRoomNumber("3" + String.format("%02d", i));
            rooms.add(room);
        }

        roomRepository.saveAll(rooms);

        System.out.println("✅ Załadowano dane testowe:");
        System.out.println("   🏨 " + hotelRepository.count() + " hoteli");
        System.out.println("   🛏️ " + roomRepository.count() + " pokoi");
    }
}