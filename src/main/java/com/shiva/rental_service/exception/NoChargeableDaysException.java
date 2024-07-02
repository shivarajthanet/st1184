package com.shiva.rental_service.exception;

/**
 * Custom exception class to represent an exception when there are no chargeable days after excluding holidays and weekends.
 */
public class NoChargeableDaysException extends RuntimeException {
    public NoChargeableDaysException(String message) {
        super(message);
    }
}
