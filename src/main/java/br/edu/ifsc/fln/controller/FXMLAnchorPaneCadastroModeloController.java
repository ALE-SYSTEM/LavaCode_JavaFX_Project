package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Modelo;
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

public class FXMLAnchorPaneCadastroModeloController implements Initializable {

    @FXML private Button btnAlterar;
    @FXML private Button btExcluir;
    @FXML private Button btInserir;
    @FXML private Label lbModeloId;
    @FXML private Label lbModeloDescricao;
    @FXML private Label lbModeloMarca;
    @FXML private Label lbModeloCategoria;
    @FXML private Label lbPotencia;
    @FXML private Label lbTipoCombustivel;
    @FXML private TableColumn<Modelo, String> tableColumnModeloDescricao;
    @FXML private TableView<Modelo> tableViewModelo;

    private List<Modelo> listaModelo;
    private ObservableList<Modelo> observableListModelo;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ModeloDAO modeloDAO = new ModeloDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modeloDAO.setConnection(connection);
        carregarTableViewModelo();

        tableViewModelo.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewModelo(newValue));
    }

    public void carregarTableViewModelo() {
        tableColumnModeloDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        listaModelo = modeloDAO.listar();
        observableListModelo = FXCollections.observableArrayList(listaModelo);
        tableViewModelo.setItems(observableListModelo);
    }

    public void selecionarItemTableViewModelo(Modelo modelo) {
        if (modelo != null) {
            lbModeloId.setText(String.valueOf(modelo.getId()));
            lbModeloDescricao.setText(modelo.getDescricao());
            lbModeloMarca.setText(modelo.getMarca() != null ? modelo.getMarca().getNome() : "");
            lbModeloCategoria.setText(modelo.getCategoria() != null ? modelo.getCategoria().toString() : "");
            lbPotencia.setText(modelo.getMotor() != null ? String.valueOf(modelo.getMotor().getPotencia()) : "");
            lbTipoCombustivel.setText(modelo.getCombustivel() != null ? modelo.getCombustivel().toString() : "");
        } else {
            lbModeloId.setText("");
            lbModeloDescricao.setText("");
            lbModeloMarca.setText("");
            lbModeloCategoria.setText("");
            lbPotencia.setText("");
            lbTipoCombustivel.setText("");
        }
    }

    @FXML
    public void handleBtInserir() throws IOException {
        Modelo modelo = new Modelo();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroModeloDialog(modelo);
        if (btConfirmarClicked) {
            modeloDAO.inserir(modelo);
            carregarTableViewModelo();
        }
    }

    @FXML
    public void handleBtAlterar() throws IOException {
        Modelo modelo = tableViewModelo.getSelectionModel().getSelectedItem();
        if (modelo != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroModeloDialog(modelo);
            if (btConfirmarClicked) {
                modeloDAO.alterar(modelo);
                carregarTableViewModelo();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um Modelo na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    public void handleBtExcluir() throws IOException {
        Modelo modelo = tableViewModelo.getSelectionModel().getSelectedItem();
        if (modelo != null) {
            modeloDAO.remover(modelo);
            carregarTableViewModelo();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um Modelo na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroModeloDialog(Modelo modelo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroModeloController.class.getResource("/view/FXMLAnchorPaneCadastroModeloDialog.fxml"));
        AnchorPane page = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Modelo");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        FXMLAnchorPaneCadastroModeloDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setModelo(modelo);

        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }
}