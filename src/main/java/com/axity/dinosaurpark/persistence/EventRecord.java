package com.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;

/*
 * Registro de evento del parque para guardar lo importante de la simulacion.
 */
public record EventRecord(
        long step,
        String eventName,
        String description,
        String affectedEntities,
        LocalDateTime timestamp) {

    public String toCsvLine() {
        return step + "," + escape(eventName) + "," + escape(description) + ","
                + escape(affectedEntities) + "," + timestamp;
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
