package org.example.apkahotels.services;

import org.example.apkahotels.dto.RoomTypeStatsDTO;
import org.example.apkahotels.models.Hotel;
import org.example.apkahotels.models.Room;
import org.example.apkahotels.models.Reservation;
import org.example.apkahotels.repositories.HotelRepository;
import org.example.apkahotels.repositories.RoomRepository;
import org.example.apkahotels.repositories.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Transactional

@Service
public class RoomService {
    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final HotelRepository hotelRepository;

    public RoomService(RoomRepository roomRepository,
                       ReservationRepository reservationRepository,
                       HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.hotelRepository = hotelRepository;
    }

    // Wszystkie istniejące metody...
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    public List<Room> getRoomsByHotelId(Long hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }

    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    public Room addRoom(Room room) {
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    public long getTotalRooms() {
        return roomRepository.count();
    }

    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<Reservation> conflictingReservations = reservationRepository.findConflictingReservations(
                roomId, checkIn, checkOut);
        return conflictingReservations.isEmpty();
    }

    public int getAvailableRoomsCount(Long hotelId, LocalDate checkIn, LocalDate checkOut) {
        List<Room> allRooms = getRoomsByHotelId(hotelId);
        int count = 0;

        for (Room room : allRooms) {
            if (isRoomAvailable(room.getId(), checkIn, checkOut)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Efektywnie pobiera dostępność dla wszystkich hoteli jednym zapytaniem
     */
    public Map<Long, Integer> getAvailableRoomsCountByHotel(LocalDate checkIn, LocalDate checkOut) {
        List<Object[]> results = roomRepository.countAvailableRoomsByHotelAndDates(checkIn, checkOut);

        return results.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],        // hotelId
                        row -> ((Long) row[1]).intValue() // count
                ));
    }

