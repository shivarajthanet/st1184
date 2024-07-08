package com.shiva.rental_service;

import com.shiva.rental_service.entities.Cart;
import com.shiva.rental_service.entities.RentalAgreement;
import com.shiva.rental_service.entities.Tool;
import com.shiva.rental_service.services.CheckoutService;
import com.shiva.rental_service.services.HolidayService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

class RentalServiceApplicationCommandLine {


    private static Map<String, Tool> initializeToolInventory() {
        Map<String, Tool> toolInventory = new HashMap<>();
        toolInventory.put("CHNS", new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, true));
        toolInventory.put("LADW", new Tool("LADW", "Ladder", "Werner", 1.99, false, false));
        toolInventory.put("JAKD", new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, true));
        toolInventory.put("JAKR", new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, true));
        return toolInventory;
    }

    public static void main(String[] args) {

        HolidayService holidayService = new HolidayService();
        CheckoutService checkoutService = new CheckoutService(holidayService);

        Scanner scanner = new Scanner(System.in);
        Map<String, Tool> toolInventory = initializeToolInventory();
        List<Tool> toolList = new ArrayList<>(toolInventory.values());

        while (true) {
            System.out.println("Press 1 to generate a new agreement or 0 to stop the application:");
            int choice = scanner.nextInt();
            if (choice == 0) {
                break;
            } else if (choice == 1) {
                Cart cart = new Cart();
                while (true) {
                    System.out.println("Choose a tool by entering its number (press 0 to finish):");
                    for (int i = 0; i < toolList.size(); i++) {
                        System.out.printf("%d: %s%n", i + 1, toolList.get(i).getCode());
                    }
                    int toolChoice = scanner.nextInt();
                    if (toolChoice == 0) {
                        break;
                    }
                    if (toolChoice > 0 && toolChoice <= toolList.size()) {
                        Tool tool = toolList.get(toolChoice - 1);
                        System.out.println("Enter quantity:");
                        int quantity = scanner.nextInt();
                        cart.addTool(tool, quantity);
                    } else {
                        System.out.println("Invalid tool number.");
                    }
                }
                System.out.println("Enter start date (MM/dd/yyyy):");
                String startDateString = scanner.next();
                LocalDate startDate = LocalDate.parse(startDateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                System.out.println("Enter total rental days:");
                int totalDays = scanner.nextInt();
                LocalDate endDate = startDate.plusDays(totalDays - 1);

                cart.setStartDate(startDate);
                cart.setEndDate(endDate);

                System.out.println("Enter discount percent:");
                int discountPercent = scanner.nextInt();

                RentalAgreement agreement = checkoutService.checkout(cart, discountPercent);
                System.out.println(agreement);
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }


}