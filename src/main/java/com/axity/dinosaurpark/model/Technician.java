package com.axity.dinosaurpark.model;

import com.axity.dinosaurpark.zone.PowerPlant;

/*
 * Tecnico del parque: atiende la planta electrica en la version basica.
 */
public class Technician extends Worker {
    public Technician(int id, String name, double dailySalary) {
        super(id, name, dailySalary);
    }

    @Override
    public String getRole() {
        return "TECHNICIAN";
    }

    public void repairIfNeeded(PowerPlant plant) {
        if (plant != null && !plant.isOperational()) {
            plant.repair();
        }
    }
}