    /**
     * Optymalizowana metoda dla typów pokoi z dostępnością
     */
    public List<RoomTypeStatsDTO> getRoomTypesWithAvailabilityOptimized(Long hotelId, LocalDate checkIn, LocalDate checkOut) {
        // Pobierz statystyki pokoi jednym zapytaniem
        List<Object[]> roomStats = roomRepository.getRoomTypeStatistics(hotelId);

        // Pobierz dostępne pokoje jednym zapytaniem
        List<Room> availableRooms = roomRepository.findAvailableRoomsByHotelAndDates(hotelId, checkIn, checkOut);

        // Grupuj dostępne pokoje według typu
        Map<String, Long> availableCountsByType = availableRooms.stream()
                .collect(Collectors.groupingBy(
                        room -> room.getRoomType() + "_" + room.getCapacity(),
                        Collectors.counting()
                ));

        // Konwertuj na DTO
        return roomStats.stream()
                .map(row -> {
                    String roomType = (String) row[0];
                    Integer capacity = (Integer) row[1];
                    Long totalRooms = (Long) row[2];
                    Double minPrice = (Double) row[3];
                    Double maxPrice = (Double) row[4];
                    Double avgPrice = (Double) row[5];

                    String key = roomType + "_" + capacity;
                    Integer availableCount = availableCountsByType.getOrDefault(key, 0L).intValue();

                    RoomTypeStatsDTO dto = new RoomTypeStatsDTO(roomType, capacity, totalRooms, minPrice, maxPrice, avgPrice);
                    dto.setAvailableRooms(availableCount);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Efektywny przydział pokoju - używa zapytania z LIMIT 1
     */
    @Transactional // Ta metoda modyfikuje dane
    public Room assignAvailableRoomOfTypeOptimized(Long hotelId, String roomType, int capacity, LocalDate checkIn, LocalDate checkOut) {
        List<Room> availableRooms = roomRepository.findFirstAvailableRoomOfType(hotelId, roomType, capacity, checkIn, checkOut);

        if (availableRooms.isEmpty()) {
            throw new RuntimeException("Brak dostępnych pokoi typu " + roomType + " (" + capacity + " os.)");
        }

        Room assignedRoom = availableRooms.get(0);
        logger.info("Przydzielono pokój {} ({}) typu {} dla hotelu {}",
                assignedRoom.getRoomNumber(), assignedRoom.getId(), roomType, hotelId);

        return assignedRoom;
    }

    @Cacheable(value = "roomStats", key = "#hotelId")
    public List<RoomTypeStatsDTO> getCachedRoomTypeStats(Long hotelId) {
        List<Object[]> roomStats = roomRepository.getRoomTypeStatistics(hotelId);

        return roomStats.stream()
                .map(row -> new RoomTypeStatsDTO(
                        (String) row[0],    // roomType
                        (Integer) row[1],   // capacity
                        (Long) row[2],      // totalRooms
                        (Double) row[3],    // minPrice
                        (Double) row[4],    // maxPrice
                        (Double) row[5]     // avgPrice
                ))
                .collect(Collectors.toList());
    }


    // Metody dla typów pokoi
    public List<RoomTypeAvailability> getRoomTypesWithAvailability(Long hotelId, LocalDate checkIn, LocalDate checkOut) {
        List<Room> allRooms = getRoomsByHotelId(hotelId);

        Map<String, List<Room>> roomsByTypeAndCapacity = allRooms.stream()
                .collect(Collectors.groupingBy(room ->
                        room.getRoomType() + "_" + room.getCapacity()));

        List<RoomTypeAvailability> result = new ArrayList<>();

        for (Map.Entry<String, List<Room>> entry : roomsByTypeAndCapacity.entrySet()) {
            String[] parts = entry.getKey().split("_");
            String roomType = parts[0];
            int capacity = Integer.parseInt(parts[1]);
            List<Room> roomsOfType = entry.getValue();

            int availableCount = 0;
            Room sampleRoom = roomsOfType.get(0);

            for (Room room : roomsOfType) {
                if (isRoomAvailable(room.getId(), checkIn, checkOut)) {
                    availableCount++;
                }
            }

            result.add(new RoomTypeAvailability(
                    roomType,
                    capacity,
                    sampleRoom.getPrice(),
                    sampleRoom.getImageUrl(),
                    roomsOfType.size(),
                    availableCount
            ));
        }

        return result;
    }

    public Room assignAvailableRoomOfType(Long hotelId, String roomType, int capacity, LocalDate checkIn, LocalDate checkOut) {
        List<Room> roomsOfType = roomRepository.findByHotelIdAndRoomTypeAndCapacity(hotelId, roomType, capacity);

        for (Room room : roomsOfType) {
            if (isRoomAvailable(room.getId(), checkIn, checkOut)) {
                return room;
            }
        }

        throw new RuntimeException("Brak dostępnych pokoi typu " + roomType + " (" + capacity + " os.)");
    }

    // Pozostałe metody...
    public List<Room> getAvailableRoomsByTypeAndDates(Long hotelId, String roomType,
                                                      Integer capacity, LocalDate checkIn, LocalDate checkOut) {
        List<Room> roomsOfType = roomRepository.findByHotelIdAndRoomTypeAndCapacity(hotelId, roomType, capacity);

        List<Room> availableRooms = new ArrayList<>();

        for (Room room : roomsOfType) {
            if (isRoomAvailable(room.getId(), checkIn, checkOut)) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    public Map<String, List<Room>> getRoomsGroupedByType(Long hotelId) {
        List<Room> allRooms = roomRepository.findByHotelId(hotelId);
        return allRooms.stream()
                .collect(Collectors.groupingBy(room ->
                        room.getRoomType() + " (" + room.getCapacity() + " os.)"));
    }

    // Klasa pomocnicza
    public static class RoomTypeAvailability {
        private final String roomType;
        private final int capacity;
        private final double price;
        private final String imageUrl;
        private final int totalRooms;
        private final int availableCount;

        public RoomTypeAvailability(String roomType, int capacity, double price, String imageUrl, int totalRooms, int availableCount) {
            this.roomType = roomType;
            this.capacity = capacity;
            this.price = price;
            this.imageUrl = imageUrl;
            this.totalRooms = totalRooms;
            this.availableCount = availableCount;
        }

        public String getRoomType() { return roomType; }
        public int getCapacity() { return capacity; }
        public double getPrice() { return price; }
        public String getImageUrl() { return imageUrl; }
        public int getTotalRooms() { return totalRooms; }
        public int getAvailableCount() { return availableCount; }
        public boolean isAvailable() { return availableCount > 0; }

        public String getDisplayName() {
            return roomType + " (" + capacity + " os.)";
        }
    }
}