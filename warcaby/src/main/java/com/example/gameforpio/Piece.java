package com.example.gameforpio;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import static java.lang.Math.abs;
import static javafx.scene.input.MouseEvent.*;

class Piece extends Circle {
    Colors color; //kolor piona
    int x_current;//ktory rzad szachownicy
    int y_current;//ktora kolumna szachownicy
    boolean isQueen; //Sprawdza, czy pion jest damkÄ…
    Tile[][] tiles; //tablicy wszystkich pol


    EventHandler<MouseEvent> enter = (MouseEvent e) -> //podswietlenia po najechaniu myszka
    {
        if (color == Logic.colorCanMove) {
            setStrokeWidth(3);
            setStroke(Color.LIGHTBLUE);
        }
    };
    EventHandler<MouseEvent> quit = e -> setStrokeWidth(0);

    public Piece(Colors color, int x, int y, Tile[][] tiles, int i, int j, boolean queen) //przyjmuje color, wsporzedne, tablice pol, rzad,kolumne
    {
        setCenterX(x);
        setCenterY(y);
        setRadius(25);
        this.color = color;
        this.y_current = i;
        this.x_current = j;
        this.tiles = tiles;
        this.isQueen = queen;
        System.out.println(i + "  " + j);
        if (color == Colors.DARK && !isQueen) {
            setFill(new ImagePattern(new Image("/Pionek_Ciemny.png")));
        } else if (color == Colors.LIGHT && !isQueen) {
            setFill(new ImagePattern(new Image("/Pionek_Jasny.png")));
        }
        if (isQueen) {
            if (color == Colors.DARK) {
                setFill(new ImagePattern(new Image("/Pionek_Ciemny_Damka.png")));
            }
            if (color == Colors.LIGHT) {
                setFill(new ImagePattern(new Image("/Pionek_Jasny_Damka.png")));
            }
        }
        this.addEventHandler(MOUSE_ENTERED, enter);
        this.addEventHandler(MOUSE_EXITED, quit);
        this.addEventHandler(MOUSE_CLICKED, path);
    }

