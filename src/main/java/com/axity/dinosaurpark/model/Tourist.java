package com.axity.dinosaurpark.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tourist {
    private final int id;
    private final String name;
    private TouristStatus status;
    private double dineroGastado;
    private final List<String> zonasVisitadas;

    public Tourist(int id, String name) {
        this.id = id;
        this.name = name;
        this.status = TouristStatus.WAITING;
        this.dineroGastado = 0.0;
        this.zonasVisitadas = new ArrayList<>();
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

    public double getDineroGastado() {
        return dineroGastado;
    }

    public void gastar(double amount) {
        if (amount > 0) {
            dineroGastado += amount;
        }
    }

    public void registrarVisita(String zoneName) {
        if (zoneName != null && !zoneName.trim().isEmpty()) {
            zonasVisitadas.add(zoneName);
        }
    }

    public List<String> getZonasVisitadas() {
        return Collections.unmodifiableList(zonasVisitadas);
    }
}
