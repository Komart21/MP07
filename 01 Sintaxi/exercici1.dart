import 'dart:math';
import 'dart:io';

const int ROWS = 6;
const int COLS = 10;
const int MINES = 8;

void main() {
  var tauler = generarTauler();
  List<List<bool>>? mines; // Generarem mines després de la primera jugada
  bool esPrimeraJugada = true;
  bool hasPerdut = false;
  int tirades = 0;
  int casellesDestapades = 0;
  int casellesSenseMines = ROWS * COLS - MINES;
  int banderesCorrectes = 0;

  mostrarTauler(tauler);

  while (!hasPerdut) {
    print("Escriu una comanda (ex. C3 o B1 bandera):");
    var input = stdin.readLineSync()?.trim().toUpperCase();

    if (input == null) continue;

    if (input.contains('FLAG') || input.contains('BANDERA')) {
      var coords = input.split(' ')[0];
      var fila = coords[0].codeUnitAt(0) - 'A'.codeUnitAt(0);
      var columna = int.parse(coords.substring(1));

      if (tauler[fila][columna] == '·') {
        tauler[fila][columna] = '#';
        if (mines != null && mines[fila][columna]) {
          banderesCorrectes++; // Incrementar si la bandera és correcta
        }
      } else if (tauler[fila][columna] == '#') {
        tauler[fila][columna] = '·';
        if (mines != null && mines[fila][columna]) {
          banderesCorrectes--; // Decrementar si treus una bandera correcta
        }
      }
    } else if (input == 'TRAMPES' || input == 'CHEAT') {
      if (mines != null) mostrarMines(tauler, mines);
    } else {
      var fila = input[0].codeUnitAt(0) - 'A'.codeUnitAt(0);
      var columna = int.parse(input.substring(1));

      // Si és la primera jugada, generar les mines després de la jugada
      if (esPrimeraJugada) {
        mines = generarMines(tauler, fila, columna);
        esPrimeraJugada = false;
      }

      // Destapar casella i comprovar si has perdut
      hasPerdut =
          destapaCasella(tauler, fila, columna, mines!, true, (destapada) {
        if (destapada) {
          casellesDestapades++;
        }
      });

      if (!hasPerdut) {
        tirades++;

        // Comprovar si s'han destapat totes les caselles sense mines
        if (casellesDestapades == casellesSenseMines ||
            banderesCorrectes == MINES) {
          print("Felicitats! Has guanyat la partida amb $tirades tirades!");
          return;
        }
      }
    }

    mostrarTauler(tauler);
  }

  print("Has perdut! Número de tirades: $tirades");
}

List<List<String>> generarTauler() {
  return List.generate(ROWS, (i) => List.generate(COLS, (j) => '·'));
}

List<List<bool>> generarMines(
    List<List<String>> tauler, int filaInicial, int columnaInicial) {
  var mines = List.generate(ROWS, (i) => List.generate(COLS, (j) => false));
  var random = Random();
  int comptadorMines = 0;

  while (comptadorMines < MINES) {
    var fila = random.nextInt(ROWS);
    var columna = random.nextInt(COLS);

    // Evitar que la primera casella destapada tingui una mina
    if ((fila == filaInicial && columna == columnaInicial) ||
        mines[fila][columna]) {
      continue; // Saltar si és la casella inicial o ja hi ha una mina
    }

    mines[fila][columna] = true;
    comptadorMines++;
  }

  return mines;
}

void mostrarTauler(List<List<String>> tauler) {
  print(" 0123456789");
  for (int i = 0; i < ROWS; i++) {
    var row = String.fromCharCode('A'.codeUnitAt(0) + i);
    print('$row${tauler[i].join()}');
  }
}

void mostrarMines(List<List<String>> tauler, List<List<bool>> mines) {
  print("Tauler amb mines:");
  for (int i = 0; i < ROWS; i++) {
    var row = String.fromCharCode('A'.codeUnitAt(0) + i);
    print('$row${mines[i].map((mina) => mina ? '*' : '·').join()}');
  }
}

bool destapaCasella(List<List<String>> tauler, int x, int y,
    List<List<bool>> mines, bool esJugadaUsuari, Function(bool) onDestapa) {
  if (x < 0 || y < 0 || x >= ROWS || y >= COLS || tauler[x][y] != '·')
    return false;

  if (mines[x][y]) {
    if (esJugadaUsuari) {
      return true; // Explosió
    } else {
      return false;
    }
  }

  // Comptar mines adjacents
  int numMines = comptaMinesAdjacents(mines, x, y);
  tauler[x][y] = numMines > 0 ? '$numMines' : ' ';
  onDestapa(true); // Notificar que s'ha destapat una casella

  if (numMines == 0) {
    // Cridar recursivament a les caselles adjacents
    for (var dx = -1; dx <= 1; dx++) {
      for (var dy = -1; dy <= 1; dy++) {
        if (dx != 0 || dy != 0) {
          destapaCasella(tauler, x + dx, y + dy, mines, false, onDestapa);
        }
      }
    }
  }

  return false; // No hi ha explosió
}

int comptaMinesAdjacents(List<List<bool>> mines, int x, int y) {
  int count = 0;
  for (var dx = -1; dx <= 1; dx++) {
    for (var dy = -1; dy <= 1; dy++) {
      if (dx == 0 && dy == 0) continue;
      int nx = x + dx;
      int ny = y + dy;
      if (nx >= 0 && ny >= 0 && nx < ROWS && ny < COLS && mines[nx][ny]) {
        count++;
      }
    }
  }
  return count;
}
