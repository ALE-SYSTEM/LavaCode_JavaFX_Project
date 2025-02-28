package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.EStatus;
import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Servico;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class FXMLAnchorPaneProcessoOrdemServicoDialogController implements Initializable {

    @FXML private Button btConfirmar;
    @FXML private Button btCancelar;
    @FXML private Button btAdicionar;
    @FXML private ComboBox<Veiculo> cbVeiculo;
    @FXML private TextField tfCliente;
    @FXML private DatePicker dpAgenda;
    @FXML private ComboBox<Servico> cbServico;
    @FXML private TextField tfObservacao;
    @FXML private TableView<ItemOS> tableView;
    @FXML private TableColumn<ItemOS, String> tableColumnServico;
    @FXML private TableColumn<ItemOS, String> tableColumnObservacao;
    @FXML private TableColumn<ItemOS, Double> tableColumnValor;
    @FXML private TextField tfDesconto;
    @FXML private TextField tfValor;
    @FXML private ComboBox<EStatus> cbStatus;
    @FXML private TextField tfPontos;
    @FXML private ContextMenu contextMenu;
    @FXML private MenuItem contextMenuItemAtualizarObs;
    @FXML private MenuItem contextMenuItemRemoverItem;

    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private OrdemServico ordemServico;
    private ObservableList<ItemOS> observableListItensOS;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private final ServicoDAO servicoDAO = new ServicoDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        veiculoDAO.setConnection(connection);
        servicoDAO.setConnection(connection);

        cbVeiculo.setItems(FXCollections.observableArrayList(veiculoDAO.listar()));
        cbServico.setItems(FXCollections.observableArrayList(servicoDAO.listar()));
        cbStatus.setItems(FXCollections.observableArrayList(EStatus.values()));

        tableColumnServico.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getServico().getDescricao()));
        tableColumnObservacao.setCellValueFactory(new PropertyValueFactory<>("observacoes"));
        tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valorServico"));

        observableListItensOS = FXCollections.observableArrayList();
        tableView.setItems(observableListItensOS);

        // Exibir o saldo de pontuação do cliente
        if (ordemServico != null && ordemServico.getVeiculo() != null && ordemServico.getVeiculo().getCliente() != null) {
            tfPontos.setText(String.valueOf(ordemServico.getVeiculo().getCliente().getPontuacao().getSaldo()));
        } else {
            tfPontos.setText("0");
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setOrdemServico(OrdemServico os) {
        this.ordemServico = os;
        if (os.getNumero() != 0) {
            cbVeiculo.getSelectionModel().select(os.getVeiculo());
            tfCliente.setText(os.getVeiculo().getCliente().getNome());
            dpAgenda.setValue(os.getAgenda());
            tfDesconto.setText(String.valueOf(os.getDesconto()));
            tfValor.setText(String.valueOf(os.getTotal()));
            cbStatus.getSelectionModel().select(os.getStatus());
            observableListItensOS.setAll(os.getItensOS());
        }
    }

    public boolean isBtConfirmarClicked() {
        return btConfirmarClicked;
    }

    @FXML
    public void handleBtAdicionar() {
        Servico servico = cbServico.getValue();
        String observacao = tfObservacao.getText();
        if (servico != null) {
            ItemOS item = new ItemOS();
            item.setServico(servico);
            item.setValorServico(servico.getValor()); // Usar valor do serviço diretamente
            item.setObservacoes(observacao);
            ordemServico.add(item);
            observableListItensOS.setAll(ordemServico.getItensOS());
            atualizarTotal();
            tfObservacao.clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Selecione um serviço!");
            alert.show();
        }
    }

    @FXML
    public void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            ordemServico.setVeiculo(cbVeiculo.getValue());
            ordemServico.setAgenda(dpAgenda.getValue());

            // Tratar o campo tfDesconto para aceitar vírgula ou ponto
            String descontoText = tfDesconto.getText().replace(",", ".");
            ordemServico.setDesconto(Double.parseDouble(descontoText));

            // Tratar o campo tfValor para aceitar vírgula ou ponto
            String valorText = tfValor.getText().replace(",", ".");
            ordemServico.setTotal(Double.parseDouble(valorText));

            ordemServico.setStatus(cbStatus.getValue());

            // Atualizar o saldo de pontos do cliente (se modificado manualmente)
            try {
                int novosPontos = Integer.parseInt(tfPontos.getText());
                if (ordemServico.getVeiculo() != null && ordemServico.getVeiculo().getCliente() != null) {
                    int saldoAtual = ordemServico.getVeiculo().getCliente().getPontuacao().getSaldo();
                    int diferenca = novosPontos - saldoAtual;
                    if (diferenca != 0) {
                        ordemServico.getVeiculo().getCliente().adicionarPontos(diferenca);
                    }
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("O valor de pontos deve ser um número inteiro!");
                alert.show();
                return;
            }

            if (ordemServico.getStatus() == EStatus.FECHADA && ordemServico.getVeiculo() != null && ordemServico.getVeiculo().getCliente() != null) {
                int pontosFidelidade = ordemServico.calcularPontosFidelidade();
                ordemServico.getVeiculo().getCliente().adicionarPontos(pontosFidelidade);
            }

            btConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void handleBtCancelar() {
        dialogStage.close();
    }

    @FXML
    public void handleContextMenuItemAtualizarObs() {
        ItemOS itemSelecionado = tableView.getSelectionModel().getSelectedItem();
        if (itemSelecionado != null) {
            TextInputDialog dialog = new TextInputDialog(itemSelecionado.getObservacoes());
            dialog.setTitle("Atualizar Observação");
            dialog.setHeaderText("Editar observação do item selecionado");
            dialog.setContentText("Nova observação:");
            dialog.showAndWait().ifPresent(novaObservacao -> {
                itemSelecionado.setObservacoes(novaObservacao);
                tableView.refresh();
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Selecione um item na tabela para atualizar a observação!");
            alert.show();
        }
    }

    @FXML
    public void handleContextMenuItemRemoverItem() {
        ItemOS itemSelecionado = tableView.getSelectionModel().getSelectedItem();
        if (itemSelecionado != null) {
            ordemServico.remove(itemSelecionado);
            observableListItensOS.setAll(ordemServico.getItensOS());
            atualizarTotal();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Selecione um item na tabela para remover!");
            alert.show();
        }
    }

    private void atualizarTotal() {
        double total = ordemServico.calcularServico();
        double desconto = 0.0;
        if (!tfDesconto.getText().isEmpty()) {
            try {
                desconto = Double.parseDouble(tfDesconto.getText().replace(",", "."));
            } catch (NumberFormatException e) {
                desconto = 0.0;
            }
        }
        total -= (total * desconto / 100);
        tfValor.setText(String.format("%.2f", total));
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (cbVeiculo.getValue() == null) errorMessage += "Selecione um veículo!\n";
        if (dpAgenda.getValue() == null) errorMessage += "Selecione uma data!\n";
        if (cbStatus.getValue() == null) errorMessage += "Selecione um status!\n";
        if (tfDesconto.getText().isEmpty()) errorMessage += "Informe o desconto!\n";
        if (observableListItensOS.isEmpty()) errorMessage += "Adicione pelo menos um serviço!\n";
        if (tfPontos.getText().isEmpty()) errorMessage += "Informe o valor de pontos!\n";

        try {
            Double.parseDouble(tfDesconto.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            errorMessage += "O desconto deve ser um número válido (ex.: 80 ou 80,00)!\n";
        }

        try {
            Integer.parseInt(tfPontos.getText());
        } catch (NumberFormatException e) {
            errorMessage += "O valor de pontos deve ser um número inteiro!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
}