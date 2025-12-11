package org.example.apkahotels.repositories;

import org.example.apkahotels.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    // ✅ POPRAWIONE WYSZUKIWANIE - UŻYWA city ZAMIAST location
    @Query("SELECT h FROM Hotel h WHERE " +
            "LOWER(h.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(h.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(h.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Hotel> searchByKeyword(@Param("keyword") String keyword);

    // ✅ POPRAWIONE WYSZUKIWANIE PO LOKALIZACJI - UŻYWA city
    @Query("SELECT h FROM Hotel h WHERE LOWER(h.city) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<Hotel> findByLocationContainingIgnoreCase(@Param("location") String location);

    // ✅ NAJPOPULARNIEJSZE HOTELE
    @Query("SELECT h FROM Hotel h ORDER BY h.name")
    List<Hotel> findTopRatedHotels();
}