package org.example.apkahotels.repositories;

import org.example.apkahotels.models.AppUser;
import org.example.apkahotels.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<AppUser> findByRole(UserRole role);
    List<AppUser> findByActiveTrue();
    List<AppUser> findByActiveFalse();
    long countByActiveTrue();
    long countByActiveFalse();
    long countByRole(UserRole role);

    @Query("SELECT u FROM AppUser u WHERE u.username LIKE %:query% OR u.email LIKE %:query% OR u.firstName LIKE %:query% OR u.lastName LIKE %:query%")
    List<AppUser> findByUsernameContainingOrEmailContainingOrFirstNameContainingOrLastNameContaining(
            @Param("query") String username,
            @Param("query") String email,
            @Param("query") String firstName,
            @Param("query") String lastName);

    @Query("SELECT u FROM AppUser u WHERE u.lastLogin < :cutoffDate")
    List<AppUser> findInactiveUsers(@Param("cutoffDate") LocalDateTime cutoffDate);

    @Query("SELECT u FROM AppUser u ORDER BY u.createdAt DESC")
    List<AppUser> findAllOrderByCreatedAtDesc();


    @Query("SELECT COUNT(u) FROM AppUser u")
    long countAllUsers();
}