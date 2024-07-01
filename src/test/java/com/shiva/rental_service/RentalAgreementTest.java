package com.shiva.rental_service;

import com.shiva.rental_service.entities.Cart;
import com.shiva.rental_service.entities.RentalAgreement;
import com.shiva.rental_service.entities.Tool;
import com.shiva.rental_service.services.CheckoutService;
import com.shiva.rental_service.services.HolidayService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class RentalAgreementTest {

    private static Map<String, Tool> toolInventory;
    private static CheckoutService checkoutService;

    @BeforeAll
    public static void setup() {
        toolInventory = new HashMap<>();
        toolInventory.put("CHNS", new Tool("CHNS", "Chainsaw", "Stihl", 1.49, false, true));
        toolInventory.put("LADW", new Tool("LADW", "Ladder", "Werner", 1.99, true, false));
        toolInventory.put("JAKD", new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, false, false));
        toolInventory.put("JAKR", new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, false, false));

        HolidayService holidayService = new HolidayService();
        checkoutService = new CheckoutService(holidayService);
    }

    @Test
    public void testCase1() {
        Map<Tool, Integer> toolQuantities = new HashMap<>();
        toolQuantities.put(toolInventory.get("LADW"), 1);
        Cart cart = new Cart(toolQuantities, LocalDate.of(2015, 9, 3), LocalDate.of(2015, 9, 7));

        RentalAgreement agreement = checkoutService.checkout(cart, 10);
        System.out.println(agreement);

        Assertions.assertEquals("LADW", agreement.getToolCode());
        Assertions.assertEquals("Ladder", agreement.getToolType());
        Assertions.assertEquals("Werner", agreement.getToolBrand());
        Assertions.assertEquals(5, agreement.getRentalDays());
        Assertions.assertEquals(LocalDate.of(2015, 9, 3), agreement.getCheckoutDate());
        Assertions.assertEquals(LocalDate.of(2015, 9, 7), agreement.getDueDate());
        Assertions.assertEquals(1.99, agreement.getDailyRentalCharge());
        Assertions.assertEquals(4, agreement.getChargeDays()); // 09/04/15 (Fri), 09/05/15 (Sat), 09/06/15 (Sun), 09/07/15 (Mon - Labor Day)
        Assertions.assertEquals(7.96, agreement.getPreDiscountCharge(), 0.01);
        Assertions.assertEquals(10, agreement.getDiscountPercent());
        Assertions.assertEquals(0.80, agreement.getDiscountAmount(), 0.01);
        Assertions.assertEquals(7.16, agreement.getFinalCharge(), 0.01);
    }
}
