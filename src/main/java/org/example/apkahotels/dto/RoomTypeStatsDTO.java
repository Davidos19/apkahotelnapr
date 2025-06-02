package org.example.apkahotels.dto;

public class RoomTypeStatsDTO {
    private String roomType;
    private Integer capacity;
    private Long totalRooms;
    private Double minPrice;
    private Double maxPrice;
    private Double avgPrice;
    private Integer availableRooms;

    // Konstruktory
    public RoomTypeStatsDTO() {}

    public RoomTypeStatsDTO(String roomType, Integer capacity, Long totalRooms,
                            Double minPrice, Double maxPrice, Double avgPrice) {
        this.roomType = roomType;
        this.capacity = capacity;
        this.totalRooms = totalRooms;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.avgPrice = avgPrice;
    }

    // Gettery i Settery
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Long getTotalRooms() { return totalRooms; }
    public void setTotalRooms(Long totalRooms) { this.totalRooms = totalRooms; }

    public Double getMinPrice() { return minPrice; }
    public void setMinPrice(Double minPrice) { this.minPrice = minPrice; }

    public Double getMaxPrice() { return maxPrice; }
    public void setMaxPrice(Double maxPrice) { this.maxPrice = maxPrice; }

    public Double getAvgPrice() { return avgPrice; }
    public void setAvgPrice(Double avgPrice) { this.avgPrice = avgPrice; }

    public Integer getAvailableRooms() { return availableRooms; }
    public void setAvailableRooms(Integer availableRooms) { this.availableRooms = availableRooms; }

    public String getDisplayName() {
        return roomType + " (" + capacity + " os.)";
    }

    public boolean isAvailable() {
        return availableRooms != null && availableRooms > 0;
    }
}