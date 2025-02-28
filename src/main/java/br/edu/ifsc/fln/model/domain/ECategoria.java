package br.edu.ifsc.fln.model.domain;

public enum ECategoria {

    PEQUENO(1, "Pequeno"), MEDIO(2, "Medio"), GRANDE(3, "Grande"),
    MOTO(4, "Moto"), PADRAO(5, "Padrao");

    private final int id;
    private final String descricao;

    private ECategoria(int id, String descricao) {
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
