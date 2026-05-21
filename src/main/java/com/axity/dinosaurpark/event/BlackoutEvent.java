package com.axity.dinosaurpark.event;

import java.time.LocalDateTime;
import java.util.Random;

import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.zone.PowerPlant;

/*
 * Evento basico: la energia falla y se registra un gasto operativo.
 */
public class BlackoutEvent implements SimulationEvent {
    private static final double GASTO_OPERATIVO = 2000.0;

    @Override
    public String getName() {
        return "APAGON_MASIVO";
    }

    @Override
    public String getDescription() {
        return "Falla electrica general en el parque";
    }

    @Override
    public void execute(ParkState state, Random rng) {
        if (state == null) {
            return;
        }

        CsvWriter csvWriter = state.getCsvWriter();
        PowerPlant plantaEnergia = state.getPlantaEnergia();
        if (plantaEnergia != null) {
            plantaEnergia.triggerFailure(csvWriter);
        }

        if (csvWriter != null) {
            csvWriter.appendExpense(new ExpenseRecord(
                    0L,
                    "BLACKOUT_OPERATION",
                    GASTO_OPERATIVO,
                    getDescription(),
                    LocalDateTime.now()));
            csvWriter.appendEvent(toRecord(state.getPasoActual()));
        }
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "PowerPlant", LocalDateTime.now());
    }
}
