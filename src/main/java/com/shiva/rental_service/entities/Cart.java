package com.shiva.rental_service.entities;

import com.shiva.rental_service.exception.InvalidDataException;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

/**
 * Represents a cart containing tools and their quantities for rental.
 * <p>
 * Attributes:
 * - toolQuantities: Map<Tool, Integer>, a mapping of Tool objects to their respective quantities
 * - startDate: LocalDate, the start date of the rental period
 * - endDate: LocalDate, the end date of the rental period
 * <p>
 * Constructors:
 * - Cart(Map<Tool, Integer> toolQuantities, LocalDate startDate, LocalDate endDate): Initializes the Cart with the given tool quantities, start date, and end date.
 */
@Getter
@AllArgsConstructor
public class Cart {

    private Map<Tool, Integer> toolQuantities; // Tool and its quantity

    private LocalDate startDate;

    private LocalDate endDate;

    /**
     * Initializes a Cart object with the given tool quantities, total days, and start date.
     *
     * @param toolQuantities a mapping of Tool objects to their respective quantities
     * @param totalDays      the total number of days for the rental period
     * @param startDate      the start date of the rental period
     */
    public Cart(Map<Tool, Integer> toolQuantities, LocalDate startDate,int totalDays) {

        if (totalDays < 1) {
            throw new InvalidDataException("Total days cannot be less than 1");
        }

        this.toolQuantities = toolQuantities;
        this.startDate = startDate;
        this.endDate = startDate.plusDays(totalDays - 1);
    }
}
