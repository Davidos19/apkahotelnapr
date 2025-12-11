package org.example.apkahotels.controllers;

import org.example.apkahotels.services.SecurityAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private SecurityAuditService auditService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {

        if (error != null) {
            model.addAttribute("error", "❌ Nieprawidłowa nazwa użytkownika lub hasło!");
            // ✅ LOG NIEUDANEJ PRÓBY (bez username z bezpieczeństwa)
            auditService.logActivity("FAILED_LOGIN_PAGE", "Login page accessed with error");
        }

        if (logout != null) {
            model.addAttribute("message", "✅ Zostałeś pomyślnie wylogowany.");
            auditService.logActivity("LOGOUT_PAGE", "Logout successful");
        }

        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "unknown";

        auditService.logActivity("ACCESS_DENIED", "Access denied for user: " + username);
        model.addAttribute("error", "🚫 Brak uprawnień do tej strony!");
        return "error/403";
    }
}