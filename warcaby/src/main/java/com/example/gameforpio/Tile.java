package com.example.gameforpio;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class Tile extends Rectangle {
    public int i;
    public int j;

    Piece piece; //pion na polu

    public Tile(int kolor, int x, int y, int size, int i, int j) //przyjmuje kolor pola , wsporzedne i rozmiar
    {
        setWidth(size);
        setHeight(size);
        setStroke(Color.CRIMSON);
        setStrokeWidth(2);
        relocate(x, y);
        this.i = i;
        this.j = j;
        if (kolor % 2 == 0) {
            setFill(Color.WHITE);
        } else {
            setFill(Color.BLACK);
        }
    }

    public boolean hasNoPiece() // sprawdza czy ma pionka na sobie
    {
        return piece == null;
    }

    public void deletePiece() {
        this.piece = null;
    }

    public void setPiece(Colors color, int x, int y, Tile[][] tiles, int i, int j, boolean queen) //tworzy piona na sobie
    {
        piece = new Piece(color, x, y, tiles, i, j, queen);
    }


}
