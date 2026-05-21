package com.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;

/*
 * Registro inmutable de ingresos usado por las zonas del parque.
 */
public class RevenueRecord {
    private final long id;
    private final String type;
    private final String source;
    private final int touristId;
    private final double amount;
    private final LocalDateTime occurredAt;

    public RevenueRecord(String type, String source, int touristId, double amount) {
        this(0L, type, source, touristId, amount, LocalDateTime.now());
    }

    public RevenueRecord(long id, String type, String source, int touristId, double amount) {
        this(id, type, source, touristId, amount, LocalDateTime.now());
    }

    public RevenueRecord(long id, String type, String source, int touristId, double amount, LocalDateTime occurredAt) {
        this.id = id;
        this.type = type;
        this.source = source;
        this.touristId = touristId;
        this.amount = amount;
        this.occurredAt = occurredAt;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public int getTouristId() {
        return touristId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
