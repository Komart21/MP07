import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false, 
      title: 'Demo Categorías',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(
            seedColor: const Color(0xFF8B0000)),
        useMaterial3: true,
      ),
      home: const CategoriesScreen(),
    );
  }
}

// Modelo para las Categorías
class Category {
  final String name;
  Category({required this.name});

  factory Category.fromJson(String json) {
    return Category(name: json);
  }
}

// Modelo para los Items
class Item {
  final int id;
  final String name;
  final String category;
  final String photo;
  final String description;

  Item({
    required this.id,
    required this.name,
    required this.category,
    required this.photo,
    required this.description,
  });

  factory Item.fromJson(Map<String, dynamic> json) {
    return Item(
      id: json['id'],
      name: json['name'],
      category: json['category'],
      photo: json['photo'],
      description: json['description'],
    );
  }
}

// Servicio para obtener los datos del servidor NodeJS
class ApiService {
  static const String baseUrl =
      'http://localhost:3000/api'; // Cambia 'localhost' por tu IP si es necesario

  // Obtener todas las categorías
  Future<List<Category>> fetchCategories() async {
    final response = await http.get(Uri.parse('$baseUrl/categories'));
    if (response.statusCode == 200) {
      List<dynamic> categoriesJson = json.decode(response.body);
      return categoriesJson.map((json) => Category.fromJson(json)).toList();
    } else {
      throw Exception('Error al cargar las categorías');
    }
  }

  // Obtener los items de una categoría
  Future<List<Item>> fetchItemsByCategory(String category) async {
    final response = await http.get(Uri.parse('$baseUrl/items/$category'));
    if (response.statusCode == 200) {
      List<dynamic> itemsJson = json.decode(response.body);
      return itemsJson.map((json) => Item.fromJson(json)).toList();
    } else {
      throw Exception('Error al cargar los items');
    }
  }

  // Obtener detalles de un item por su ID
  Future<Item> fetchItemDetails(int id) async {
    final response = await http.get(Uri.parse('$baseUrl/items/id/$id'));
    if (response.statusCode == 200) {
      return Item.fromJson(json.decode(response.body));
    } else {
      throw Exception('Item no encontrado');
    }
  }

  // Obtener el ítem de fondo (Fondo)
  Future<Item> fetchBackground() async {
    final response = await http.get(Uri.parse('$baseUrl/items/Fondo'));
    if (response.statusCode == 200) {
      List<dynamic> itemsJson = json.decode(response.body);
      // Debería haber solo un ítem de fondo, tomamos el primero.
      return Item.fromJson(itemsJson[0]);
    } else {
      throw Exception('Fondo no encontrado');
    }
  }
}

// Pantalla de Categorías con fondo dinámico desde el servidor
class CategoriesScreen extends StatefulWidget {
  const CategoriesScreen({super.key});

  @override
  _CategoriesScreenState createState() => _CategoriesScreenState();
}

class _CategoriesScreenState extends State<CategoriesScreen> {
  late Future<List<Category>> futureCategories;
  late Future<Item> futureBackground;
  String? selectedCategory; // Variable para la categoría seleccionada

  @override
  void initState() {
    super.initState();
    futureCategories = ApiService().fetchCategories();
    futureBackground = ApiService().fetchBackground(); // Cargar el fondo
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Categorías', style: TextStyle(color: Colors.black, fontWeight: FontWeight.bold)),
        backgroundColor: const Color(0xFF8B0000), // Color de fondo de la AppBar
      ),
      body: FutureBuilder<Item>(
        future: futureBackground, // Cargar la imagen de fondo
        builder: (context, snapshot) {
          if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          } else if (snapshot.hasData) {
            final background = snapshot.data!;
            final backgroundUrl = 'http://localhost:3000/${background.photo}'; // URL de la imagen de fondo

            return Stack(
              children: [
                // Imagen de fondo
                Positioned.fill(
                  child: Image.network(
                    backgroundUrl,
                    fit: BoxFit.cover,
                  ),
                ),
                // Contenido sobre la imagen de fondo
                FutureBuilder<List<Category>>(
                  future: futureCategories,
                  builder: (context, snapshot) {
                    if (snapshot.hasError) {
                      return Center(child: Text('Error: ${snapshot.error}'));
                    } else if (snapshot.hasData) {
                      final categories = snapshot.data!;
                      return Padding(
                        padding: const EdgeInsets.all(16.0),
                        child: Column(
                          children: <Widget>[
                            // Título de las categorías
                            const Padding(
                              padding: EdgeInsets.symmetric(vertical: 10.0),
                              child: Text(
                                'Selecciona una Categoría',
                                style: TextStyle(
                                  fontSize: 22,
                                  fontWeight: FontWeight.bold,
                                  color: Colors.black, // Texto en negro
                                ),
                              ),
                            ),
                            // Caja con borde que contiene el Dropdown
                            Container(
                              decoration: BoxDecoration(
                                borderRadius: BorderRadius.circular(8.0),
                                color: const Color(0xFF8B0000), // Fondo rojo oscuro
                                border: Border.all(
                                  color: const Color(0xFF3b3db2),
                                  width: 2.0,
                                ),
                              ),
                              child: Padding(
                                padding: const EdgeInsets.all(8.0),
                                child: DropdownButton<String>(
                                  dropdownColor: const Color(0xFF8B0000), // Fondo rojo oscuro
                                  value: selectedCategory,
                                  hint: const Text("Selecciona una categoría", style: TextStyle(color: Colors.black, fontWeight: FontWeight.bold)),
                                  items: categories.map((category) {
                                    return DropdownMenuItem(
                                      value: category.name,
                                      child: Text(
                                        category.name,
                                        style: const TextStyle(color: Colors.black, fontWeight: FontWeight.bold),
                                      ),
                                    );
                                  }).toList(),
                                  onChanged: (value) {
                                    setState(() {
                                      selectedCategory = value!;
                                    });
                                    Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                        builder: (context) =>
                                            ItemsScreen(category: selectedCategory!),
                                      ),
                                    );
                                  },
                                  isExpanded: true,
                                ),
                              ),
                            ),
                          ],
                        ),
                      );
                    }
                    return Container(); // Eliminar el CircularProgressIndicator
                  },
                ),
              ],
            );
          }
          return Container(); // Eliminar el CircularProgressIndicator
        },
      ),
    );
  }
}

