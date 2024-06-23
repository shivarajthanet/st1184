package com.shiva.rental_service.exception;

public class NoChargeableDaysException extends RuntimeException {
    public NoChargeableDaysException(String message) {
        super(message);
    }
}
