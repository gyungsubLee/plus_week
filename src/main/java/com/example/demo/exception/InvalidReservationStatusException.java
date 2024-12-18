package com.example.demo.exception;

public class InvalidReservationStatusException extends RuntimeException {

    public InvalidReservationStatusException(String message) {
        super(message);
    }
}
