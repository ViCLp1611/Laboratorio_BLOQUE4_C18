package com.axity.dinosaurpark.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.axity.dinosaurpark.zone.PowerPlant;

class ModelTest {
    @Test
    void touristStartsWaitingAndTracksSpendingAndVisits() {
        Tourist tourist = new Tourist(1, "Ana");

        assertEquals(1, tourist.getId());
        assertEquals("Ana", tourist.getName());
        assertEquals(TouristStatus.WAITING, tourist.getStatus());
        assertEquals(0.0, tourist.getDineroGastado());

        tourist.gastar(25.0);
        tourist.gastar(-10.0);
        tourist.registrarVisita("Centro");
        tourist.registrarVisita("");
        tourist.setStatus(TouristStatus.IN_PARK);

        assertEquals(25.0, tourist.getDineroGastado());
        assertEquals(List.of("Centro"), tourist.getZonasVisitadas());
        assertEquals(TouristStatus.IN_PARK, tourist.getStatus());
        assertThrows(UnsupportedOperationException.class, () -> tourist.getZonasVisitadas().add("VIP"));
    }

    @Test
    void dinosaursExposeDietDangerAndStateChanges() {
        Dinosaur carnivore = new CarnivoreDinosaur(10, "Rex", "Tyrannosaurus");
        Dinosaur herbivore = new HerbivoreDinosaur(20, "Tri", "Triceratops");

        assertEquals(DinosaurStatus.IN_ENCLOSURE, carnivore.getStatus());
        assertEquals("CARNIVORE", carnivore.getDiet());
        assertEquals(0.9, carnivore.getDangerLevel());
        assertEquals(500.0, carnivore.getFeedingCostPerDay());

        carnivore.escape();
        assertEquals(DinosaurStatus.ESCAPED, carnivore.getStatus());
        carnivore.recapture();
        assertEquals(DinosaurStatus.RECAPTURED, carnivore.getStatus());
        carnivore.returnToEnclosure();
        assertEquals(DinosaurStatus.IN_ENCLOSURE, carnivore.getStatus());

        assertEquals("HERBIVORE", herbivore.getDiet());
        assertEquals(0.2, herbivore.getDangerLevel());
        assertEquals(200.0, herbivore.getFeedingCostPerDay());
    }

    @Test
    void guardRecapturesEscapedDinosaursAndTechnicianRepairsPlant() {
        Dinosaur dinosaur = new CarnivoreDinosaur(1, "Rex", "Tyrannosaurus");
        dinosaur.escape();

        Guard guard = new Guard(1, "Guardia", 450.0);
        guard.recaptureEscapedDinosaurs(List.of(dinosaur));

        assertEquals("GUARD", guard.getRole());
        assertEquals(DinosaurStatus.IN_ENCLOSURE, dinosaur.getStatus());
        assertEquals(450.0, guard.getSalarioDiario());

        PowerPlant plant = new PowerPlant(false);
        Technician technician = new Technician(2, "Tecnico", 500.0);
        technician.repairIfNeeded(plant);

        assertEquals("TECNICO", technician.getRole());
        assertEquals(true, plant.isOperational());
    }

    @Test
    void ticketAndSurveyKeepConstructorValues() {
        LocalDateTime issuedAt = LocalDateTime.of(2026, 5, 21, 10, 0);
        Ticket ticket = new Ticket(100L, 7, 30.0, "BASIC", issuedAt);
        SatisfactionSurvey survey = new SatisfactionSurvey(7, "Recinto Basico", 3);

        assertEquals(100L, ticket.getId());
        assertEquals(7, ticket.getTuristaId());
        assertEquals(30.0, ticket.getPrecio());
        assertEquals("BASIC", ticket.getCategoria());
        assertEquals(issuedAt, ticket.getEmitidoEn());

        assertEquals(7, survey.getTuristaId());
        assertEquals("Recinto Basico", survey.getRecintoNombre());
        assertEquals(3, survey.getPuntaje());
    }
}
