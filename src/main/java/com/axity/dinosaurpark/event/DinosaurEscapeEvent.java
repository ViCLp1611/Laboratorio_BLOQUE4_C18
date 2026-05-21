package com.axity.dinosaurpark.event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;

/*
 * Evento basico: un dinosaurio se escapa y puede atacar a un turista.
 */
public class DinosaurEscapeEvent implements SimulationEvent {
    @Override
    public String getName() {
        return "ESCAPE_DINOSAURIO";
    }

    @Override
    public String getDescription() {
        return "Un dinosaurio escapo de su recinto";
    }

    @Override
    public void execute(ParkState state, Random rng) {
        if (state == null) {
            return;
        }

        Random random = rng == null ? new Random() : rng;
        List<Dinosaur> candidatos = dinosauriosEnRecinto(state);
        if (candidatos.isEmpty()) {
            registrarEvento(state, "No habia dinosaurios disponibles para escapar", "");
            return;
        }

        Dinosaur dinosaur = candidatos.get(random.nextInt(candidatos.size()));
        dinosaur.escape();

        String afectados = dinosaur.getName();
        List<Tourist> turistasEnParque = turistasEnParque(state);
        if (!turistasEnParque.isEmpty() && random.nextDouble() < dinosaur.getDangerLevel()) {
            Tourist tourist = turistasEnParque.get(random.nextInt(turistasEnParque.size()));
            tourist.setStatus(TouristStatus.ATTACKED);
            afectados = afectados + "; turista atacado: " + tourist.getName();
        }

        registrarEvento(state, getDescription(), afectados);
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "", LocalDateTime.now());
    }

    private List<Dinosaur> dinosauriosEnRecinto(ParkState state) {
        List<Dinosaur> candidatos = new ArrayList<>();
        for (Dinosaur dinosaur : state.getDinosaurios()) {
            if (dinosaur.getStatus() == DinosaurStatus.IN_ENCLOSURE) {
                candidatos.add(dinosaur);
            }
        }
        return candidatos;
    }

    private List<Tourist> turistasEnParque(ParkState state) {
        List<Tourist> turistas = new ArrayList<>();
        for (Tourist tourist : state.getTuristas()) {
            if (tourist.getStatus() == TouristStatus.IN_PARK) {
                turistas.add(tourist);
            }
        }
        return turistas;
    }

    private void registrarEvento(ParkState state, String descripcion, String afectados) {
        CsvWriter csvWriter = state.getCsvWriter();
        if (csvWriter != null) {
            csvWriter.appendEvent(new EventRecord(
                    state.getPasoActual(),
                    getName(),
                    descripcion,
                    afectados,
                    LocalDateTime.now()));
        }
    }
}
