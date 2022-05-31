package com.example.gameforpio;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class Tile extends Rectangle {
    public int y;
    public int x;

    Piece piece; //pion na polu
    Colors color;

    public Tile(Colors color, int positionX, int positionY, int size, int y, int x) //przyjmuje kolor pola , wsporzedne i rozmiar
    {
        this.color = color;
        setWidth(size);
        setHeight(size);
        setStroke(Color.CRIMSON);
        setStrokeWidth(2);
        relocate(positionX, positionY);
        this.y = y;
        this.x = x;
        if (color == Colors.LIGHT) {
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
