
package org.example.apkahotels.services;

import org.example.apkahotels.models.Hotel;
import org.example.apkahotels.models.Room;
import org.example.apkahotels.repositories.HotelRepository;
import org.example.apkahotels.repositories.RoomRepository;
import org.example.apkahotels.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    // ✅ UPROSZCZONE WYSZUKIWANIE - BEZ FILTROWANIA PO CENIE
    public List<Hotel> searchHotels(SearchCriteria criteria) {
        List<Hotel> hotels;

        // Podstawowe wyszukiwanie
        if (criteria.getKeyword() != null && !criteria.getKeyword().trim().isEmpty()) {
            hotels = hotelRepository.searchByKeyword(criteria.getKeyword());
        } else if (criteria.getLocation() != null && !criteria.getLocation().trim().isEmpty()) {
            hotels = hotelRepository.findByLocationContainingIgnoreCase(criteria.getLocation());
        } else {
            hotels = hotelRepository.findAll();
        }

        // ✅ TYLKO SORTOWANIE PO NAZWIE
        return sortHotels(hotels, criteria.getSortBy());
    }

    // ✅ AUTOCOMPLETE
    public List<String> getLocationSuggestions(String query) {
        if (query == null || query.trim().length() < 2) {
            return Collections.emptyList();
        }

        return hotelRepository.findAll().stream()
                .map(Hotel::getLocation)
                .filter(location -> location.toLowerCase().contains(query.toLowerCase()))
                .distinct()
                .limit(5)
                .collect(Collectors.toList());
    }

    // ✅ NAJLEPSZE OFERTY
    public List<Hotel> getBestDeals() {
        return hotelRepository.findAll().stream()
                .limit(6)
                .collect(Collectors.toList());
    }

    // ✅ POPULARNE DESTYNACJE
    public Map<String, Long> getPopularDestinations() {
        return hotelRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Hotel::getLocation,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(8)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    // ✅ UPROSZCZONE SORTOWANIE - TYLKO PO NAZWIE
    private List<Hotel> sortHotels(List<Hotel> hotels, String sortBy) {
        if (sortBy == null || sortBy.isEmpty() || !"name".equals(sortBy)) {
            return hotels;
        }

        return hotels.stream()
                .sorted(Comparator.comparing(Hotel::getName))
                .collect(Collectors.toList());
    }

    // ✅ KLASA CRITERIA - BEZ CEN
    public static class SearchCriteria {
        private String keyword;
        private String location;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private Double minRating;
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
        private String sortBy;

        public SearchCriteria() {}

        // Getters i Setters
        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public LocalDate getCheckIn() {
            return checkIn;
        }

        public void setCheckIn(LocalDate checkIn) {
            this.checkIn = checkIn;
        }

        public LocalDate getCheckOut() {
            return checkOut;
        }

        public void setCheckOut(LocalDate checkOut) {
            this.checkOut = checkOut;
        }

        public Double getMinRating() {
            return minRating;
        }

        public void setMinRating(Double minRating) {
            this.minRating = minRating;
        }

        public BigDecimal getMinPrice() {
            return minPrice;
        }

        public void setMinPrice(BigDecimal minPrice) {
            this.minPrice = minPrice;
        }

        public BigDecimal getMaxPrice() {
            return maxPrice;
        }

        public void setMaxPrice(BigDecimal maxPrice) {
            this.maxPrice = maxPrice;
        }

        public String getSortBy() {
            return sortBy;
        }

        public void setSortBy(String sortBy) {
            this.sortBy = sortBy;
        }
    }
}