package br.edu.ifsc.fln.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

public class FXMLVBoxMainAppController implements Initializable {

    @FXML private MenuItem menuItemCadastroMarca;
    @FXML private MenuItem menuItemCadastroCor;
    @FXML private MenuItem menuItemCadastroServicos; // Corrigido para plural
    @FXML private MenuItem menuItemCadastroVeiculos;
    @FXML private MenuItem menuItemCadastroProduto;
    @FXML private MenuItem menuItemCadastroCliente;
    @FXML private MenuItem menuItemCadastroFornecedor;
    @FXML private MenuItem menuItemProcessoVenda;
    @FXML private MenuItem menuItemProcessoEstoque;
    @FXML private MenuItem menuItemGraficoVendasPorMes; // Corrigido para singular
    @FXML private MenuItem menuItemRelatorioEstoque;
    @FXML private MenuItem menuItemProcessoOrdemServico; // Adicionado
    @FXML private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialização, se necessário
    }

    @FXML
    public void handleMenuItemCadastroMarca() throws IOException {
        carregarTela("/view/FXMLAnchorPaneCadastroMarca.fxml");
    }

    @FXML
    public void handleMenuItemCadastroCor() throws IOException {
        carregarTela("/view/FXMLAnchorPaneCadastroCor.fxml");
    }

    @FXML
    public void handleMenuItemCadastroServico() throws IOException {
        carregarTela("/view/FXMLAnchorPaneCadastroServico.fxml");
    }

    @FXML
    public void handleMenuItemCadastroModelo() throws IOException {
        carregarTela("/view/FXMLAnchorPaneCadastroModelo.fxml");
    }

    @FXML
    public void handleMenuItemCadastroVeiculos() throws IOException {
        carregarTela("/view/FXMLAnchorPaneCadastroVeiculo.fxml");
    }

    @FXML
    public void handleMenuItemCadastroCliente() throws IOException {
        carregarTela("/view/FXMLAnchorPaneCadastroCliente.fxml");
    }

    @FXML
    public void handleMenuItemCadastroFornecedor() throws IOException {
        showAlert("Não Implementado", "Funcionalidade de cadastro de fornecedores ainda não implementada.");
    }

    @FXML
    public void handleMenuItemCadastroProduto() throws IOException {
        showAlert("Não Implementado", "Funcionalidade de cadastro de produtos ainda não implementada.");
    }

    @FXML
    public void handleMenuItemProcessoEstoque() throws IOException {
        showAlert("Não Implementado", "Funcionalidade de processo de estoque ainda não implementada.");
    }

    @FXML
    public void handleMenuItemProcessoVenda() throws IOException {
        showAlert("Não Implementado", "Funcionalidade de processo de venda ainda não implementada.");
    }

    @FXML
    public void handleMenuItemGraficosVendasPorMes() throws IOException {
        showAlert("Não Implementado", "Funcionalidade de gráficos de vendas por mês ainda não implementada.");
    }

    @FXML
    public void handleMenuItemRelatorioEstoqueProdutos() throws IOException {
        showAlert("Não Implementado", "Funcionalidade de relatório de estoque ainda não implementada.");
    }

    @FXML
    public void handleMenuItemProcessoOrdemServico() throws IOException {
        carregarTela("/view/FXMLAnchorPaneProcessoOrdemServico.fxml");
    }

    private void carregarTela(String caminhoFXML) throws IOException {
        try {
            AnchorPane a = FXMLLoader.load(getClass().getResource(caminhoFXML));
            // Ajustar o tamanho do AnchorPane para ocupar todo o espaço disponível
            AnchorPane.setTopAnchor(a, 0.0);
            AnchorPane.setBottomAnchor(a, 0.0);
            AnchorPane.setLeftAnchor(a, 0.0);
            AnchorPane.setRightAnchor(a, 0.0);
            anchorPane.getChildren().setAll(a);
        } catch (IOException e) {
            showAlert("Erro ao Carregar Tela", "Não foi possível carregar a tela: " + caminhoFXML + "\n" + e.getMessage());
            throw e;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}