package com.example.gameforpio;

class Board {
    Tile[][] tiles = new Tile[8][8]; //wszystkie pola


    Board() {
        int color = 0; //odpowiada za kolor pĂłl
        int size = 80;//rozmiar pola
        int y = 35; //poczatkowy y
        for (int i = 0; i < 8; i++, y += size) {
            int x = 320;//poczatkowy x
            for (int j = 0; j < 8; j++, x += size) {
                Tile tile = new Tile(color, x, y, size, i, j);
                tiles[i][j] = tile;
                if ((i % 2 == 0 && j % 2 != 0) || (i % 2 != 0 && j % 2 == 0)) //czy pole jest czarne
                {
                    if (i < 3) //czy gora dla czarnych
                        tile.setPiece(Colors.DARK, x + 40, y + 40, tiles, i, j, false);
                    if (i > 4) // czy dol dla bialych
                        tile.setPiece(Colors.LIGHT, x + 40, y + 40, tiles, i, j, false);
                }
                color++;
            }
            color++;
        }
    }


}
