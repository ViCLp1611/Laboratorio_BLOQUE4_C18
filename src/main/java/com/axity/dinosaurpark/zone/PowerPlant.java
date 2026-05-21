package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.ExpenseRecord;

import java.util.Random;

/*
 * Planta electrica basica: consume energia por paso, puede fallar y puede ser
 * reparada por un tecnico.
 */
public class PowerPlant implements ParkZone {
    private final String name;
    private final int maxCapacity;
    private double energy;
    private final double consumptionPerStep;
    private final double failureProbability;
    private final double maintenanceCost;
    private final double repairCost;
    private boolean operational;

    public PowerPlant(boolean operational) {
        this("Power Plant", 1, 100.0, 1.0, 0.0, 0.0, 0.0, operational);
    }

    public PowerPlant(String name, int maxCapacity, double energy, double consumptionPerStep,
                      double failureProbability, double maintenanceCost, double repairCost) {
        this(name, maxCapacity, energy, consumptionPerStep, failureProbability, maintenanceCost, repairCost, true);
    }

    public PowerPlant(String name, int maxCapacity, double energy, double consumptionPerStep,
                      double failureProbability, double maintenanceCost, double repairCost, boolean operational) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.energy = energy;
        this.consumptionPerStep = consumptionPerStep;
        this.failureProbability = failureProbability;
        this.maintenanceCost = maintenanceCost;
        this.repairCost = repairCost;
        this.operational = operational;
    }

    public void tick(Random rng, CsvWriter csvWriter) {
        if (!operational) {
            return;
        }

        energy = Math.max(0.0, energy - consumptionPerStep);
        writeExpense(csvWriter, new ExpenseRecord("POWER_MAINTENANCE", name, maintenanceCost));

        if (energy <= 0.0 || (rng != null && rng.nextDouble() < failureProbability)) {
            triggerFailure(csvWriter);
        }
    }

    public void triggerFailure(CsvWriter csvWriter) {
        operational = false;
        writeExpense(csvWriter, new ExpenseRecord("POWER_FAILURE", name, repairCost));
        if (csvWriter != null) {
            csvWriter.writeEvent("POWER_FAILURE", name + " is not operational");
        }
    }

    public void repair() {
        operational = true;
        energy = Math.max(energy, consumptionPerStep * 10.0);
    }

    public boolean isOperational() {
        return operational;
    }

    public double getEnergy() {
        return energy;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasCapacity() {
        return getCurrentOccupancy() < maxCapacity;
    }

    @Override
    public int getCurrentOccupancy() {
        return 0;
    }

    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public void enter(Tourist tourist) {
        // La planta no recibe turistas en el nivel basico.
    }

    @Override
    public void exit(Tourist tourist) {
        // La planta no recibe turistas en el nivel basico.
    }

    private void writeExpense(CsvWriter csvWriter, ExpenseRecord record) {
        if (csvWriter != null) {
            csvWriter.writeExpense(record);
        }
    }
}
