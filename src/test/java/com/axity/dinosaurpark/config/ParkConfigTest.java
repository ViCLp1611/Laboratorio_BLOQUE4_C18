package com.axity.dinosaurpark.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class ParkConfigTest {
    @Test
    void singletonLoadsPropertiesFromResources() {
        ParkConfig.resetForTesting();
        ParkConfig config = ParkConfig.getInstance();
        ParkConfig sameConfig = ParkConfig.getInstance();

        assertNotNull(config);
        assertSame(config, sameConfig);
        assertEquals(42L, config.getSeed());
        assertTrue(config.getTotalSteps() > 0);
        assertEquals(50, config.getInt("tourists", 0));
    }

    @Test
    void returnsDefaultValuesWhenKeyIsMissingOrInvalid() {
        ParkConfig.resetForTesting();
        ParkConfig config = ParkConfig.getInstance();

        assertEquals(12, config.getInt("missing.int", 12));
        assertEquals(2.5, config.getDouble("missing.double", 2.5));
        assertEquals("default", config.getString("missing.string", "default"));
    }
}
