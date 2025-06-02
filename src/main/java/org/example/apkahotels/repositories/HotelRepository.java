package org.example.apkahotels.repositories;

import org.example.apkahotels.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByNameContainingIgnoreCase(String name);

    List<Hotel> findByCityContainingIgnoreCase(String city);

    @Query("SELECT h FROM Hotel h WHERE " +
            "LOWER(h.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(h.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(h.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Hotel> searchByKeyword(@Param("keyword") String keyword);

    List<Hotel> findByStarsGreaterThanEqual(String stars);
}