package com.axity.dinosaurpark.model;

import java.util.List;

import com.axity.dinosaurpark.simulation.ParkState;

/*
 * Guardia del parque: revisa dinosaurios escapados y los devuelve al encierro.
 */
public class Guard extends Worker {
    public Guard(int id, String name, double dailySalary) {
        super(id, name, dailySalary);
    }

    @Override
    public String getRole() {
        return "GUARD";
    }

    @Override
    public void ejecutarTarea(ParkState state) {
        recaptureEscapedDinosaurs(state.getDinosaurios());
    }

    public void recaptureEscapedDinosaurs(List<Dinosaur> dinosaurs) {
        if (dinosaurs == null) {
            return;
        }

        for (Dinosaur dinosaur : dinosaurs) {
            if (dinosaur != null && dinosaur.getStatus() == DinosaurStatus.ESCAPED) {
                dinosaur.returnToEnclosure();
            }
        }
    }
}
