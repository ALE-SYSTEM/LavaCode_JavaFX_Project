package br.edu.ifsc.fln.model.domain;

public class Marca {

    private int id;
    private String nome;

    public Marca(String nome) {
        this.nome = nome;
    }

    public Marca() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }

}

