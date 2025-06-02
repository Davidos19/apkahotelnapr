
package org.example.apkahotels.services;

import org.example.apkahotels.models.Hotel;
import org.example.apkahotels.models.Reservation;
import org.example.apkahotels.models.Room;
import org.example.apkahotels.repositories.HotelRepository;
import org.example.apkahotels.repositories.ReservationRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
@Service
@Transactional
public class HotelService {

    private final HotelRepository hotelRepository;
    private final RoomService roomService;
    // USUŃ ReservationRepository z konstruktora - może powodować cykliczne dependency

    public HotelService(HotelRepository hotelRepository, RoomService roomService) {
        this.hotelRepository = hotelRepository;
        this.roomService = roomService;
    }

    // ===== PODSTAWOWE METODY =====
    @Cacheable("hotels")
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Cacheable(value = "hotels", key = "#id")
    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id).orElse(null);
    }

    @CacheEvict(value = "hotels", allEntries = true)
    public Hotel saveHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }

    public List<Hotel> searchHotels(String keyword) {
        return hotelRepository.searchByKeyword(keyword);
    }

    public long getTotalHotels() {
        return hotelRepository.count();
    }

    public List<Hotel> getAllHotelsSorted(String sort) {
        List<Hotel> hotels = hotelRepository.findAll();

        if (sort != null) {
            switch (sort) {
                case "name":
                    hotels.sort(Comparator.comparing(Hotel::getName));
                    break;
                case "city":
                    hotels.sort(Comparator.comparing(Hotel::getCity));
                    break;
                case "stars":
                    hotels.sort(Comparator.comparing(Hotel::getStars, Comparator.nullsLast(Comparator.naturalOrder())));
                    break;
                case "created":
                    hotels.sort(Comparator.comparing(Hotel::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())));
                    break;
                default:
                    hotels.sort(Comparator.comparing(Hotel::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())));
                    break;
            }
        }

        return hotels;
    }

    // UŻYJ RoomService do obliczania dostępności
    public int getAvailableRoomsCount(Long hotelId, LocalDate checkIn, LocalDate checkOut) {
        return roomService.getAvailableRoomsCount(hotelId, checkIn, checkOut);
    }

    public List<Hotel> getTopRatedHotels(int limit) {
        return hotelRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Hotel::getStars, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(limit)
                .toList();
    }

    public List<Hotel> getAllHotelsWithAvailability(LocalDate checkIn, LocalDate checkOut) {
        List<Hotel> hotels = getAllHotels();

        for (Hotel hotel : hotels) {
            int availableRooms = roomService.getAvailableRoomsCount(hotel.getId(), checkIn, checkOut);
            hotel.setAvailableRooms(availableRooms);
        }

        return hotels;
    }

    public Hotel getHotelWithAvailability(Long hotelId, LocalDate checkIn, LocalDate checkOut) {
        Hotel hotel = hotelRepository.findById(hotelId).orElse(null);
        if (hotel != null) {
            int availableCount = roomService.getAvailableRoomsCount(hotelId, checkIn, checkOut);
            hotel.setAvailableRooms(availableCount);
        }
        return hotel;
    }

    public List<Hotel> searchHotelsWithAvailability(String keyword, LocalDate checkIn, LocalDate checkOut) {
        List<Hotel> hotels = hotelRepository.searchByKeyword(keyword);
        Map<Long, Integer> availabilityMap = roomService.getAvailableRoomsCountByHotel(checkIn, checkOut);

        hotels.forEach(hotel -> {
            Integer available = availabilityMap.get(hotel.getId());
            hotel.setAvailableRooms(available != null ? available : 0);
        });

        return hotels;
    }
}