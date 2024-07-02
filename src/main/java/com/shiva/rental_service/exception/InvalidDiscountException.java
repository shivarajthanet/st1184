package com.shiva.rental_service.exception;

/**
 * Custom exception class to represent an invalid discount value.
 * Extends the RuntimeException class.
 */
public class InvalidDiscountException extends RuntimeException {
    public InvalidDiscountException(String message) {
        super(message);
    }
}
