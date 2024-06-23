package com.shiva.rental_service;

import com.shiva.rental_service.entities.Cart;
import com.shiva.rental_service.entities.RentalAgreement;
import com.shiva.rental_service.entities.Tool;
import com.shiva.rental_service.services.CheckoutService;
import com.shiva.rental_service.services.HolidayService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RentalServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(RentalServiceApplication.class, args);

		// Initialize tool inventory
		Map<String, Tool> toolInventory = new HashMap<>();
		toolInventory.put("CHNS", new Tool("CHNS", "Chainsaw", "Stihl", 1.49, false, true));
		toolInventory.put("LADW", new Tool("LADW", "Ladder", "Werner", 1.99, true, false));
		toolInventory.put("JAKD", new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, false, false));
		toolInventory.put("JAKR", new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, false, false));

		HolidayService holidayService = new HolidayService();
		CheckoutService checkoutService = new CheckoutService(toolInventory, holidayService);

		// Perform a checkout as an example
		Map<Tool, Integer> toolQuantities = new HashMap<>();
		toolQuantities.put(toolInventory.get("LADW"), 1);
		Cart cart = new Cart(toolQuantities, LocalDate.of(2024, 7, 2), LocalDate.of(2024, 7, 5));

		RentalAgreement agreement = checkoutService.checkout(cart, 10);
		System.out.println(agreement);
	}
}
