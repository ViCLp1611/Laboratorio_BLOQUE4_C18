package com.axity.dinosaurpark.zone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.time.LocalDateTime;

import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.SatisfactionSurvey;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.RevenueRecord;

/*
 * Recinto de observacion: cobra entrada, registra visita y genera una encuesta
 * segun el tipo de experiencia.
 */
public class ObservationEnclosure implements ParkZone {
    private final String name;
    private final int capacidadMaxima;
    private final double cuotaEntrada;
    private final ExperienceType experienceType;
    private final List<Tourist> turistasActuales;
    private final List<Dinosaur> dinosaurios;

    public ObservationEnclosure(String name, int capacidadMaxima, double cuotaEntrada,
                                ExperienceType experienceType, List<Dinosaur> dinosaurios) {
        this.name = name;
        this.capacidadMaxima = capacidadMaxima;
        this.cuotaEntrada = cuotaEntrada;
        this.experienceType = experienceType;
        this.turistasActuales = new ArrayList<>();
        this.dinosaurios = new ArrayList<>();
        if (dinosaurios != null) {
            this.dinosaurios.addAll(dinosaurios);
        }
    }

    public SatisfactionSurvey visitar(Tourist tourist, Random rng, CsvWriter csvWriter) {
        if (tourist == null || !hasCapacity()) {
            return null;
        }

        enter(tourist);
        tourist.gastar(cuotaEntrada);
        registrarIngreso(csvWriter, new RevenueRecord(
                0L,
                "ENCLOSURE",
                cuotaEntrada,
                tourist.getId(),
                name,
                LocalDateTime.now()));
        tourist.registrarVisita(name);
        SatisfactionSurvey survey = realizarEncuesta(tourist, rng);
        exit(tourist);
        return survey;
    }

    public SatisfactionSurvey visit(Tourist tourist, Random rng, CsvWriter csvWriter) {
        return visitar(tourist, rng, csvWriter);
    }

    public SatisfactionSurvey realizarEncuesta(Tourist tourist, Random rng) {
        Random random = rng == null ? new Random() : rng;
        int minScore;
        int maxScore;

        switch (experienceType) {
            case PREMIUM:
                minScore = 2;
                maxScore = 4;
                break;
            case VIP:
                minScore = 3;
                maxScore = 5;
                break;
            case BASIC:
            default:
                minScore = 1;
                maxScore = 3;
                break;
        }

        int score = minScore + random.nextInt(maxScore - minScore + 1);
        return new SatisfactionSurvey(tourist.getId(), name, score);
    }

    public List<Tourist> getTuristasActuales() {
        return Collections.unmodifiableList(turistasActuales);
    }

    public List<Dinosaur> getDinosaurios() {
        return Collections.unmodifiableList(dinosaurios);
    }

    public ExperienceType getExperienceType() {
        return experienceType;
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
