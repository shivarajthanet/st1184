package com.shiva.rental_service.entities;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
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
                "Tool code: %s\nTool type: %s\nTool brand: %s\nRental days: %d\nCheckout date: %s\nDue date: %s\n" +
                        "Daily rental charge: $%.2f\nCharge days: %d\nPre-discount charge: $%.2f\nDiscount percent: %d%%\n" +
                        "Discount amount: $%.2f\nFinal charge: $%.2f\n",
                toolCode, toolType, toolBrand, rentalDays, checkoutDate.format(dateFormatter), dueDate.format(dateFormatter),
                dailyRentalCharge, chargeDays, preDiscountCharge, discountPercent, discountAmount, finalCharge
        );
    }
}
