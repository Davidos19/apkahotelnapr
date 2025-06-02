package org.example.apkahotels.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidReservationDateException.class)
    public ResponseEntity<String> handleInvalidReservationDate(InvalidReservationDateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RoomNotAvailableException.class)
    public String handleRoomNotAvailable(RoomNotAvailableException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Pokój niedostępny: " + e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Użytkownik nie znaleziony");
        return "redirect:/login";
    }

    @ExceptionHandler(HotelNotAvailableException.class)
    public ResponseEntity<String> handleHotelNotAvailable(HotelNotAvailableException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Obsługa innych RuntimeException (opcjonalnie)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    public static class RoomNotAvailableException extends RuntimeException {
        public RoomNotAvailableException(String message) {
            super(message);
        }
    }
    public static class HotelNotFoundException extends RuntimeException {
        public HotelNotFoundException(String message) {
            super(message);
        }
    }
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

}