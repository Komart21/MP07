package com.project;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class Controller {

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    @FXML
    private ListView<String> itemListView;

    @FXML
    private ImageView itemImageView;

    @FXML
    private Text itemDescription;

    @FXML
    private Text itemTitle; 

    private Map<String, String[]> itemsMap;
    private Map<String, String> imagePathMap; // Mapa para almacenar las rutas de las imágenes
    private Map<String, String> descriptionMap; // Mapa para almacenar descripciones

    @FXML
    public void initialize() {
        // Inicializar el mapa de elementos
        itemsMap = new HashMap<>();
        itemsMap.put("Jocs", new String[] {"Game Donkey Kong", "Metroid", "Pikmin", "Pokemon Red y Blue", "Super Mario Bros", "Super Mario Kart", "Legends of Zelda"});
        itemsMap.put("Personatges", new String[] {"Bowser", "Donkey Kong", "Fox", "Inkling", "Kirby", "Link", "Luigi", "Mario", "Olimar", "Peach", "Pikachu", "Samus", "Toad", "Wario"});
        itemsMap.put("Consoles", new String[] {"Nintento 64", "Nintento GameCube", "Nintento Nes", "Super Nintendo", "Nintento Switch", "Wii", "Wiiu"});

        // Mapa para almacenar las rutas de las imágenes
        imagePathMap = new HashMap<>();
        imagePathMap.put("Game Donkey Kong", "./src/main/resources/assets/images/game_dk.png");
        imagePathMap.put("Metroid", "./src/main/resources/assets/images/game_metroid.png");
        imagePathMap.put("Pikmin", "./src/main/resources/assets/images/game_pikmin.png");
        imagePathMap.put("Pokemon Red y Blue", "./src/main/resources/assets/images/game_pred.png");
        imagePathMap.put("Super Mario Bros", "./src/main/resources/assets/images/game_smb.png");
        imagePathMap.put("Super Mario Kart", "./src/main/resources/assets/images/game_smk.png");
        imagePathMap.put("Legends of Zelda", "./src/main/resources/assets/images/game_zelda.png");
        
        imagePathMap.put("Bowser", "./src/main/resources/assets/images/character_bowser.png");
        imagePathMap.put("Donkey Kong", "./src/main/resources/assets/images/character_dk.png");
        imagePathMap.put("Fox", "./src/main/resources/assets/images/character_fox.png");
        imagePathMap.put("Inkling", "./src/main/resources/assets/images/character_inkling.png");
        imagePathMap.put("Kirby", "./src/main/resources/assets/images/character_kirby.png");
        imagePathMap.put("Link", "./src/main/resources/assets/images/character_link.png");
        imagePathMap.put("Luigi", "./src/main/resources/assets/images/character_luigi.png");
        imagePathMap.put("Mario", "./src/main/resources/assets/images/character_mario.png");
        imagePathMap.put("Olimar", "./src/main/resources/assets/images/character_olimar.png");
        imagePathMap.put("Peach", "./src/main/resources/assets/images/character_peach.png");
        imagePathMap.put("Pikachu", "./src/main/resources/assets/images/character_pikachu.png");
        imagePathMap.put("Samus", "./src/main/resources/assets/images/character_samus.png");
        imagePathMap.put("Toad", "./src/main/resources/assets/images/character_toad.png");
        imagePathMap.put("Wario", "./src/main/resources/assets/images/character_wario.png");

        imagePathMap.put("Nintento 64", "./src/main/resources/assets/images/nintendo_64.png");
        imagePathMap.put("Nintento GameCube", "./src/main/resources/assets/images/nintendo_gamecube.png");
        imagePathMap.put("Nintento Nes", "./src/main/resources/assets/images/nintendo_nes.png");
        imagePathMap.put("Super Nintendo", "./src/main/resources/assets/images/nintendo_sn.png");
        imagePathMap.put("Nintento Switch", "./src/main/resources/assets/images/nintendo_switch.png");
        imagePathMap.put("Wii", "./src/main/resources/assets/images/nintendo_wii.png");
        imagePathMap.put("Wiiu", "./src/main/resources/assets/images/nintendo_wiiu.png");

        // Inicializar el mapa de descripciones
        descriptionMap = new HashMap<>();
        descriptionMap.put("Game Donkey Kong", "¡Únete a Donkey Kong en su emocionante aventura!");
        descriptionMap.put("Metroid", "Explora el universo y enfréntate a la caza de Metroid.");
        descriptionMap.put("Pikmin", "Dirige a los Pikmin en misiones estratégicas.");
        descriptionMap.put("Pokemon Red y Blue", "Captura Pokémon y conviértete en el campeón.");
        descriptionMap.put("Super Mario Bros", "Acompaña a Mario en su búsqueda para rescatar a la princesa.");
        descriptionMap.put("Super Mario Kart", "Compite en emocionantes carreras con tus personajes favoritos.");
        descriptionMap.put("Legends of Zelda", "Embárcate en una épica aventura para salvar a Hyrule.");
        
        // Descripciones de personajes
        descriptionMap.put("Bowser", "El rey de los Koopas y eterno rival de Mario.");
        descriptionMap.put("Donkey Kong", "El famoso gorila conocido por su fuerza.");
        descriptionMap.put("Fox", "El astuto piloto de Star Fox.");
        descriptionMap.put("Inkling", "Los jóvenes guerreros del juego Splatoon.");
        descriptionMap.put("Kirby", "El adorable héroe que puede copiar habilidades.");
        descriptionMap.put("Link", "El valiente héroe de la saga Zelda.");
        descriptionMap.put("Luigi", "El hermano de Mario, siempre listo para ayudar.");
        descriptionMap.put("Mario", "El icónico fontanero y héroe de Nintendo.");
        descriptionMap.put("Olimar", "El capitán que dirige a los Pikmin.");
        descriptionMap.put("Peach", "La princesa de Mushroom Kingdom.");
        descriptionMap.put("Pikachu", "El Pokémon más famoso y el compañero de Ash.");
        descriptionMap.put("Samus", "La cazarrecompensas intergaláctica.");
        descriptionMap.put("Toad", "El leal servidor de la princesa Peach.");
        descriptionMap.put("Wario", "El codicioso rival de Mario.");

        // Descripciones de consolas
        descriptionMap.put("Nintento 64", "La consola que trajo la 3D a los videojuegos.");
        descriptionMap.put("Nintento GameCube", "Consola con forma de cubo y grandes títulos.");
        descriptionMap.put("Nintento Nes", "La consola que resucitó la industria de los videojuegos.");
        descriptionMap.put("Super Nintendo", "Consola clásica con un gran catálogo de juegos.");
        descriptionMap.put("Nintento Switch", "La consola híbrida de Nintendo, portátil y de sobremesa.");
        descriptionMap.put("Wii", "La consola que revolucionó el juego con su control por movimiento.");
        descriptionMap.put("Wiiu", "La consola que introdujo el juego asimétrico con su GamePad.");

        // Agregar categorías al ChoiceBox
        categoryChoiceBox.getItems().addAll("Jocs", "Personatges", "Consoles");

        // Configurar el ListView según la categoría seleccionada
        categoryChoiceBox.setOnAction(event -> updateItemList());
        itemListView.setOnMouseClicked(event -> updateItemDetails());

        // Configurar el ListView para usar celdas personalizadas
        itemListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
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
                            String imagePath = imagePathMap.get(item);
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

    private void updateItemList() {
        String selectedCategory = categoryChoiceBox.getValue();
        itemListView.getItems().clear();
        if (selectedCategory != null) {
            String[] items = itemsMap.get(selectedCategory);
            if (items != null) {
                itemListView.getItems().addAll(items);
            }
        }
    }

    private void updateItemDetails() {
        String selectedItem = itemListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String imagePath = imagePathMap.get(selectedItem); // Obtén la ruta de la imagen
            if (imagePath != null) {
                itemImageView.setImage(new Image(new File(imagePath).toURI().toString())); // Cargar imagen
            }

            // Mostrar el título
            itemTitle.setText(selectedItem); // Mostrar el nombre del elemento

            // Mostrar la descripción
            String description = descriptionMap.get(selectedItem); // Obténemos descripción
            itemDescription.setText(description != null ? description : "Descripción no disponible");
        }
    }
}
