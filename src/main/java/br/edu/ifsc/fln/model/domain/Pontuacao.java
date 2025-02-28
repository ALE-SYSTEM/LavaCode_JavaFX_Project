package br.edu.ifsc.fln.model.domain;

public class Pontuacao {
    private int quantidade;
    private int adicional;
    private int subtrair;
    private int saldo;
    private Cliente cliente; // Referência ao cliente associado

    public Pontuacao() {
        this.quantidade = 0;
        this.adicional = 0;
        this.subtrair = 0;
        this.saldo = 0;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getAdicional() {
        return adicional;
    }

    public void setAdicional(int adicional) {
        this.adicional = adicional;
        atualizarSaldo();
    }

    public int getSubtrair() {
        return subtrair;
    }

    public void setSubtrair(int subtrair) {
        this.subtrair = subtrair;
        atualizarSaldo();
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    private void atualizarSaldo() {
        this.saldo = quantidade + adicional - subtrair;
    }

    public void pontuacao() {
        atualizarSaldo();
    }

    public void adicionar(int pontos) {
    }
}