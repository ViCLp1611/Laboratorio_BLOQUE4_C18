package com.axity.dinosaurpark.event;

import java.nio.file.Path;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.axity.dinosaurpark.model.CarnivoreDinosaur;
import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.zone.PowerPlant;

class EventTest {
    @TempDir
    Path tempDir;

    @Test
    void dinosaurEscapeChangesStateAndCanAttackTourist() {
        Dinosaur dinosaur = new CarnivoreDinosaur(1, "Rex", "Tyrannosaurus");
        Tourist tourist = new Tourist(1, "Ana");
        tourist.setStatus(TouristStatus.IN_PARK);
        ParkState state = new ParkState(List.of(dinosaur), List.of(tourist), new PowerPlant(true), new CsvWriter(tempDir.toString()));
        state.setPasoActual(7);

        new DinosaurEscapeEvent().execute(state, new FixedRandom());

        assertEquals(DinosaurStatus.ESCAPED, dinosaur.getStatus());
        assertEquals(TouristStatus.ATTACKED, tourist.getStatus());
        assertEquals(1, state.countEscapedDinosaurs());
        assertEquals(1, state.countAttackedTourists());
    }

    @Test
    void blackoutFailsPlantAndAddsExpense() {
        PowerPlant plant = new PowerPlant(true);
        ParkState state = new ParkState(List.of(), List.of(), plant, new CsvWriter(tempDir.toString()));

        new BlackoutEvent().execute(state, new Random(1));

        assertFalse(plant.estaOperativa());
        assertEquals(2000.0, state.getTotalExpenses());
    }

    @Test
    void stormRegistersEvacuationAndExpense() {
        Tourist tourist = new Tourist(1, "Ana");
        tourist.setStatus(TouristStatus.IN_PARK);
        ParkState state = new ParkState(List.of(), List.of(tourist), new PowerPlant(true), new CsvWriter(tempDir.toString()));

        new StormEvent().execute(state, new Random(1));

        assertEquals(List.of("Evacuacion"), tourist.getZonasVisitadas());
        assertEquals(500.0, state.getTotalExpenses());
        assertTrue(new StormEvent().toRecord(3).toCsvLine().contains("TORMENTA_TORRENCIAL"));
    }

    private static class FixedRandom extends Random {
        @Override
        public int nextInt(int bound) {
            return 0;
        }

        @Override
        public double nextDouble() {
            return 0.0;
        }
    }
}
