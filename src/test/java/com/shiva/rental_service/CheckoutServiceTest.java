package com.shiva.rental_service;

import com.shiva.rental_service.entities.Cart;
import com.shiva.rental_service.entities.RentalAgreement;
import com.shiva.rental_service.entities.Tool;
import com.shiva.rental_service.exception.InvalidDataException;
import com.shiva.rental_service.exception.InvalidDateRangeException;
import com.shiva.rental_service.exception.InvalidDiscountException;
import com.shiva.rental_service.exception.NoChargeableDaysException;
import com.shiva.rental_service.services.CheckoutService;
import com.shiva.rental_service.services.HolidayService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CheckoutServiceTest {
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
    public void testCheckout() {

        // Add test cases
        Map<Tool, Integer> toolQuantities1 = new HashMap<>();
        toolQuantities1.put(toolInventory.get("LADW"), 1);
        Cart cart1 = new Cart(toolQuantities1, LocalDate.of(2024, 7, 2), LocalDate.of(2024, 7, 5));
        RentalAgreement agreement1 = checkoutService.checkout(cart1, 10);
        Assertions.assertEquals(agreement1.getTools().get(0).getCode(), "LADW");
        Assertions.assertEquals(agreement1.getFinalCharge(), 5.37, 0.01);
//
        Map<Tool, Integer> toolQuantities2 = new HashMap<>();
        toolQuantities2.put(toolInventory.get("CHNS"), 1);
        Cart cart2 = new Cart(toolQuantities2, LocalDate.of(2024, 7, 2), LocalDate.of(2024, 7, 6));
        RentalAgreement agreement2 = checkoutService.checkout(cart2, 25);
        Assertions.assertEquals(agreement2.getTools().get(0).getCode(), "CHNS");
        Assertions.assertEquals(agreement2.getFinalCharge(), 4.47, 0.01);

    }

    @Test
    public void checkout_with_emptyCart() {
        Assertions.assertThrows(InvalidDataException.class, () -> checkoutService.checkout(null, 10));
    }

    @Test
    public void checkout_with_Cart_having_null_tools() {
        Cart cart = new Cart(null, LocalDate.of(2024, 7, 2), LocalDate.of(2024, 7, 5));
        Assertions.assertThrows(InvalidDataException.class, () -> checkoutService.checkout(cart, 10));
    }

    @Test
    public void checkout_with_Cart_having_empty_tools() {
        Cart cart = new Cart(new HashMap<>(), LocalDate.of(2024, 7, 2), LocalDate.of(2024, 7, 5));
        Assertions.assertThrows(InvalidDataException.class, () -> checkoutService.checkout(cart, 10));
    }

    @Test
    public void checkout_with_Cart_having_null_start_dates() {
        Map<Tool, Integer> toolQuantities1 = new HashMap<>();
        toolQuantities1.put(toolInventory.get("LADW"), 1);
        Cart cart = new Cart(toolQuantities1, null, LocalDate.of(2024, 7, 5));
        Assertions.assertThrows(InvalidDateRangeException.class, () -> checkoutService.checkout(cart, 10));
    }


    @Test
    public void checkout_with_Cart_having_null_end_dates() {
        Map<Tool, Integer> toolQuantities1 = new HashMap<>();
        toolQuantities1.put(toolInventory.get("LADW"), 1);
        Cart cart = new Cart(toolQuantities1, LocalDate.of(2024, 7, 5), null);
        Assertions.assertThrows(InvalidDateRangeException.class, () -> checkoutService.checkout(cart, 10));
    }

    @Test
    public void checkout_with_Cart_having_null_dates() {
        Map<Tool, Integer> toolQuantities1 = new HashMap<>();
        toolQuantities1.put(toolInventory.get("LADW"), 1);
        Cart cart = new Cart(toolQuantities1, null, null);
        Assertions.assertThrows(InvalidDateRangeException.class, () -> checkoutService.checkout(cart, 10));
    }

    @Test
    public void invalid_total_days() {
        Map<Tool, Integer> toolQuantities1 = new HashMap<>();
        toolQuantities1.put(toolInventory.get("JAKR"), 1);
        Assertions.assertThrows(InvalidDataException.class, () -> new Cart(toolQuantities1, LocalDate.of(2020, 7, 2),0));
    }

    @Test
    public void testInvalidDiscountException() {
        // Test Case: Discount is more than 100%
        Map<Tool, Integer> toolQuantities = new HashMap<>();
        toolQuantities.put(toolInventory.get("LADW"), 1);
        Cart cart = new Cart(toolQuantities, LocalDate.of(2024, 6, 23), LocalDate.of(2024, 6, 27));

        Assertions.assertThrows(InvalidDiscountException.class, () -> checkoutService.checkout(cart, 101));
    }

    @Test
    public void testInvalidDiscountException_negative() {
        // Test Case: Discount is more than 100%
        Map<Tool, Integer> toolQuantities = new HashMap<>();
        toolQuantities.put(toolInventory.get("LADW"), 1);
        Cart cart = new Cart(toolQuantities, LocalDate.of(2024, 6, 23), LocalDate.of(2024, 6, 27));

        Assertions.assertThrows(InvalidDiscountException.class, () -> checkoutService.checkout(cart, -10));
    }

    @Test
    public void testInvalidDateRangeException() {
        // Test Case: To-date is less than from-date
        Map<Tool, Integer> toolQuantities = new HashMap<>();
        toolQuantities.put(toolInventory.get("CHNS"), 1);
        Cart cart = new Cart(toolQuantities, LocalDate.of(2024, 6, 27), LocalDate.of(2024, 6, 23));

        Assertions.assertThrows(InvalidDateRangeException.class, () -> checkoutService.checkout(cart, 10));
    }

    @Test
    public void testNoChargeableDaysException() {
        // Test Case: No chargeable days after excluding holidays and weekends
        Map<Tool, Integer> toolQuantities = new HashMap<>();
        toolQuantities.put(toolInventory.get("JAKD"), 1);
        Cart cart = new Cart(toolQuantities, LocalDate.of(2024, 7, 4), LocalDate.of(2024, 7, 4)); // Independence Day

        Assertions.assertThrows(NoChargeableDaysException.class, () -> checkoutService.checkout(cart, 10));
    }

    @Test
    public void testForCommandLine() {
        Cart cart = new Cart();
        cart.addTool(toolInventory.get("JAKD"), 1);
        cart.setStartDate(LocalDate.of(2024, 7, 4));
        cart.setEndDate(LocalDate.of(2024, 7, 6));
        RentalAgreement agreement = checkoutService.checkout(cart, 10);
        Assertions.assertEquals(2.69, agreement.getFinalCharge(), 0.01);
    }
}
