package br.edu.ifsc.fln.model.domain;

public class PessoaJuridica extends Cliente {
    private String cnpj;
    private String inscricaoEstadual;

    public PessoaJuridica() {
        super();
    }

    public PessoaJuridica(int id, String nome, String celular, String email, String cnpj, String inscricaoEstadual) {
        super(id, nome, celular, email);
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }
}