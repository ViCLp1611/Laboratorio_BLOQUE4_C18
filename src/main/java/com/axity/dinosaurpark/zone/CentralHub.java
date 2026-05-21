package com.axity.dinosaurpark.zone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.time.LocalDateTime;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.RevenueRecord;

/*
 * Zona central del parque: registra visitas y puede generar ingresos por
 * souvenirs durante la visita del turista.
 */
public class CentralHub implements ParkZone {
    private final String name;
    private final int capacidadMaxima;
    private final double precioSouvenir;
    private final double probabilidadCompraSouvenir;
    private final List<Tourist> turistasActuales;

    public CentralHub(String name, int capacidadMaxima, double precioSouvenir, double probabilidadCompraSouvenir) {
        this.name = name;
        this.capacidadMaxima = capacidadMaxima;
        this.precioSouvenir = precioSouvenir;
        this.probabilidadCompraSouvenir = probabilidadCompraSouvenir;
        this.turistasActuales = new ArrayList<>();
    }

    public void visitar(Tourist tourist, Random rng, CsvWriter csvWriter) {
        if (tourist == null || !hasCapacity()) {
            return;
        }

        enter(tourist);
        tourist.registrarVisita(name);
        if (rng != null && rng.nextDouble() < probabilidadCompraSouvenir) {
            tourist.gastar(precioSouvenir);
            registrarIngreso(csvWriter, new RevenueRecord(
                    0L,
                    "SOUVENIR",
                    precioSouvenir,
                    tourist.getId(),
                    name,
                    LocalDateTime.now()));
        }
        exit(tourist);
    }

    public List<Tourist> getTuristasActuales() {
        return Collections.unmodifiableList(turistasActuales);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasCapacity() {
        return turistasActuales.size() < capacidadMaxima;
    }

    @Override
    public int getCurrentOccupancy() {
        return turistasActuales.size();
    }

    @Override
    public int getMaxCapacity() {
        return capacidadMaxima;
    }

    @Override
    public void enter(Tourist tourist) {
        if (tourist != null && hasCapacity()) {
            turistasActuales.add(tourist);
        }
    }

    @Override
    public void exit(Tourist tourist) {
        turistasActuales.remove(tourist);
    }

    private void registrarIngreso(CsvWriter csvWriter, RevenueRecord record) {
        if (csvWriter != null) {
            csvWriter.appendRevenue(record);
        }
    }
}

