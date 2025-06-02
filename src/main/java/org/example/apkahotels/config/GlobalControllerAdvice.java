package org.example.apkahotels.config;

import org.example.apkahotels.services.SecurityService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final SecurityService securityService;

    public GlobalControllerAdvice(SecurityService securityService) {
        this.securityService = securityService;
    }

    @ModelAttribute("currentUser")
    public org.example.apkahotels.models.AppUser getCurrentUser() {
        return securityService.getCurrentUser().orElse(null);
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        return securityService.isAdmin();
    }

    @ModelAttribute("isHotelManager")
    public boolean isHotelManager() {
        return securityService.isHotelManager();
    }

    @ModelAttribute("canManageReservations")
    public boolean canManageReservations() {
        return securityService.canManageReservations();
    }

    @ModelAttribute("currentUsername")
    public String getCurrentUsername() {
        return securityService.getCurrentUsername();
    }
}