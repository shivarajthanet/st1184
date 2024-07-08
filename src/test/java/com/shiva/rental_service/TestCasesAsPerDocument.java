package com.shiva.rental_service;

import com.shiva.rental_service.entities.Cart;
import com.shiva.rental_service.entities.RentalAgreement;
import com.shiva.rental_service.entities.Tool;
import com.shiva.rental_service.exception.InvalidDiscountException;
import com.shiva.rental_service.services.CheckoutService;
import com.shiva.rental_service.services.HolidayService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TestCasesAsPerDocument {

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
    public void test1() {

        Map<Tool, Integer> toolQuantities1 = new HashMap<>();
        toolQuantities1.put(toolInventory.get("JAKR"), 1);
        Cart cart = new Cart(toolQuantities1, LocalDate.of(2015, 9, 3), 5);
        Assertions.assertThrows(InvalidDiscountException.class, () -> checkoutService.checkout(cart, 101));

    }


    @Test
    public void test2() {
        Map<Tool, Integer> toolQuantities1 = new HashMap<>();
        toolQuantities1.put(toolInventory.get("LADW"), 1);
        Cart cart = new Cart(toolQuantities1, LocalDate.of(2020, 7, 2), 3);
        RentalAgreement agreement = checkoutService.checkout(cart, 10);
        Assertions.assertEquals(3.58, agreement.getFinalCharge(), 0.01);
    }


    @Test
    public void test2_independence_day_on_sunday() {

        Map<Tool, Integer> toolQuantities1 = new HashMap<>();
        toolQuantities1.put(toolInventory.get("LADW"), 1);
        Cart cart = new Cart(toolQuantities1, LocalDate.of(2021, 7, 2), 3);
        RentalAgreement agreement = checkoutService.checkout(cart, 10);
        Assertions.assertEquals(5.37, agreement.getFinalCharge(), 0.01);
    }

    @Test
    public void test3() {

        Map<Tool, Integer> toolQuantities1 = new HashMap<>();
        toolQuantities1.put(toolInventory.get("CHNS"), 1);
        Cart cart = new Cart(toolQuantities1, LocalDate.of(2015, 7, 2), 5);
        RentalAgreement agreement = checkoutService.checkout(cart, 25);
        Assertions.assertEquals(3.35, agreement.getFinalCharge(), 0.01);

    }

    @Test
    public void test4() {

        Map<Tool, Integer> toolQuantities1 = new HashMap<>();
        toolQuantities1.put(toolInventory.get("JAKD"), 1);
        Cart cart = new Cart(toolQuantities1, LocalDate.of(2015, 9, 3), 6);
        RentalAgreement agreement = checkoutService.checkout(cart, 0);
        Assertions.assertEquals(8.97, agreement.getFinalCharge(), 0.01);

    }


    @Test
    public void test5() {

        Map<Tool, Integer> toolQuantities1 = new HashMap<>();
        toolQuantities1.put(toolInventory.get("JAKR"), 1);
        Cart cart = new Cart(toolQuantities1, LocalDate.of(2015, 7, 2), 9);
        RentalAgreement agreement = checkoutService.checkout(cart, 0);
        Assertions.assertEquals(17.94, agreement.getFinalCharge(), 0.01);

    }

    @Test
    public void test6() {

        Map<Tool, Integer> toolQuantities1 = new HashMap<>();
        toolQuantities1.put(toolInventory.get("JAKR"), 1);
        Cart cart = new Cart(toolQuantities1, LocalDate.of(2020, 7, 2), 4);
        RentalAgreement agreement = checkoutService.checkout(cart, 50);
        Assertions.assertEquals(1.49, agreement.getFinalCharge(), 0.01);

    }


}
