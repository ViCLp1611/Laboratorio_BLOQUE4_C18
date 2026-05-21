package com.axity.dinosaurpark.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tourist {
    private final int id;
    private final String name;
    private TouristStatus status;
    private double moneySpent;
    private final List<String> visitedZones;

    public Tourist(int id, String name) {
        this.id = id;
        this.name = name;
        this.status = TouristStatus.WAITING;
        this.moneySpent = 0.0;
        this.visitedZones = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TouristStatus getStatus() {
        return status;
    }

    public void setStatus(TouristStatus status) {
        this.status = status;
    }

    public double getMoneySpent() {
        return moneySpent;
    }

    public void spend(double amount) {
        if (amount > 0) {
            moneySpent += amount;
        }
    }

    public void recordVisit(String zoneName) {
        if (zoneName != null && !zoneName.trim().isEmpty()) {
            visitedZones.add(zoneName);
        }
    }

    public List<String> getVisitedZones() {
        return Collections.unmodifiableList(visitedZones);
    }
}
