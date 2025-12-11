
package org.example.apkahotels.services;

import org.example.apkahotels.models.*;
import org.example.apkahotels.repositories.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

    // ✅ ZAAWANSOWANY DASHBOARD
    public Map<String, Object> getAdvancedDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        try {
            // 📊 PODSTAWOWE STATYSTYKI
            List<Hotel> allHotels = hotelRepository.findAll();
            List<Reservation> allReservations = reservationRepository.findAll();

            stats.put("totalHotels", allHotels.size());
            stats.put("totalReservations", allReservations.size());
            stats.put("totalRooms", roomRepository.findAll().size());

            // 💰 PRZYCHODY (POPRAWIONE TYPY!)
            Double totalRevenue = calculateTotalRevenue();
            Double monthRevenue = calculateMonthlyRevenue();
            Double todayRevenue = calculateTodayRevenue();

            stats.put("totalRevenue", totalRevenue);
            stats.put("monthRevenue", monthRevenue);
            stats.put("todayRevenue", todayRevenue);

            // 📅 REZERWACJE WEDŁUG OKRESÓW
            LocalDate today = LocalDate.now();
            long todayReservations = allReservations.stream()
                    .filter(r -> r.getCheckIn().equals(today))
                    .count();

            long thisWeekReservations = allReservations.stream()
                    .filter(r -> r.getCheckIn().isAfter(today.minusDays(7)) &&
                            r.getCheckIn().isBefore(today.plusDays(1)))
                    .count();

            stats.put("todayReservations", todayReservations);
            stats.put("thisWeekReservations", thisWeekReservations);

            // 📈 WYKRES OBŁOŻENIA (7 dni do przodu)
            Map<String, Double> occupancyChart = getOccupancyChartData();
            stats.put("occupancyChart", occupancyChart);

            // 🏆 TOP 5 HOTELI
            List<Map<String, Object>> topHotels = getTopHotels(5);
            stats.put("topHotels", topHotels);

            // 🔔 ALERTY
            List<String> alerts = generateAlerts();
            stats.put("alerts", alerts);

            // 📊 STATYSTYKI POKOI
            Map<String, Long> roomStats = getRoomTypeStats();
            stats.put("roomStats", roomStats);

        } catch (Exception e) {
            e.printStackTrace();
            // Zwróć podstawowe statystyki w razie błędu
            stats.put("error", "Błąd pobierania zaawansowanych statystyk: " + e.getMessage());
        }

        return stats;
    }

    // 💰 OBLICZANIE PRZYCHODÓW (POPRAWIONE!)
    private Double calculateTotalRevenue() {
        return reservationRepository.findAll().stream()
                .filter(r -> r.getTotalPrice() != null)
                .mapToDouble(r -> r.getTotalPrice()) // ✅ mapToDouble zamiast map
                .sum(); // ✅ sum() zamiast reduce
    }

    private Double calculateMonthlyRevenue() {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        return reservationRepository.findAll().stream()
                .filter(r -> r.getCheckIn().isAfter(startOfMonth.minusDays(1)))
                .filter(r -> r.getTotalPrice() != null)
                .mapToDouble(r -> r.getTotalPrice()) // ✅ mapToDouble
                .sum(); // ✅ sum()
    }

    private Double calculateTodayRevenue() {
        LocalDate today = LocalDate.now();
        return reservationRepository.findAll().stream()
                .filter(r -> r.getCheckIn().equals(today))
                .filter(r -> r.getTotalPrice() != null)
                .mapToDouble(r -> r.getTotalPrice()) // ✅ mapToDouble
                .sum(); // ✅ sum()
    }

    // 📈 WYKRES OBŁOŻENIA
    private Map<String, Double> getOccupancyChartData() {
        Map<String, Double> occupancyData = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        for (int i = 0; i < 7; i++) {
            LocalDate date = today.plusDays(i);
            double occupancy = calculateOccupancyForDate(date);
            occupancyData.put(date.format(formatter), occupancy);
        }

        return occupancyData;
    }

    private double calculateOccupancyForDate(LocalDate date) {
        List<Room> allRooms = roomRepository.findAll();
        if (allRooms.isEmpty()) return 0.0;

        long occupiedRooms = reservationRepository.findAll().stream()
                .filter(r -> !date.isBefore(r.getCheckIn()) && date.isBefore(r.getCheckOut()))
                .count();

        return (double) occupiedRooms / allRooms.size() * 100;
    }

    // 🏆 TOP HOTELE
    private List<Map<String, Object>> getTopHotels(int limit) {
        Map<Long, Long> hotelReservationCounts = reservationRepository.findAll().stream()
                .filter(r -> r.getHotelId() != null)
                .collect(Collectors.groupingBy(
                        Reservation::getHotelId,
                        Collectors.counting()
                ));

        return hotelReservationCounts.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> {
                    Map<String, Object> hotelData = new HashMap<>();
                    Hotel hotel = hotelRepository.findById(entry.getKey()).orElse(null);
                    hotelData.put("hotel", hotel);
                    hotelData.put("reservationCount", entry.getValue());

                    // Oblicz przychód z tego hotelu (POPRAWIONY!)
                    Double hotelRevenue = reservationRepository.findAll().stream()
                            .filter(r -> r.getHotelId() != null && r.getHotelId().equals(entry.getKey()))
                            .filter(r -> r.getTotalPrice() != null)
                            .mapToDouble(r -> r.getTotalPrice()) // ✅ mapToDouble
                            .sum(); // ✅ sum()
                    hotelData.put("revenue", hotelRevenue);

                    return hotelData;
                })
                .collect(Collectors.toList());
    }

    // 🔔 GENERUJ ALERTY
    private List<String> generateAlerts() {
        List<String> alerts = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // Alert o zaległych check-outach
        long overdueCheckouts = reservationRepository.findAll().stream()
                .filter(r -> r.getCheckOut().isBefore(today))
                .count();
        if (overdueCheckouts > 0) {
            alerts.add("⚠️ " + overdueCheckouts + " zaległych check-outów");
        }

        // Alert o dzisiejszych check-inach
        long todayCheckins = reservationRepository.findAll().stream()
                .filter(r -> r.getCheckIn().equals(today))
                .count();
        if (todayCheckins > 0) {
            alerts.add("📅 " + todayCheckins + " check-inów dzisiaj");
        }

        // Alert o wysokim obłożeniu
        double todayOccupancy = calculateOccupancyForDate(today);
        if (todayOccupancy > 90) {
            alerts.add("🏨 Wysokie obłożenie dzisiaj: " + String.format("%.1f%%", todayOccupancy));
        }

        // Alert o niskim obłożeniu
        if (todayOccupancy < 20) {
            alerts.add("📉 Niskie obłożenie dzisiaj: " + String.format("%.1f%%", todayOccupancy));
        }

        return alerts;
    }

    // 📊 STATYSTYKI TYPÓW POKOI
    private Map<String, Long> getRoomTypeStats() {
        return roomRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        room -> room.getRoomType() != null ? room.getRoomType() : "Nieokreślony",
                        Collectors.counting()
                ));
    }

    // ===== STARE METODY (zachowujemy dla kompatybilności) =====
    public Map<String, Object> getDashboardStats() {
        return getAdvancedDashboardStats();
    }

    public List<Hotel> getHotelsWithAvailableRooms() {
        return hotelRepository.findAll().stream()
                .filter(hotel -> getAvailableRoomsCount(hotel.getId()) > 0)
                .collect(Collectors.toList());
    }

    public int getAvailableRoomsCount(Long hotelId) {
        List<Room> hotelRooms = roomRepository.findAll().stream()
                .filter(room -> room.getHotelId() != null && room.getHotelId().equals(hotelId))
                .collect(Collectors.toList());
        return hotelRooms.size();
    }

    public List<Reservation> getRecentReservations(int limit) {
        return reservationRepository.findAll().stream()
                .sorted((r1, r2) -> r2.getCheckIn().compareTo(r1.getCheckIn()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Map<String, Long> getReservationStats() {
        List<Reservation> allReservations = reservationRepository.findAll();
        Map<String, Long> stats = new HashMap<>();

        LocalDate today = LocalDate.now();
        stats.put("total", (long) allReservations.size());
        stats.put("today", allReservations.stream()
                .filter(r -> r.getCheckIn().equals(today))
                .count());
        stats.put("thisWeek", allReservations.stream()
                .filter(r -> r.getCheckIn().isAfter(today.minusDays(7)))
                .count());

        return stats;
    }

    public List<Hotel> getPopularHotels(int limit) {
        return getTopHotels(limit).stream()
                .map(data -> (Hotel) data.get("hotel"))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}