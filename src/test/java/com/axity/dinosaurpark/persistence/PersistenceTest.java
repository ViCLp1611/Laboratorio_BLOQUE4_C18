package com.axity.dinosaurpark.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PersistenceTest {
    @TempDir
    Path tempDir;

    @Test
    void recordsAreConvertedToCsvLines() {
        LocalDateTime timestamp = LocalDateTime.of(2026, 5, 21, 11, 0);

        assertEquals("1,TICKET,30.0,7,Llegada," + timestamp,
                new RevenueRecord(1L, "TICKET", 30.0, 7, "Llegada", timestamp).toCsvLine());
        assertEquals("2,POWER,100.0,Mantenimiento," + timestamp,
                new ExpenseRecord(2L, "POWER", 100.0, "Mantenimiento", timestamp).toCsvLine());
        assertEquals("3,ESCAPE,Descripcion,Afectados," + timestamp,
                new EventRecord(3L, "ESCAPE", "Descripcion", "Afectados", timestamp).toCsvLine());
    }

    @Test
    void csvWriterCreatesFilesAndAppendsRows() throws Exception {
        CsvWriter writer = new CsvWriter(tempDir.toString());
        LocalDateTime timestamp = LocalDateTime.of(2026, 5, 21, 12, 0);

        writer.appendRevenue(new RevenueRecord(1L, "TICKET", 30.0, 1, "Llegada", timestamp));
        writer.appendExpense(new ExpenseRecord(1L, "POWER", 100.0, "Mantenimiento", timestamp));
        writer.appendEvent(new EventRecord(1L, "STORM", "Tormenta", "Turistas", timestamp));

        List<String> revenues = Files.readAllLines(tempDir.resolve("revenues.csv"));
        List<String> expenses = Files.readAllLines(tempDir.resolve("expenses.csv"));
        List<String> events = Files.readAllLines(tempDir.resolve("events.csv"));

        assertEquals("id,type,amount,touristId,zone,timestamp", revenues.get(0));
        assertTrue(revenues.get(1).contains("TICKET"));
        assertEquals("id,type,amount,description,timestamp", expenses.get(0));
        assertTrue(expenses.get(1).contains("POWER"));
        assertEquals("step,eventName,description,affectedEntities,timestamp", events.get(0));
        assertTrue(events.get(1).contains("STORM"));
    }
}
