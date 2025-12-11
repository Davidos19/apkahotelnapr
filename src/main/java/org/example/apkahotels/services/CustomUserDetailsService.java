package org.example.apkahotels.services;

import org.example.apkahotels.models.AppUser;
import org.example.apkahotels.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("🔍 SZUKAM UŻYTKOWNIKA: " + username);
        System.out.println("📊 ILOŚĆ UŻYTKOWNIKÓW W BAZIE: " + userRepository.count());

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("❌ UŻYTKOWNIK NIE ZNALEZIONY: " + username);
                    System.out.println("📋 DOSTĘPNI UŻYTKOWNICY:");
                    userRepository.findAll().forEach(u ->
                            System.out.println("   - " + u.getUsername())
                    );
                    return new UsernameNotFoundException("Użytkownik nie znaleziony: " + username);
                });

        System.out.println("✅ ZNALAZŁEM UŻYTKOWNIKA: " + user.getUsername());
        System.out.println("🔐 HASH HASŁA: " + user.getPassword().substring(0, 20) + "...");

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .disabled(!user.isActive())
                .build();
    }


}