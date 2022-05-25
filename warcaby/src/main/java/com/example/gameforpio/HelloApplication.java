package com.example.gameforpio;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import static java.lang.Math.abs;
import static javafx.scene.input.MouseEvent.*;

enum Colors {LIGHT, DARK}

class info {
    static boolean clicked;//czy pion jest klikniety
    static boolean isBeat = false;//czy jest bicie
    static Colors colorCanMove = Colors.LIGHT; // Kolor, ktory moze sie poruszac 2-jasne 1-ciemne
    static int actualpiecex;
    static int actualpiecey;
}

class Piece extends Circle {
    Colors color; //kolor piona
    int x_current;//ktory rzad szachownicy
    int y_current;//ktora kolumna szachownicy
    boolean isQueen ; //Sprawdza, czy pion jest damkÄ…
    Tile[][] tiles; //tablicy wszystkich pol


    EventHandler<MouseEvent> enter = (MouseEvent e) -> //podswietlenia po najechaniu myszka
    {
        if (color == info.colorCanMove) {
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
        this.isQueen=queen;
        System.out.println(i+"  "+j);
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
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                tiles[i][j].removeEventHandler(MOUSE_CLICKED,move);
            }
        }
    }

    //zgasza po wyjechaniu myszka
    public boolean canIBeat(int y,int x,int new_y, int new_x) {
        if (tiles[(y + new_y) / 2][(x + new_x) / 2].piece == null || tiles[y][x].piece == null)
            return false;
        return (tiles[(y + new_y) / 2][(x + new_x) / 2].piece.color != tiles[y][x].piece.color) && tiles[new_y][new_x].hasNoPiece() && !tiles[(y + new_y) / 2][(x + new_x) / 2].hasNoPiece();

    }    //po kliknieciu na podswietlony Tile przemieszcza pionek na odpowiedni Tile
    EventHandler<MouseEvent> move = ev -> {
        if (info.clicked) {
            clear();
            unmove();
        }
        Tile tile = (Tile) ev.getSource();
        System.out.println("y: " + y_current + "x: " + x_current);
        System.out.println("c: " + color);
        info.isBeat= abs(info.actualpiecey - tile.i) == 2;//przypisanie czy wystepuje bicie po przesunieciu w y
        HelloApplication.movePieceFromOneTileToAnother(info.actualpiecey, info.actualpiecex, tile.i, tile.j);
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

        info.clicked = false;
        //Zamiana tur
        int bicia=0;
        if(info.isBeat)
            bicia=possiblecapture(tile.i, tile.j);
        if(bicia==0){
            if (info.colorCanMove == Colors.DARK)
                info.colorCanMove = Colors.LIGHT;
            else
                info.colorCanMove = Colors.DARK;
            info.isBeat=false;
        }
        unmove();
    };
    int possiblemoves() {
        int flag = 0;
        if (color == Colors.DARK&& !isQueen) {
            if (y_current < 7) {
                if (x_current < 7)
                    if (tiles[y_current + 1][x_current + 1].hasNoPiece() && color == info.colorCanMove) {
                        tiles[y_current + 1][x_current + 1].addEventHandler(MOUSE_CLICKED, move);
                        tiles[y_current + 1][x_current + 1].setStroke(Color.BLUE);
                        tiles[y_current + 1][x_current + 1].setStrokeWidth(5);
                        flag++;
                    }
                if (x_current > 0)
                    if (tiles[y_current + 1][x_current - 1].hasNoPiece() && color == info.colorCanMove) {
                        tiles[y_current + 1][x_current - 1].addEventHandler(MOUSE_CLICKED, move);
                        tiles[y_current + 1][x_current - 1].setStroke(Color.BLUE);
                        tiles[y_current + 1][x_current - 1].setStrokeWidth(5);
                        flag++;
                    }
            }
        } else {
            if (x_current < 7&& !isQueen)
                if (tiles[y_current - 1][x_current + 1].hasNoPiece() && color == info.colorCanMove) {
                    tiles[y_current - 1][x_current + 1].addEventHandler(MOUSE_CLICKED, move);
                    tiles[y_current - 1][x_current + 1].setStroke(Color.BLUE);
                    tiles[y_current - 1][x_current + 1].setStrokeWidth(5);
                    flag++;
                }
            if (x_current > 0)
                if (tiles[y_current - 1][x_current - 1].hasNoPiece() && color == info.colorCanMove) {
                    tiles[y_current - 1][x_current - 1].addEventHandler(MOUSE_CLICKED, move);
                    tiles[y_current - 1][x_current - 1].setStroke(Color.BLUE);
                    tiles[y_current - 1][x_current - 1].setStrokeWidth(5);
                    flag++;
                }
        }
        return flag;
    }
    int possiblecapture(int y,int x) {
        int flag = 0;
        if (y < 6 && x < 6) {
            if (canIBeat(y,x,y + 2, x + 2) && color == info.colorCanMove) {
                tiles[y + 2][x + 2].addEventHandler(MOUSE_CLICKED, move);
                tiles[y + 2][x + 2].setStroke(Color.BLUE);
                tiles[y + 2][x + 2].setStrokeWidth(5);
                possiblecapture(y+2,x+2);
                flag++;
            }
        }
        if (y < 6 && x > 1) {
            if (canIBeat(y,x,y + 2, x - 2) && color == info.colorCanMove) {
                tiles[y + 2][x - 2].addEventHandler(MOUSE_CLICKED, move);
                tiles[y + 2][x - 2].setStroke(Color.BLUE);
                tiles[y + 2][x - 2].setStrokeWidth(5);
                possiblecapture(y+2,x-2);
                flag++;
            }
        }
        if (y > 1 && x < 6) {
            if (canIBeat(y,x,y - 2, x + 2) && color == info.colorCanMove) {
                tiles[y - 2][x + 2].addEventHandler(MOUSE_CLICKED, move);
                tiles[y - 2][x + 2].setStroke(Color.BLUE);
                tiles[y - 2][x + 2].setStrokeWidth(5);
                possiblecapture(y-2,x+2);
                flag++;
            }
        }
        if (y > 1 && x > 1) {
            if (canIBeat(y,x,y - 2, x - 2) && color == info.colorCanMove) {
                tiles[y - 2][x - 2].addEventHandler(MOUSE_CLICKED, move);
                tiles[y - 2][x - 2].setStroke(Color.BLUE);
                tiles[y - 2][x - 2].setStrokeWidth(5);
                possiblecapture(y-2,x-2);
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
    EventHandler<MouseEvent> path =new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {

            if (info.clicked) {
                clear();
                unmove(); //TrochÄ™ nie wiem po co to, bez tego dziaĹ‚a
            }
            if(info.isBeat==false)
                possiblemoves();
            possiblecapture(y_current,x_current);
            setStrokeWidth(0);
            info.actualpiecex=x_current;
            info.actualpiecey=y_current;
            info.clicked = true;
        }
    };

}

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


public class HelloApplication extends Application {

    static AnchorPane tileMap = new AnchorPane();
    static Board board = new Board();

    public static void movePieceFromOneTileToAnother(int old_i, int old_j, int i, int j) {
        int size = 80;//rozmiar pola
        int y = 35 + (i * size) + 40; //poczatkowy y
        int x = 320 + (j * size) + 40;//poczatkowy x
        if(board.tiles[old_i][old_j].piece==null)
            return;
        board.tiles[i][j].setPiece( board.tiles[old_i][old_j].piece.color, x, y, board.tiles, i, j, board.tiles[old_i][old_j].piece.isQueen);
        //Zastanawiam sie, czy da sie to ustawic w jakis inny prostszy sposob albo w innym miejscu ale no
        //Tutaj to musialm polozyc bo inaczej przemiana w damke nie dzialala, po wyjsciu z pola zmieniala sie
        //w zwykĹ‚y pion
        if ( board.tiles[old_i][old_j].piece.isQueen) {
            board.tiles[i][j].piece.isQueen = true;
            if (board.tiles[i][j].piece.color == Colors.DARK) {
                board.tiles[i][j].piece.setFill(new ImagePattern(new Image("/Pionek_Ciemny_Damka.png")));
            }
            if (board.tiles[i][j].piece.color == Colors.LIGHT) {
                board.tiles[i][j].piece.setFill(new ImagePattern(new Image("/Pionek_Jasny_Damka.png")));
            }
        }
        //
        tileMap.getChildren().add(board.tiles[i][j].piece);
        tileMap.getChildren().remove(board.tiles[old_i][old_j].piece);
        board.tiles[old_i][old_j].deletePiece();
        if (info.isBeat) {
            tileMap.getChildren().remove(board.tiles[(i + old_i) / 2][(j + old_j) / 2].piece);
            board.tiles[(i + old_i) / 2][(j + old_j) / 2].deletePiece();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {

        Scene content = new Scene(tileMap, 1280, 720);
        primaryStage.setScene(content);
        info.clicked = false;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tileMap.getChildren().add(board.tiles[i][j]);
                if ((i % 2 == 0 && j % 2 != 0) || (i % 2 != 0 && j % 2 == 0)) //dodaje odpowiednie pola jako dzieci
                {
                    if (i < 3)
                        tileMap.getChildren().add(board.tiles[i][j].piece);
                    if (i > 4)
                        tileMap.getChildren().add(board.tiles[i][j].piece);
                }

            }

        }
        primaryStage.show();

    }
}

