package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Servico;
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

public class FXMLAnchorPaneCadastroServicoController implements Initializable {

    @FXML
    private Button btnAlterar;

    @FXML
    private Button btRemover;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbServicoId;

    @FXML
    private Label lbServicoDescricao;

    @FXML
    private Label lbServicoValor; // Adicionado

    @FXML
    private Label lbServicoPontos; // Adicionado

    @FXML
    private TableColumn<Servico, String> tableColumnServicoDescricao;

    @FXML
    private TableView<Servico> tableViewServico;

    private List<Servico> listaServico;
    private ObservableList<Servico> observableListServico;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ServicoDAO servicoDAO = new ServicoDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        servicoDAO.setConnection(connection);
        carregarTableViewServico();

        tableViewServico.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewServico(newValue));
    }

    public void carregarTableViewServico() {
        tableColumnServicoDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        listaServico = servicoDAO.listar();
        observableListServico = FXCollections.observableArrayList(listaServico);
        tableViewServico.setItems(observableListServico);
    }

    public void selecionarItemTableViewServico(Servico servico) {
        if (servico != null) {
            lbServicoId.setText(String.valueOf(servico.getId()));
            lbServicoDescricao.setText(servico.getDescricao());
            lbServicoValor.setText(String.valueOf(servico.getValor())); // Adicionado
            lbServicoPontos.setText(String.valueOf(servico.getPontos())); // Adicionado
        } else {
            lbServicoId.setText("");
            lbServicoDescricao.setText("");
            lbServicoValor.setText(""); // Adicionado
            lbServicoPontos.setText(""); // Adicionado
        }
    }

    @FXML
    public void handleBtInserir() throws IOException {
        Servico servico = new Servico();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroServicoDialog(servico);
        if (btConfirmarClicked) {
            servicoDAO.inserir(servico);
            carregarTableViewServico();
        }
    }

    @FXML
    public void handleBtAlterar() throws IOException {
        Servico servico = tableViewServico.getSelectionModel().getSelectedItem();
        if (servico != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroServicoDialog(servico);
            if (btConfirmarClicked) {
                servicoDAO.alterar(servico);
                carregarTableViewServico();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção de um serviço na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    public void handleBtRemover() throws IOException {
        Servico servico = tableViewServico.getSelectionModel().getSelectedItem();
        if (servico != null) {
            servicoDAO.remover(servico);
            carregarTableViewServico();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção de um serviço na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroServicoDialog(Servico servico) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroServicoController.class.getResource("/view/FXMLAnchorPaneCadastroServicoDialog.fxml"));
        AnchorPane page = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Serviço");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        FXMLAnchorPaneCadastroServicoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setServico(servico);

        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }
}