package com.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Controller {

    @FXML
    private Label display; 

    @FXML
    private Button boton0, boton1, boton2, boton3, boton4, boton5, boton6, boton7, boton8, boton9;
    @FXML
    private Button botonsumar, botonrestar, botonmultiplicar, botondividir, botonborrar, botonigual, botoneliminar;

    private StringBuilder input = new StringBuilder();
    private double firstNumber = 0;
    private String operation = "";
    private boolean startNewNumber = true;

    @FXML
    private void initialize() {
        boton0.setOnAction(this::handleNumber);
        boton1.setOnAction(this::handleNumber);
        boton2.setOnAction(this::handleNumber);
        boton3.setOnAction(this::handleNumber);
        boton4.setOnAction(this::handleNumber);
        boton5.setOnAction(this::handleNumber);
        boton6.setOnAction(this::handleNumber);
        boton7.setOnAction(this::handleNumber);
        boton8.setOnAction(this::handleNumber);
        boton9.setOnAction(this::handleNumber);

        botonsumar.setOnAction(this::handleOperation);
        botonrestar.setOnAction(this::handleOperation);
        botonmultiplicar.setOnAction(this::handleOperation);
        botondividir.setOnAction(this::handleOperation);

        botonigual.setOnAction(this::handleEquals);
        botoneliminar.setOnAction(this::handleClear);
        botonborrar.setOnAction(this::handleDelete);
    }

    @FXML
    private void handleNumber(ActionEvent event) {
        if (startNewNumber) {
            input.setLength(0); 
            startNewNumber = false;
        }

        Button button = (Button) event.getSource();
        input.append(button.getText());
        updateDisplay();
    }

    @FXML
    private void handleOperation(ActionEvent event) {
        Button button = (Button) event.getSource();
        String op = button.getText();

        if (!input.toString().isEmpty()) {
            firstNumber = Double.parseDouble(input.toString());
            operation = op;
            startNewNumber = true;
        }
    }

    @FXML
    private void handleEquals(ActionEvent event) {
        if (operation.isEmpty() || input.toString().isEmpty()) {
            return; 
        }

        double secondNumber = Double.parseDouble(input.toString());
        double result = 0;

        switch (operation) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "-":
                result = firstNumber - secondNumber;
                break;
            case "X":
                result = firstNumber * secondNumber;
                break;
            case "÷":
                if (secondNumber != 0) {
                    result = firstNumber / secondNumber;
                } else {
                    display.setText("Error");
                    input.setLength(0);  
                    operation = ""; 
                    startNewNumber = true;
                    return;
                }
                break;
        }

        // Mostrar el resultado en el display
        display.setText(String.valueOf(result));
        input.setLength(0);  
        input.append(result);
        operation = ""; 
        startNewNumber = true;
    }

    @FXML
    private void handleClear(ActionEvent event) {
        input.setLength(0);
        display.setText(""); 
        firstNumber = 0;
        operation = "";
        startNewNumber = true;
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        if (input.length() > 0) {
            input.deleteCharAt(input.length() - 1);
            updateDisplay();
        }
    }

    private void updateDisplay() {
        display.setText(input.toString());
    }
}
