package org.example.apkahotels.services;

import org.example.apkahotels.models.AppUser;
import org.example.apkahotels.models.UserRole;
import org.example.apkahotels.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;

    // ✅ USUŃ InMemoryUserDetailsManager z konstruktora!
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       SecurityService securityService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityService = securityService;
    }

    public void registerUser(AppUser newUser) {
        String username = newUser.getUsername();

        System.out.println("=== REJESTRACJA UŻYTKOWNIKA ===");
        System.out.println("Username: " + username);
        System.out.println("Email: " + newUser.getEmail());

        // Sprawdź czy użytkownik już istnieje
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Użytkownik już istnieje");
        }

        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new RuntimeException("Email już jest używany");
        }

        // Zakoduj hasło
        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);

        // ✅ ZAPISZ TYLKO W BAZIE DANYCH - bez Spring Security InMemory
        AppUser savedUser = userRepository.save(newUser);
        System.out.println("Użytkownik zapisany w bazie danych z ID: " + savedUser.getId());
        System.out.println("Użytkownik " + username + " został pomyślnie zarejestrowany");
    }

    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public void changeUserRole(Long userId, UserRole newRole) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie został znaleziony"));

        // Nie pozwól na zmianę własnej roli na inną niż ADMIN
        String currentUsername = securityService.getCurrentUsername();
        if (user.getUsername().equals(currentUsername) && newRole != UserRole.ADMIN) {
            throw new RuntimeException("Nie możesz zmienić własnej roli administratora!");
        }

        user.setRole(newRole);
        userRepository.save(user);

        // Loguj zmianę
        System.out.println("Admin " + currentUsername + " zmienił rolę użytkownika " +
                user.getUsername() + " na " + newRole.getDescription());
    }

    public void toggleUserActive(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));

        user.setActive(!user.isActive());
        userRepository.save(user);
    }

    public Optional<AppUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public AppUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails ud) {
            return userRepository.findByUsername(ud.getUsername()).orElse(null);
        }
        return null;
    }

    public void updateUser(AppUser updatedUser) {
        if (!userRepository.existsById(updatedUser.getId())) {
            throw new RuntimeException("Użytkownik nie został znaleziony");
        }
        userRepository.save(updatedUser);
    }

    public String resetUserPassword(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie został znaleziony"));

        // Wygeneruj nowe hasło
        String newPassword = generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(newPassword);

        user.setPassword(encodedPassword);
        userRepository.save(user);

        // Loguj akcję
        String currentUsername = securityService.getCurrentUsername();
        System.out.println("Admin " + currentUsername + " zresetował hasło użytkownika " + user.getUsername());

        return newPassword;
    }

    public AppUser getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie został znaleziony"));
    }

    public void saveUser(AppUser user) {
        userRepository.save(user);
    }

    public List<AppUser> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    public List<AppUser> getActiveUsers() {
        return userRepository.findByActiveTrue();
    }

    public long getActiveUsersCount() {
        return userRepository.countByActiveTrue();
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }

    // ✅ USUŃ REFERENCJE DO userDetailsManager
    public void deleteUser(Long userId) {
        AppUser user = getUserById(userId);
        if (user != null) {
            // Sprawdź czy nie ma aktywnych rezerwacji
            String currentUsername = securityService.getCurrentUsername();
            System.out.println("Admin " + currentUsername + " usuwa użytkownika " + user.getUsername());

            // ✅ USUŃ TYLKO Z BAZY DANYCH
            userRepository.deleteById(userId);
        }
    }

    public List<AppUser> searchUsers(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllUsers();
        }

        return userRepository.findByUsernameContainingOrEmailContainingOrFirstNameContainingOrLastNameContaining(
                searchTerm, searchTerm, searchTerm, searchTerm);
    }

    public Map<String, Object> getUserStats(Long userId) {
        AppUser user = getUserById(userId);
        if (user == null) return new HashMap<>();

        Map<String, Object> stats = new HashMap<>();
        // Podstawowe statystyki
        stats.put("userId", userId);
        stats.put("username", user.getUsername());
        stats.put("role", user.getRole().getDescription());
        stats.put("active", user.isActive());
        stats.put("email", user.getEmail());
        stats.put("createdAt", user.getCreatedAt());

        // Te statystyki będą implementowane z ReservationService
        stats.put("totalReservations", 0);
        stats.put("completedReservations", 0);
        stats.put("cancelledReservations", 0);
        stats.put("totalSpent", BigDecimal.ZERO);

        return stats;
    }

    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    public AppUser getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}