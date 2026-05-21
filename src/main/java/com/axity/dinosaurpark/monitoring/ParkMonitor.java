package com.axity.dinosaurpark.monitoring;

import com.axity.dinosaurpark.simulation.ParkState;

/*
 * Monitor basico de consola. Sirve para ver una foto rapida del parque por paso.
 */
public final class ParkMonitor {
    private ParkMonitor() {
    }

    public static void displaySnapshot(ParkState state) {
        if (state == null) {
            return;
        }

        System.out.println("Paso " + state.getCurrentStep()
                + " | turistas activos: " + state.countActiveTourists()
                + " | dinosaurios en recinto: " + state.countDinosaursInEnclosure()
                + " | ingresos: " + state.getTotalRevenue()
                + " | gastos: " + state.getTotalExpenses());
    }
}
