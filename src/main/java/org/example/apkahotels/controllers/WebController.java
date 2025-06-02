package org.example.apkahotels.controllers;

import jakarta.validation.Valid;
import org.example.apkahotels.dto.RoomTypeStatsDTO;
import org.example.apkahotels.models.Hotel;
import org.example.apkahotels.models.Reservation;
import org.example.apkahotels.models.Review;
import org.example.apkahotels.models.Room;
import org.example.apkahotels.services.HotelService;
import org.example.apkahotels.services.ReservationService;
import org.example.apkahotels.services.ReviewService;
import org.example.apkahotels.services.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;



@Controller
public class WebController {
    private static final Logger logger = LoggerFactory.getLogger(WebController.class);
    private final ReservationService reservationService;
    private final ReviewService reviewService;
    private final HotelService hotelService;
    private final RoomService roomService;


    public WebController(HotelService hotelService, ReservationService reservationService,
                         ReviewService reviewService, RoomService roomService) {
        this.hotelService = hotelService;
        this.reservationService = reservationService;
        this.reviewService = reviewService;
        this.roomService = roomService;

    }

    @GetMapping("/")
    public String showHomePage(Model model,
                               @RequestParam(required = false) String checkIn,
                               @RequestParam(required = false) String checkOut) {

        LocalDate checkInDate = parseDate(checkIn);
        LocalDate checkOutDate = parseDate(checkOut);

        // Pobierz hotele z dostępnością dla podanych dat
        List<Hotel> hotels;
        if (checkInDate != null && checkOutDate != null) {
            hotels = hotelService.getAllHotelsWithAvailability(checkInDate, checkOutDate);
        } else {
            // Jeśli nie ma dat, pokaż dla dzisiaj + 1 dzień
            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);
            hotels = hotelService.getAllHotelsWithAvailability(today, tomorrow);
        }

