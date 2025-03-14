package br.edu.ifsc.fln;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class JavaFXApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/view/FXMLVBoxMainApp.fxml"));
        } catch (IOException ex) {
            System.out.println("Não foi possível carregar o formulário");
            ex.printStackTrace();
            return;
        }

        Scene scene = new Scene(root, 600, 450);

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/IFSC_logo_vertical.png")));
        primaryStage.setTitle("SISTEMA LAVACAR ALEJANDRO");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}