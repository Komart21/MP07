package com.project;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;

import org.json.JSONException;
import org.json.JSONObject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private Button enviar, parar, imagenes;

    @FXML
    private TextArea text_user;

    @FXML
    private ScrollPane respuesta;

    @FXML
    private VBox outputContent;

    @FXML
    private ImageView img;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private CompletableFuture<HttpResponse<InputStream>> streamRequest;
    private CompletableFuture<HttpResponse<String>> completeRequest;
    private AtomicBoolean isCancelled = new AtomicBoolean(false);
    private InputStream currentInputStream;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Future<?> streamReadingTask;
    private String base64ImageString;

    @FXML
    public void initialize() {
        respuesta.setStyle("-fx-border-color: black; -fx-border-width: 2;");
        respuesta.setFitToWidth(true);  // Ajustar el VBox al ancho del ScrollPane
        respuesta.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Deshabilitar desplazamiento horizontal
        setButtonsIdle();
    }

    @FXML
    private void callStream(ActionEvent event) {
        String prompt = text_user.getText();
        if (prompt.isEmpty()) {
            return;
        }
        clearOutput();
        text_user.clear();
        setButtonsRunning();
        isCancelled.set(false);

        JSONObject jsonRequest = new JSONObject();

        String model = (base64ImageString != null) ? "llava-phi3" : "llama3.2:1b";
        jsonRequest.put("model", model);
        jsonRequest.put("prompt", prompt);
        jsonRequest.put("stream", true);

        if (base64ImageString != null) {
            jsonRequest.put("image", base64ImageString);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:11434/api/generate"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(jsonRequest.toString()))
                .build();

        streamRequest = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                .thenApply(response -> {
                    currentInputStream = response.body();
                    streamReadingTask = executorService.submit(() -> {
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(currentInputStream))) {
                            String line;
                            StringBuilder completeResponse = new StringBuilder();
                            while ((line = reader.readLine()) != null) {
                                if (isCancelled.get()) {
                                    break;
                                }
                                System.out.println("Línea recibida: " + line);
                                try {
                                    JSONObject jsonResponse = new JSONObject(line);
                                    if (jsonResponse.has("response")) {
                                        String responseText = jsonResponse.getString("response");

                                        // Aquí es donde se muestra el texto gradualmente
                                        completeResponse.append(responseText).append(" ");
                                        String finalResponseText = completeResponse.toString().trim();

                                        // Mostrar el texto progresivamente en la UI
                                        Platform.runLater(() -> updateOutputText(finalResponseText));
                                    } else {
                                        System.err.println("Respuesta sin clave 'response': " + jsonResponse);
                                    }
                                } catch (JSONException e) {
                                    System.err.println("Error al parsear la línea como JSON: " + line);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Platform.runLater(() -> {
                                appendTextToOutput("Error durante la transmisión.");
                                setButtonsIdle();
                            });
                        } finally {
                            try {
                                if (currentInputStream != null) {
                                    currentInputStream.close();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Platform.runLater(this::setButtonsIdle);
                        }
                    });
                    return response;
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    Platform.runLater(this::setButtonsIdle);
                    return null;
                });
    }

    @FXML
    private void handleLoadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) enviar.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(selectedFile);
                if (bufferedImage == null) {
                    System.err.println("Error: La imagen no se pudo leer.");
                    return;
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                if (bytes.length == 0) {
                    System.err.println("Error: No se pudieron obtener los bytes de la imagen.");
                    return;
                }
                base64ImageString = Base64.getEncoder().encodeToString(bytes);
                img.setImage(new Image(new ByteArrayInputStream(bytes)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearOutput() {
        outputContent.getChildren().clear();
    }

    private void appendTextToOutput(String text) {
        TextFlow textFlow = new TextFlow(new Text(text));
        textFlow.setMaxWidth(outputContent.getWidth());
        textFlow.setPrefWidth(Region.USE_COMPUTED_SIZE);
        outputContent.getChildren().add(textFlow);
        respuesta.setVvalue(1.0);
    }

    // Método para actualizar el contenido gradualmente
    private void updateOutputText(String text) {
        clearOutput(); // Limpia la salida antes de agregar el nuevo contenido
        TextFlow textFlow = new TextFlow(new Text(text));
        textFlow.setMaxWidth(outputContent.getWidth()); 
        textFlow.setPrefWidth(Region.USE_COMPUTED_SIZE);
        outputContent.getChildren().add(textFlow);
        respuesta.setVvalue(1.0); // Mueve el scroll hacia abajo automáticamente
    }

    private void setButtonsRunning() {
        enviar.setDisable(true);
        parar.setDisable(false);
        imagenes.setDisable(true);
    }

    private void setButtonsIdle() {
        enviar.setDisable(false);
        parar.setDisable(true);
        imagenes.setDisable(false);
    }

    @FXML
    private void callBreak(ActionEvent event) {
        isCancelled.set(true);
        if (streamReadingTask != null) {
            streamReadingTask.cancel(true);
        }
        appendTextToOutput("Solicitud cancelada.");
        setButtonsIdle();
    }
}