        model.addAttribute("hotels", hotels);
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);

        // Dodaj rezerwacje użytkownika
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!"anonymousUser".equals(username)) {
            model.addAttribute("reservations", reservationService.getReservationsByUsername(username));
        }

        return "index";
    }

    // ===== POPRAWIONY CONTROLLER DLA SZCZEGÓŁÓW HOTELU =====
    @GetMapping({"/hotel/{id}", "/hotels/{id}"})
    public String hotelDetails(@PathVariable Long id,
                               @RequestParam(required = false) String checkIn,
                               @RequestParam(required = false) String checkOut,
                               Model model) {

        logger.info("Dostęp do hotelu ID: {}", id);

        Hotel hotel = hotelService.getHotelById(id);
        if (hotel == null) {
            logger.warn("Hotel o ID {} nie istnieje", id);
            return "redirect:/";
        }

        LocalDate checkInDate = parseDate(checkIn);
        LocalDate checkOutDate = parseDate(checkOut);

        if (checkInDate == null || checkOutDate == null) {
            checkInDate = LocalDate.now();
            checkOutDate = LocalDate.now().plusDays(1);
        }

        try {
            // NAJPIERW SPRÓBUJ STARĄ METODĘ (która na pewno istnieje)
            List<RoomService.RoomTypeAvailability> roomTypesAvailability = roomService.getRoomTypesWithAvailability(id, checkInDate, checkOutDate);

            // Policz całkowitą dostępność
            int totalAvailable = roomTypesAvailability.stream()
                    .mapToInt(RoomService.RoomTypeAvailability::getAvailableCount)
                    .sum();

            hotel.setAvailableRooms(totalAvailable);

            // WAŻNE: Użyj tej samej nazwy co w HTML
            model.addAttribute("roomTypesWithAvailability", roomTypesAvailability);
            model.addAttribute("checkIn", checkInDate);
            model.addAttribute("checkOut", checkOutDate);

            logger.debug("Załadowano {} typów pokoi, {} dostępnych łącznie", roomTypesAvailability.size(), totalAvailable);

        } catch (Exception e) {
            logger.error("Błąd podczas ładowania pokoi dla hotelu {}: {}", id, e.getMessage(), e);
            model.addAttribute("roomTypesWithAvailability", new ArrayList<>());
            model.addAttribute("error", "Błąd podczas ładowania pokoi");
            hotel.setAvailableRooms(0);
        }

        // Pobierz recenzje
        try {
            model.addAttribute("reviews", reviewService.getReviewsForHotel(id));
            model.addAttribute("averageRating", reviewService.getAverageRating(id));
        } catch (Exception e) {
            logger.warn("Błąd podczas pobierania recenzji: {}", e.getMessage());
            model.addAttribute("reviews", new ArrayList<>());
            model.addAttribute("averageRating", 0.0);
        }

        model.addAttribute("hotel", hotel);
        model.addAttribute("reservation", new Reservation());

        return "hotel_details";
    }


    @PostMapping("/reservation")
    public String makeReservation(@ModelAttribute Reservation reservation,
                                  @RequestParam(required = false) Long hotelId,
                                  @RequestParam(required = false) String roomType,
                                  @RequestParam(required = false) Integer capacity,
                                  @RequestParam(required = false) String checkIn,
                                  @RequestParam(required = false) String checkOut,
                                  RedirectAttributes redirectAttributes) {

        try {
            // Walidacja danych wejściowych
            validateReservationInput(hotelId, roomType, capacity, checkIn, checkOut);

            // Parsuj daty
            LocalDate checkInDate = LocalDate.parse(checkIn);
            LocalDate checkOutDate = LocalDate.parse(checkOut);

            // UŻYJ ZOPTYMALIZOWANEJ METODY
            Room assignedRoom = roomService.assignAvailableRoomOfTypeOptimized(hotelId, roomType, capacity, checkInDate, checkOutDate);

            // Skonfiguruj rezerwację
            reservation.setHotelId(hotelId);
            reservation.setRoomId(assignedRoom.getId());
            reservation.setCheckIn(checkInDate);
            reservation.setCheckOut(checkOutDate);
            reservation.setUsername(getCurrentUsername());

            // Zapisz rezerwację
            reservationService.makeReservation(reservation);

            redirectAttributes.addFlashAttribute("message",
                    String.format("Rezerwacja pokoju %s (%s) została utworzona! Koszt: %s zł",
                            assignedRoom.getRoomNumber(), roomType + " " + capacity + " os.",
                            reservation.getTotalPrice()));

            return "redirect:/myReservations";

        } catch (Exception e) {
            logger.error("Błąd rezerwacji: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Błąd: " + e.getMessage());
            return "redirect:/hotels/" + hotelId;
        }
    }

    // Metody pomocnicze
    private void validateReservationInput(Long hotelId, String roomType, Integer capacity, String checkIn, String checkOut) {
        if (hotelId == null) throw new RuntimeException("Hotel nie został wybrany");
        if (roomType == null || roomType.trim().isEmpty()) throw new RuntimeException("Typ pokoju nie został wybrany");
        if (capacity == null) throw new RuntimeException("Pojemność pokoju nie została określona");
        if (checkIn == null || checkIn.trim().isEmpty()) throw new RuntimeException("Data przyjazdu jest wymagana");
        if (checkOut == null || checkOut.trim().isEmpty()) throw new RuntimeException("Data wyjazdu jest wymagana");
    }

    private String getCurrentUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(username)) {
            throw new RuntimeException("Musisz być zalogowany aby dokonać rezerwacji");
        }
        return username;
    }
    @PostMapping("/cancelReservation")
    public String cancelReservation(@RequestParam("reservationId") Long reservationId,
                                    RedirectAttributes redirectAttributes) {
        try {
            reservationService.cancelReservation(reservationId);
            redirectAttributes.addFlashAttribute("message", "Rezerwacja anulowana!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Błąd przy anulowaniu rezerwacji: " + e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/reservation/edit/{id}")
    public String editReservation(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.getReservationById(id);
        if (reservation == null) {
            return "redirect:/";
        }
        model.addAttribute("reservation", reservation);
        List<Room> rooms = roomService.getRoomsByHotelId(reservation.getHotelId());
        model.addAttribute("rooms", rooms);
        return "edit_reservation";
    }

    @PostMapping("/reservation/update")
    public String updateReservation(@ModelAttribute("reservation") Reservation updatedRes) {
        reservationService.updateReservation(updatedRes);
        return "redirect:/";
    }

    @GetMapping("/search")
    public String searchHotels(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("hotels", reservationService.searchHotels(keyword));
        model.addAttribute("reservations", reservationService.getAllReservations());
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("keyword", keyword);
        return "index";
    }

    @GetMapping("/myReservations")
    public String myReservations(Model model) {
        model.addAttribute("reservations", reservationService.getUserReservations());
        return "my_reservations";
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            logger.warn("Nie można sparsować daty: {}", dateStr);
            return null;
        }
    }
}
