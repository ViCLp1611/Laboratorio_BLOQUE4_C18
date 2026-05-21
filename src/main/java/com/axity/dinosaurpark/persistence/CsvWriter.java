package com.axity.dinosaurpark.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/*
 * Escritor CSV basico del laboratorio.
 * Crea la carpeta output y reinicia los archivos cada vez que se inicializa.
 */
public class CsvWriter {
    private static final String REVENUES_HEADER = "id,type,amount,touristId,zone,timestamp";
    private static final String EXPENSES_HEADER = "id,type,amount,description,timestamp";
    private static final String EVENTS_HEADER = "step,eventName,description,affectedEntities,timestamp";

    private final Path revenuesFile;
    private final Path expensesFile;
    private final Path eventsFile;

    public CsvWriter(String outputDir) {
        try {
            Path carpetaSalida = Path.of(outputDir);
            Files.createDirectories(carpetaSalida);
            revenuesFile = carpetaSalida.resolve("revenues.csv");
            expensesFile = carpetaSalida.resolve("expenses.csv");
            eventsFile = carpetaSalida.resolve("events.csv");
            inicializarArchivo(revenuesFile, REVENUES_HEADER);
            inicializarArchivo(expensesFile, EXPENSES_HEADER);
            inicializarArchivo(eventsFile, EVENTS_HEADER);
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo inicializar la salida CSV", ex);
        }
    }

    public void appendRevenue(RevenueRecord record) {
        appendLine(revenuesFile, record.toCsvLine());
    }

    public void appendExpense(ExpenseRecord record) {
        appendLine(expensesFile, record.toCsvLine());
    }

    public void appendEvent(EventRecord record) {
        appendLine(eventsFile, record.toCsvLine());
    }

    private void inicializarArchivo(Path archivo, String header) throws IOException {
        Files.writeString(
                archivo,
                header + System.lineSeparator(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE);
    }

    private void appendLine(Path archivo, String linea) {
        try {
            Files.writeString(
                    archivo,
                    linea + System.lineSeparator(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND,
                    StandardOpenOption.WRITE);
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo escribir en el archivo CSV: " + archivo, ex);
        }
    }
}
