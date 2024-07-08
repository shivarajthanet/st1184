package com.shiva.rental_service.entities;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a rental agreement for a tool, containing information such as tool code, type, brand, rental days,
 * checkout date, due date, daily rental charge, charge days, pre-discount charge, discount percent, discount amount,
 * and final charge.
 */
@Getter
@Setter
@Data
public class RentalAgreement {
    // Common properties
    private int rentalDays;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private double preDiscountCharge;
    private int discountPercent;
    private double discountAmount;
    private double finalCharge;

    // Tool-specific information
    @Data
    public static class ToolInfo {
        private String code;
        private String type;
        private String brand;
        private double dailyCharge;
        private int quantity;
        private int chargeDays;
    }

    private List<ToolInfo> tools = new ArrayList<>();

    public void addTool(Tool tool, int quantity, int chargeDays) {
        ToolInfo toolInfo = new ToolInfo();
        toolInfo.setCode(tool.getCode());
        toolInfo.setType(tool.getType());
        toolInfo.setBrand(tool.getBrand());
        toolInfo.setDailyCharge(tool.getDailyCharge());
        toolInfo.setQuantity(quantity);
        toolInfo.setChargeDays(chargeDays);
        tools.add(toolInfo);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Rental Agreement\n");
        sb.append("Rental Days: ").append(rentalDays).append("\n");
        sb.append("Checkout Date: ").append(checkoutDate).append("\n");
        sb.append("Due Date: ").append(dueDate).append("\n");
        sb.append("Pre-discount Charge: $").append(String.format("%.2f", preDiscountCharge)).append("\n");
        sb.append("Discount Percent: ").append(discountPercent).append("%\n");
        sb.append("Discount Amount: $").append(String.format("%.2f", discountAmount)).append("\n");
        sb.append("Final Charge: $").append(String.format("%.2f", finalCharge)).append("\n");

        sb.append("\nTools:\n");
        sb.append(String.format("%-10s %-15s %-15s %-15s %-10s %-10s\n", "Tool Code", "Tool Type", "Tool Brand", "Daily Charge", "Quantity", "Charge Days"));
        sb.append("---------------------------------------------------------------------\n");

        for (ToolInfo toolInfo : tools) {
            sb.append(String.format("%-10s %-15s %-15s $%-14.2f %-10d %-10d\n",
                    toolInfo.getCode(), toolInfo.getType(), toolInfo.getBrand(), toolInfo.getDailyCharge(), toolInfo.getQuantity(), toolInfo.getChargeDays()));
        }

        return sb.toString();
    }
}
