package org.example.apkahotels.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SecurityAuditService {

    private final List<String> auditLogs = new ArrayList<>();
    private final ConcurrentHashMap<String, Integer> loginAttempts = new ConcurrentHashMap<>();

    // ✅ PROSTY LOG
    public void logActivity(String action, String details) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anonymous";

        String logEntry = String.format("[%s] %s - %s: %s",
                LocalDateTime.now(), username, action, details);

        auditLogs.add(logEntry);
        System.out.println("🔐 AUDIT: " + logEntry);
    }

    // ✅ FAILED LOGINS
    public void recordFailedLogin(String username) {
        loginAttempts.merge(username, 1, Integer::sum);
        logActivity("FAILED_LOGIN", "Failed attempt for: " + username);
    }

    public void recordSuccessfulLogin(String username) {
        loginAttempts.remove(username);
        logActivity("SUCCESSFUL_LOGIN", "User logged in: " + username);
    }

    public boolean isAccountLocked(String username) {
        return loginAttempts.getOrDefault(username, 0) >= 5;
    }

    // ✅ GET LOGS
    public List<String> getRecentLogs(int limit) {
        return auditLogs.stream()
                .skip(Math.max(0, auditLogs.size() - limit))
                .toList();
    }
}