package br.edu.ifsc.fln.model.domain;

import java.time.LocalDate;

public class PessoaFisica extends Cliente {
    private String cpf;
    private LocalDate dataNascimento;

    public PessoaFisica() {
        super();
    }

    public PessoaFisica(int id, String nome, String celular, String email, String cpf, LocalDate dataNascimento) {
        super(id, nome, celular, email);
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}