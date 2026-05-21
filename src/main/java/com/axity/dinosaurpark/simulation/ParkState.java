package com.axity.dinosaurpark.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.model.Worker;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.zone.ArrivalZone;
import com.axity.dinosaurpark.zone.BathroomZone;
import com.axity.dinosaurpark.zone.CentralHub;
import com.axity.dinosaurpark.zone.ObservationEnclosure;
import com.axity.dinosaurpark.zone.PowerPlant;

/*
 * Estado global del parque durante la simulacion.
 * Aqui se guardan las entidades principales, las zonas y los acumulados basicos.
 */
public class ParkState {
    private final List<Tourist> turistas;
    private final List<Dinosaur> dinosaurios;
    private final List<Worker> trabajadores;
    private final ArrivalZone arrivalZone;
    private final CentralHub centralHub;
    private final BathroomZone bathroomZone;
    private final PowerPlant powerPlant;
    private final List<ObservationEnclosure> recintos;
    private final CsvWriter csvWriter;
    private final Random rng;
    private long currentStep;
    private double totalRevenue;
    private double totalExpenses;

    public ParkState(List<Tourist> turistas, List<Dinosaur> dinosaurios, List<Worker> trabajadores,
                     ArrivalZone arrivalZone, CentralHub centralHub, BathroomZone bathroomZone,
                     PowerPlant powerPlant, List<ObservationEnclosure> recintos,
                     CsvWriter csvWriter, Random rng, long currentStep,
                     double totalRevenue, double totalExpenses) {
        this.turistas = copiarLista(turistas);
        this.dinosaurios = copiarLista(dinosaurios);
        this.trabajadores = copiarLista(trabajadores);
        this.arrivalZone = arrivalZone;
        this.centralHub = centralHub;
        this.bathroomZone = bathroomZone;
        this.powerPlant = powerPlant;
        this.recintos = copiarLista(recintos);
        this.csvWriter = csvWriter;
        this.rng = rng == null ? new Random() : rng;
        this.currentStep = currentStep;
        this.totalRevenue = totalRevenue;
        this.totalExpenses = totalExpenses;
    }

    public ParkState(List<Dinosaur> dinosaurios, List<Tourist> turistas,
                     PowerPlant powerPlant, CsvWriter csvWriter) {
        this(turistas, dinosaurios, Collections.emptyList(), null, null, null, powerPlant,
                Collections.emptyList(), csvWriter, new Random(), 0L, 0.0, 0.0);
    }

    public List<Tourist> getTuristas() {
        return Collections.unmodifiableList(turistas);
    }

    public List<Dinosaur> getDinosaurios() {
        return Collections.unmodifiableList(dinosaurios);
    }

    public List<Worker> getTrabajadores() {
        return Collections.unmodifiableList(trabajadores);
    }

    public ArrivalZone getArrivalZone() {
        return arrivalZone;
    }

    public CentralHub getCentralHub() {
        return centralHub;
    }

    public BathroomZone getBathroomZone() {
        return bathroomZone;
    }

    public PowerPlant getPowerPlant() {
        return powerPlant;
    }

    public PowerPlant getPlantaEnergia() {
        return powerPlant;
    }

    public List<ObservationEnclosure> getRecintos() {
        return Collections.unmodifiableList(recintos);
    }

    public CsvWriter getCsvWriter() {
        return csvWriter;
    }

    public Random getRng() {
        return rng;
    }

    public long getCurrentStep() {
        return currentStep;
    }

    public long getPasoActual() {
        return currentStep;
    }

    public void setPasoActual(long pasoActual) {
        this.currentStep = pasoActual;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void addRevenue(double amount) {
        if (amount > 0) {
            totalRevenue += amount;
        }
    }

    public void addExpense(double amount) {
        if (amount > 0) {
            totalExpenses += amount;
        }
    }

    public void incrementStep() {
        currentStep++;
    }

    public int countActiveTourists() {
        int total = 0;
        for (Tourist tourist : turistas) {
            if (tourist.getStatus() == TouristStatus.IN_PARK) {
                total++;
            }
        }
        return total;
    }

    public int countDinosaursInEnclosure() {
        int total = 0;
        for (Dinosaur dinosaur : dinosaurios) {
            if (dinosaur.getStatus() == DinosaurStatus.IN_ENCLOSURE) {
                total++;
            }
        }
        return total;
    }

    private static <T> List<T> copiarLista(List<T> valores) {
        if (valores == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(valores);
    }
}
