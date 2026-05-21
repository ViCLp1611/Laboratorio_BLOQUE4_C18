package com.axity.dinosaurpark.model;

import com.axity.dinosaurpark.simulation.ParkState;

/*
 * Base comun para los trabajadores del parque.
 * Cada subclase define su rol concreto mediante getRole().
 */
public abstract class Worker {
    private final int id;
    private final String name;
    private final double salarioDiario;

    public Worker(int id, String name, double salarioDiario) {
        this.id = id;
        this.name = name;
        this.salarioDiario = salarioDiario;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getSalarioDiario() {
        return salarioDiario;
    }

    public abstract String getRole();

    public abstract void ejecutarTarea(ParkState state);
}
