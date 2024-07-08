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
    public void rental_agreement_with_single_tool() {
        Map<Tool, Integer> toolQuantities = new HashMap<>();
        toolQuantities.put(toolInventory.get("LADW"), 1);
        Cart cart = new Cart(toolQuantities, LocalDate.of(2015, 9, 3), LocalDate.of(2015, 9, 7));

        RentalAgreement agreement = checkoutService.checkout(cart, 10);
        System.out.println(agreement);

        Assertions.assertEquals("LADW", agreement.getTools().get(0).getCode());
        Assertions.assertEquals("Ladder", agreement.getTools().get(0).getType());
        Assertions.assertEquals("Werner", agreement.getTools().get(0).getBrand());
        Assertions.assertEquals(5, agreement.getRentalDays());
        Assertions.assertEquals(LocalDate.of(2015, 9, 3), agreement.getCheckoutDate());
        Assertions.assertEquals(LocalDate.of(2015, 9, 7), agreement.getDueDate());
        Assertions.assertEquals(1.99, agreement.getTools().get(0).getDailyCharge());
        Assertions.assertEquals(4, agreement.getTools().get(0).getChargeDays()); // 09/04/15 (Fri), 09/05/15 (Sat), 09/06/15 (Sun), 09/07/15 (Mon - Labor Day)
        Assertions.assertEquals(7.96, agreement.getPreDiscountCharge(), 0.01);
        Assertions.assertEquals(10, agreement.getDiscountPercent());
        Assertions.assertEquals(0.80, agreement.getDiscountAmount(), 0.01);
        Assertions.assertEquals(7.16, agreement.getFinalCharge(), 0.01);
    }

    @Test
    public void rental_agreement_with_single_tool_multiple_quantity() {
        Map<Tool, Integer> toolQuantities = new HashMap<>();
        toolQuantities.put(toolInventory.get("LADW"), 2);
        Cart cart = new Cart(toolQuantities, LocalDate.of(2015, 9, 3), LocalDate.of(2015, 9, 7));

        RentalAgreement agreement = checkoutService.checkout(cart, 10);
        System.out.println(agreement);

        Assertions.assertEquals("LADW", agreement.getTools().get(0).getCode());
        Assertions.assertEquals("Ladder", agreement.getTools().get(0).getType());
        Assertions.assertEquals("Werner", agreement.getTools().get(0).getBrand());
        Assertions.assertEquals(5, agreement.getRentalDays());
        Assertions.assertEquals(LocalDate.of(2015, 9, 3), agreement.getCheckoutDate());
        Assertions.assertEquals(LocalDate.of(2015, 9, 7), agreement.getDueDate());
        Assertions.assertEquals(1.99, agreement.getTools().get(0).getDailyCharge());
        Assertions.assertEquals(4, agreement.getTools().get(0).getChargeDays()); // 09/04/15 (Fri), 09/05/15 (Sat), 09/06/15 (Sun), 09/07/15 (Mon - Labor Day)
        Assertions.assertEquals(15.92, agreement.getPreDiscountCharge(), 0.01);
        Assertions.assertEquals(10, agreement.getDiscountPercent());
        Assertions.assertEquals(1.59, agreement.getDiscountAmount(), 0.01);
        Assertions.assertEquals(14.33, agreement.getFinalCharge(), 0.01);
    }

    @Test
    public void rental_agreement_with_multiple_tool_single_quantity() {
        Map<Tool, Integer> toolQuantities = new HashMap<>();
        toolQuantities.put(toolInventory.get("LADW"), 1);
        toolQuantities.put(toolInventory.get("JAKD"), 1);
        Cart cart = new Cart(toolQuantities, LocalDate.of(2015, 9, 3), LocalDate.of(2015, 9, 7));

        RentalAgreement agreement = checkoutService.checkout(cart, 10);
        System.out.println(agreement);

        // find index of tool JAKD in the list

        int JAKDindex = 0;
        for (RentalAgreement.ToolInfo tool : agreement.getTools()) {
            if (tool.getCode().equals("JAKD")) {
                JAKDindex = agreement.getTools().indexOf(tool);
                break;
            }
        }
        int LAKDindex = JAKDindex == 0 ? 1 : 0;


        Assertions.assertEquals("JAKD", agreement.getTools().get(JAKDindex).getCode());
        Assertions.assertEquals("LADW", agreement.getTools().get(LAKDindex).getCode());

        Assertions.assertEquals("Jackhammer", agreement.getTools().get(JAKDindex).getType());
        Assertions.assertEquals("Ladder", agreement.getTools().get(LAKDindex).getType());

        Assertions.assertEquals("DeWalt", agreement.getTools().get(JAKDindex).getBrand());
        Assertions.assertEquals("Werner", agreement.getTools().get(LAKDindex).getBrand());

        Assertions.assertEquals(2.99, agreement.getTools().get(JAKDindex).getDailyCharge());
        Assertions.assertEquals(1.99, agreement.getTools().get(LAKDindex).getDailyCharge());

        Assertions.assertEquals(2, agreement.getTools().get(JAKDindex).getChargeDays()); // 09/04/15 (Fri), 09/05/15 (Sat), 09/06/15 (Sun), 09/07/15 (Mon - Labor Day)
        Assertions.assertEquals(4, agreement.getTools().get(LAKDindex).getChargeDays()); // 09/04/15 (Fri), 09/05/15 (Sat), 09/06/15 (Sun), 09/07/15 (Mon - Labor Day)

        //common properties
        Assertions.assertEquals(5, agreement.getRentalDays());
        Assertions.assertEquals(LocalDate.of(2015, 9, 3), agreement.getCheckoutDate());
        Assertions.assertEquals(LocalDate.of(2015, 9, 7), agreement.getDueDate());
        Assertions.assertEquals(13.94, agreement.getPreDiscountCharge(), 0.01, "Pre discount charge");
        Assertions.assertEquals(10, agreement.getDiscountPercent(), "Discount percent");
        Assertions.assertEquals(1.39, agreement.getDiscountAmount(), 0.01, "Discount amount");
        Assertions.assertEquals(12.55, agreement.getFinalCharge(), 0.01, "Final charge");
    }


    @Test
    public void rental_agreement_with_multiple_tool_multiple_quantity() {
        Map<Tool, Integer> toolQuantities = new HashMap<>();
        toolQuantities.put(toolInventory.get("LADW"), 2);
        toolQuantities.put(toolInventory.get("JAKD"), 3);
        Cart cart = new Cart(toolQuantities, LocalDate.of(2015, 9, 3), LocalDate.of(2015, 9, 7));

        RentalAgreement agreement = checkoutService.checkout(cart, 10);
        System.out.println(agreement);

        //common properties
        Assertions.assertEquals(5, agreement.getRentalDays());
        Assertions.assertEquals(LocalDate.of(2015, 9, 3), agreement.getCheckoutDate());
        Assertions.assertEquals(LocalDate.of(2015, 9, 7), agreement.getDueDate());
        Assertions.assertEquals(33.86, agreement.getPreDiscountCharge(), 0.01, "Pre discount charge");
        Assertions.assertEquals(10, agreement.getDiscountPercent(), "Discount percent");
        Assertions.assertEquals(3.39, agreement.getDiscountAmount(), 0.01, "Discount amount");
        Assertions.assertEquals(30.47, agreement.getFinalCharge(), 0.01, "Final charge");
    }

}
