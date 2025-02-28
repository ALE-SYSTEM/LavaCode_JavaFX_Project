package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FXMLAnchorPaneCadastroVeiculoController implements Initializable {

    @FXML private Button btnAlterar;
    @FXML private Button btExcluir;
    @FXML private Button btInserir;
    @FXML private Label lbVeiculoPlaca;
    @FXML private Label lbVeiculoId;
    @FXML private Label lbVeiculoObservacoes; // Adicionado
    @FXML private TableColumn<Veiculo, String> tableColumnVeiculoPlaca;
    @FXML private TableView<Veiculo> tableViewVeiculo;

    private List<Veiculo> listaVeiculos;
    private ObservableList<Veiculo> observableListVeiculos;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("URL do FXML carregado: " + url);
        System.out.println("tableViewVeiculo: " + tableViewVeiculo);
        if (tableViewVeiculo == null) {
            System.err.println("Erro: tableViewVeiculo não foi injetado!");
        }
        veiculoDAO.setConnection(connection);
        carregarTableViewVeiculo();

        tableViewVeiculo.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewVeiculo(newValue));
    }

    public void carregarTableViewVeiculo() {
        tableColumnVeiculoPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));

        listaVeiculos = veiculoDAO.listar();
        observableListVeiculos = FXCollections.observableArrayList(listaVeiculos);
        if (tableViewVeiculo != null) {
            tableViewVeiculo.setItems(observableListVeiculos);
        } else {
            System.err.println("Erro: tableViewVeiculo é null, não pode definir itens!");
        }
    }

    public void selecionarItemTableViewVeiculo(Veiculo veiculo) {
        if (veiculo != null) {
            lbVeiculoId.setText(String.valueOf(veiculo.getId()));
            lbVeiculoPlaca.setText(veiculo.getPlaca());
            lbVeiculoObservacoes.setText(veiculo.getObservacoes() != null ? veiculo.getObservacoes() : ""); // Adicionado
        } else {
            lbVeiculoId.setText("");
            lbVeiculoPlaca.setText("");
            lbVeiculoObservacoes.setText(""); // Adicionado
        }
    }

    @FXML
    public void handleBtInserir() throws IOException {
        Veiculo veiculo = new Veiculo();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroVeiculoDialog(veiculo);
        if (btConfirmarClicked) {
            veiculoDAO.inserir(veiculo);
            carregarTableViewVeiculo();
        }
    }

    @FXML
    public void handleBtAlterar() throws IOException {
        Veiculo veiculo = tableViewVeiculo != null ? tableViewVeiculo.getSelectionModel().getSelectedItem() : null;
        if (veiculo != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroVeiculoDialog(veiculo);
            if (btConfirmarClicked) {
                veiculoDAO.alterar(veiculo);
                carregarTableViewVeiculo();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção de um veículo na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    public void handleBtExcluir() throws IOException {
        Veiculo veiculo = tableViewVeiculo != null ? tableViewVeiculo.getSelectionModel().getSelectedItem() : null;
        if (veiculo != null) {
            veiculoDAO.remover(veiculo);
            carregarTableViewVeiculo();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção de um veículo na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroVeiculoDialog(Veiculo veiculo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroVeiculoController.class.getResource("/view/FXMLAnchorPaneCadastroVeiculoDialog.fxml"));
        AnchorPane page = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Veículo");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        FXMLAnchorPaneProcessoOrdemServicoController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setVeiculo(veiculo);

        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }
}