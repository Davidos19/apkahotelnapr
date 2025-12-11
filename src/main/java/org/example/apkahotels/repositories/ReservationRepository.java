
package org.example.apkahotels.repositories;

import org.example.apkahotels.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // ===== NAPRAWIONE ZAPYTANIE =====
    @Query("SELECT r FROM Reservation r WHERE r.roomId = :roomId AND " +
            "((r.checkIn <= :checkOut AND r.checkOut >= :checkIn))")
    List<Reservation> findConflictingReservations(@Param("roomId") Long roomId,
                                                  @Param("checkIn") LocalDate checkIn,
                                                  @Param("checkOut") LocalDate checkOut);

    // Podstawowe zapytania
    List<Reservation> findByUsernameOrderByCheckInDesc(String username);

    List<Reservation> findByHotelIdOrderByCheckInDesc(Long hotelId);

    List<Reservation> findByCheckInBetween(LocalDate startDate, LocalDate endDate);

    // ===== DODAJ TĘ METODĘ =====
    @Query("SELECT r FROM Reservation r WHERE r.hotelId = :hotelId AND " +
            "r.checkIn <= :endDate AND r.checkOut >= :startDate")
    List<Reservation> findReservationsInDateRange(@Param("hotelId") Long hotelId,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);

    // Zapytania pomocnicze dla administratora
    @Query("SELECT r FROM Reservation r WHERE r.checkIn >= :date ORDER BY r.checkIn ASC")
    List<Reservation> findUpcomingReservations(@Param("date") LocalDate date);

    @Query("SELECT r FROM Reservation r WHERE r.checkIn = :date")
    List<Reservation> findReservationsByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.hotelId = :hotelId")
    long countReservationsByHotelId(@Param("hotelId") Long hotelId);
    @Query("SELECT r FROM Reservation r WHERE r.roomId = :roomId " +
            "AND r.checkIn < :checkOut AND r.checkOut > :checkIn")
    List<Reservation> findByRoomIdAndCheckInBeforeAndCheckOutAfter(
            @Param("roomId") Long roomId,
            @Param("checkOut") LocalDate checkOut,
            @Param("checkIn") LocalDate checkIn
    );

}