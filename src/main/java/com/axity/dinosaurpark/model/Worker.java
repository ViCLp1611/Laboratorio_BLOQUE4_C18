package com.axity.dinosaurpark.model;

/*
 * Base comun para los trabajadores del parque.
 * Cada subclase define su rol concreto mediante getRole().
 */
public abstract class Worker {
    private final int id;
    private final String name;
    private final double dailySalary;

    public Worker(int id, String name, double dailySalary) {
        this.id = id;
        this.name = name;
        this.dailySalary = dailySalary;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getDailySalary() {
        return dailySalary;
    }

    public abstract String getRole();
}
