package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.SatisfactionSurvey;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/*
 * Recinto de observacion: cobra entrada, registra visita y genera una encuesta
 * segun el tipo de experiencia.
 */
public class ObservationEnclosure implements ParkZone {
    private final String name;
    private final int maxCapacity;
    private final double entryFee;
    private final ExperienceType experienceType;
    private final List<Tourist> currentTourists;
    private final List<Dinosaur> dinosaurs;

    public ObservationEnclosure(String name, int maxCapacity, double entryFee,
                                ExperienceType experienceType, List<Dinosaur> dinosaurs) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.entryFee = entryFee;
        this.experienceType = experienceType;
        this.currentTourists = new ArrayList<>();
        this.dinosaurs = new ArrayList<>();
        if (dinosaurs != null) {
            this.dinosaurs.addAll(dinosaurs);
        }
    }

    public SatisfactionSurvey visit(Tourist tourist, Random rng, CsvWriter csvWriter) {
        if (tourist == null || !hasCapacity()) {
            return null;
        }

        enter(tourist);
        tourist.spend(entryFee);
        writeRevenue(csvWriter, new RevenueRecord("ENCLOSURE", name, tourist.getId(), entryFee));
        tourist.recordVisit(name);
        SatisfactionSurvey survey = conductSurvey(tourist, rng);
        exit(tourist);
        return survey;
    }

    public SatisfactionSurvey conductSurvey(Tourist tourist, Random rng) {
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

    public List<Tourist> getCurrentTourists() {
        return Collections.unmodifiableList(currentTourists);
    }

    public List<Dinosaur> getDinosaurs() {
        return Collections.unmodifiableList(dinosaurs);
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
