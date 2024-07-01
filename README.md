# Tool Rental Application

This is a Spring Boot application for a tool rental service. The application calculates rental charges based on the rental period, tool type, holidays, weekends, and discount rates.

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
