package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/*
 * Zona de baños: usa cupos temporales y libera cada turista despues de cierto
 * numero de pasos de simulacion.
 */
public class BathroomZone implements ParkZone {
    private final String name;
    private final int maxCapacity;
    private final int useDurationSteps;
    private final double spaPrice;
    private final double spaPurchaseProbability;
    private final Map<Tourist, Integer> occupiedSlots;

    public BathroomZone(String name, int maxCapacity, int useDurationSteps, double spaPrice, double spaPurchaseProbability) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.useDurationSteps = useDurationSteps;
        this.spaPrice = spaPrice;
        this.spaPurchaseProbability = spaPurchaseProbability;
        this.occupiedSlots = new HashMap<>();
    }

    public boolean tryEnter(Tourist tourist, Random rng, CsvWriter csvWriter) {
        if (tourist == null || !hasCapacity()) {
            return false;
        }

        enter(tourist);
        tourist.recordVisit(name);
        if (rng != null && rng.nextDouble() < spaPurchaseProbability) {
            tourist.spend(spaPrice);
            writeRevenue(csvWriter, new RevenueRecord("SPA", name, tourist.getId(), spaPrice));
        }
        return true;
    }

    public void tick() {
        Iterator<Map.Entry<Tourist, Integer>> iterator = occupiedSlots.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Tourist, Integer> entry = iterator.next();
            int remainingSteps = entry.getValue() - 1;
            if (remainingSteps <= 0) {
                iterator.remove();
            } else {
                entry.setValue(remainingSteps);
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasCapacity() {
        return occupiedSlots.size() < maxCapacity;
    }

    @Override
    public int getCurrentOccupancy() {
        return occupiedSlots.size();
    }

    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public void enter(Tourist tourist) {
        if (tourist != null && hasCapacity()) {
            occupiedSlots.put(tourist, useDurationSteps);
        }
    }

    @Override
    public void exit(Tourist tourist) {
        occupiedSlots.remove(tourist);
    }

    private void writeRevenue(CsvWriter csvWriter, RevenueRecord record) {
        if (csvWriter != null) {
            csvWriter.writeRevenue(record);
        }
    }
}
