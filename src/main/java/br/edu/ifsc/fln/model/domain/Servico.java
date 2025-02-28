package br.edu.ifsc.fln.model.domain;

public class Servico {
    private int id;
    private String descricao;
    private double valor;
    private static int pontos = 10; // Valor padrão estático

    public Servico() {
    }

    public Servico(int id, String descricao, double valor) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
    }

    public Servico(String descricao, double valor) {
        this.descricao = descricao;
        this.valor = valor;
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

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public static int getPontos() {
        return pontos;
    }

    public static void setPontos(int pontos) {
        if (pontos < 0) {
            throw new IllegalArgumentException("Pontos não pode ser negativo.");
        }
        Servico.pontos = pontos;
    }

    @Override
    public String toString() {
        return descricao;
    }
}