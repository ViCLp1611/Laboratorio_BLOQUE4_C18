package com.axity.dinosaurpark.simulation;

import java.util.ArrayList;
import java.util.List;

import com.axity.dinosaurpark.event.SimulationEvent;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.model.Worker;
import com.axity.dinosaurpark.monitoring.ParkMonitor;
import com.axity.dinosaurpark.zone.ObservationEnclosure;

/*
 * Motor principal del nivel basico.
 * Ejecuta el ciclo de llegadas, movimientos, zonas, eventos, trabajadores y monitoreo.
 */
public class SimulationEngine {
    private final ParkState state;
    private final EventScheduler scheduler;
    private final int totalSteps;
    private final int batchSize;

    public SimulationEngine(ParkState state, EventScheduler scheduler, int totalSteps, int batchSize) {
        this.state = state;
        this.scheduler = scheduler;
        this.totalSteps = totalSteps;
        this.batchSize = batchSize;
    }

    public void run() {
        for (int step = 1; step <= totalSteps; step++) {
            state.setPasoActual(step);

            double ingresosAntes = calcularDineroGastado();
            procesarLlegadas();
            moverTuristas();
            state.addRevenue(calcularDineroGastado() - ingresosAntes);
            ejecutarTickZonas();
            ejecutarEvento(step);
            ParkMonitor.displaySnapshot(state);
            ejecutarTrabajadores();

            state.incrementStep();
        }
    }

    private double calcularDineroGastado() {
        double total = 0.0;
        for (Tourist tourist : state.getTuristas()) {
            total += tourist.getDineroGastado();
        }
        return total;
    }

    private void procesarLlegadas() {
        if (state.getArrivalZone() != null) {
            state.getArrivalZone().processBatch(batchSize, state.getCsvWriter());
        }
    }

    private void moverTuristas() {
        List<Tourist> copiaTuristas = new ArrayList<>(state.getTuristas());
        for (Tourist tourist : copiaTuristas) {
            if (tourist.getStatus() != TouristStatus.IN_PARK) {
                continue;
            }

            if (state.getCentralHub() != null) {
                state.getCentralHub().visit(tourist, state.getRng(), state.getCsvWriter());
            }
            if (state.getBathroomZone() != null) {
                state.getBathroomZone().tryEnter(tourist, state.getRng(), state.getCsvWriter());
            }

            ObservationEnclosure recinto = elegirRecinto(tourist);
            if (recinto != null) {
                recinto.visit(tourist, state.getRng(), state.getCsvWriter());
            }
        }
    }

    private ObservationEnclosure elegirRecinto(Tourist tourist) {
        List<ObservationEnclosure> recintos = state.getRecintos();
        if (recintos.isEmpty()) {
            return null;
        }
        int indice = Math.floorMod(tourist.getId(), recintos.size());
        return recintos.get(indice);
    }

    private void ejecutarTickZonas() {
        if (state.getBathroomZone() != null) {
            state.getBathroomZone().tick();
        }
        if (state.getPowerPlant() != null) {
            state.getPowerPlant().tick(state.getRng(), state.getCsvWriter());
        }
    }

    private void ejecutarEvento(int step) {
        List<SimulationEvent> eventos = scheduler.checkForEvents(step);
        for (SimulationEvent evento : eventos) {
            evento.execute(state, state.getRng());
        }
    }

    private void ejecutarTrabajadores() {
        for (Worker worker : state.getTrabajadores()) {
            worker.ejecutarTarea(state);
        }
    }
}
