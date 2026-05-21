package com.axity.dinosaurpark.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ParkConfig {
    private static final String CONFIG_FILE = "park.properties";
    private static final String SEED_KEY = "simulation.seed";
    private static final String TOTAL_STEPS_KEY = "simulation.totalSteps";

    private static ParkConfig instance;
    private final Properties props;

    /*
     * Singleton: el constructor privado evita crear configuraciones desde fuera.
     * La unica instancia se obtiene mediante getInstance(), centralizando la
     * lectura de park.properties para todo el parque.
     */
    private ParkConfig() {
        props = new Properties();
        loadProperties();
    }

    /*
     * Singleton lazy: crea la instancia solo la primera vez que se solicita y
     * reutiliza la misma configuracion durante toda la ejecucion.
     */
    public static synchronized ParkConfig getInstance() {
        if (instance == null) {
            instance = new ParkConfig();
        }
        return instance;
    }

    public int getInt(String key, int defaultValue) {
        String value = props.getProperty(key);
        if (value == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public double getDouble(String key, double defaultValue) {
        String value = props.getProperty(key);
        if (value == null) {
            return defaultValue;
        }

        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public String getString(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public long getSeed() {
        String value = props.getProperty(SEED_KEY);
        if (value == null) {
            throw new IllegalStateException("Missing required property: " + SEED_KEY);
        }

        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalStateException("Invalid long value for property: " + SEED_KEY, ex);
        }
    }

    public int getTotalSteps() {
        return getInt(TOTAL_STEPS_KEY, 0);
    }

    static synchronized void resetForTesting() {
        instance = null;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException("Configuration file not found: " + CONFIG_FILE);
            }
            props.load(input);
        } catch (IOException ex) {
            throw new IllegalStateException("Could not load configuration file: " + CONFIG_FILE, ex);
        }
    }
}
