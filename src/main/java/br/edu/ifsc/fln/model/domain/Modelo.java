package br.edu.ifsc.fln.model.domain;

public class Modelo {
    private int id;
    private String descricao;
    private Marca marca;
    private Motor motor;
    private ECategoria categoria;
    private ETipoCombustivel combustivel;

    public Modelo() {
        this.marca = new Marca();
        this.motor = new Motor();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Motor getMotor() {
        return motor;
    }

    public void setMotor(Motor motor) {
        this.motor = motor;
    }

    public ECategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(ECategoria categoria) {
        this.categoria = categoria;
    }

    public ETipoCombustivel getCombustivel() {
        return combustivel;
    }

    public void setCombustivel(ETipoCombustivel combustivel) {
        this.combustivel = combustivel;
    }

    @Override
    public String toString() {
        return descricao;
    }
}