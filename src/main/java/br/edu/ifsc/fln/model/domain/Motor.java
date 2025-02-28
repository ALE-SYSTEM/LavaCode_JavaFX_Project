package br.edu.ifsc.fln.model.domain;

public class Motor {
    private int id;
    private int potencia;

    public Motor() {
    }

    public Motor(int potencia) {
        this.potencia = potencia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPotencia() {
        return potencia;
    }

    public void setPotencia(int potencia) {
        this.potencia = potencia;
    }

    @Override
    public String toString() {
        return String.valueOf(potencia);
    }
}