package org.example.apkahotels.controllers;

import jakarta.validation.Valid;
import org.example.apkahotels.models.AppUser;
import org.example.apkahotels.models.RegistrationForm;
import org.example.apkahotels.repositories.AppUserRepository;
import org.example.apkahotels.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class RegistrationController {

    private final InMemoryUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository userRepo;
    private final UserService userService;    // ← dołóż

    @Autowired
    public RegistrationController(InMemoryUserDetailsManager udm, PasswordEncoder passwordEncoder, AppUserRepository userRepo, UserService userService) {
        this.userDetailsManager  = udm;
        this.passwordEncoder = passwordEncoder;
        this.userRepo           = userRepo;
        this.userService= userService;

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

        // Sprawdź czy użytkownik już istnieje (wystarczy jedna z metod)
        if (userDetailsManager.userExists(form.getUsername())) {
            model.addAttribute("error", "Użytkownik o tej nazwie już istnieje");
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

            // Zarejestruj użytkownika (UserService zajmie się kodowaniem hasła i dodaniem do Spring Security)
            userService.registerUser(newUser);

            return "redirect:/login?registered=true";

        } catch (Exception e) {
            model.addAttribute("error", "Błąd rejestracji: " + e.getMessage());
            return "register";
        }
    }

}
