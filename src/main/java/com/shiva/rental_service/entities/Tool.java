package com.shiva.rental_service.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

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
