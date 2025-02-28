package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.domain.Servico;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLAnchorPaneCadastroServicoDialogController implements Initializable {

    @FXML
    private TextField textFieldDescricao;

    @FXML
    private TextField textFieldValor;

    @FXML
    private TextField textFieldPontos; // Adicionado para manipular pontos

    @FXML
    private Button btnConfirmar;

    @FXML
    private Button btnCancelar;

    private Stage dialogStage;
    private Servico servico;
    private boolean btConfirmarClicked = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialização, se necessário
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
        if (servico != null) {
            textFieldDescricao.setText(servico.getDescricao());
            textFieldValor.setText(String.valueOf(servico.getValor()));
            textFieldPontos.setText(String.valueOf(servico.getPontos())); // Configura pontos
        }
    }

    public boolean isBtConfirmarClicked() {
        return btConfirmarClicked;
    }

    @FXML
    private void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            servico.setDescricao(textFieldDescricao.getText());
            servico.setValor(Double.parseDouble(textFieldValor.getText()));
            servico.setPontos(Integer.parseInt(textFieldPontos.getText())); // Define pontos
            btConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleBtCancelar() {
        dialogStage.close();
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (textFieldDescricao.getText() == null || textFieldDescricao.getText().trim().isEmpty()) {
            errorMessage += "Descrição inválida!\n";
        }
        if (textFieldValor.getText() == null || textFieldValor.getText().trim().isEmpty()) {
            errorMessage += "Valor inválido!\n";
        } else {
            try {
                Double.parseDouble(textFieldValor.getText());
            } catch (NumberFormatException e) {
                errorMessage += "O valor deve ser um número!\n";
            }
        }
        if (textFieldPontos.getText() == null || textFieldPontos.getText().trim().isEmpty()) {
            errorMessage += "Pontos inválidos!\n";
        } else {
            try {
                Integer.parseInt(textFieldPontos.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Os pontos devem ser um número inteiro!\n";
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Corrija os campos inválidos!");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}