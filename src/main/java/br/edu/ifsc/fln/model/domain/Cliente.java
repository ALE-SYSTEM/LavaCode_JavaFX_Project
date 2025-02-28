package br.edu.ifsc.fln.model.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Cliente implements ICliente {
    private int id;
    private String nome;
    private String celular;
    private String email;
    private LocalDate dataCadastro;
    private List<Veiculo> veiculo;
    private Pontuacao pontuacao;

    private void createPontuacao() {
        this.pontuacao = new Pontuacao();
        this.pontuacao.setCliente(this);
    }

    public Cliente() {
        this.veiculo = new ArrayList<>();
        this.createPontuacao();
    }

    public Cliente(int id, String nome, String celular, String email) {
        this();
        this.id = id;
        this.nome = nome;
        this.celular = celular;
        this.email = email;
    }

    @Override
    public String getDados() {
        return String.format("ID: %d, Nome: %s, Celular: %s, Email: %s, Data Cadastro: %s",
                id, nome, celular, email, dataCadastro);
    }

    @Override
    public String getDados(String formato) {
        if ("resumido".equalsIgnoreCase(formato)) {
            return String.format("Nome: %s, Celular: %s", nome, celular);
        }
        return getDados();
    }

    @Override
    public void adicionarVeiculo(Veiculo veiculo) {
        if (veiculo != null && !this.veiculo.contains(veiculo)) {
            this.veiculo.add(veiculo);
        }
    }

    @Override
    public void removerVeiculo(Veiculo veiculo) {
        this.veiculo.remove(veiculo);
    }

    @Override
    public List<Veiculo> getVeiculo() {
        return new ArrayList<>(veiculo);
    }

    @Override
    public void adicionarPontos(int pontos) {
        if (pontuacao != null) {
            pontuacao.adicionar(pontos);
        }
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCelular() {
        return this.celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataCadastro() {
        return this.dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Pontuacao getPontuacao() {
        return this.pontuacao;
    }

    public void setPontuacao(Pontuacao pontuacao) {
        this.pontuacao = pontuacao;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}