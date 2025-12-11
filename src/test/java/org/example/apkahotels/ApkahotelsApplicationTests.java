package org.example.apkahotels;

import org.example.apkahotels.models.Hotel;
import org.example.apkahotels.repositories.HotelRepository;
import org.example.apkahotels.services.HotelService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class ApkahotelsApplicationTests {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    @Test
    void shouldReturnAllHotels() {
        // Given
        List<Hotel> hotels = Arrays.asList(new Hotel(), new Hotel());
        when(hotelRepository.findAll()).thenReturn(hotels);

        // When
        List<Hotel> result = hotelService.getAllHotels();

        // Then
        assertEquals(2, result.size());
    }

}
