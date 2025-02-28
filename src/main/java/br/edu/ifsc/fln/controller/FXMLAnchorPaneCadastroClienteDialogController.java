package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLAnchorPaneCadastroClienteDialogController implements Initializable {

    @FXML private TextField tfNome;
    @FXML private TextField tfCelular;
    @FXML private TextField tfEmail;
    @FXML private TextField tfCpfCnpj;
    @FXML private DatePicker dpDataNascimento;
    @FXML private TextField tfInscricaoEstadual;
    @FXML private CheckBox cbPessoaJuridica;
    @FXML private Button btConfirmar;
    @FXML private Button btCancelar;

    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Cliente cliente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbPessoaJuridica.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                tfCpfCnpj.setPromptText("CNPJ");
                tfInscricaoEstadual.setDisable(false);
                dpDataNascimento.setDisable(true);
            } else {
                tfCpfCnpj.setPromptText("CPF");
                tfInscricaoEstadual.setDisable(true);
                dpDataNascimento.setDisable(false);
            }
        });
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        if (cliente.getId() != 0) {
            tfNome.setText(cliente.getNome());
            tfCelular.setText(cliente.getCelular());
            tfEmail.setText(cliente.getEmail());
            if (cliente instanceof PessoaFisica pf) {
                cbPessoaJuridica.setSelected(false);
                tfCpfCnpj.setText(pf.getCpf());
                dpDataNascimento.setValue(pf.getDataNascimento());
            } else if (cliente instanceof PessoaJuridica pj) {
                cbPessoaJuridica.setSelected(true);
                tfCpfCnpj.setText(pj.getCnpj());
                tfInscricaoEstadual.setText(pj.getInscricaoEstadual());
            }
        }
    }

    public boolean isBtConfirmarClicked() {
        return btConfirmarClicked;
    }

    @FXML
    private void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            cliente.setNome(tfNome.getText());
            cliente.setCelular(tfCelular.getText());
            cliente.setEmail(tfEmail.getText());

            if (cbPessoaJuridica.isSelected()) {
                if (!(cliente instanceof PessoaJuridica)) {
                    cliente = new PessoaJuridica();
                }
                ((PessoaJuridica) cliente).setCnpj(tfCpfCnpj.getText());
                ((PessoaJuridica) cliente).setInscricaoEstadual(tfInscricaoEstadual.getText());
            } else {
                if (!(cliente instanceof PessoaFisica)) {
                    cliente = new PessoaFisica();
                }
                ((PessoaFisica) cliente).setCpf(tfCpfCnpj.getText());
                ((PessoaFisica) cliente).setDataNascimento(dpDataNascimento.getValue());
            }

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
        if (tfNome.getText() == null || tfNome.getText().trim().isEmpty()) {
            errorMessage += "Nome inválido!\n";
        }
        if (tfCelular.getText() == null || tfCelular.getText().trim().isEmpty()) {
            errorMessage += "Celular inválido!\n";
        }
        if (tfCpfCnpj.getText() == null || tfCpfCnpj.getText().trim().isEmpty()) {
            errorMessage += cbPessoaJuridica.isSelected() ? "CNPJ inválido!\n" : "CPF inválido!\n";
        }
        if (cbPessoaJuridica.isSelected() && (tfInscricaoEstadual.getText() == null || tfInscricaoEstadual.getText().trim().isEmpty())) {
            errorMessage += "Inscrição Estadual inválida!\n";
        }
        if (!cbPessoaJuridica.isSelected() && dpDataNascimento.getValue() == null) {
            errorMessage += "Data de Nascimento inválida!\n";
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