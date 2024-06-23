package com.shiva.rental_service.services;

import com.shiva.rental_service.entities.Cart;
import com.shiva.rental_service.entities.RentalAgreement;
import com.shiva.rental_service.entities.Tool;
import com.shiva.rental_service.exception.InvalidDateRangeException;
import com.shiva.rental_service.exception.InvalidDiscountException;
import com.shiva.rental_service.exception.NoChargeableDaysException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class CheckoutService {

    private final HolidayService holidayService;

    public CheckoutService(Map<String, Tool> toolInventory, HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    public RentalAgreement checkout(Cart cart, int discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new InvalidDiscountException("Discount percent must be between 0 and 100.");
        }

        if (cart.getEndDate().isBefore(cart.getStartDate())) {
            throw new InvalidDateRangeException("End date cannot be before start date.");
        }

        int rentalDays = (int) (cart.getStartDate().until(cart.getEndDate(), ChronoUnit.DAYS) + 1);
        double totalPreDiscountCharge = 0;
        int totalChargeDays = 0;

        for (Map.Entry<Tool, Integer> entry : cart.getToolQuantities().entrySet()) {
            Tool tool = entry.getKey();
            int quantity = entry.getValue();

            int chargeDays = calculateChargeDays(cart.getStartDate(), cart.getEndDate(), tool);
            double preDiscountCharge = chargeDays * tool.getDailyCharge() * quantity;

            totalChargeDays += chargeDays;
            totalPreDiscountCharge += preDiscountCharge;
        }

        if (totalChargeDays == 0) {
            throw new NoChargeableDaysException("No chargeable days after excluding holidays and weekends.");
        }

        double discountAmount = totalPreDiscountCharge * discountPercent / 100;
        double finalCharge = totalPreDiscountCharge - discountAmount;

        Tool firstTool = cart.getToolQuantities().keySet().iterator().next();
        RentalAgreement agreement = new RentalAgreement();
        agreement.setToolCode(firstTool.getCode());
        agreement.setToolType(firstTool.getType());
        agreement.setToolBrand(firstTool.getBrand());
        agreement.setRentalDays(rentalDays);
        agreement.setCheckoutDate(cart.getStartDate());
        agreement.setDueDate(cart.getEndDate());
        agreement.setDailyRentalCharge(firstTool.getDailyCharge());
        agreement.setChargeDays(totalChargeDays);
        agreement.setPreDiscountCharge(totalPreDiscountCharge);
        agreement.setDiscountPercent(discountPercent);
        agreement.setDiscountAmount(discountAmount);
        agreement.setFinalCharge(finalCharge);

        return agreement;
    }

    private int calculateChargeDays(LocalDate startDate, LocalDate endDate, Tool tool) {
        int chargeDays = 0;
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            boolean isHoliday = holidayService.isHoliday(date);
            if ((dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) || tool.isChargeOnWeekends()) {
                if (!isHoliday || tool.isChargeOnHolidays()) {
                    chargeDays++;
                }
            }
        }
        return chargeDays;
    }
}
