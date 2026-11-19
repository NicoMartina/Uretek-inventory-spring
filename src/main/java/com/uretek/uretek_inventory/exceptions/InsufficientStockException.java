package com.uretek.uretek_inventory.exceptions;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String itemName, double available, double required) {
        super("Insufficient stock for '" + itemName + "': available=" + available + ", required=" + required);
    }
}