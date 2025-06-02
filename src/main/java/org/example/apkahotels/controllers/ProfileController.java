package org.example.apkahotels.controllers;

import jakarta.validation.Valid;
import org.example.apkahotels.models.*;
import org.example.apkahotels.services.HotelService;
import org.example.apkahotels.services.ReservationService;
import org.example.apkahotels.services.RoomService;
import org.example.apkahotels.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProfileController {

    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ReservationService reservationService;
    private final HotelService hotelService;
    private final RoomService roomService;


    public ProfileController(InMemoryUserDetailsManager inMemoryUserDetailsManager,
                             PasswordEncoder passwordEncoder,
                             UserService userService,
                             ReservationService reservationService,HotelService hotelService,RoomService roomService) {
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.reservationService = reservationService;
        this.hotelService = hotelService;
        this.roomService = roomService;
    }

    // Wyświetlanie profilu i formularza zmiany hasła
    @GetMapping("/profile")
    public String showProfile(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);
        model.addAttribute("passwordUpdateForm", new PasswordUpdateForm());

        // Aktualny użytkownik (jeśli potrzebujesz do wyświetlania profilu)
        AppUser user = userService.getCurrentUser();
        model.addAttribute("user", user);

        // Lista rezerwacji dla zalogowanego użytkownika
        List<Reservation> reservations = reservationService.getReservationsByUsername(username);

        // Mapa: klucz = hotelId, wartość = obiekt Hotel
        Map<Long, Hotel> hotelDetails = new HashMap<>();
        // Mapa: klucz = roomId, wartość = obiekt Room
        Map<Long, Room> roomDetails = new HashMap<>();

        // Dla każdej rezerwacji wczytujemy hotel i pokój, o ile istnieją
        for (Reservation res : reservations) {
            if (res.getHotelId() != null) {
                Hotel h = hotelService.getHotelById(res.getHotelId());
                if (h != null) {
                    hotelDetails.put(res.getHotelId(), h);
                }
            }
            if (res.getRoomId() != null) {
                Room r = roomService.getRoomById(res.getRoomId());
                if (r != null) {
                    roomDetails.put(res.getRoomId(), r);
                }
            }
        }

        // Dodajemy listę rezerwacji i mapy do modelu
        model.addAttribute("reservations", reservations);
        model.addAttribute("hotelDetails", hotelDetails);
        model.addAttribute("roomDetails", roomDetails);

        return "profile";  // widok Thymeleaf (np. profile.html)
    }

    @GetMapping("/my-reservations") // lub podobny endpoint
    public String myReservations(Model model, Principal principal) {
        try {
            String username = principal.getName();
            List<Reservation> reservations = reservationService.getReservationsByUsername(username);
            model.addAttribute("reservations", reservations);
            return "my_reservations";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Błąd przy ładowaniu rezerwacji: " + e.getMessage());
            return "index";
        }
    }

    // Zmiana hasła
    @PostMapping("/profile/update")
    public String updatePassword(@ModelAttribute("passwordUpdateForm") @Valid PasswordUpdateForm form,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "profile";
        }
        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("error", "Nowe hasło i potwierdzenie nie są zgodne!");
            return "redirect:/profile";
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetails user = inMemoryUserDetailsManager.loadUserByUsername(username);
        if (!passwordEncoder.matches(form.getOldPassword(), user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Stare hasło jest nieprawidłowe!");
            return "redirect:/profile";
        }
        UserDetails updatedUser = User.withUserDetails(user)
                .password(passwordEncoder.encode(form.getNewPassword()))
                .build();
        inMemoryUserDetailsManager.updateUser(updatedUser);
        redirectAttributes.addFlashAttribute("message", "Hasło zostało zmienione!");
        return "redirect:/profile";
    }

    // Formularz edycji profilu – przykładowo, edycja danych takich jak email, imię, nazwisko
    @GetMapping("/profile/edit")
    public String editProfile(Model model) {
        AppUser user = userService.getCurrentUser();
        model.addAttribute("user", user);
        return "edit_profile"; // widok edit_profile.html
    }

    // Zapis edytowanego profilu
    @PostMapping("/profile/save")
    public String saveProfile(@ModelAttribute("user") AppUser user, RedirectAttributes redirectAttributes) {
        // W InMemoryUserDetailsManager dane są przechowywane tylko w pamięci. W przypadku bazy danych
        // należałoby zapisać zmodyfikowanego użytkownika, np. userService.saveUser(user);
        // Tutaj możesz zaktualizować dane w pamięci lub po prostu potwierdzić zmiany
        userService.updateUser(user);
        redirectAttributes.addFlashAttribute("message", "Profil został zaktualizowany.");
        return "redirect:/profile";
    }

    // Historia rezerwacji użytkownika
    @GetMapping("/profile/reservations")
    public String userReservations(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Reservation> reservations = reservationService.getReservationsByUsername(username);
        model.addAttribute("reservations", reservations);
        return "profile_reservations"; // widok profile_reservations.html
    }
}
