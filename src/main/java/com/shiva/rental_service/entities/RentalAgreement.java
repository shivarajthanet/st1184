package com.shiva.rental_service.entities;

import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class RentalAgreement {
    private String toolCode;
    private String toolType;
    private String toolBrand;
    private int rentalDays;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private double dailyRentalCharge;
    private int chargeDays;
    private double preDiscountCharge;
    private int discountPercent;
    private double discountAmount;
    private double finalCharge;

    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        return String.format(
                """
                        Tool code: %s
                        Tool type: %s
                        Tool brand: %s
                        Rental days: %d
                        Checkout date: %s
                        Due date: %s
                        Daily rental charge: $%.2f
                        Charge days: %d
                        Pre-discount charge: $%.2f
                        Discount percent: %d%%
                        Discount amount: $%.2f
                        Final charge: $%.2f
                        """,
                toolCode, toolType, toolBrand, rentalDays, checkoutDate.format(dateFormatter), dueDate.format(dateFormatter),
                dailyRentalCharge, chargeDays, preDiscountCharge, discountPercent, discountAmount, finalCharge
        );
    }
}
