package com.axity.dinosaurpark.monitoring;

import com.axity.dinosaurpark.simulation.ParkState;

/*
 * Monitor basico de consola.
 * Muestra una foto clara del estado del parque en cada paso de simulacion.
 */
public final class ParkMonitor {
    private ParkMonitor() {
    }

    public static void displaySnapshot(ParkState state) {
        if (state == null) {
            return;
        }

        double energiaDisponible = 0.0;
        boolean plantaOperativa = false;
        if (state.getPowerPlant() != null) {
            energiaDisponible = state.getPowerPlant().getEnergy();
            plantaOperativa = state.getPowerPlant().isOperational();
        }

        System.out.println("==============================");
        System.out.println("ESTADO DEL PARQUE");
        System.out.println("==============================");
        System.out.println("Step actual: " + state.getCurrentStep());
        System.out.println("Turistas activos: " + state.countActiveTourists());
        System.out.println("Dinosaurios en recinto: " + state.countDinosaursInEnclosure());
        System.out.println("Dinosaurios escapados: " + state.countEscapedDinosaurs());
        System.out.println("Turistas atacados: " + state.countAttackedTourists());
        System.out.println("Energia disponible: " + energiaDisponible);
        System.out.println("Ingresos totales: " + state.getTotalRevenue());
        System.out.println("Gastos totales: " + state.getTotalExpenses());
        System.out.println("Planta operativa: " + plantaOperativa);
        System.out.println("==============================");
    }
}
