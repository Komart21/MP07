package com.project;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    final int WINDOW_WIDTH = 600;
    final int WINDOW_HEIGHT = 400;
    final int MOBILE_WIDTH_THRESHOLD = 360;  // Umbral para cambiar a la vista móvil

    private Parent desktopRoot;
    private Parent mobileRoot;

    @Override
    public void start(Stage stage) throws Exception {
        // Cargar ambos layouts al iniciar la aplicación
        URL desktopFXMLLocation = getClass().getResource("/assets/layout.fxml");
        URL mobileFXMLLocation = getClass().getResource("/assets/mobile_layout.fxml");

        if (desktopFXMLLocation == null || mobileFXMLLocation == null) {
            System.out.println("Error al cargar los archivos FXML.");
            return;
        }

        // Cargar ambos layouts
        desktopRoot = FXMLLoader.load(desktopFXMLLocation);
        mobileRoot = FXMLLoader.load(mobileFXMLLocation);

        // Escena inicial basada en el layout de escritorio
        Scene scene = new Scene(desktopRoot, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Configurar la escena
        stage.setScene(scene);
        stage.setTitle("App");
        stage.show();

        // Listener para el tamaño de la ventana
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() < MOBILE_WIDTH_THRESHOLD) {
                // Cambiar al layout móvil si el ancho es menor al umbral
                stage.getScene().setRoot(mobileRoot);
            } else {
                // Cambiar al layout de escritorio si el ancho es mayor al umbral
                stage.getScene().setRoot(desktopRoot);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
