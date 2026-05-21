package com.axity.dinosaurpark.model;

import com.axity.dinosaurpark.simulation.ParkState;
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
        return "TECNICO";
    }

    @Override
    public void ejecutarTarea(ParkState state) {
        repararSiEsNecesario(state.getPowerPlant());
    }

    public void repararSiEsNecesario(PowerPlant plant) {
        if (plant != null && !plant.estaOperativa()) {
            plant.reparar();
        }
    }

    public void repairIfNeeded(PowerPlant plant) {
        repararSiEsNecesario(plant);
    }
}
