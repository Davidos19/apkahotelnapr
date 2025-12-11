
package org.example.apkahotels.config;

import org.example.apkahotels.models.AppUser;
import org.example.apkahotels.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserService userService;

    @ModelAttribute("currentUser")
    public AppUser getCurrentUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                String username = auth.getName();
                // ✅ POPRAWIONE: obsługa Optional
                return userService.findByUsername(username).orElse(null);
            }
        } catch (Exception e) {
            // Log error but don't break
            System.out.println("Error getting current user: " + e.getMessage());
        }
        return null;
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return auth != null && auth.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        } catch (Exception e) {
            return false;
        }
    }

    @ModelAttribute("isHotelManager")
    public boolean isHotelManager() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return auth != null && auth.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_HOTEL_MANAGER"));
        } catch (Exception e) {
            return false;
        }
    }

    @ModelAttribute("canManageReservations")
    public boolean canManageReservations() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return auth != null && auth.getAuthorities().stream()
                    .anyMatch(grantedAuthority ->
                            grantedAuthority.getAuthority().equals("ROLE_ADMIN") ||
                                    grantedAuthority.getAuthority().equals("ROLE_HOTEL_MANAGER") ||
                                    grantedAuthority.getAuthority().equals("ROLE_RECEPTIONIST")
                    );
        } catch (Exception e) {
            return false;
        }
    }
}