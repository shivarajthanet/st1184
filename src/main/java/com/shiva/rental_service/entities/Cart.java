package com.shiva.rental_service.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
public class Cart {
    private Map<Tool, Integer> toolQuantities; // Tool and its quantity
    private LocalDate startDate;
    private LocalDate endDate;
}
