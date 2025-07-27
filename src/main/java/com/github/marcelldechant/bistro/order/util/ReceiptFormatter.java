package com.github.marcelldechant.bistro.order.util;

import com.github.marcelldechant.bistro.order.entity.Order;
import com.github.marcelldechant.bistro.orderitem.entity.OrderItem;

import java.math.BigDecimal;

public class ReceiptFormatter {
    private static final String SEPARATOR_LINE = "-------------------------\n";

    private ReceiptFormatter() {
    }

    public static String format(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append(SEPARATOR_LINE);
        sb.append("Table Nr. ").append(order.getTableNumber()).append("\n");
        sb.append(SEPARATOR_LINE);

        for (OrderItem item : order.getItems()) {
            String name = item.getProduct().getName();
            int qty = item.getQuantity();
            BigDecimal unit = item.getPricePerUnit();
            BigDecimal total = item.getTotalPrice();

            sb.append(String.format("%d x %s @ %.2f = %.2f%n", qty, name, unit, total));
        }

        sb.append(SEPARATOR_LINE);
        sb.append(String.format("Subtotal: %.2f\n", order.getSubtotal()));
        sb.append(order.isHappyHour() ? String.format("Discount: %s%n", "10%") : "");
        sb.append(String.format("Total: %.2f\n", order.getTotal()));

        return sb.toString();
    }
}
