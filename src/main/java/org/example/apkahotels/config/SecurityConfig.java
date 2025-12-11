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
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/hotels/**", "/hotel/**",
                                "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                        .requestMatchers("/h2-console/**").permitAll() // H2 Console
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/profile/**", "/reservations/**", "/my-reservations/**").authenticated()
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .successHandler(successHandler)
                        .failureUrl("/login?error")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                .sessionManagement(session -> session
                        .maximumSessions(3)
                        .maxSessionsPreventsLogin(false)
                        .sessionRegistry(sessionRegistry())
                )

                // ✅ POPRAWKA DLA H2-CONSOLE
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions
                                .sameOrigin()) // ✅ ZMIANA: deny() -> sameOrigin()
                        .contentTypeOptions(contentType -> {})
                        .httpStrictTransportSecurity(hsts -> hsts
                                .maxAgeInSeconds(31536000)
                        )
                )

                // ✅ WYŁĄCZ CSRF DLA H2-CONSOLE
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                )

                .rememberMe(remember -> remember
                        .key("mySecretKey")
                        .tokenValiditySeconds(86400)
                );

        return http.build();
    }

    @Bean
    public org.springframework.security.core.session.SessionRegistry sessionRegistry() {
        return new org.springframework.security.core.session.SessionRegistryImpl();
    }
}