# Tool Rental Application

This is a Spring Boot application for a tool rental service. The application calculates rental charges based on the rental period, tool type, holidays, weekends, and discount rates.

**Please Note: there are no REST endpoints added to this application**

## Features

- Calculate rental charges for various tools.
- Support for holiday and weekend charges.
- Discount rate validation.
- Custom exceptions for invalid input scenarios.
- Lombok integration to reduce boilerplate code.

## Prerequisites

- Java 11 or higher
- Maven 3.6.3 or higher

## Getting Started

### Usage

1. configure the tools inventory as per below example, more tools can be added to the collections
```java
toolInventory = new HashMap<>();
toolInventory.put("CHNS", new Tool("CHNS", "Chainsaw", "Stihl", 1.49, false, true));
toolInventory.put("LADW", new Tool("LADW", "Ladder", "Werner", 1.99, true, false));
toolInventory.put("JAKD", new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, false, false));
toolInventory.put("JAKR", new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, false, false));
```
2. to generate agreement use below code

```java
//setup tool quantity  
Map<Tool, Integer> toolQuantities = new HashMap<>();
toolQuantities1.put(toolInventory.get("LADW"), 1);

//create cart object using toolQuantities map with start and end date. 
Cart cart1 = new Cart(toolQuantities, LocalDate.of(2024, 7, 2), LocalDate.of(2024, 7, 5));

//cart object can also be created using number of days and start date
Cart cart2 = new Cart(toolQuantities, LocalDate.of(2024, 7, 5), 2);

//finally generate the agreement.
RentalAgreement agreement = checkoutService.checkout(cart1, 10);
```


### Clone the Repository

```sh
git clone https://github.com/yourusername/tool-rental-application.git
cd tool-rental-application
```

## Build and run the Application
```sh
./gradlew build
./gradlew bootRun
```

## Execute Test Cases
```sh
./gradlew test
```

## review the test coverage post test execution 
```
build/reports/jacoco/test/html/index.html
```
