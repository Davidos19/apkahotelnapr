package org.example.apkahotels.services;

import org.example.apkahotels.models.Review;
import org.example.apkahotels.repositories.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }
    public void addReview(Review review) {
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }

    // ===== PODSTAWOWE METODY =====
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public List<Review> getReviewsByHotelId(Long hotelId) {
        return reviewRepository.findByHotelIdOrderByCreatedAtDesc(hotelId);
    }

    // DODAJ TĘ METODĘ (alias dla getReviewsByHotelId)
    @Cacheable(value = "reviews", key = "#hotelId")
    public List<Review> getReviewsForHotel(Long hotelId) {
        return reviewRepository.findByHotelIdOrderByCreatedAtDesc(hotelId);
    }


    public List<Review> getReviewsByUsername(String username) {
        return reviewRepository.findByUsernameOrderByCreatedAtDesc(username);
    }

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    // ===== METODY STATYSTYCZNE =====
    public Double getAverageRating(Long hotelId) {
        return reviewRepository.getAverageRatingForHotel(hotelId);
    }


    public Long getReviewCount(Long hotelId) {
        return reviewRepository.getReviewCountForHotel(hotelId);
    }

    public List<Object[]> getRatingDistribution(Long hotelId) {
        return reviewRepository.getRatingDistribution(hotelId);
    }

    public long getTotalReviews() {
        return reviewRepository.count();
    }
}