    public void unmove() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tiles[i][j].removeEventHandler(MOUSE_CLICKED, move);
            }
        }
    }

    //zgasza po wyjechaniu myszka
    public boolean canIBeat(int y, int x, int new_y, int new_x) {
        if (tiles[(y + new_y) / 2][(x + new_x) / 2].piece == null || tiles[y][x].piece == null)
            return false;
        return (tiles[(y + new_y) / 2][(x + new_x) / 2].piece.color != tiles[y][x].piece.color) && tiles[new_y][new_x].hasNoPiece() && !tiles[(y + new_y) / 2][(x + new_x) / 2].hasNoPiece();

    }    //po kliknieciu na podswietlony Tile przemieszcza pionek na odpowiedni Tile

    EventHandler<MouseEvent> move = ev -> {
        if (Logic.clicked) {
            clear();
            unmove();
        }
        Tile tile = (Tile) ev.getSource();
        System.out.println("y: " + y_current + "x: " + x_current);
        System.out.println("c: " + color);
        Logic.isBeat = abs(Logic.actualpiecey - tile.i) == 2;//przypisanie czy wystepuje bicie po przesunieciu w y
        HelloApplication.movePieceFromOneTileToAnother(Logic.actualpiecey, Logic.actualpiecex, tile.i, tile.j);
        //Przemiana w Damke

        if (tile.i == 0 && color == Colors.LIGHT) {
            System.out.println("Warunek do przemiany w damke spelniony");
            tiles[tile.i][tile.j].piece.isQueen = true;
            tiles[tile.i][tile.j].piece.setFill(new ImagePattern(new Image("/Pionek_Jasny_Damka.png")));
        }
        if (tile.i == 7 && color == Colors.DARK) {
            System.out.println("Warunek do przemiany w damke ciemna spelniony");
            tiles[tile.i][tile.j].piece.isQueen = true;
            tiles[tile.i][tile.j].piece.setFill(new ImagePattern(new Image("/Pionek_Ciemny_Damka.png")));
        }

        Logic.clicked = false;
        //Zamiana tur
        int bicia = 0;
        if (Logic.isBeat)
            bicia = possiblecapture(tile.i, tile.j);
        if (bicia == 0) {
            if (Logic.colorCanMove == Colors.DARK)
                Logic.colorCanMove = Colors.LIGHT;
            else
                Logic.colorCanMove = Colors.DARK;
            Logic.isBeat = false;
        }
        unmove();
    };

    int possiblemoves() {
        int flag = 0;
        if (color == Colors.DARK && !isQueen) {
            if (y_current < 7) {
                if (x_current < 7)
                    if (tiles[y_current + 1][x_current + 1].hasNoPiece() && color == Logic.colorCanMove) {
                        tiles[y_current + 1][x_current + 1].addEventHandler(MOUSE_CLICKED, move);
                        tiles[y_current + 1][x_current + 1].setStroke(Color.BLUE);
                        tiles[y_current + 1][x_current + 1].setStrokeWidth(5);
                        flag++;
                    }
                if (x_current > 0)
                    if (tiles[y_current + 1][x_current - 1].hasNoPiece() && color == Logic.colorCanMove) {
                        tiles[y_current + 1][x_current - 1].addEventHandler(MOUSE_CLICKED, move);
                        tiles[y_current + 1][x_current - 1].setStroke(Color.BLUE);
                        tiles[y_current + 1][x_current - 1].setStrokeWidth(5);
                        flag++;
                    }
            }
        } else {
            if (x_current < 7 && !isQueen)
                if (tiles[y_current - 1][x_current + 1].hasNoPiece() && color == Logic.colorCanMove) {
                    tiles[y_current - 1][x_current + 1].addEventHandler(MOUSE_CLICKED, move);
                    tiles[y_current - 1][x_current + 1].setStroke(Color.BLUE);
                    tiles[y_current - 1][x_current + 1].setStrokeWidth(5);
                    flag++;
                }
            if (x_current > 0)
                if (tiles[y_current - 1][x_current - 1].hasNoPiece() && color == Logic.colorCanMove) {
                    tiles[y_current - 1][x_current - 1].addEventHandler(MOUSE_CLICKED, move);
                    tiles[y_current - 1][x_current - 1].setStroke(Color.BLUE);
                    tiles[y_current - 1][x_current - 1].setStrokeWidth(5);
                    flag++;
                }
        }
        return flag;
    }

    int possiblecapture(int y, int x) {
        int flag = 0;
        if (y < 6 && x < 6) {
            if (canIBeat(y, x, y + 2, x + 2) && color == Logic.colorCanMove) {
                tiles[y + 2][x + 2].addEventHandler(MOUSE_CLICKED, move);
                tiles[y + 2][x + 2].setStroke(Color.BLUE);
                tiles[y + 2][x + 2].setStrokeWidth(5);
                possiblecapture(y + 2, x + 2);
                flag++;
            }
        }
        if (y < 6 && x > 1) {
            if (canIBeat(y, x, y + 2, x - 2) && color == Logic.colorCanMove) {
                tiles[y + 2][x - 2].addEventHandler(MOUSE_CLICKED, move);
                tiles[y + 2][x - 2].setStroke(Color.BLUE);
                tiles[y + 2][x - 2].setStrokeWidth(5);
                possiblecapture(y + 2, x - 2);
                flag++;
            }
        }
        if (y > 1 && x < 6) {
            if (canIBeat(y, x, y - 2, x + 2) && color == Logic.colorCanMove) {
                tiles[y - 2][x + 2].addEventHandler(MOUSE_CLICKED, move);
                tiles[y - 2][x + 2].setStroke(Color.BLUE);
                tiles[y - 2][x + 2].setStrokeWidth(5);
                possiblecapture(y - 2, x + 2);
                flag++;
            }
        }
        if (y > 1 && x > 1) {
            if (canIBeat(y, x, y - 2, x - 2) && color == Logic.colorCanMove) {
                tiles[y - 2][x - 2].addEventHandler(MOUSE_CLICKED, move);
                tiles[y - 2][x - 2].setStroke(Color.BLUE);
                tiles[y - 2][x - 2].setStrokeWidth(5);
                possiblecapture(y - 2, x - 2);
                flag++;
            }
        }
        return flag;
    }

    void clear() //przywraca pola do punktu zero
    {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tiles[i][j].setStroke(Color.CRIMSON);
                tiles[i][j].setStrokeWidth(2);
            }
        }
    }

    EventHandler<MouseEvent> path = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {

            if (Logic.clicked) {
                clear();
                unmove(); //TrochÄ™ nie wiem po co to, bez tego dziaĹ‚a
            }
            if (Logic.isBeat == false)
                possiblemoves();
            possiblecapture(y_current, x_current);
            setStrokeWidth(0);
            Logic.actualpiecex = x_current;
            Logic.actualpiecey = y_current;
            Logic.clicked = true;
        }
    };

}
