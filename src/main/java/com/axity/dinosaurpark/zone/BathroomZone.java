package com.axity.dinosaurpark.zone;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.RevenueRecord;

/*
 * Zona de banos: usa cupos temporales y libera cada turista despues de cierto
 * numero de pasos de simulacion.
 */
public class BathroomZone implements ParkZone {
    private final String name;
    private final int capacidadMaxima;
    private final int duracionUsoPasos;
    private final double precioSpa;
    private final double probabilidadCompraSpa;
    private final Map<Tourist, Integer> cuposOcupados;

    public BathroomZone(String name, int capacidadMaxima, int duracionUsoPasos, double precioSpa, double probabilidadCompraSpa) {
        this.name = name;
        this.capacidadMaxima = capacidadMaxima;
        this.duracionUsoPasos = duracionUsoPasos;
        this.precioSpa = precioSpa;
        this.probabilidadCompraSpa = probabilidadCompraSpa;
        this.cuposOcupados = new HashMap<>();
    }

    public boolean intentarEntrar(Tourist tourist, Random rng, CsvWriter csvWriter) {
        if (tourist == null || !hasCapacity()) {
            return false;
        }

        enter(tourist);
        tourist.registrarVisita(name);
        if (rng != null && rng.nextDouble() < probabilidadCompraSpa) {
            tourist.gastar(precioSpa);
            registrarIngreso(csvWriter, new RevenueRecord(
                    0L,
                    "SPA",
                    precioSpa,
                    tourist.getId(),
                    name,
                    LocalDateTime.now()));
        }
        return true;
    }

    public boolean tryEnter(Tourist tourist, Random rng, CsvWriter csvWriter) {
        return intentarEntrar(tourist, rng, csvWriter);
    }

    public void tick() {
        Iterator<Map.Entry<Tourist, Integer>> iterator = cuposOcupados.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Tourist, Integer> entry = iterator.next();
            int pasosRestantes = entry.getValue() - 1;
            if (pasosRestantes <= 0) {
                iterator.remove();
            } else {
                entry.setValue(pasosRestantes);
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasCapacity() {
        return cuposOcupados.size() < capacidadMaxima;
    }

    @Override
    public int getCurrentOccupancy() {
        return cuposOcupados.size();
    }

    @Override
    public int getMaxCapacity() {
        return capacidadMaxima;
    }

    @Override
    public void enter(Tourist tourist) {
        if (tourist != null && hasCapacity()) {
            cuposOcupados.put(tourist, duracionUsoPasos);
        }
    }

    @Override
    public void exit(Tourist tourist) {
        cuposOcupados.remove(tourist);
    }

    private void registrarIngreso(CsvWriter csvWriter, RevenueRecord record) {
        if (csvWriter != null) {
            csvWriter.appendRevenue(record);
        }
    }
}
