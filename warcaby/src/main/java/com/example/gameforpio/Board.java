package com.example.gameforpio;


class Board {
    final int BOARD_WIDTH = 8;
    final int BOARD_HEIGHT = 8;
    final int PRIMARY_ROW_POSITION_BLACK = 3;
    final int PRIMARY_ROW_POSITION_WHITE = 4;
    final int TILE_SIZE = 80;
    final int PRIMARY_POSITION_X = 320;
    final int PRIMARY_POSITION_Y = 35;
    Tile[][] tiles = new Tile[BOARD_HEIGHT][BOARD_WIDTH]; //wszystkie pola


    Board() {
        Colors color = Colors.LIGHT;
        int y = PRIMARY_POSITION_Y; //poczatkowy y
        for (int i = 0; i < BOARD_HEIGHT; i++, y += TILE_SIZE) {
            int x = PRIMARY_POSITION_X;//poczatkowy x
            for (int j = 0; j < BOARD_WIDTH; j++, x += TILE_SIZE) {
                Tile tile = new Tile(color, x, y, TILE_SIZE, i, j);
                tiles[i][j] = tile;
                if (color == Colors.DARK) {
                    if (i < PRIMARY_ROW_POSITION_BLACK) {
                        tile.setPiece(Colors.DARK, x + TILE_SIZE / 2, y + TILE_SIZE / 2, tiles, i, j, false);
                    }
                    if (i > PRIMARY_ROW_POSITION_WHITE) {
                        tile.setPiece(Colors.LIGHT, x + TILE_SIZE / 2, y + TILE_SIZE / 2, tiles, i, j, false);
                    }
                }
                color = switchColor(color);
            }
            color = switchColor(color);
        }
    }


    private Colors switchColor(Colors color) {
        if (color == Colors.LIGHT) {
            color = Colors.DARK;
        } else {
            color = Colors.LIGHT;
        }
        return color;
    }


}
