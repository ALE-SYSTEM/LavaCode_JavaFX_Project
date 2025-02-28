package br.edu.ifsc.fln.model.domain;

import java.util.List;

public interface ICliente {
    /**
     * Retorna uma representação textual dos dados do cliente.
     * @return String com os dados do cliente.
     */
    String getDados();

    /**
     * Retorna uma representação textual dos dados do cliente no formato especificado.
     * @param formato String indicando o formato desejado (e.g., "completo", "resumido").
     * @return String com os dados formatados.
     */
    String getDados(String formato);

    /**
     * Adiciona um veículo à lista de veículos do cliente.
     * @param veiculo Veículo a ser adicionado.
     */
    void adicionarVeiculo(Veiculo veiculo);

    /**
     * Remove um veículo da lista de veículos do cliente.
     * @param veiculo Veículo a ser removido.
     */
    void removerVeiculo(Veiculo veiculo);

    /**
     * Retorna a lista de veículos associados ao cliente.
     * @return List<Veiculo> com os veículos do cliente.
     */
    List<Veiculo> getVeiculo();

    /**
     * Adiciona pontos à pontuação do cliente.
     * @param pontos Quantidade de pontos a ser adicionada.
     */
    void adicionarPontos(int pontos);
}