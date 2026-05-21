package com.axity.dinosaurpark.model;

import java.time.LocalDateTime;

/*
 * Ticket inmutable: representa una venta sin permitir cambios posteriores.
 */
public class Ticket {
    private final long id;
    private final int turistaId;
    private final double precio;
    private final String categoria;
    private final LocalDateTime emitidoEn;

    public Ticket(long id, int turistaId, double precio, String categoria, LocalDateTime emitidoEn) {
        this.id = id;
        this.turistaId = turistaId;
        this.precio = precio;
        this.categoria = categoria;
        this.emitidoEn = emitidoEn;
    }

    public long getId() {
        return id;
    }

    public int getTuristaId() {
        return turistaId;
    }

    public double getPrecio() {
        return precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public LocalDateTime getEmitidoEn() {
        return emitidoEn;
    }
}
