package com.github.marcelldechant.bistro.order.util;

import com.github.marcelldechant.bistro.order.entity.Order;
import com.github.marcelldechant.bistro.orderitem.entity.OrderItem;

import java.math.BigDecimal;

/**
 * Utility class for formatting receipts for orders.
 * This class provides a method to format an order into a receipt string.
 * It is not intended to be instantiated, hence the private constructor.
 *
 * @author Marcell Dechant
 */
public class ReceiptFormatter {

    /**
     * Private constructor to prevent instantiation of the utility class.
     * This class is intended to be used statically, so no instances should be created.
     */
    private ReceiptFormatter() {
    }

    /**
     * Formats an order into a receipt string.
     * The receipt includes the table number, items ordered, subtotal, discount (if applicable), and total amount.
     *
     * @param order the order to format
     * @return a formatted receipt string
     */
    public static String format(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("-------------------------\n");
        sb.append("Table Nr. ").append(order.getTableNumber()).append("\n");
        sb.append("-------------------------\n");

        for (OrderItem item : order.getItems()) {
            String name = item.getProduct().getName();
            int qty = item.getQuantity();
            BigDecimal unit = item.getPricePerUnit();
            BigDecimal total = item.getTotalPrice();

            sb.append(String.format("%d x %s @ %.2f = %.2f\n", qty, name, unit, total));
        }

        sb.append("-------------------------\n");
        sb.append(String.format("Subtotal: %.2f\n", order.getSubtotal()));
        sb.append(order.isHappyHour() ? String.format("Discount: %s\n", "10%") : "");
        sb.append(String.format("Total: %.2f\n", order.getTotal()));

        return sb.toString();
    }

}
