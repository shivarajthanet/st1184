package com.shiva.rental_service.entities;

import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Getter
@AllArgsConstructor
public class Cart {
    private Map<Tool, Integer> toolQuantities; // Tool and its quantity
    private LocalDate startDate;
    private LocalDate endDate;


    // New constructor
    public Cart(Map<Tool, Integer> toolQuantities, int totalDays, LocalDate startDate) {
        this.toolQuantities = toolQuantities;
        this.startDate = startDate;
        this.endDate = startDate.plusDays(totalDays - 1);
    }
}
