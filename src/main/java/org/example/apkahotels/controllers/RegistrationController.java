package org.example.apkahotels.controllers;

import jakarta.validation.Valid;
import org.example.apkahotels.models.AppUser;
import org.example.apkahotels.models.RegistrationForm;
import org.example.apkahotels.models.UserRole;
import org.example.apkahotels.repositories.UserRepository;
import org.example.apkahotels.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public RegistrationController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid RegistrationForm form,
                               BindingResult br, Model model) {
        // Walidacja podstawowa
        if (br.hasErrors()) {
            return "register";
        }

        // Sprawdź zgodność haseł
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("error", "Hasła nie są zgodne");
            return "register";
        }

        // Sprawdź w bazie danych
        if (userRepository.existsByUsername(form.getUsername())) {
            model.addAttribute("error", "Użytkownik o tej nazwie już istnieje");
            return "register";
        }

        if (userRepository.existsByEmail(form.getEmail())) {
            model.addAttribute("error", "Email już jest używany");
            return "register";
        }

        try {
            // Utwórz obiekt AppUser z danymi z formularza
            AppUser newUser = new AppUser();
            newUser.setUsername(form.getUsername());
            newUser.setPassword(form.getPassword()); // RAW hasło - zostanie zakodowane w UserService
            newUser.setEmail(form.getEmail());
            newUser.setFirstName(form.getFirstName());
            newUser.setLastName(form.getLastName());
            newUser.setPhoneNumber(form.getPhoneNumber());
            newUser.setProfileImageUrl(null);

            // ✅ POPRAWKA: CLIENT zamiast USER!
            newUser.setRole(UserRole.CLIENT);
            newUser.setActive(true);

            // Zarejestruj użytkownika przez UserService
            userService.registerUser(newUser);

            return "redirect:/login?registered=true";

        } catch (Exception e) {
            System.err.println("Błąd rejestracji: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Błąd rejestracji: " + e.getMessage());
            return "register";
        }
    }
}