package com.axity.dinosaurpark.simulation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import com.axity.dinosaurpark.event.BlackoutEvent;
import com.axity.dinosaurpark.event.DinosaurEscapeEvent;
import com.axity.dinosaurpark.event.SimulationEvent;
import com.axity.dinosaurpark.event.StormEvent;

/*
 * Agenda eventos de forma determinista usando una semilla.
 * No usa probabilidades: solo asigna un paso aleatorio a cada evento basico.
 */
public class EventScheduler {
    private final Map<Integer, SimulationEvent> eventosProgramados;

    public EventScheduler(long seed, int totalSteps) {
        this.eventosProgramados = new HashMap<>();
        Random rng = new Random(seed);
        int limite = Math.max(1, totalSteps);
        programarEvento(rng, limite, new DinosaurEscapeEvent());
        programarEvento(rng, limite, new BlackoutEvent());
        programarEvento(rng, limite, new StormEvent());
    }

    public Optional<SimulationEvent> checkForEvent(int step) {
        return Optional.ofNullable(eventosProgramados.get(step));
    }

    public Map<Integer, SimulationEvent> getEventosProgramados() {
        return Map.copyOf(eventosProgramados);
    }

    private void programarEvento(Random rng, int totalSteps, SimulationEvent event) {
        if (eventosProgramados.size() >= totalSteps) {
            return;
        }

        int step = 1 + rng.nextInt(totalSteps);
        while (eventosProgramados.containsKey(step)) {
            step++;
            if (step > totalSteps) {
                step = 1;
            }
        }
        eventosProgramados.put(step, event);
    }
}
