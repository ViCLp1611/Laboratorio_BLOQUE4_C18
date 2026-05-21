package com.axity.dinosaurpark.model;

import java.util.List;

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
