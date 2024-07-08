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

/**
 * Represents a service for checking out tools and generating rental agreements.
 * The service calculates the total charge for renting tools based on the provided cart and discount percentage.
 * It ensures that the discount percentage is valid, the cart has a valid date range and tool quantities,
 * and calculates the chargeable days for each tool considering weekends and holidays.
 * The service then generates a rental agreement with details such as tool code, type, brand, rental days,
 * checkout date, due date, daily rental charge, charge days, pre-discount charge, discount percent, discount amount, and final charge.
 */
public class CheckoutService {

    private final HolidayService holidayService;

    /**
     * Constructs a new CheckoutService with the provided HolidayService.
     *
     * @param holidayService the HolidayService to be used for checking holidays.
     */
    public CheckoutService(HolidayService holidayService) {
        this.holidayService = holidayService;
    }


    /**
     * Generates a rental agreement based on the provided cart and discount percentage.
     *
     * @param cart            the cart containing tools and rental dates
     * @param discountPercent the discount percentage to be applied
     * @return the generated rental agreement
     */
    public RentalAgreement checkout(Cart cart, int discountPercent) {
        // Validate the discount percentage
        if (discountPercent < 0 || discountPercent > 100) {
            throw new InvalidDiscountException("Discount percent must be between 0 and 100.");
        }

        // Validate the cart and its contents
        if (cart == null || cart.getEndDate().isBefore(cart.getStartDate()) || cart.getToolQuantities() == null) {
            throw new InvalidDateRangeException("End date cannot be before start date or tool quantities are null.");
        }

        // Calculate rental days and initialize variables
        int rentalDays = (int) (cart.getStartDate().until(cart.getEndDate(), ChronoUnit.DAYS) + 1);
        BigDecimal totalPreDiscountCharge = BigDecimal.ZERO;
        int totalChargeDays = 0;

        RentalAgreement agreement = new RentalAgreement();

        // Calculate charges for each tool in the cart
        for (Map.Entry<Tool, Integer> entry : cart.getToolQuantities().entrySet()) {
            Tool tool = entry.getKey();
            int quantity = entry.getValue();

            // Calculate charge days and pre-discount charge for the tool
            int chargeDays = calculateChargeDays(cart.getStartDate(), cart.getEndDate(), tool);
            BigDecimal preDiscountCharge = BigDecimal.valueOf(chargeDays).multiply(BigDecimal.valueOf(tool.getDailyCharge())).multiply(BigDecimal.valueOf(quantity));


            totalChargeDays += chargeDays;
            totalPreDiscountCharge = totalPreDiscountCharge.add(preDiscountCharge);
            agreement.addTool(tool, quantity, chargeDays);
        }

        // Handle case where no chargeable days are found
        if (totalChargeDays == 0) {
            throw new NoChargeableDaysException("No chargeable days after excluding holidays and weekends.");
        }

        // Calculate discount amount and final charge
        BigDecimal discountAmount = totalPreDiscountCharge.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        BigDecimal finalCharge = totalPreDiscountCharge.subtract(discountAmount);


        agreement.setRentalDays(rentalDays);
        agreement.setCheckoutDate(cart.getStartDate());
        agreement.setDueDate(cart.getEndDate());
        agreement.setPreDiscountCharge(totalPreDiscountCharge.doubleValue());
        agreement.setDiscountPercent(discountPercent);
        agreement.setDiscountAmount(discountAmount.doubleValue());
        agreement.setFinalCharge(finalCharge.doubleValue());

        return agreement;
    }

    /**
     * Calculates the number of chargeable days between the start and end dates
     * based on the tool's charging policy taking into account weekends and holidays.
     *
     * @param startDate the start date of the period
     * @param endDate   the end date of the period
     * @param tool      the tool used for calculations
     * @return the total number of chargeable days
     */
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
