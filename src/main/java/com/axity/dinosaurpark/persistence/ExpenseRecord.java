package com.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;

/*
 * Registro de gasto para el CSV de expenses.
 * Es inmutable y deja la conversion CSV junto a los datos.
 */
public record ExpenseRecord(
        long id,
        String type,
        double amount,
        String description,
        LocalDateTime timestamp) {

    public String toCsvLine() {
        return id + "," + escape(type) + "," + amount + ","
                + escape(description) + "," + timestamp;
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
