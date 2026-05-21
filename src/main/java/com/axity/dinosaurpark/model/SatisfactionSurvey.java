package com.axity.dinosaurpark.model;

/*
 * Encuesta inmutable asociada a un turista y a un recinto visitado.
 */
public class SatisfactionSurvey {
    private final int turistaId;
    private final String recintoNombre;
    private final int puntaje;

    public SatisfactionSurvey(int turistaId, String recintoNombre, int puntaje) {
        this.turistaId = turistaId;
        this.recintoNombre = recintoNombre;
        this.puntaje = puntaje;
    }

    public int getTuristaId() {
        return turistaId;
    }

    public String getRecintoNombre() {
        return recintoNombre;
    }

    public int getPuntaje() {
        return puntaje;
    }
}
