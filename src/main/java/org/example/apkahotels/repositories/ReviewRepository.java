package org.example.apkahotels.repositories;

import org.example.apkahotels.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByHotelIdOrderByCreatedAtDesc(Long hotelId);

    List<Review> findByUsernameOrderByCreatedAtDesc(String username);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.hotelId = :hotelId")
    Double getAverageRatingForHotel(@Param("hotelId") Long hotelId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.hotelId = :hotelId")
    Long getReviewCountForHotel(@Param("hotelId") Long hotelId);

    @Query("SELECT r.rating, COUNT(r) FROM Review r WHERE r.hotelId = :hotelId GROUP BY r.rating ORDER BY r.rating DESC")
    List<Object[]> getRatingDistribution(@Param("hotelId") Long hotelId);
}