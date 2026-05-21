package com.axity.dinosaurpark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.CarnivoreDinosaur;
import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.Guard;
import com.axity.dinosaurpark.model.HerbivoreDinosaur;
import com.axity.dinosaurpark.model.Technician;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.Worker;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.simulation.EventScheduler;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.simulation.SimulationEngine;
import com.axity.dinosaurpark.zone.ArrivalZone;
import com.axity.dinosaurpark.zone.BathroomZone;
import com.axity.dinosaurpark.zone.CentralHub;
import com.axity.dinosaurpark.zone.ExperienceType;
import com.axity.dinosaurpark.zone.ObservationEnclosure;
import com.axity.dinosaurpark.zone.PowerPlant;

public class Main {
    public static void main(String[] args) {
        ParkConfig config = ParkConfig.getInstance();
        long seed = config.getSeed();
        int totalSteps = config.getTotalSteps();
        int totalTuristas = config.getInt("tourists", 50);

        Random rng = new Random(seed);
        CsvWriter csvWriter = new CsvWriter("output");

        List<Tourist> turistas = crearTuristas(totalTuristas);
        List<Dinosaur> dinosaurios = crearDinosaurios();
        List<Worker> trabajadores = crearTrabajadores();

        ArrivalZone arrivalZone = new ArrivalZone("Llegada", 100, 30.0);
        for (Tourist tourist : turistas) {
            arrivalZone.agregarACola(tourist);
        }

        CentralHub centralHub = new CentralHub("Centro del Parque", 80, 25.0, 0.35);
        BathroomZone bathroomZone = new BathroomZone("Banos", 12, 2, 15.0, 0.20);
        PowerPlant powerPlant = new PowerPlant("Planta Electrica", 1, 100.0, 1.5, 0.03, 100.0, 500.0);
        List<ObservationEnclosure> recintos = crearRecintos(dinosaurios);

        ParkState state = new ParkState(
                turistas,
                dinosaurios,
                trabajadores,
                arrivalZone,
                centralHub,
                bathroomZone,
                powerPlant,
                recintos,
                csvWriter,
                rng,
                0L,
                0.0,
                0.0);

        EventScheduler scheduler = new EventScheduler(seed, totalSteps);
        SimulationEngine engine = new SimulationEngine(state, scheduler, totalSteps, 5);
        engine.run();

        System.out.println("Simulación finalizada");
    }

    /*
     * Datos iniciales sencillos para correr el laboratorio sin depender de BD.
     */
    private static List<Tourist> crearTuristas(int totalTuristas) {
        List<Tourist> turistas = new ArrayList<>();
        for (int i = 1; i <= totalTuristas; i++) {
            turistas.add(new Tourist(i, "Turista " + i));
        }
        return turistas;
    }

    private static List<Dinosaur> crearDinosaurios() {
        List<Dinosaur> dinosaurios = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            dinosaurios.add(new CarnivoreDinosaur(i, "Carnivoro " + i, "Tyrannosaurus"));
        }
        for (int i = 1; i <= 15; i++) {
            dinosaurios.add(new HerbivoreDinosaur(100 + i, "Herbivoro " + i, "Triceratops"));
        }
        return dinosaurios;
    }

    private static List<Worker> crearTrabajadores() {
        return Arrays.asList(
                new Guard(1, "Guardia Ana", 450.0),
                new Guard(2, "Guardia Luis", 450.0),
                new Technician(3, "Tecnico Marta", 500.0),
                new Technician(4, "Tecnico Diego", 500.0));
    }

    private static List<ObservationEnclosure> crearRecintos(List<Dinosaur> dinosaurios) {
        return Arrays.asList(
                new ObservationEnclosure("Recinto Basico", 30, 20.0, ExperienceType.BASIC, dinosaurios),
                new ObservationEnclosure("Recinto Premium", 20, 45.0, ExperienceType.PREMIUM, dinosaurios),
                new ObservationEnclosure("Recinto VIP", 10, 80.0, ExperienceType.VIP, dinosaurios));
    }
}
