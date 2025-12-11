package org.example.apkahotels.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // ✅ Silniejsze hashowanie
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ✅ PODSTAWOWE UPRAWNIENIA
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/hotels/**", "/hotel/**",
                                "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                        .requestMatchers("/h2-console/**").permitAll() // Tylko dev

                        // ✅ ADMIN ROUTES
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // ✅ USER ROUTES
                        .requestMatchers("/profile/**", "/reservations/**", "/my-reservations/**").authenticated()

                        .anyRequest().authenticated()
                )

                // ✅ LOGIN FORM
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .successHandler(successHandler)
                        .failureUrl("/login?error")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()
                )

                // ✅ LOGOUT
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                // ✅ SESSION MANAGEMENT
                .sessionManagement(session -> session
                        .maximumSessions(3)
                        .maxSessionsPreventsLogin(false)
                        .sessionRegistry(sessionRegistry())
                )

                // ✅ TYLKO PODSTAWOWE HEADERS (bez problematycznych)
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.deny()) // Zapobiega Clickjacking
                        .contentTypeOptions(contentType -> {}) // Podstawowa ochrona
                        .httpStrictTransportSecurity(hsts -> hsts
                                .maxAgeInSeconds(31536000) // HTTPS tylko
                        )
                )

                // ✅ CSRF PROTECTION
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )

                // ✅ REMEMBER ME
                .rememberMe(remember -> remember
                        .key("mySecretKey")
                        .tokenValiditySeconds(86400) // 24h
                );

        return http.build();
    }

    // ✅ SESSION REGISTRY
    @Bean
    public org.springframework.security.core.session.SessionRegistry sessionRegistry() {
        return new org.springframework.security.core.session.SessionRegistryImpl();
    }
}