// Pantalla de Items por Categoría con fondo dinámico
class ItemsScreen extends StatelessWidget {
  final String category;

  const ItemsScreen({super.key, required this.category});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Items de $category', style: const TextStyle(color: Colors.black, fontWeight: FontWeight.bold))),
      body: FutureBuilder<Item>(
        future: ApiService().fetchBackground(), // Cargar la imagen de fondo
        builder: (context, snapshot) {
          if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          } else if (snapshot.hasData) {
            final background = snapshot.data!;
            final backgroundUrl = 'http://localhost:3000/${background.photo}';

            return Stack(
              children: [
                // Imagen de fondo
                Positioned.fill(
                  child: Image.network(
                    backgroundUrl,
                    fit: BoxFit.cover,
                  ),
                ),
                // Contenido sobre la imagen de fondo
                FutureBuilder<List<Item>>(
                  future: ApiService().fetchItemsByCategory(category),
                  builder: (context, snapshot) {
                    if (snapshot.hasError) {
                      return Center(child: Text('Error: ${snapshot.error}'));
                    } else if (snapshot.hasData) {
                      final items = snapshot.data!;
                      return ListView.builder(
                        itemCount: items.length,
                        itemBuilder: (context, index) {
                          return ListTile(
                            title: Text(
                              items[index].name,
                              style: const TextStyle(color: Colors.black, fontWeight: FontWeight.bold),
                            ),
                            onTap: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (context) =>
                                      ItemDetailScreen(itemId: items[index].id),
                                ),
                              );
                            },
                          );
                        },
                      );
                    }
                    return Container(); // Eliminar el CircularProgressIndicator
                  },
                ),
              ],
            );
          }
          return Container(); // Eliminar el CircularProgressIndicator
        },
      ),
    );
  }
}

// Pantalla de Detalle del Item
class ItemDetailScreen extends StatelessWidget {
  final int itemId;

  const ItemDetailScreen({super.key, required this.itemId});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Detalle del Ítem', style: TextStyle(color: Colors.black, fontWeight: FontWeight.bold))),
      body: FutureBuilder<Item>(
        future: ApiService().fetchItemDetails(itemId),
        builder: (context, snapshot) {
          if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          } else if (snapshot.hasData) {
            final item = snapshot.data!;
            final imageUrl =
                'http://localhost:3000/api/item/${item.id}/photo'; // Cambia la IP si es necesario
            return Center(
              child: SingleChildScrollView(
                child: Padding(
                  padding: const EdgeInsets.symmetric(
                      horizontal: 16.0), // Margen en los lados
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      // Mostrar imagen del item con tamaño más pequeño
                      Padding(
                        padding: const EdgeInsets.only(
                            bottom: 20.0), // Margen debajo de la imagen
                        child: ClipRRect(
                          borderRadius:
                              BorderRadius.circular(12), // Bordes redondeados
                          child: Container(
                            height: 200, // Tamaño reducido para la imagen
                            width: double.infinity,
                            child: Image.network(
                              imageUrl,
                              fit: BoxFit.contain, // Ajuste para que la imagen se vea completa
                              errorBuilder: (context, error, stackTrace) {
                                return const Text('Imagen no encontrada');
                              },
                            ),
                          ),
                        ),
                      ),
                      // Mostrar nombre del item
                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Text(
                          item.name,
                          textAlign: TextAlign.center,
                          style: const TextStyle(
                            fontSize: 24,
                            fontWeight: FontWeight.bold,
                            color: Colors.black, // Texto en negro
                          ),
                        ),
                      ),
                      // Mostrar descripción del item
                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Text(
                          item.description,
                          textAlign: TextAlign.center,
                          style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: Colors.black),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            );
          }
          return Container(); 
        },
      ),
    );
  }
}
