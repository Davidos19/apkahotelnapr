package org.example.apkahotels.controllers;

import org.example.apkahotels.models.*;
import org.example.apkahotels.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin/hotels")
public class AdminController {

    private final UserService userService;
    private final ReservationService reservationService;
    private final HotelService hotelService;
    private final RoomService roomService;
    private final ReviewService reviewService;
    private final DashboardService dashboardService; // ✅ DODAJ TO

    public AdminController(UserService userService,
                           ReservationService reservationService,
                           HotelService hotelService,
                           RoomService roomService,
                           ReviewService reviewService,
                           DashboardService dashboardService) { // ✅ DODAJ TO
        this.userService = userService;
        this.reservationService = reservationService;
        this.hotelService = hotelService;
        this.roomService = roomService;
        this.reviewService = reviewService;
        this.dashboardService = dashboardService; // ✅ DODAJ TO
    }




    @GetMapping("/users")
    public String listUsers(Model model) {
        try {
            List<AppUser> users = userService.getAllUsers();
            model.addAttribute("users", users);
            model.addAttribute("userRoles", UserRole.values());
            return "admin/users"; // Szukaj template admin/users.html
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Błąd przy ładowaniu użytkowników: " + e.getMessage());
            return "admin/dashboard";
        }
    }
    @Autowired
    private SecurityAuditService auditService;

    @PostMapping("/users/{id}/role")
    public String changeUserRole(@PathVariable Long id,
                                 @RequestParam UserRole newRole,
                                 RedirectAttributes redirectAttributes) {
        auditService.logActivity("CHANGE_USER_ROLE", "Changed role for user ID: " + id + " to: " + newRole);
        try {
            userService.changeUserRole(id, newRole);
            redirectAttributes.addFlashAttribute("message",
                    "Rola użytkownika została zmieniona na: " + newRole.getDescription());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd: " + e.getMessage());
        }
        return "redirect:/admin/hotels/users";
    }

    @PostMapping("/users/{id}/toggle-active")
    public String toggleUserActive(@PathVariable Long id,
                                   RedirectAttributes redirectAttributes) {
        try {
            AppUser user = userService.getUserById(id);
            user.setActive(!user.isActive());
            userService.saveUser(user);

            String status = user.isActive() ? "aktywowany" : "dezaktywowany";
            redirectAttributes.addFlashAttribute("message",
                    "Użytkownik został " + status);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd: " + e.getMessage());
        }
        return "redirect:/admin/hotels/users";
    }

    @PostMapping("/users/{id}/reset-password")
    public String resetUserPassword(@PathVariable Long id,
                                    RedirectAttributes redirectAttributes) {
        try {
            String newPassword = userService.resetUserPassword(id);
            redirectAttributes.addFlashAttribute("message",
                    "Hasło zostało zresetowane. Nowe hasło: " + newPassword);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd: " + e.getMessage());
        }
        return "redirect:/admin/hotels/users";
    }



    @GetMapping("/users/{id}/details")
    public String getUserDetails(@PathVariable Long id, Model model) {
        try {
            AppUser user = userService.getUserById(id);
            if (user == null) {
                model.addAttribute("error", "Użytkownik nie znaleziony");
                return "redirect:/admin/hotels/users";
            }

            // Pobierz rezerwacje użytkownika
            List<Reservation> userReservations = reservationService.getReservationsByUsername(user.getUsername());

            // Dodaj szczegóły hoteli i pokoi do rezerwacji
            Map<Long, Hotel> hotelDetails = new HashMap<>();
            Map<Long, Room> roomDetails = new HashMap<>();

            for (Reservation reservation : userReservations) {
                if (reservation.getHotelId() != null) {
                    Hotel hotel = hotelService.getHotelById(reservation.getHotelId());
                    if (hotel != null) {
                        hotelDetails.put(reservation.getHotelId(), hotel);
                    }
                }
                if (reservation.getRoomId() != null) {
                    Room room = roomService.getRoomById(reservation.getRoomId());
                    if (room != null) {
                        roomDetails.put(reservation.getRoomId(), room);
                    }
                }
            }

            model.addAttribute("user", user);
            model.addAttribute("userReservations", userReservations);
            model.addAttribute("hotelDetails", hotelDetails);
            model.addAttribute("roomDetails", roomDetails);
            model.addAttribute("userRoles", UserRole.values());

            // ✅ ZMIEŃ: admin/user_details -> user_details
            return "user_details";  // SZUKA: templates/user_details.html

        } catch (Exception e) {
            model.addAttribute("error", "Błąd pobierania danych użytkownika: " + e.getMessage());
            return "redirect:/admin/hotels/users";
        }
    }


