/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Marca;
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


public class FXMLAnchorPaneCadastroMarcaController implements Initializable {

    
    @FXML
    private Button btnAlterar;

    @FXML
    private Button btExcluir;
    
    @FXML
    private Button btInserir;

    @FXML
    private Label lbMarcaNome;

    @FXML
    private Label lbMarcaId;

    @FXML
    private TableColumn<Marca, String> tableColumnMarcaNome;

    @FXML
    private TableView<Marca> tableViewMarca;
    
    private List<Marca> listaMarca;
    private ObservableList<Marca> observableListMarca;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final MarcaDAO marcaDAO = new MarcaDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        marcaDAO.setConnection(connection);
        carregarTableViewMarca();
        
        tableViewMarca.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewMarca(newValue));
    }     
    
    public void carregarTableViewMarca() {
        tableColumnMarcaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        
        listaMarca = marcaDAO.listar();
        
        observableListMarca = FXCollections.observableArrayList(listaMarca);
        tableViewMarca.setItems(observableListMarca);
    }
    
    public void selecionarItemTableViewMarca(Marca marca) {
        if (marca != null) {
            lbMarcaId.setText(String.valueOf(marca.getId())); 
            lbMarcaNome.setText(marca.getNome());
        } else {
            lbMarcaId.setText(""); 
            lbMarcaNome.setText("");
        }
        
    }
    
    @FXML
    public void handleBtInserir() throws IOException {
        Marca marca = new Marca();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroMarcaDialog(marca);
        if (btConfirmarClicked) {
            marcaDAO.inserir(marca);
            carregarTableViewMarca();
        } 
    }
    
    @FXML 
    public void handleBtAlterar() throws IOException {
        Marca marca = tableViewMarca.getSelectionModel().getSelectedItem();
        if (marca != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroMarcaDialog(marca);
            if (btConfirmarClicked) {
                marcaDAO.alterar(marca);
                carregarTableViewMarca();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Marca na tabela ao lado");
            alert.show();
        }
    }
    
    @FXML
    public void handleBtExcluir() throws IOException {
        Marca marca = tableViewMarca.getSelectionModel().getSelectedItem();
        if (marca != null) {
            marcaDAO.remover(marca);
            carregarTableViewMarca();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Marca na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroMarcaDialog(Marca marca) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroMarcaController.class.getResource("/view/FXMLAnchorPaneCadastroMarcaDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Marca");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //enviando o objeto marca para o controller
        FXMLAnchorPaneCadastroMarcaDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setMarca(marca);
        
        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();
        
        return controller.isBtConfirmarClicked();
    }
    
}
