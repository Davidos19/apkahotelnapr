package org.example.apkahotels.controllers;

import org.example.apkahotels.models.AppUser;
import org.example.apkahotels.models.UserRole;
import org.example.apkahotels.services.SecurityService;
import org.example.apkahotels.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class UserManagementController {

    private final UserService userService;
    private final SecurityService securityService;

    public UserManagementController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @GetMapping
    public String listUsers(Model model) {
        securityService.checkPermission(UserRole.ADMIN);

        List<AppUser> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("userRoles", UserRole.values());

        return "admin/users";
    }

    @PostMapping("/{userId}/role")
    public String changeUserRole(@PathVariable Long userId,
                                 @RequestParam UserRole newRole,
                                 RedirectAttributes redirectAttributes) {
        try {
            securityService.checkPermission(UserRole.ADMIN);
            userService.changeUserRole(userId, newRole);
            redirectAttributes.addFlashAttribute("message", "Rola użytkownika została zmieniona!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }

    @PostMapping("/{userId}/toggle-active")
    public String toggleUserActive(@PathVariable Long userId,
                                   RedirectAttributes redirectAttributes) {
        try {
            securityService.checkPermission(UserRole.ADMIN);
            userService.toggleUserActive(userId);
            redirectAttributes.addFlashAttribute("message", "Status użytkownika został zmieniony!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }
}