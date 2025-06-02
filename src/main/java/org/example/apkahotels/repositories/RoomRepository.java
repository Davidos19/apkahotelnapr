package org.example.apkahotels.repositories;

import org.example.apkahotels.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByHotelId(Long hotelId);
    List<Room> findByHotelIdAndCapacityGreaterThanEqual(Long hotelId, int capacity);
    List<Room> findByHotelIdAndPriceBetween(Long hotelId, double minPrice, double maxPrice);
    List<Room> findByHotelIdAndRoomTypeAndCapacity(Long hotelId, String roomType, Integer capacity);

    @Query("SELECT r FROM Room r WHERE r.id IN :roomIds")
    List<Room> findByIdIn(@Param("roomIds") Set<Long> roomIds);

    @Query("SELECT r.hotelId, COUNT(r) FROM Room r GROUP BY r.hotelId")
    List<Object[]> countRoomsByHotel();

    // ===== NOWE OPTYMALIZOWANE ZAPYTANIA =====

    /**
     * Zwraca pokoje które MOGĄ być dostępne (nie są zarezerwowane w podanych datach)
     */
    @Query("""
        SELECT r FROM Room r 
        WHERE r.hotelId = :hotelId 
        AND r.id NOT IN (
            SELECT res.roomId FROM Reservation res 
            WHERE res.status != 'CANCELLED' 
            AND (
                (res.checkIn <= :checkOut AND res.checkOut >= :checkIn)
            )
        )
        """)
    List<Room> findAvailableRoomsByHotelAndDates(@Param("hotelId") Long hotelId,
                                                 @Param("checkIn") LocalDate checkIn,
                                                 @Param("checkOut") LocalDate checkOut);

    /**
     * Zlicza dostępne pokoje dla wszystkich hoteli jednym zapytaniem
     */
    @Query("""
        SELECT r.hotelId, COUNT(r) FROM Room r 
        WHERE r.id NOT IN (
            SELECT res.roomId FROM Reservation res 
            WHERE res.status != 'CANCELLED' 
            AND (
                (res.checkIn <= :checkOut AND res.checkOut >= :checkIn)
            )
        )
        GROUP BY r.hotelId
        """)
    List<Object[]> countAvailableRoomsByHotelAndDates(@Param("checkIn") LocalDate checkIn,
                                                      @Param("checkOut") LocalDate checkOut);

    /**
     * Zwraca statystyki pokoi według typu dla hotelu
     */
    @Query("""
        SELECT r.roomType, r.capacity, COUNT(r), MIN(r.price), MAX(r.price), AVG(r.price)
        FROM Room r 
        WHERE r.hotelId = :hotelId 
        GROUP BY r.roomType, r.capacity
        ORDER BY r.capacity, r.roomType
        """)
    List<Object[]> getRoomTypeStatistics(@Param("hotelId") Long hotelId);

    /**
     * Znajduje pierwszy dostępny pokój danego typu
     */
    @Query("""
        SELECT r FROM Room r 
        WHERE r.hotelId = :hotelId 
        AND r.roomType = :roomType 
        AND r.capacity = :capacity 
        AND r.id NOT IN (
            SELECT res.roomId FROM Reservation res 
            WHERE res.status != 'CANCELLED' 
            AND (
                (res.checkIn <= :checkOut AND res.checkOut >= :checkIn)
            )
        )
        ORDER BY r.roomNumber
        """)
    List<Room> findFirstAvailableRoomOfType(@Param("hotelId") Long hotelId,
                                            @Param("roomType") String roomType,
                                            @Param("capacity") Integer capacity,
                                            @Param("checkIn") LocalDate checkIn,
                                            @Param("checkOut") LocalDate checkOut);
}