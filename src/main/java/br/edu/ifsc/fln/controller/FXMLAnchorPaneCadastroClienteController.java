package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FXMLAnchorPaneCadastroClienteController implements Initializable {

    @FXML private TableView<Cliente> tableViewCliente;
    @FXML private TableColumn<Cliente, Integer> tableColumnClienteId;
    @FXML private TableColumn<Cliente, String> tableColumnClienteNome;
    @FXML private TableColumn<Cliente, String> tableColumnClienteCelular;
    @FXML private TableColumn<Cliente, String> tableColumnClienteCpfCnpj;
    @FXML private Button btInserir;
    @FXML private Button btAlterar;
    @FXML private Button btExcluir;

    private List<Cliente> listaCliente;
    private ObservableList<Cliente> observableListCliente;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO.setConnection(database.conectar());
        try {
            carregarTableViewCliente();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        tableViewCliente.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewCliente(newValue));
    }

    public void carregarTableViewCliente() throws SQLException {
        tableColumnClienteId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnClienteCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
        tableColumnClienteCpfCnpj.setCellValueFactory(cellData -> {
            Cliente cliente = cellData.getValue();
            if (cliente instanceof PessoaFisica) {
                return new javafx.beans.property.SimpleStringProperty(((PessoaFisica) cliente).getCpf());
            } else if (cliente instanceof PessoaJuridica) {
                return new javafx.beans.property.SimpleStringProperty(((PessoaJuridica) cliente).getCnpj());
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });

        if (clienteDAO.getConnection() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Falha ao conectar ao banco de dados. Verifique as configurações de conexão.");
            alert.showAndWait();
            return;
        }

        listaCliente = clienteDAO.listar();
        observableListCliente = FXCollections.observableArrayList(listaCliente);
        tableViewCliente.setItems(observableListCliente);
    }

    public void selecionarItemTableViewCliente(Cliente cliente) {
        // Pode ser usado para exibir detalhes no futuro
    }

    @FXML
    public void handleBtInserir() throws IOException, SQLException {
        Cliente cliente = new PessoaFisica(); // Pode ser alterado para PessoaJuridica conforme necessidade
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroClienteDialog(cliente);
        if (btConfirmarClicked) {
            clienteDAO.inserir(cliente);
            carregarTableViewCliente();
        }
    }

    @FXML
    public void handleBtAlterar() throws IOException, SQLException {
        Cliente cliente = tableViewCliente.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroClienteDialog(cliente);
            if (btConfirmarClicked) {
                clienteDAO.alterar(cliente);
                carregarTableViewCliente();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um Cliente na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    public void handleBtExcluir() throws IOException, SQLException {
        Cliente cliente = tableViewCliente.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação de Exclusão");
            alert.setContentText("Tem certeza que deseja excluir o cliente " + cliente.getNome() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                clienteDAO.remover(cliente);
                carregarTableViewCliente();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um Cliente na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroClienteDialog(Cliente cliente) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroClienteController.class.getResource("/view/FXMLAnchorPaneCadastroClienteDialog.fxml"));
        AnchorPane page = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Cliente");
        Scene scene = new Scene(page, 442, 308);
        dialogStage.setScene(scene);
        dialogStage.setResizable(false);

        FXMLAnchorPaneCadastroClienteDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCliente(cliente);

        dialogStage.showAndWait();
        return controller.isBtConfirmarClicked();
    }
}