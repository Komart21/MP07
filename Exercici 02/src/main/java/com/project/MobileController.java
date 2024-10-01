package com.project;

import java.io.File;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class MobileController {

    @FXML
    private Button jocsButton;
    @FXML
    private Button personatgesButton;
    @FXML
    private Button consolesButton;
    @FXML
    private Button backButton;

    @FXML
    private VBox menuPanel; 
    @FXML
    private VBox contentPanel; 

    @FXML
    private ListView<String> itemListViewMobile;

    @FXML
    private ImageView itemImageViewMobile;

    @FXML
    private Text itemDescriptionMobile; 

    @FXML
    private Text itemTitleMobile; 

    // Listas para almacenar elementos manualmente
    private List<String> jocsList;
    private List<String> personatgesList;
    private List<String> consolesList;

    @FXML
    public void initialize() {
        // Inicializar listas manualmente
        initializeLists();

        // Configurar el ListView para usar celdas personalizadas
        configureListView();

        // Configurar el evento para el ListView
        itemListViewMobile.setOnMouseClicked(event -> updateSelectedItem());
        showMenuPanel();
    }

    private void initializeLists() {
        // Inicializar listas
        jocsList = List.of("Game Donkey Kong", "Metroid", "Pikmin", "Pokemon Red y Blue",
                           "Super Mario Bros", "Super Mario Kart", "Legends of Zelda");
        personatgesList = List.of("Bowser", "Donkey Kong", "Fox", "Inkling", "Kirby",
                                   "Link", "Luigi", "Mario", "Olimar", "Peach", "Pikachu",
                                   "Samus", "Toad", "Wario");
        consolesList = List.of("Nintento 64", "Nintento GameCube", "Nintento Nes", 
                               "Super Nintendo", "Nintento Switch", "Wii", "Wiiu");
    }

    private void configureListView() {
        // Configurar el ListView para usar celdas personalizadas
        itemListViewMobile.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    private final HBox hbox = new HBox();
                    private final ImageView imageView = new ImageView();
                    private final Text text = new Text();

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            String imagePath = getImagePath(item);
                            if (imagePath != null) {
                                imageView.setImage(new Image(new File(imagePath).toURI().toString()));
                                imageView.setFitWidth(40);
                                imageView.setFitHeight(40);
                            }
                            text.setText(item);
                            hbox.getChildren().clear();
                            hbox.getChildren().addAll(imageView, text);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });
    }

    private String getImagePath(String item) {
        // Devuelve la ruta de la imagen según el ítem
        switch (item) {
            case "Game Donkey Kong": return "./src/main/resources/assets/images/game_dk.png";
            case "Metroid": return "./src/main/resources/assets/images/game_metroid.png";
            case "Pikmin": return "./src/main/resources/assets/images/game_pikmin.png";
            case "Pokemon Red y Blue": return "./src/main/resources/assets/images/game_pred.png";
            case "Super Mario Bros": return "./src/main/resources/assets/images/game_smb.png";
            case "Super Mario Kart": return "./src/main/resources/assets/images/game_smk.png";
            case "Legends of Zelda": return "./src/main/resources/assets/images/game_zelda.png";
            case "Bowser": return "./src/main/resources/assets/images/character_bowser.png";
            case "Donkey Kong": return "./src/main/resources/assets/images/character_dk.png";
            case "Fox": return "./src/main/resources/assets/images/character_fox.png";
            case "Inkling": return "./src/main/resources/assets/images/character_inkling.png";
            case "Kirby": return "./src/main/resources/assets/images/character_kirby.png";
            case "Link": return "./src/main/resources/assets/images/character_link.png";
            case "Luigi": return "./src/main/resources/assets/images/character_luigi.png";
            case "Mario": return "./src/main/resources/assets/images/character_mario.png";
            case "Olimar": return "./src/main/resources/assets/images/character_olimar.png";
            case "Peach": return "./src/main/resources/assets/images/character_peach.png";
            case "Pikachu": return "./src/main/resources/assets/images/character_pikachu.png";
            case "Samus": return "./src/main/resources/assets/images/character_samus.png";
            case "Toad": return "./src/main/resources/assets/images/character_toad.png";
            case "Wario": return "./src/main/resources/assets/images/character_wario.png";
            case "Nintento 64": return "./src/main/resources/assets/images/nintendo_64.png";
            case "Nintento GameCube": return "./src/main/resources/assets/images/nintendo_gamecube.png";
            case "Nintento Nes": return "./src/main/resources/assets/images/nintendo_nes.png";
            case "Super Nintendo": return "./src/main/resources/assets/images/nintendo_sn.png";
            case "Nintento Switch": return "./src/main/resources/assets/images/nintendo_switch.png";
            case "Wii": return "./src/main/resources/assets/images/nintendo_wii.png";
            case "Wiiu": return "./src/main/resources/assets/images/nintendo_wiiu.png";
            default: return null;
        }
    }

    private String getDescription(String item) {
        // Devuelve la descripción según el ítem
        switch (item) {
            case "Game Donkey Kong": return "El juego clásico de plataformas con Donkey Kong.";
            case "Metroid": return "Un juego de acción y aventura en un mundo alienígena.";
            case "Pikmin": return "Una aventura estratégica donde controlas pequeños seres.";
            case "Pokemon Red y Blue": return "Los juegos que dieron inicio a la saga Pokémon.";
            case "Super Mario Bros": return "El juego que definió el género de plataformas.";
            case "Super Mario Kart": return "La primera entrega de la popular saga de carreras.";
            case "Legends of Zelda": return "Embárcate en una épica aventura para salvar a Hyrule.";
            case "Bowser": return "El rey de los Koopas y eterno rival de Mario.";
            case "Donkey Kong": return "El famoso gorila conocido por su fuerza.";
            case "Fox": return "El astuto piloto de Star Fox.";
            case "Inkling": return "Los jóvenes guerreros del juego Splatoon.";
            case "Kirby": return "El adorable héroe que puede copiar habilidades.";
            case "Link": return "El valiente héroe de la saga Zelda.";
            case "Luigi": return "El hermano de Mario, siempre listo para ayudar.";
            case "Mario": return "El icónico fontanero y héroe de Nintendo.";
            case "Olimar": return "El capitán que dirige a los Pikmin.";
            case "Peach": return "La princesa de Mushroom Kingdom.";
            case "Pikachu": return "El Pokémon más famoso y el compañero de Ash.";
            case "Samus": return "La cazarrecompensas intergaláctica.";
            case "Toad": return "El leal servidor de la princesa Peach.";
            case "Wario": return "El codicioso rival de Mario.";
            case "Nintento 64": return "La consola que trajo la 3D a los videojuegos.";
            case "Nintento GameCube": return "Consola con forma de cubo y grandes títulos.";
            case "Nintento Nes": return "La consola que resucitó la industria de los videojuegos.";
            case "Super Nintendo": return "Consola clásica con un gran catálogo de juegos.";
            case "Nintento Switch": return "La consola híbrida de Nintendo, portátil y de sobremesa.";
            case "Wii": return "La consola que revolucionó el juego con su control por movimiento.";
            case "Wiiu": return "La consola que introdujo el juego asimétrico con su GamePad.";
            default: return "Descripción no disponible.";
        }
    }

    @FXML
    private void updateSelectedItem() {
        String selectedItem = itemListViewMobile.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Mostrar la imagen y descripción del elemento seleccionado
            String imagePath = getImagePath(selectedItem);
            if (imagePath != null) {
                itemImageViewMobile.setImage(new Image(new File(imagePath).toURI().toString()));
            }
            itemDescriptionMobile.setText(getDescription(selectedItem));
            itemTitleMobile.setText(selectedItem);
        }
    }

    private void showContentPanel(List<String> items) {
        itemListViewMobile.getItems().clear();
        itemListViewMobile.getItems().addAll(items);
        menuPanel.setVisible(false); // Ocultar el panel de menú
        contentPanel.setVisible(true); // Mostrar el panel de contenido
    }

    private void showMenuPanel() {
        menuPanel.setVisible(true);
        contentPanel.setVisible(false);
    }

    @FXML
    public void onJocsButtonClick() {
        showContentPanel(jocsList);
    }

    @FXML
    public void onPersonatgesButtonClick() {
        showContentPanel(personatgesList);
    }

    @FXML
    public void onConsolesButtonClick() {
        showContentPanel(consolesList);
    }

    @FXML
    public void onBackButtonClick() {
        showMenuPanel(); // Usar el método para mostrar el menú
    }
}
