package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.dao.CorDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.Cor;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLAnchorPaneProcessoOrdemServicoController implements Initializable {

    @FXML private Button btCancelar;
    @FXML private Button btConfirmar;
    @FXML private TextField tfPlaca;
    @FXML private ComboBox<Modelo> cbModelo;
    @FXML private ComboBox<Cor> cbCor;
    @FXML private ComboBox<Cliente> cbCliente;
    @FXML private TextField tfObservacao;

    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Veiculo veiculo;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection;
    private final ModeloDAO modeloDAO = new ModeloDAO();
    private final CorDAO corDAO = new CorDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    {
        try {
            connection = database.conectar();
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao estabelecer conexão com o banco de dados: " + e.getMessage(), e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (connection == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Conexão");
            alert.setHeaderText("Falha na conexão com o banco de dados");
            alert.setContentText("Verifique as configurações de conexão e tente novamente.");
            alert.show();
            return;
        }

        modeloDAO.setConnection(connection);
        corDAO.setConnection(connection);
        clienteDAO.setConnection(connection);

        try {
            cbModelo.setItems(FXCollections.observableArrayList(modeloDAO.listar()));
            cbCor.setItems(FXCollections.observableArrayList(corDAO.listar()));
            cbCliente.setItems(FXCollections.observableArrayList(clienteDAO.listar()));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro ao Carregar Dados");
            alert.setHeaderText("Falha ao carregar dados do banco de dados");
            alert.setContentText("Erro: " + e.getMessage() + "\nOs ComboBox podem estar vazios.");
            alert.show();
            cbModelo.setItems(FXCollections.observableArrayList());
            cbCor.setItems(FXCollections.observableArrayList());
            cbCliente.setItems(FXCollections.observableArrayList());
        }
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

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
        tfPlaca.setText(veiculo.getPlaca());
        tfObservacao.setText(veiculo.getObservacoes());
        if (veiculo.getModelo() != null) cbModelo.getSelectionModel().select(veiculo.getModelo());
        if (veiculo.getCor() != null) cbCor.getSelectionModel().select(veiculo.getCor());
        if (veiculo.getCliente() != null) cbCliente.getSelectionModel().select(veiculo.getCliente());
    }

    @FXML
    public void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            veiculo.setPlaca(tfPlaca.getText());
            veiculo.setObservacoes(tfObservacao.getText());
            veiculo.setModelo(cbModelo.getValue());
            veiculo.setCor(cbCor.getValue());
            veiculo.setCliente(cbCliente.getValue());

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
        if (tfPlaca.getText() == null || tfPlaca.getText().trim().isEmpty()) {
            errorMessage += "Placa inválida.\n";
        }
        if (cbModelo.getValue() == null) {
            errorMessage += "Modelo inválido.\n";
        }
        if (cbCor.getValue() == null) {
            errorMessage += "Cor inválida.\n";
        }
        if (cbCliente.getValue() == null) {
            errorMessage += "Cliente inválido.\n";
        }

        if (errorMessage.isEmpty()) {
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