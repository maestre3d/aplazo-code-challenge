package com.aruiz.loans.shared.domain.exceptions;

public class ItemNotFoundException extends Exception {
    private final String itemName;

    public ItemNotFoundException(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String toString() {
        return String.format("%s not found", itemName);
    }
}
