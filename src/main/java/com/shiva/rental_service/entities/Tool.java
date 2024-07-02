package com.shiva.rental_service.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a tool that can be rented out.
 * Attributes:
 * - code: String, the unique code of the tool
 * - type: String, the type of the tool
 * - brand: String, the brand of the tool
 * - dailyCharge: double, the daily rental charge for the tool
 * - chargeOnWeekends: boolean, indicates if there is a charge for renting the tool on weekends
 * - chargeOnHolidays: boolean, indicates if there is a charge for renting the tool on holidays
 */
@Getter
@AllArgsConstructor
public class Tool {
    private String code;
    private String type;
    private String brand;
    private double dailyCharge;
    private boolean chargeOnWeekends;
    private boolean chargeOnHolidays;
}
