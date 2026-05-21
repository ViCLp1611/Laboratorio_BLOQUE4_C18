package com.axity.dinosaurpark.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import com.axity.dinosaurpark.event.BlackoutEvent;
import com.axity.dinosaurpark.event.DinosaurEscapeEvent;
import com.axity.dinosaurpark.event.SimulationEvent;
import com.axity.dinosaurpark.event.StormEvent;

/*
 * Agenda eventos de forma determinista usando una semilla.
 * No usa probabilidades en tiempo de ejecucion: solo reparte varios eventos
 * aleatorios dentro del rango total de pasos.
 */
public class EventScheduler {
    private final Map<Integer, List<SimulationEvent>> eventosProgramados;

    public EventScheduler(long seed, int totalSteps) {
        this.eventosProgramados = new HashMap<>();
        Random rng = new Random(seed);
        int limite = Math.max(1, totalSteps);

        programarEventos(rng, limite, 5 + rng.nextInt(6), DinosaurEscapeEvent::new);
        programarEventos(rng, limite, 3 + rng.nextInt(3), StormEvent::new);
        programarEventos(rng, limite, 2 + rng.nextInt(3), BlackoutEvent::new);
    }

    public List<SimulationEvent> checkForEvents(int step) {
        return eventosProgramados.getOrDefault(step, Collections.emptyList());
    }

    public Optional<SimulationEvent> checkForEvent(int step) {
        List<SimulationEvent> eventos = checkForEvents(step);
        if (eventos.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(eventos.get(0));
    }

    public Map<Integer, List<SimulationEvent>> getEventosProgramados() {
        Map<Integer, List<SimulationEvent>> copia = new HashMap<>();
        for (Map.Entry<Integer, List<SimulationEvent>> entry : eventosProgramados.entrySet()) {
            copia.put(entry.getKey(), List.copyOf(entry.getValue()));
        }
        return Map.copyOf(copia);
    }

    private void programarEventos(Random rng, int totalSteps, int cantidad, EventFactory factory) {
        for (int i = 0; i < cantidad; i++) {
            int step = 1 + rng.nextInt(totalSteps);
            eventosProgramados
                    .computeIfAbsent(step, ignored -> new ArrayList<>())
                    .add(factory.create());
        }
    }

    private interface EventFactory {
        SimulationEvent create();
    }
}
