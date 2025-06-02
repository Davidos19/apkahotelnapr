package org.example.apkahotels.services;

import org.example.apkahotels.models.Hotel;
import org.example.apkahotels.models.Reservation;
import org.example.apkahotels.models.Room;
import org.example.apkahotels.repositories.HotelRepository;
import org.example.apkahotels.repositories.ReservationRepository;
import org.example.apkahotels.repositories.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

@Service
public class DashboardService {

    private final HotelRepository hotelRepository;
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public DashboardService(HotelRepository hotelRepository,
                            ReservationRepository reservationRepository,
                            RoomRepository roomRepository) {
        this.hotelRepository = hotelRepository;
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    // ===== DODAJ TĘ METODĘ - brakujący getDashboardStats =====
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // Liczba wszystkich hoteli
        stats.put("totalHotels", hotelRepository.count());

        // Liczba wszystkich rezerwacji
        stats.put("totalReservations", reservationRepository.count());

        // Liczba wszystkich pokoi
        stats.put("totalRooms", roomRepository.count());

        // Dostępne pokoje (wszystkie pokoje - można rozszerzyć o logikę dostępności)
        stats.put("totalAvailableRooms", roomRepository.count());

        // Rezerwacje dzisiaj
        long todayReservations = reservationRepository.findAll()
                .stream()
                .filter(r -> r.getCheckIn().equals(LocalDate.now()))
                .count();
        stats.put("todayReservations", todayReservations);

        // Rezerwacje w tym miesiącu
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        long thisMonthReservations = reservationRepository.findAll()
                .stream()
                .filter(r -> !r.getCheckIn().isBefore(startOfMonth) && !r.getCheckIn().isAfter(endOfMonth))
                .count();
        stats.put("thisMonthReservations", thisMonthReservations);

        return stats;
    }

    public List<Hotel> getHotelsWithAvailableRooms() {
        List<Hotel> allHotels = hotelRepository.findAll();

        // Dla każdego hotelu sprawdź czy ma pokoje
        return allHotels.stream()
                .filter(hotel -> {
                    List<Room> hotelRooms = roomRepository.findByHotelId(hotel.getId());
                    return !hotelRooms.isEmpty(); // hotel ma pokoje
                })
                .collect(Collectors.toList());
    }

    public int getAvailableRoomsCount(Long hotelId) {
        return roomRepository.findByHotelId(hotelId).size();
    }

    public List<Reservation> getRecentReservations(int limit) {
        return reservationRepository.findAll()
                .stream()
                .sorted((r1, r2) -> r2.getCheckIn().compareTo(r1.getCheckIn()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Map<String, Long> getReservationStats() {
        long totalReservations = reservationRepository.count();
        long todayReservations = reservationRepository.findAll()
                .stream()
                .filter(r -> r.getCheckIn().equals(LocalDate.now()))
                .count();

        return Map.of(
                "total", totalReservations,
                "today", todayReservations
        );
    }

    public List<Hotel> getPopularHotels(int limit) {
        // Najpopularniejsze hotele na podstawie liczby rezerwacji
        Map<Long, Long> hotelReservationCount = reservationRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Reservation::getHotelId,
                        Collectors.counting()
                ));

        return hotelRepository.findAll()
                .stream()
                .sorted((h1, h2) -> {
                    long count1 = hotelReservationCount.getOrDefault(h1.getId(), 0L);
                    long count2 = hotelReservationCount.getOrDefault(h2.getId(), 0L);
                    return Long.compare(count2, count1); // sortowanie malejące
                })
                .limit(limit)
                .collect(Collectors.toList());
    }

    // ===== DODATKOWE METODY DLA ROZSZERZENIA DASHBOARDU =====

    public Map<String, Object> getDetailedStats() {
        Map<String, Object> stats = new HashMap<>();

        // Podstawowe statystyki
        stats.putAll(getDashboardStats());

        // Najpopularniejsze hotele (top 5)
        stats.put("popularHotels", getPopularHotels(5));

        // Ostatnie rezerwacje (10)
        stats.put("recentReservations", getRecentReservations(10));

        // Hotele z dostępnymi pokojami
        stats.put("hotelsWithRooms", getHotelsWithAvailableRooms());

        return stats;
    }
}