package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador da janela de cadastro de modelos.
 */
public class FXMLAnchorPaneCadastroModeloDialogController implements Initializable {

    @FXML
    private ChoiceBox<ECategoria> cbCategoria;

    @FXML
    private ComboBox<Marca> cbMarca;

    @FXML
    private ChoiceBox<ETipoCombustivel> cbTipoCombustivel;

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private TextField tfDescricao;

    @FXML
    private TextField tfPotencia;

    private List<Marca> listaMarca;
    private ObservableList<Marca> observableListMarca;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final MarcaDAO marcaDAO = new MarcaDAO();

    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Modelo modelo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        marcaDAO.setConnection(connection);
        carregarComboBoxMarca();
        carregarChoiceBoxCategoria();
        carregarChoiceBoxTipoCombustivel();

        tfPotencia.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfPotencia.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void carregarChoiceBoxCategoria() {
        cbCategoria.setItems(FXCollections.observableArrayList(ECategoria.values()));
    }

    public void carregarChoiceBoxTipoCombustivel() {
        cbTipoCombustivel.setItems(FXCollections.observableArrayList(ETipoCombustivel.values()));
    }

    public void carregarComboBoxMarca() {
        listaMarca = marcaDAO.listar();
        observableListMarca = FXCollections.observableArrayList(listaMarca);
        cbMarca.setItems(observableListMarca);
    }

    public boolean isBtConfirmarClicked() {
        return btConfirmarClicked;
    }

    public void setBtConfirmarClicked(boolean btConfirmarClicked) {
        this.btConfirmarClicked = btConfirmarClicked;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
        this.tfDescricao.setText(modelo.getDescricao());
        this.cbCategoria.getSelectionModel().select(modelo.getCategoria());
        this.cbTipoCombustivel.getSelectionModel().select(modelo.getCombustivel());
        this.tfPotencia.setText(String.valueOf(modelo.getMotor().getPotencia()));
    }

    @FXML
    public void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            modelo.setDescricao(tfDescricao.getText());
            modelo.setMarca(cbMarca.getSelectionModel().getSelectedItem());
            modelo.setCategoria(cbCategoria.getSelectionModel().getSelectedItem());
            modelo.setCombustivel(cbTipoCombustivel.getSelectionModel().getSelectedItem());
            modelo.getMotor().setPotencia(Integer.parseInt(tfPotencia.getText()));
            btConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void handleBtCancelar() {
        dialogStage.close();
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (tfDescricao.getText() == null || tfDescricao.getText().length() == 0) {
            errorMessage += "Descrição inválida.\n";
        }
        if (cbMarca.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma marca.\n";
        }
        if (cbCategoria.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Categoria não selecionada.\n";
        }
        if (cbTipoCombustivel.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione um tipo de combustível.\n";
        }
        if (tfPotencia.getText() == null || tfPotencia.getText().length() == 0) {
            errorMessage += "Potência inválida.\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Corrija os campos inválidos!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
}