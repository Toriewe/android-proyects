package com.ama.finanzapp;

public class Finanza {
    private String idDoc;
    private String email;
    public Double venta;
    public Double compra;
    public Double monto;
    public String ganoPer;

    public String getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(String idDoc) {
        this.idDoc = idDoc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getVenta() {
        return venta;
    }

    public void setVenta(Double venta) {
        this.venta = venta;
    }

    public Double getCompra() {
        return compra;
    }

    public void setCompra(Double compra) {
        this.compra = compra;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getGanoPer() {
        return ganoPer;
    }

    public void setGanoPer(String ganoPer) {
        this.ganoPer = ganoPer;
    }
}
