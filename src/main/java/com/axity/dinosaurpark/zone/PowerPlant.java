package com.axity.dinosaurpark.zone;

import java.time.LocalDateTime;
import java.util.Random;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;

/*
 * Planta electrica basica: consume energia por paso, puede fallar y puede ser
 * reparada por un tecnico.
 */
public class PowerPlant implements ParkZone {
    private final String name;
    private final int capacidadMaxima;
    private double energiaDisponible;
    private final double consumoPorPaso;
    private final double probabilidadFalla;
    private final double costoMantenimiento;
    private final double costoReparacion;
    private boolean operativa;

    public PowerPlant(boolean operativa) {
        this("Power Plant", 1, 100.0, 1.0, 0.0, 0.0, 0.0, operativa);
    }

    public PowerPlant(String name, int capacidadMaxima, double energiaDisponible, double consumoPorPaso,
                      double probabilidadFalla, double costoMantenimiento, double costoReparacion) {
        this(name, capacidadMaxima, energiaDisponible, consumoPorPaso, probabilidadFalla, costoMantenimiento, costoReparacion, true);
    }

    public PowerPlant(String name, int capacidadMaxima, double energiaDisponible, double consumoPorPaso,
                      double probabilidadFalla, double costoMantenimiento, double costoReparacion, boolean operativa) {
        this.name = name;
        this.capacidadMaxima = capacidadMaxima;
        this.energiaDisponible = energiaDisponible;
        this.consumoPorPaso = consumoPorPaso;
        this.probabilidadFalla = probabilidadFalla;
        this.costoMantenimiento = costoMantenimiento;
        this.costoReparacion = costoReparacion;
        this.operativa = operativa;
    }

    public void paso(Random rng, CsvWriter csvWriter) {
        if (!operativa) {
            return;
        }

        energiaDisponible = Math.max(0.0, energiaDisponible - consumoPorPaso);
        registrarGasto(csvWriter, new ExpenseRecord(
                0L,
                "POWER_MAINTENANCE",
                costoMantenimiento,
                name,
                LocalDateTime.now()));

        if (energiaDisponible <= 0.0 || (rng != null && rng.nextDouble() < probabilidadFalla)) {
            activarFalla(csvWriter);
        }
    }

    public void activarFalla(CsvWriter csvWriter) {
        operativa = false;
        registrarGasto(csvWriter, new ExpenseRecord(
                0L,
                "POWER_FAILURE",
                costoReparacion,
                name,
                LocalDateTime.now()));
        if (csvWriter != null) {
            csvWriter.appendEvent(new EventRecord(
                    0L,
                    "POWER_FAILURE",
                    name + " no esta operativa",
                    name,
                    LocalDateTime.now()));
        }
    }

    public void triggerFailure(CsvWriter csvWriter) {
        activarFalla(csvWriter);
    }

    public void reparar() {
        operativa = true;
        energiaDisponible = Math.max(energiaDisponible, consumoPorPaso * 10.0);
    }

    public boolean estaOperativa() {
        return operativa;
    }

    public boolean isOperational() {
        return estaOperativa();
    }

    public double getEnergy() {
        return energiaDisponible;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasCapacity() {
        return getCurrentOccupancy() < capacidadMaxima;
    }

    @Override
    public int getCurrentOccupancy() {
        return 0;
    }

    @Override
    public int getMaxCapacity() {
        return capacidadMaxima;
    }

    @Override
    public void enter(Tourist tourist) {
        // La planta no recibe turistas en el nivel basico.
    }

    @Override
    public void exit(Tourist tourist) {
        // La planta no recibe turistas en el nivel basico.
    }

    private void registrarGasto(CsvWriter csvWriter, ExpenseRecord record) {
        if (csvWriter != null) {
            csvWriter.appendExpense(record);
        }
    }
}
