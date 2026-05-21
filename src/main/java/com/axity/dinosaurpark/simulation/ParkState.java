package com.axity.dinosaurpark.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.zone.PowerPlant;

/*
 * Estado minimo compartido por los eventos basicos.
 * Mas adelante puede crecer con nuevas zonas o metricas de la simulacion.
 */
public class ParkState {
    private final List<Dinosaur> dinosaurios;
    private final List<Tourist> turistas;
    private final PowerPlant plantaEnergia;
    private final CsvWriter csvWriter;
    private long pasoActual;

    public ParkState(List<Dinosaur> dinosaurios, List<Tourist> turistas,
                     PowerPlant plantaEnergia, CsvWriter csvWriter) {
        this.dinosaurios = new ArrayList<>();
        this.turistas = new ArrayList<>();
        if (dinosaurios != null) {
            this.dinosaurios.addAll(dinosaurios);
        }
        if (turistas != null) {
            this.turistas.addAll(turistas);
        }
        this.plantaEnergia = plantaEnergia;
        this.csvWriter = csvWriter;
        this.pasoActual = 0L;
    }

    public List<Dinosaur> getDinosaurios() {
        return Collections.unmodifiableList(dinosaurios);
    }

    public List<Tourist> getTuristas() {
        return Collections.unmodifiableList(turistas);
    }

    public PowerPlant getPlantaEnergia() {
        return plantaEnergia;
    }

    public CsvWriter getCsvWriter() {
        return csvWriter;
    }

    public long getPasoActual() {
        return pasoActual;
    }

    public void setPasoActual(long pasoActual) {
        this.pasoActual = pasoActual;
    }
}
