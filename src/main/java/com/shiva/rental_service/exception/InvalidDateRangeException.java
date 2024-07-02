package com.shiva.rental_service.exception;

/**
 * Custom exception class to represent an invalid date range.
 * Extends the RuntimeException class.
 */
public class InvalidDateRangeException extends RuntimeException {
    public InvalidDateRangeException(String message) {
        super(message);
    }
}