    @GetMapping("/users/{id}/edit")
    public String editUser(@PathVariable Long id, Model model) {
        try {
            AppUser user = userService.getUserById(id);
            if (user == null) {
                return "redirect:/admin/hotels/users";
            }

            model.addAttribute("user", user);
            model.addAttribute("userRoles", UserRole.values());

            return "admin_edit_user";  // template: admin_edit_user.html

        } catch (Exception e) {
            model.addAttribute("error", "Błąd pobierania danych użytkownika");
            return "redirect:/admin/hotels/users";
        }
    }


    @PostMapping("/users/{id}/update")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute AppUser updatedUser,
                             RedirectAttributes redirectAttributes) {
        try {
            AppUser existingUser = userService.getUserById(id);
            if (existingUser == null) {
                redirectAttributes.addFlashAttribute("error", "Użytkownik nie został znaleziony");
                return "redirect:/admin/hotels/users";
            }

            // Aktualizuj dane (zachowaj hasło i username)
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            // NIE aktualizujemy hasła ani username w tej metodzie

            userService.saveUser(existingUser);
            redirectAttributes.addFlashAttribute("message", "Dane użytkownika zostały zaktualizowane");
            return "redirect:/admin/hotels/users/" + id + "/details";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd aktualizacji: " + e.getMessage());
            return "redirect:/admin/hotels/users/" + id + "/edit";
        }
    }

    @PostMapping("/users/{userId}/reservations/{reservationId}/cancel")
    public String cancelUserReservation(@PathVariable Long userId,
                                        @PathVariable Long reservationId,
                                        RedirectAttributes redirectAttributes) {
        try {
            reservationService.cancelReservation(reservationId);
            redirectAttributes.addFlashAttribute("message", "Rezerwacja została anulowana");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd anulowania rezerwacji: " + e.getMessage());
        }
        return "redirect:/admin/hotels/users/" + userId + "/details";
    }

    @GetMapping("/users/{userId}/reservations/{reservationId}/edit")
    public String editUserReservation(@PathVariable Long userId,
                                      @PathVariable Long reservationId,
                                      Model model) {
        try {
            AppUser user = userService.getUserById(userId);
            Reservation reservation = reservationService.getReservationById(reservationId);

            if (user == null || reservation == null) {
                return "redirect:/admin/hotels/users/" + userId + "/details";
            }

            Hotel hotel = hotelService.getHotelById(reservation.getHotelId());
            List<Room> availableRooms = roomService.getRoomsByHotelId(reservation.getHotelId());

            model.addAttribute("user", user);
            model.addAttribute("reservation", reservation);
            model.addAttribute("hotel", hotel);
            model.addAttribute("availableRooms", availableRooms);
            model.addAttribute("reservationStatuses", Arrays.asList("CONFIRMED", "CANCELLED", "COMPLETED"));

            return "admin/edit_user_reservation";
        } catch (Exception e) {
            return "redirect:/admin/hotels/users/" + userId + "/details";
        }
    }

    @PostMapping("/users/{userId}/reservations/{reservationId}/update")
    public String updateUserReservation(@PathVariable Long userId,
                                        @PathVariable Long reservationId,
                                        @ModelAttribute Reservation updatedReservation,
                                        RedirectAttributes redirectAttributes) {
        try {
            Reservation existingReservation = reservationService.getReservationById(reservationId);
            if (existingReservation != null) {
                // Aktualizuj tylko wybrane pola
                existingReservation.setCheckIn(updatedReservation.getCheckIn());
                existingReservation.setCheckOut(updatedReservation.getCheckOut());
                existingReservation.setRoomId(updatedReservation.getRoomId());
                existingReservation.setTotalPrice(updatedReservation.getTotalPrice());
                // Dodaj status jeśli masz pole status w Reservation

                reservationService.updateReservation(existingReservation);
                redirectAttributes.addFlashAttribute("message", "Rezerwacja została zaktualizowana");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd aktualizacji rezerwacji: " + e.getMessage());
        }
        return "redirect:/admin/hotels/users/" + userId + "/details";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            AppUser user = userService.getUserById(id);
            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "Użytkownik nie został znaleziony");
                return "redirect:/admin/hotels/users";
            }

            // Sprawdź czy użytkownik ma aktywne rezerwacje
            List<Reservation> userReservations = reservationService.getReservationsByUsername(user.getUsername());
            long activeReservations = userReservations.stream()
                    .filter(r -> r.getCheckOut().isAfter(LocalDate.now()))
                    .count();

            if (activeReservations > 0) {
                redirectAttributes.addFlashAttribute("error",
                        "Nie można usunąć użytkownika z aktywnymi rezerwacjami (" + activeReservations + ")");
                return "redirect:/admin/hotels/users/" + id + "/details";
            }

            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message", "Użytkownik został usunięty");
            return "redirect:/admin/hotels/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd usuwania użytkownika: " + e.getMessage());
            return "redirect:/admin/hotels/users/" + id + "/details";
        }
    }




    // Wyświetlenie listy hoteli dla administratora

    @GetMapping("") // To obsłuży "/admin/hotels"
    public String listHotels(Model model) {
        try {
            // Pobierz wszystkie hotele
            List<Hotel> hotels = hotelService.getAllHotels();

            // Dodaj liczbę pokoi dla każdego hotelu
            for (Hotel hotel : hotels) {
                List<Room> hotelRooms = roomService.getRoomsByHotelId(hotel.getId());
                hotel.setAvailableRooms(hotelRooms.size());
            }

            // Statystyki
            model.addAttribute("hotels", hotels);
            model.addAttribute("totalHotels", hotels.size());

            // Policz pokoje
            int totalRooms = 0;
            for (Hotel hotel : hotels) {
                totalRooms += roomService.getRoomsByHotelId(hotel.getId()).size();
            }
            model.addAttribute("totalRooms", totalRooms);

            // Policz rezerwacje
            model.addAttribute("totalReservations", reservationService.getAllReservationsForAdmin().size());

            return "admin_hotels";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Błąd przy ładowaniu hoteli: " + e.getMessage());
            model.addAttribute("hotels", new ArrayList<>());
            model.addAttribute("totalHotels", 0);
            model.addAttribute("totalRooms", 0);
            model.addAttribute("totalReservations", 0);
            return "admin_hotels";
        }
    }




    @GetMapping("/admin/reservations")
    public String listReservations(Model model) {
        List<Reservation> reservations = reservationService.getAllReservationsForAdmin();
        Map<Long, Room> roomDetails = new HashMap<>();
        for (Reservation res : reservations) {
            if (res.getRoomId() != null) {
                Room room = roomService.getRoomById(res.getRoomId());
                if (room != null) {
                    roomDetails.put(res.getRoomId(), room);
                }
            }
        }
        // Dodaj logowanie
        System.out.println("Rezerwacje: " + reservations);
        System.out.println("Room Details: " + roomDetails);

        model.addAttribute("reservations", reservations);
        model.addAttribute("roomDetails", roomDetails);
        return "admin_reservations";
    }
    // Wyświetlenie listy pokoi dla danego hotelu
    @GetMapping("/{id}/rooms")
    public String listRooms(@PathVariable Long id, Model model) {
        var hotel = hotelService.getHotelById(id);
        if (hotel == null) {
            return "redirect:/admin/hotels";
        }
        var rooms = roomService.getRoomsByHotelId(id);
        model.addAttribute("hotel", hotel);
        model.addAttribute("rooms", rooms);
        return "edit_hotel";  // Widok, który wyświetla listę pokoi dla hotelu
    }

    // Formularz dodawania nowego pokoju do hotelu
    @GetMapping("/{hotelId}/rooms/new")
    public String newRoom(@PathVariable Long hotelId, Model model) {
        Hotel hotel = hotelService.getHotelById(hotelId);
        if (hotel == null) {
            return "redirect:/admin/hotels";
        }
        Room room = new Room();
        room.setHotelId(hotelId);  // Ustawienie hotelId!
        model.addAttribute("hotel", hotel);
        model.addAttribute("room", room);
        return "admin_new_room";
    }


    // Zapis nowego pokoju
    @PostMapping("/{hotelId}/rooms/save")
    public String saveRoom(@PathVariable Long hotelId, @ModelAttribute("room") Room room, RedirectAttributes redirectAttributes) {
        roomService.addRoom(room);
        redirectAttributes.addFlashAttribute("message", "Pokój został dodany");
        return "redirect:/admin/hotels/" + hotelId + "/rooms";
    }



    // Formularz dodawania nowej rezerwacji przez admina
    @GetMapping("/{hotelId}/reservations/new")
    public String newReservationForHotel(@PathVariable Long hotelId, Model model) {
        Hotel hotel = hotelService.getHotelById(hotelId);
        if (hotel == null) {
            return "redirect:/admin/hotels";
        }

        // Nowa rezerwacja z ustawionym hotelId
        Reservation reservation = new Reservation();
        reservation.setHotelId(hotelId);

        // Pobierz listę pokoi danego hotelu
        List<Room> rooms = roomService.getRoomsByHotelId(hotelId);

        model.addAttribute("hotel", hotel);
        model.addAttribute("rooms", rooms);
        model.addAttribute("reservation", reservation);

        return "admin_new_reservation";
    }




    // Zapis rezerwacji przez admina
    @PostMapping("/reservations/save")
    public String saveReservation(@ModelAttribute("reservation") Reservation reservation,
                                  RedirectAttributes redirectAttributes) {
        try {
            reservationService.makeReservation(reservation);
            redirectAttributes.addFlashAttribute("message", "Rezerwacja została dodana.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        // Po zapisaniu wracamy do rezerwacji konkretnego hotelu
        return "redirect:/admin/hotels/" + reservation.getHotelId() + "/reservations";
    }




    // Formularz dodawania nowego hotelu
    @GetMapping("/new")
    public String newHotel(Model model) {
        model.addAttribute("hotel", new Hotel());
        return "edit_hotel";  // Widok formularza tworzenia nowego hotelu
    }



    // Zapis nowego hotelu (lub aktualizacja istniejącego)
    @PostMapping("/save")
    public String saveHotel(@ModelAttribute("hotel") Hotel hotel) {
        hotelService.saveHotel(hotel);
        return "redirect:/admin/hotels";
    }

    @GetMapping("/edit/{id}")
    public String editHotel(@PathVariable Long id, Model model) {
        Hotel hotel = hotelService.getHotelById(id);
        if (hotel == null) {
            return "redirect:/admin/hotels";
        }
        model.addAttribute("hotel", hotel);
        // Pobierz pokoje związane z hotelem
        model.addAttribute("rooms", roomService.getRoomsByHotelId(id));
        return "edit_hotel";
    }

    @PostMapping("/{hotelId}/reservations/delete/{reservationId}")
    public String deleteReservation(
            @PathVariable Long hotelId,
            @PathVariable Long reservationId,
            RedirectAttributes redirectAttributes) {
        try {
            // zamiast deleteReservation:
            reservationService.cancelReservation(reservationId);
            redirectAttributes.addFlashAttribute("message", "Rezerwacja została anulowana i pokój zwolniony!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Nie udało się anulować rezerwacji: " + ex.getMessage());
        }
        return "redirect:/admin/hotels/" + hotelId + "/reservations";
    }


    // Usunięcie hotelu
    @GetMapping("/delete/{id}")
    public String deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return "redirect:/admin/hotels";
    }
    @GetMapping("/{id}/reservations")
    public String hotelReservations(@PathVariable Long id, Model model ) {
        Hotel hotel = hotelService.getHotelById(id);
        if (hotel == null) {
            return "redirect:/admin/hotels";
        }

        List<Reservation> reservations = reservationService.getReservationsByHotelId(id);
        Map<Long, Room> roomDetails = new HashMap<>();
        for (Reservation res : reservations) {
            if (res.getRoomId() != null) {
                Room room = roomService.getRoomById(res.getRoomId());
                if (room != null) {
                    roomDetails.put(res.getRoomId(), room);
                }
            }
        }

        model.addAttribute("hotel", hotel);
        model.addAttribute("reservations", reservations);
        model.addAttribute("roomDetails", roomDetails);

        // <-- DODAJ TO: obiekt dla formularza w modalu
        model.addAttribute("reservation", new Reservation());
        // <-- DODAJ TO: lista pokoi do selecta w modalu
        model.addAttribute("rooms", roomService.getRoomsByHotelId(id));

        return "admin_hotel_reservations";
    }


}