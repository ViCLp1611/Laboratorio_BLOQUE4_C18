package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

/*
 * Zona de llegada: administra la fila inicial, vende boletos y permite el
 * ingreso de turistas que pasan de WAITING a IN_PARK.
 */
public class ArrivalZone implements ParkZone {
    private final String name;
    private final int maxCapacity;
    private final double ticketPrice;
    private final Queue<Tourist> waitingQueue;
    private final List<Tourist> currentTourists;
    private long ticketSequence;

    public ArrivalZone(String name, int maxCapacity, double ticketPrice) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.ticketPrice = ticketPrice;
        this.waitingQueue = new ArrayDeque<>();
        this.currentTourists = new ArrayList<>();
        this.ticketSequence = 0;
    }

    public void addToQueue(Tourist tourist) {
        if (tourist != null) {
            waitingQueue.offer(tourist);
        }
    }

    public List<Tourist> processBatch(int batchSize, CsvWriter csvWriter) {
        List<Tourist> admittedTourists = new ArrayList<>();
        int processed = 0;

        while (processed < batchSize && hasCapacity() && !waitingQueue.isEmpty()) {
            Tourist tourist = waitingQueue.poll();
            enter(tourist);
            tourist.setStatus(TouristStatus.IN_PARK);
            tourist.recordVisit(name);
            tourist.spend(ticketPrice);
            ticketSequence++;
            writeRevenue(csvWriter, new RevenueRecord(ticketSequence, "TICKET", name, tourist.getId(), ticketPrice));
            admittedTourists.add(tourist);
            processed++;
        }

        return admittedTourists;
    }

    public int getWaitingQueueSize() {
        return waitingQueue.size();
    }

    public List<Tourist> getCurrentTourists() {
        return Collections.unmodifiableList(currentTourists);
    }

    public long getTicketSequence() {
        return ticketSequence;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasCapacity() {
        return currentTourists.size() < maxCapacity;
    }

    @Override
    public int getCurrentOccupancy() {
        return currentTourists.size();
    }

    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public void enter(Tourist tourist) {
        if (tourist != null && hasCapacity()) {
            currentTourists.add(tourist);
        }
    }

    @Override
    public void exit(Tourist tourist) {
        currentTourists.remove(tourist);
    }

    private void writeRevenue(CsvWriter csvWriter, RevenueRecord record) {
        if (csvWriter != null) {
            csvWriter.writeRevenue(record);
        }
    }
}
