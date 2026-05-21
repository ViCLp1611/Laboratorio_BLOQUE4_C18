package com.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;

/*
 * Registro inmutable de gastos usado por servicios como la planta electrica.
 */
public class ExpenseRecord {
    private final String type;
    private final String source;
    private final double amount;
    private final LocalDateTime occurredAt;

    public ExpenseRecord(String type, String source, double amount) {
        this(type, source, amount, LocalDateTime.now());
    }

    public ExpenseRecord(String type, String source, double amount, LocalDateTime occurredAt) {
        this.type = type;
        this.source = source;
        this.amount = amount;
        this.occurredAt = occurredAt;
    }

    public String getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
