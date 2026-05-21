package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/*
 * Zona central del parque: registra visitas y puede generar ingresos por
 * souvenirs durante la visita del turista.
 */
public class CentralHub implements ParkZone {
    private final String name;
    private final int maxCapacity;
    private final double souvenirPrice;
    private final double souvenirPurchaseProbability;
    private final List<Tourist> currentTourists;

    public CentralHub(String name, int maxCapacity, double souvenirPrice, double souvenirPurchaseProbability) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.souvenirPrice = souvenirPrice;
        this.souvenirPurchaseProbability = souvenirPurchaseProbability;
        this.currentTourists = new ArrayList<>();
    }

    public void visit(Tourist tourist, Random rng, CsvWriter csvWriter) {
        if (tourist == null || !hasCapacity()) {
            return;
        }

        enter(tourist);
        tourist.recordVisit(name);
        if (rng != null && rng.nextDouble() < souvenirPurchaseProbability) {
            tourist.spend(souvenirPrice);
            writeRevenue(csvWriter, new RevenueRecord("SOUVENIR", name, tourist.getId(), souvenirPrice));
        }
        exit(tourist);
    }

    public List<Tourist> getCurrentTourists() {
        return Collections.unmodifiableList(currentTourists);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasCapacity() {
        return currentTourists.size() < maxCapacity;
    }

    @Override
    public int getCurrentOccupancy() {
        return currentTourists.size();
    }

    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public void enter(Tourist tourist) {
        if (tourist != null && hasCapacity()) {
            currentTourists.add(tourist);
        }
    }

    @Override
    public void exit(Tourist tourist) {
        currentTourists.remove(tourist);
    }

    private void writeRevenue(CsvWriter csvWriter, RevenueRecord record) {
        if (csvWriter != null) {
            csvWriter.writeRevenue(record);
        }
    }
}
