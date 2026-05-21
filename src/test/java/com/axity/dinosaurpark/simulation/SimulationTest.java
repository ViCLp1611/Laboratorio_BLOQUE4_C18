package com.axity.dinosaurpark.simulation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.axity.dinosaurpark.event.SimulationEvent;
import com.axity.dinosaurpark.model.CarnivoreDinosaur;
import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.Guard;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.zone.ArrivalZone;
import com.axity.dinosaurpark.zone.BathroomZone;
import com.axity.dinosaurpark.zone.CentralHub;
import com.axity.dinosaurpark.zone.ExperienceType;
import com.axity.dinosaurpark.zone.ObservationEnclosure;
import com.axity.dinosaurpark.zone.PowerPlant;

class SimulationTest {
    @TempDir
    Path tempDir;

    @Test
    void schedulerIsDeterministicAndSchedulesSeveralEvents() {
        EventScheduler first = new EventScheduler(42L, 20);
        EventScheduler second = new EventScheduler(42L, 20);

        Map<Integer, List<SimulationEvent>> firstEvents = first.getEventosProgramados();
        Map<Integer, List<SimulationEvent>> secondEvents = second.getEventosProgramados();

        assertEquals(firstEvents.keySet(), secondEvents.keySet());
        assertTrue(firstEvents.values().stream().mapToInt(List::size).sum() >= 10);
        assertFalse(first.checkForEvents(1).isEmpty() && first.checkForEvents(2).isEmpty()
                && first.checkForEvents(3).isEmpty() && first.checkForEvents(4).isEmpty());
    }

    @Test
    void parkStateCountsAndIncrementsStep() {
        Tourist active = new Tourist(1, "Ana");
        active.setStatus(TouristStatus.IN_PARK);
        Dinosaur dinosaur = new CarnivoreDinosaur(1, "Rex", "Tyrannosaurus");
        ParkState state = new ParkState(List.of(dinosaur), List.of(active), new PowerPlant(true), new CsvWriter(tempDir.toString()));

        assertEquals(1, state.countActiveTourists());
        assertEquals(1, state.countDinosaursInEnclosure());
        assertEquals(0, state.getCurrentStep());

        state.incrementStep();

        assertEquals(1, state.getCurrentStep());
    }

    @Test
    void simulationEngineRunsBasicLoop() {
        CsvWriter writer = new CsvWriter(tempDir.toString());
        Tourist tourist = new Tourist(1, "Ana");
        Dinosaur dinosaur = new CarnivoreDinosaur(1, "Rex", "Tyrannosaurus");
        ArrivalZone arrival = new ArrivalZone("Llegada", 5, 30.0);
        arrival.agregarACola(tourist);
        ParkState state = new ParkState(
                List.of(tourist),
                List.of(dinosaur),
                List.of(new Guard(1, "Guardia", 450.0)),
                arrival,
                new CentralHub("Centro", 5, 10.0, 0.0),
                new BathroomZone("Banos", 5, 1, 5.0, 0.0),
                new PowerPlant(true),
                List.of(new ObservationEnclosure("Basico", 5, 20.0, ExperienceType.BASIC, List.of(dinosaur))),
                writer,
                new Random(1),
                0,
                0.0,
                0.0);

        new SimulationEngine(state, new EventScheduler(1L, 1), 1, 1).run();

        assertEquals(2, state.getCurrentStep());
        assertTrue(state.getTotalRevenue() > 0);
    }
}
