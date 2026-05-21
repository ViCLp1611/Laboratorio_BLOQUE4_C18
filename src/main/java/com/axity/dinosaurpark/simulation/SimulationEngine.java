package com.axity.dinosaurpark.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.axity.dinosaurpark.event.SimulationEvent;
import com.axity.dinosaurpark.model.Guard;
import com.axity.dinosaurpark.model.Technician;
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

            procesarLlegadas();
            moverTuristas();
            ejecutarTickZonas();
            ejecutarEvento(step);
            ejecutarTrabajadores();
            ParkMonitor.displaySnapshot(state);

            state.incrementStep();
        }
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
        Optional<SimulationEvent> evento = scheduler.checkForEvent(step);
        evento.ifPresent(simulationEvent -> simulationEvent.execute(state, state.getRng()));
    }

    private void ejecutarTrabajadores() {
        for (Worker worker : state.getTrabajadores()) {
            if (worker instanceof Guard guard) {
                guard.recaptureEscapedDinosaurs(state.getDinosaurios());
            } else if (worker instanceof Technician technician) {
                technician.repairIfNeeded(state.getPowerPlant());
            }
        }
    }
}
