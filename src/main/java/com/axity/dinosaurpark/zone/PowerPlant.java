package com.axity.dinosaurpark.zone;

/*
 * Version basica de la planta electrica necesaria para que el tecnico
 * pueda verificar su estado y repararla.
 */
public class PowerPlant {
    private boolean operational;

    public PowerPlant(boolean operational) {
        this.operational = operational;
    }

    public boolean isOperational() {
        return operational;
    }

    public void repair() {
        operational = true;
    }
}
