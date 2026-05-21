package com.axity.dinosaurpark.event;

import java.time.LocalDateTime;
import java.util.Random;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.simulation.ParkState;

/*
 * Evento basico: una tormenta obliga a mover turistas a evacuacion.
 */
public class StormEvent implements SimulationEvent {
    private static final double GASTO_OPERATIVO = 500.0;

    @Override
    public String getName() {
        return "TORMENTA_TORRENCIAL";
    }

    @Override
    public String getDescription() {
        return "Tormenta fuerte obliga a evacuar zonas del parque";
    }

    @Override
    public void execute(ParkState state, Random rng) {
        if (state == null) {
            return;
        }

        int turistasEvacuados = 0;
        for (Tourist tourist : state.getTuristas()) {
            if (tourist.getStatus() == TouristStatus.IN_PARK) {
                tourist.registrarVisita("Evacuacion");
                turistasEvacuados++;
            }
        }

        CsvWriter csvWriter = state.getCsvWriter();
        if (csvWriter != null) {
            csvWriter.appendExpense(new ExpenseRecord(
                    0L,
                    "STORM_OPERATION",
                    GASTO_OPERATIVO,
                    getDescription(),
                    LocalDateTime.now()));
            csvWriter.appendEvent(new EventRecord(
                    state.getPasoActual(),
                    getName(),
                    getDescription(),
                    "Turistas evacuados: " + turistasEvacuados,
                    LocalDateTime.now()));
        }
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "", LocalDateTime.now());
    }
}
