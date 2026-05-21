package com.axity.dinosaurpark.zone;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.time.LocalDateTime;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.RevenueRecord;

/*
 * Zona de llegada: administra la fila inicial, vende boletos y permite el
 * ingreso de turistas que pasan de WAITING a IN_PARK.
 */
public class ArrivalZone implements ParkZone {
    private final String name;
    private final int capacidadMaxima;
    private final double precioBoleto;
    private final Queue<Tourist> colaEspera;
    private final List<Tourist> turistasActuales;
    private long secuenciaBoletos;

    public ArrivalZone(String name, int capacidadMaxima, double precioBoleto) {
        this.name = name;
        this.capacidadMaxima = capacidadMaxima;
        this.precioBoleto = precioBoleto;
        this.colaEspera = new ArrayDeque<>();
        this.turistasActuales = new ArrayList<>();
        this.secuenciaBoletos = 0;
    }

    public void agregarACola(Tourist tourist) {
        if (tourist != null) {
            colaEspera.offer(tourist);
        }
    }

    public List<Tourist> procesarLote(int tamanoLote, CsvWriter csvWriter) {
        List<Tourist> turistasAdmitidos = new ArrayList<>();
        int procesados = 0;

        while (procesados < tamanoLote && hasCapacity() && !colaEspera.isEmpty()) {
            Tourist tourist = colaEspera.poll();
            enter(tourist);
            tourist.setStatus(TouristStatus.IN_PARK);
            tourist.registrarVisita(name);
            tourist.gastar(precioBoleto);
            secuenciaBoletos++;
            registrarIngreso(csvWriter, new RevenueRecord(
                    secuenciaBoletos,
                    "TICKET",
                    precioBoleto,
                    tourist.getId(),
                    name,
                    LocalDateTime.now()));
            turistasAdmitidos.add(tourist);
            procesados++;
        }

        return turistasAdmitidos;
    }

    public List<Tourist> processBatch(int batchSize, CsvWriter csvWriter) {
        return procesarLote(batchSize, csvWriter);
    }

    public int getTamanoColaEspera() {
        return colaEspera.size();
    }

    public List<Tourist> getTuristasActuales() {
        return Collections.unmodifiableList(turistasActuales);
    }

    public long getSecuenciaBoletos() {
        return secuenciaBoletos;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasCapacity() {
        return turistasActuales.size() < capacidadMaxima;
    }

    @Override
    public int getCurrentOccupancy() {
        return turistasActuales.size();
    }

    @Override
    public int getMaxCapacity() {
        return capacidadMaxima;
    }

    @Override
    public void enter(Tourist tourist) {
        if (tourist != null && hasCapacity()) {
            turistasActuales.add(tourist);
        }
    }

    @Override
    public void exit(Tourist tourist) {
        turistasActuales.remove(tourist);
    }

    private void registrarIngreso(CsvWriter csvWriter, RevenueRecord record) {
        if (csvWriter != null) {
            csvWriter.appendRevenue(record);
        }
    }
}
