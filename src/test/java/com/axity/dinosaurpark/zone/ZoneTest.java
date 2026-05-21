package com.axity.dinosaurpark.zone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.axity.dinosaurpark.model.CarnivoreDinosaur;
import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.SatisfactionSurvey;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.CsvWriter;

class ZoneTest {
    @TempDir
    Path tempDir;

    @Test
    void arrivalZoneProcessesBatchAndRespectsCapacity() {
        CsvWriter writer = new CsvWriter(tempDir.toString());
        ArrivalZone arrival = new ArrivalZone("Llegada", 2, 30.0);
        Tourist first = new Tourist(1, "Uno");
        Tourist second = new Tourist(2, "Dos");
        Tourist third = new Tourist(3, "Tres");

        arrival.agregarACola(first);
        arrival.agregarACola(second);
        arrival.agregarACola(third);

        List<Tourist> admitted = arrival.processBatch(3, writer);

        assertEquals(2, admitted.size());
        assertEquals(1, arrival.getTamanoColaEspera());
        assertEquals(2, arrival.getCurrentOccupancy());
        assertFalse(arrival.hasCapacity());
        assertEquals(TouristStatus.IN_PARK, first.getStatus());
        assertEquals(30.0, first.getDineroGastado());
        assertEquals(List.of("Llegada"), first.getZonasVisitadas());

        arrival.exit(first);
        assertTrue(arrival.hasCapacity());
    }

    @Test
    void centralHubSellsSouvenirAndLeavesZone() {
        CsvWriter writer = new CsvWriter(tempDir.toString());
        CentralHub hub = new CentralHub("Centro", 1, 25.0, 1.0);
        Tourist tourist = new Tourist(1, "Ana");

        hub.visit(tourist, new Random(1), writer);

        assertEquals(25.0, tourist.getDineroGastado());
        assertEquals(List.of("Centro"), tourist.getZonasVisitadas());
        assertEquals(0, hub.getCurrentOccupancy());
    }

    @Test
    void bathroomOccupiesSlotSellsSpaAndFreesAfterTicks() {
        CsvWriter writer = new CsvWriter(tempDir.toString());
        BathroomZone bathroom = new BathroomZone("Banos", 1, 2, 15.0, 1.0);
        Tourist tourist = new Tourist(1, "Ana");

        assertTrue(bathroom.tryEnter(tourist, new Random(1), writer));
        assertFalse(bathroom.hasCapacity());
        assertEquals(15.0, tourist.getDineroGastado());

        bathroom.tick();
        assertEquals(1, bathroom.getCurrentOccupancy());
        bathroom.tick();
        assertEquals(0, bathroom.getCurrentOccupancy());
    }

    @Test
    void powerPlantConsumesEnergyFailsAndRepairs() {
        CsvWriter writer = new CsvWriter(tempDir.toString());
        PowerPlant plant = new PowerPlant("Planta", 1, 10.0, 2.0, 1.0, 100.0, 500.0);

        plant.tick(new Random(1), writer);

        assertFalse(plant.isOperational());
        assertEquals(8.0, plant.getEnergy());
        plant.reparar();
        assertTrue(plant.isOperational());
    }

    @Test
    void enclosureChargesEntryAndGeneratesSurveyInRange() {
        CsvWriter writer = new CsvWriter(tempDir.toString());
        Dinosaur dinosaur = new CarnivoreDinosaur(1, "Rex", "Tyrannosaurus");
        ObservationEnclosure enclosure = new ObservationEnclosure(
                "VIP", 1, 80.0, ExperienceType.VIP, List.of(dinosaur));
        Tourist tourist = new Tourist(1, "Ana");

        SatisfactionSurvey survey = enclosure.visit(tourist, new Random(1), writer);

        assertNotNull(survey);
        assertEquals(80.0, tourist.getDineroGastado());
        assertEquals(0, enclosure.getCurrentOccupancy());
        assertTrue(survey.getPuntaje() >= 3 && survey.getPuntaje() <= 5);
        assertEquals(List.of(dinosaur), enclosure.getDinosaurios());
    }
}
