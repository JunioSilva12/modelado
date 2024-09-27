package org.evaporatorsimulator.model;

public class SimulinkModel {

    private double capacidadEvaporador1; // Capacidad en litros
    private double capacidadTanqueAlimento;
    private double capacidadTanqueCondensadot3;
    private double capacidadTanqueProductot5;
    private double presionMaximaManometro; // En PSI
    private double rangoVacuoManometro; // En PSI

    public SimulinkModel(double capacidadEvaporador1, double capacidadTanqueAlimento, double capacidadTanqueCondensadot3, double capacidadTanqueProductot5, double presionMaximaManometro, double rangoVacuoManometro) {
        this.capacidadEvaporador1 = capacidadEvaporador1;
        this.capacidadTanqueAlimento = capacidadTanqueAlimento;
        this.capacidadTanqueCondensadot3 = capacidadTanqueCondensadot3;
        this.capacidadTanqueProductot5 = capacidadTanqueProductot5;
        this.presionMaximaManometro = presionMaximaManometro;
        this.rangoVacuoManometro = rangoVacuoManometro;
    }

    public double getCapacidadEvaporador1() {
        return capacidadEvaporador1;
    }

    public void setCapacidadEvaporador1(double capacidadEvaporador1) {
        this.capacidadEvaporador1 = capacidadEvaporador1;
    }

    public double getCapacidadTanqueAlimento() {
        return capacidadTanqueAlimento;
    }

    public void setCapacidadTanqueAlimento(double capacidadTanqueAlimento) {
        this.capacidadTanqueAlimento = capacidadTanqueAlimento;
    }

    public double getCapacidadTanqueCondensadot3() {
        return capacidadTanqueCondensadot3;
    }

    public void setCapacidadTanqueCondensadot3(double capacidadTanqueCondensadot3) {
        this.capacidadTanqueCondensadot3 = capacidadTanqueCondensadot3;
    }

    public double getCapacidadTanqueProductot5() {
        return capacidadTanqueProductot5;
    }

    public void setCapacidadTanqueProductot5(double capacidadTanqueProductot5) {
        this.capacidadTanqueProductot5 = capacidadTanqueProductot5;
    }

    public double getPresionMaximaManometro() {
        return presionMaximaManometro;
    }

    public void setPresionMaximaManometro(double presionMaximaManometro) {
        this.presionMaximaManometro = presionMaximaManometro;
    }

    public double getRangoVacuoManometro() {
        return rangoVacuoManometro;
    }

    public void setRangoVacuoManometro(double rangoVacuoManometro) {
        this.rangoVacuoManometro = rangoVacuoManometro;
    }


}
