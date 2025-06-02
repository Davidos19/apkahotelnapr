
package org.example.apkahotels.services;

import org.example.apkahotels.models.AppUser;
import org.example.apkahotels.models.Hotel;
import org.example.apkahotels.models.Reservation;
import org.example.apkahotels.models.Room;
import org.example.apkahotels.repositories.HotelRepository;
import org.example.apkahotels.repositories.ReservationRepository;
import org.example.apkahotels.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationService {

    private final HotelRepository hotelRepository;
    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final RoomService roomService;
    private final UserRepository userRepository;

    public ReservationService(HotelRepository hotelRepository,
                              ReservationRepository reservationRepository,
                              UserService userService,
                              RoomService roomService,
                              UserRepository userRepository) {
        this.hotelRepository = hotelRepository;
        this.reservationRepository = reservationRepository;
        this.userService = userService;
        this.roomService = roomService;
        this.userRepository = userRepository;
    }


    // ===== METODY HOTELI =====
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll(); // JPA standardowa metoda
    }

    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id).orElse(null); // JPA standardowa metoda
    }

    public Map<Long, Hotel> getHotelsByIds(Set<Long> hotelIds) {
        return hotelRepository.findAllById(hotelIds)
                .stream()
                .collect(Collectors.toMap(Hotel::getId, hotel -> hotel));
    }

    public List<Hotel> searchHotels(String keyword) {
        return hotelRepository.searchByKeyword(keyword);
    }
    public long getReservationCountByHotelId(Long hotelId) {
        try {
            return reservationRepository.countReservationsByHotelId(hotelId);
        } catch (Exception e) {
            System.err.println("Błąd przy liczeniu rezerwacji dla hotelu " + hotelId + ": " + e.getMessage());
            return 0;
        }
    }


    // ===== METODY REZERWACJI =====
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null); // JPA standardowa metoda
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll(); // JPA standardowa metoda
    }

    public void cancelReservation(Long reservationId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if (reservation.isPresent()) {
            reservationRepository.deleteById(reservationId); // JPA standardowa metoda
        } else {
            throw new RuntimeException("Rezerwacja nie została znaleziona");
        }
    }
    public void makeReservation(Reservation reservation) {
        // Walidacja
        if (reservation.getCheckIn() == null || reservation.getCheckOut() == null) {
            throw new RuntimeException("Daty przyjazdu i wyjazdu są wymagane");
        }

        if (reservation.getCheckIn().isAfter(reservation.getCheckOut())) {
            throw new RuntimeException("Data wyjazdu musi być po dacie przyjazdu");
        }

        if (reservation.getCheckIn().isBefore(LocalDate.now())) {
            throw new RuntimeException("Data przyjazdu nie może być w przeszłości");
        }

        // Pobierz aktualnego użytkownika
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            reservation.setUsername(auth.getName());
        }

        // DODAJ OBLICZANIE CENY
        if (reservation.getRoomId() != null) {
            Room room = roomService.getRoomById(reservation.getRoomId());
            if (room != null) {
                reservation.calculateTotalPrice(room.getPrice());
            }
        }

        // Sprawdź dostępność pokoju typu (zamiast konkretnego pokoju)
        if (reservation.getRoomId() != null) {
            Room selectedRoom = roomService.getRoomById(reservation.getRoomId());
            if (selectedRoom != null) {
                // Sprawdź czy jest dostępny pokój tego typu w wybranych datach
                List<Room> availableRooms = roomService.getAvailableRoomsByTypeAndDates(
                        selectedRoom.getHotelId(),
                        selectedRoom.getRoomType(),
                        selectedRoom.getCapacity(),
                        reservation.getCheckIn(),
                        reservation.getCheckOut()
                );

                if (availableRooms.isEmpty()) {
                    throw new RuntimeException("Brak dostępnych pokoi tego typu w wybranych datach");
                }

                // Przypisz pierwszy dostępny pokój
                reservation.setRoomId(availableRooms.get(0).getId());
            }
        }

        // Ustaw daty startDate i endDate dla kompatybilności
        reservation.setStartDate(reservation.getCheckIn());
        reservation.setEndDate(reservation.getCheckOut());

        reservation.setCreatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);
    }

    public void updateReservation(Reservation reservation) {
        if (!reservationRepository.existsById(reservation.getId())) {
            throw new RuntimeException("Rezerwacja nie została znaleziona");
        }

        // Ustaw daty dla kompatybilności
        reservation.setStartDate(reservation.getCheckIn());
        reservation.setEndDate(reservation.getCheckOut());

        reservationRepository.save(reservation); // JPA standardowa metoda
    }

    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut, Long excludeReservationId) {
        List<Reservation> conflicting = reservationRepository.findConflictingReservations(roomId, checkIn, checkOut);

        // Wykluczamy bieżącą rezerwację (przy edycji)
        if (excludeReservationId != null) {
            conflicting = conflicting.stream()
                    .filter(r -> !r.getId().equals(excludeReservationId))
                    .collect(Collectors.toList());
        }

        return conflicting.isEmpty();
    }

    public List<Reservation> getUserReservations() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            String username = auth.getName();
            return reservationRepository.findByUsernameOrderByCheckInDesc(username);
        }
        return List.of();
    }

    public List<Reservation> getAllReservationsForAdmin() {
        return reservationRepository.findAll(); // JPA standardowa metoda
    }

    public List<Reservation> getReservationsByHotelId(Long hotelId) {
        return reservationRepository.findByHotelIdOrderByCheckInDesc(hotelId);
    }

    public List<Reservation> getUserReservationsByDate(LocalDate date) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            String username = auth.getName();
            return reservationRepository.findByUsernameOrderByCheckInDesc(username)
                    .stream()
                    .filter(r -> r.getCheckIn().equals(date))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    public void deleteReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId); // JPA standardowa metoda
    }

    public List<Reservation> getReservationsByUsername(String username) {
        return reservationRepository.findByUsernameOrderByCheckInDesc(username);
    }

    // ===== NOWE METODY DLA ADMINA =====
    public long getTotalReservations() {
        try {
            return reservationRepository.count();
        } catch (Exception e) {
            System.err.println("Błąd przy liczeniu wszystkich rezerwacji: " + e.getMessage());
            return 0;
        }
    }

    public List<Reservation> getRecentReservations(int limit) {
        return reservationRepository.findAll()
                .stream()
                .sorted((r1, r2) -> r2.getCheckIn().compareTo(r1.getCheckIn()))
                .limit(limit)
                .collect(Collectors.toList());
    }
    public List<Reservation> getReservationsByUserId(Long userId) {
        AppUser user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return reservationRepository.findByUsernameOrderByCheckInDesc(user.getUsername());
        }
        return List.of();
    }

}