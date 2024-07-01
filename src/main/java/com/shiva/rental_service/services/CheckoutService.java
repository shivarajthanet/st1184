package com.shiva.rental_service.services;

import com.shiva.rental_service.entities.Cart;
import com.shiva.rental_service.entities.RentalAgreement;
import com.shiva.rental_service.entities.Tool;
import com.shiva.rental_service.exception.InvalidDateRangeException;
import com.shiva.rental_service.exception.InvalidDiscountException;
import com.shiva.rental_service.exception.NoChargeableDaysException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;



public class CheckoutService {

    private final HolidayService holidayService;

    public CheckoutService( HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    public RentalAgreement checkout(Cart cart, int discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new InvalidDiscountException("Discount percent must be between 0 and 100.");
        }

        if (cart == null || cart.getEndDate().isBefore(cart.getStartDate()) || cart.getToolQuantities() == null) {
            throw new InvalidDateRangeException("End date cannot be before start date or tool quantities are null.");
        }

        int rentalDays = (int) (cart.getStartDate().until(cart.getEndDate(), ChronoUnit.DAYS) + 1);
        BigDecimal totalPreDiscountCharge = BigDecimal.ZERO;
        int totalChargeDays = 0;

        for (Map.Entry<Tool, Integer> entry : cart.getToolQuantities().entrySet()) {
            Tool tool = entry.getKey();
            int quantity = entry.getValue();

            int chargeDays = calculateChargeDays(cart.getStartDate(), cart.getEndDate(), tool);
            BigDecimal preDiscountCharge = BigDecimal.valueOf(chargeDays)
                    .multiply(BigDecimal.valueOf(tool.getDailyCharge()))
                    .multiply(BigDecimal.valueOf(quantity));

            totalChargeDays += chargeDays;
            totalPreDiscountCharge = totalPreDiscountCharge.add(preDiscountCharge);
        }

        if (totalChargeDays == 0) {
            throw new NoChargeableDaysException("No chargeable days after excluding holidays and weekends.");
        }

        BigDecimal discountAmount = totalPreDiscountCharge.multiply(BigDecimal.valueOf(discountPercent))
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        BigDecimal finalCharge = totalPreDiscountCharge.subtract(discountAmount);

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
        agreement.setPreDiscountCharge(totalPreDiscountCharge.doubleValue());
        agreement.setDiscountPercent(discountPercent);
        agreement.setDiscountAmount(discountAmount.doubleValue());
        agreement.setFinalCharge(finalCharge.doubleValue());

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
