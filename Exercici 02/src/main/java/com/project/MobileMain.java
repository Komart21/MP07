package com.project;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MobileMain extends Application {

    final int MOBILE_WIDTH = 400;  // Tamaño por defecto para móvil
    final int MOBILE_HEIGHT = 700; // Tamaño por defecto para móvil

    @Override
    public void start(Stage stage) throws Exception {
        URL fxmlLocation = getClass().getResource("/assets/mobile_layout.fxml");

        if (fxmlLocation == null) {
            System.out.println("Archivo FXML móvil no encontrado.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        Scene scene = new Scene(root, MOBILE_WIDTH, MOBILE_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Nintendo DB - Móvil");

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
