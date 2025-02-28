//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.dao.CorDAO;
import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.Cor;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class FXMLAnchorPaneCadastroVeiculoDialogController implements Initializable {
    @FXML
    private TextField tfPlaca;
    @FXML
    private TextField tfObservacoes;
    @FXML
    private ComboBox<Modelo> cbModelo;
    @FXML
    private ComboBox<Marca> cbMarca;
    @FXML
    private ComboBox<Cor> cbCor;
    @FXML
    private ComboBox<Cliente> cbCliente;
    @FXML
    private Button btConfirmar;
    @FXML
    private Button btCancelar;
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection;
    private final MarcaDAO marcaDAO;
    private final ModeloDAO modeloDAO;
    private final CorDAO corDAO;
    private final ClienteDAO clienteDAO;
    private Stage dialogStage;
    private boolean buttonConfirmarClicked;
    private Veiculo veiculo;
    private Marca marca;
    private List<Modelo> listaModelos;
    private ObservableList<Modelo> observableListModelos;
    private List<Marca> listaMarca;
    private ObservableList<Marca> observableListMarca;
    private List<Cliente> listaCliente;
    private ObservableList<Cliente> observableListCliente;
    private List<Cor> listaCores;
    private ObservableList<Cor> observableListCores;

    public FXMLAnchorPaneCadastroVeiculoDialogController() {
        this.connection = this.database.conectar();
        this.marcaDAO = new MarcaDAO();
        this.modeloDAO = new ModeloDAO();
        this.corDAO = new CorDAO();
        this.clienteDAO = new ClienteDAO();
        this.buttonConfirmarClicked = false;
    }

    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.marcaDAO.setConnection(this.connection);
            this.carregarComboBoxMarca();
            this.corDAO.setConnection(this.connection);
            this.carregarComboBoxCor();
            this.clienteDAO.setConnection(this.connection);
            this.carregarComboBoxCliente();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLAnchorPaneCadastroVeiculoDialogController.class.getName()).log(Level.SEVERE, (String)null, ex);
        }

    }

    private void setFocusLostHandle() {
        this.tfPlaca.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!newV && (this.tfPlaca.getText() == null || this.tfPlaca.getText().isEmpty())) {
                this.tfPlaca.requestFocus();
            }

        });
    }

    public void carregarComboBoxMarca() {
        this.listaMarca = this.marcaDAO.listar();
        this.observableListMarca = FXCollections.observableArrayList(this.listaMarca);
        this.cbMarca.setItems(this.observableListMarca);
    }

    public void carregarComboBoxModelos() throws SQLException {
        this.listaModelos = this.modeloDAO.listarPorMarca(this.marca);
        this.observableListModelos = FXCollections.observableArrayList(this.listaModelos);
        this.cbModelo.setItems(this.observableListModelos);
    }

    public void carregarComboBoxCor() {
        this.listaCores = this.corDAO.listar();
        this.observableListCores = FXCollections.observableArrayList(this.listaCores);
        this.cbCor.setItems(this.observableListCores);
    }

    public void carregarComboBoxCliente() throws SQLException {
        this.listaCliente = this.clienteDAO.listar();
        this.observableListCliente = FXCollections.observableArrayList(this.listaCliente);
        this.cbCliente.setItems(this.observableListCliente);
    }

    public Stage getDialogStage() {
        return this.dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isButtonConfirmarClicked() {
        return this.buttonConfirmarClicked;
    }

    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    public Veiculo getVeiculo() {
        return this.veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
        this.tfPlaca.setText(veiculo.getPlaca());
        this.cbModelo.getSelectionModel().select(veiculo.getModelo());
        this.cbCor.getSelectionModel().select(veiculo.getCor());
        veiculo.setCliente((Cliente)this.cbCliente.getSelectionModel().getSelectedItem());
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
        this.cbMarca.getSelectionModel().select(marca);
    }

    @FXML
    private void handleCbMarca() throws SQLException {
        this.marca = (Marca)this.cbMarca.getSelectionModel().getSelectedItem();
        if (this.marca != null) {
            this.modeloDAO.setConnection(this.connection);
            this.carregarComboBoxModelos();
        }

    }

    @FXML
    private void handleBtConfirmar() {
        if (this.validarEntradaDeDados()) {
            this.veiculo.setPlaca(this.tfPlaca.getText());
            this.veiculo.setObservacoes(this.tfObservacoes.getText());
            this.veiculo.setModelo((Modelo)this.cbModelo.getSelectionModel().getSelectedItem());
            this.veiculo.setCor((Cor)this.cbCor.getSelectionModel().getSelectedItem());
            this.veiculo.getModelo().setMarca((Marca)this.cbMarca.getSelectionModel().getSelectedItem());
            this.veiculo.setCliente((Cliente)this.cbCliente.getSelectionModel().getSelectedItem());
            this.buttonConfirmarClicked = true;
            this.dialogStage.close();
        }

    }

    @FXML
    private void handleBtCancelar() {
        this.dialogStage.close();
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (this.tfPlaca.getText() == null || this.tfPlaca.getText().isEmpty()) {
            errorMessage = errorMessage + "Nome inválido!\n";
        }

        if (this.tfObservacoes.getText() == null || this.tfObservacoes.getText().isEmpty()) {
            errorMessage = errorMessage + "Nome inválido!\n";
        }

        if (this.cbModelo.getSelectionModel().getSelectedItem() == null) {
            errorMessage = errorMessage + "Selecione um modelo!\n";
        }

        if (this.cbCor.getSelectionModel().getSelectedItem() == null) {
            errorMessage = errorMessage + "Selecione uma cor!\n";
        }

        if (this.cbCliente.getSelectionModel().getSelectedItem() == null) {
            errorMessage = errorMessage + "Selecione uma cor!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campo(s) inválido(s), por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
}
