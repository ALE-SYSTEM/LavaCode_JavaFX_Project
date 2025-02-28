package br.edu.ifsc.fln.model.domain;

public enum EStatus {
    ABERTA(1, "Aberta"), FECHADA(2, "Fechada"), CANCELADA(3, "Cancelada");

    private final int id;
    private final String descricao;

    private EStatus(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

}


