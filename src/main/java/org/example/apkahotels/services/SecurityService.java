package org.example.apkahotels.services;

import org.example.apkahotels.models.AppUser;
import org.example.apkahotels.models.UserRole;
import org.example.apkahotels.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SecurityService {

    private final UserRepository userRepository;

    public SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Optional<AppUser> getCurrentUser() {
        String username = getCurrentUsername();
        if ("anonymousUser".equals(username)) {
            return Optional.empty();
        }
        return userRepository.findByUsername(username);
    }

    public boolean hasRole(UserRole role) {
        return getCurrentUser()
                .map(user -> user.hasRole(role))
                .orElse(false);
    }

    public boolean isAdmin() {
        return hasRole(UserRole.ADMIN);
    }

    public boolean isHotelManager() {
        return hasRole(UserRole.HOTEL_MANAGER) || isAdmin();
    }

    public boolean canManageReservations() {
        return hasRole(UserRole.RECEPTIONIST) || isHotelManager();
    }

    public void updateLastLogin(String username) {
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    user.setLastLogin(LocalDateTime.now());
                    userRepository.save(user);
                });
    }

    public void checkPermission(UserRole requiredRole) {
        if (!hasRole(requiredRole)) {
            throw new SecurityException("Brak uprawnień. Wymagana rola: " + requiredRole.getDescription());
        }
    }
}