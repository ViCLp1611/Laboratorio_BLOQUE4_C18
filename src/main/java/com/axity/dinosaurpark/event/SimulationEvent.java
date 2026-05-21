package com.axity.dinosaurpark.event;

import java.util.Random;

import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;

public interface SimulationEvent {
    String getName();

    String getDescription();

    void execute(ParkState state, Random rng);

    EventRecord toRecord(long step);
}
