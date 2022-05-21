package com.example.gameforpio;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
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

import static javafx.scene.input.MouseEvent.*;


class info{
    static boolean clicked;//czy pion jest klikniety
    static boolean isBeat=false;//czy jest bicie
}

class Piece extends Circle{
    int color; //kolor piona
    int x_current;//ktory rzad szachownicy
    int y_current;//ktora kolumna szachownicy

    Tile[][] tiles; //tablicy wszystkich pol
    //podswietla mozliwe ruchy (bez bicia)

    //podswietlenia po najechaniu myszka
    EventHandler<MouseEvent> eventHandler = (MouseEvent e) -> {
        setStrokeWidth(3);
        setStroke(Color.LIGHTBLUE);
    };
    public void unmove(){
        if (y_current < 7) {
            if (x_current < 7)
                if (tiles[y_current + 1][x_current + 1].hasNoPiece()) {
                    tiles[y_current + 1][x_current + 1].removeEventHandler(MOUSE_CLICKED, move);
                }
                else if(y_current<6 && x_current<6){
                    if (tiles[y_current + 2][x_current + 2].hasNoPiece()) {
                        tiles[y_current + 2][x_current + 2].removeEventHandler(MOUSE_CLICKED, move);
                    }
                }
            if (x_current > 0)
                if (tiles[y_current + 1][x_current - 1].hasNoPiece()) {
                    tiles[y_current + 1][x_current - 1].removeEventHandler(MOUSE_CLICKED, move);
                }
                else if(y_current<6 && x_current>1){
                    if (tiles[y_current + 2][x_current - 2].hasNoPiece()) {
                        tiles[y_current + 2][x_current - 2].removeEventHandler(MOUSE_CLICKED, move);
                    }
                }
        }
        if (y_current > 0) {
            if (x_current < 7)
                if (tiles[y_current - 1][x_current + 1].hasNoPiece()) {
                    tiles[y_current - 1][x_current + 1].removeEventHandler(MOUSE_CLICKED, move);
                }
                else if(y_current>1 && x_current<6){
                    if (tiles[y_current - 2][x_current + 2].hasNoPiece()) {
                        tiles[y_current - 2][x_current + 2].removeEventHandler(MOUSE_CLICKED, move);
                    }
                }
            if (x_current > 0)
                if (tiles[y_current - 1][x_current - 1].hasNoPiece()) {
                    tiles[y_current - 1][x_current - 1].removeEventHandler(MOUSE_CLICKED, move);
                }
                else if(y_current>1 && x_current>1){
                    if (tiles[y_current - 2][x_current - 2].hasNoPiece()) {
                        tiles[y_current - 2][x_current - 2].removeEventHandler(MOUSE_CLICKED, move);
                    }
                }
        }
    }
    //zgasza po wyjechaniu myszka
    EventHandler<MouseEvent> eventHandler2 = e -> setStrokeWidth(0);
    //po kliknieciu na podswietlony Tile przemieszcza pionek na odpowiedni Tile
    EventHandler<MouseEvent> move = ev -> {
        if (info.clicked) {
            clear();
            unmove();
        }
        Tile tile = (Tile) ev.getSource();
        System.out.println(y_current+x_current+tile.i+ tile.j );
        HelloApplication.movePieceFromOneTileToAnother(y_current, x_current, tile.i, tile.j);
        info.clicked = true;
        unmove();
    };

    SimpleObjectProperty<EventHandler<MouseEvent>> path = new SimpleObjectProperty<>(this, "path", new EventHandler<>() //podswietla mozliwe ruchy (bez bicia)
    {
        @Override
        public void handle(MouseEvent e) {

            if (info.clicked) {
                clear();
                unmove();
            }

            if (y_current < 7) {
                if (x_current < 7)
                    if (tiles[y_current + 1][x_current + 1].hasNoPiece()) {
                        tiles[y_current + 1][x_current + 1].addEventHandler(MOUSE_CLICKED, move);
                        tiles[y_current + 1][x_current + 1].setStroke(Color.BLUE);
                        tiles[y_current + 1][x_current + 1].setStrokeWidth(5);
                    }
                    else if(y_current<6 && x_current<6){
                        if (tiles[y_current + 2][x_current + 2].hasNoPiece()) {
                            tiles[y_current + 2][x_current + 2].addEventHandler(MOUSE_CLICKED, move);
                            info.isBeat=true;
                            tiles[y_current + 2][x_current + 2].setStroke(Color.BLUE);
                            tiles[y_current + 2][x_current + 2].setStrokeWidth(5);
                        }
                    }
                if (x_current > 0)
                    if (tiles[y_current + 1][x_current - 1].hasNoPiece()) {
                        tiles[y_current + 1][x_current - 1].addEventHandler(MOUSE_CLICKED, move);
                        tiles[y_current + 1][x_current - 1].setStroke(Color.BLUE);
                        tiles[y_current + 1][x_current - 1].setStrokeWidth(5);
                    }
                    else if(y_current<6 && x_current>1){
                        if (tiles[y_current + 2][x_current - 2].hasNoPiece()) {
                            tiles[y_current + 2][x_current - 2].addEventHandler(MOUSE_CLICKED, move);
                            info.isBeat=true;
                            tiles[y_current + 2][x_current - 2].setStroke(Color.BLUE);
                            tiles[y_current + 2][x_current - 2].setStrokeWidth(5);
                        }
                    }
            }
            if (y_current > 0) {
                if (x_current < 7)
                    if (tiles[y_current - 1][x_current + 1].hasNoPiece()) {
                        tiles[y_current - 1][x_current + 1].addEventHandler(MOUSE_CLICKED, move);
                        tiles[y_current - 1][x_current + 1].setStroke(Color.BLUE);
                        tiles[y_current - 1][x_current + 1].setStrokeWidth(5);
                    }
                    else if(y_current>1 && x_current<6){
                        if (tiles[y_current - 2][x_current + 2].hasNoPiece()) {
                            tiles[y_current - 2][x_current + 2].addEventHandler(MOUSE_CLICKED, move);
                            info.isBeat=true;
                            tiles[y_current - 2][x_current + 2].setStroke(Color.BLUE);
                            tiles[y_current - 2][x_current + 2].setStrokeWidth(5);
                        }
                    }
                if (x_current > 0)
                    if (tiles[y_current - 1][x_current - 1].hasNoPiece()) {
                        tiles[y_current - 1][x_current - 1].addEventHandler(MOUSE_CLICKED, move);
                        tiles[y_current - 1][x_current - 1].setStroke(Color.BLUE);
                        tiles[y_current - 1][x_current - 1].setStrokeWidth(5);
                    }
                    else if(y_current>1 && x_current>1){
                        if (tiles[y_current - 2][x_current - 2].hasNoPiece()) {
                            tiles[y_current - 2][x_current - 2].addEventHandler(MOUSE_CLICKED, move);
                            info.isBeat=true;
                            tiles[y_current - 2][x_current - 2].setStroke(Color.BLUE);
                            tiles[y_current - 2][x_current - 2].setStrokeWidth(5);
                        }
                    }
            }
            setStrokeWidth(0);
            info.clicked = true;
        }
    });


    void clear() //przywraca pola do punktu zero
    {
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                tiles[i][j].setStroke(Color.CRIMSON);
                tiles[i][j].setStrokeWidth(2);
            }
        }
    }
    public Piece(int color, int x, int y,Tile[][] tiles,int i,int j) //przyjmuje color(1,0), wsporzedne, tablice pol, rzad,kolumne
    {
        setCenterX(x);
        setCenterY(y);
        setRadius(25);
        this.color=color;
        this.y_current =i;
        this.x_current =j;
        this.tiles=tiles;
        if(color==1)
        {
            setFill(new ImagePattern(new Image("/Pionek_Ciemny.png")));
        }
        else {
            setFill(new ImagePattern(new Image("/Pionek_Jasny.png")));
        }
        this.addEventHandler(MOUSE_ENTERED, eventHandler);
        this.addEventHandler(MOUSE_EXITED, eventHandler2);
        this.addEventHandler(MOUSE_CLICKED, path.get());

    }

}

class Tile extends Rectangle
{
    public int i;
    public int j;

    Piece piece; //pion na polu
    public boolean hasNoPiece() // sprawdza czy ma pionka na sobie
    {
        return piece == null;
    }
    public void deletePiece(){
        this.piece = null;
    }
    public Tile(int kolor,int x,int y,int size, int i, int j) //przyjmuje kolor pola , wsporzedne i rozmiar
    {
        setWidth(size);
        setHeight(size);
        setStroke(Color.CRIMSON);
        setStrokeWidth(2);
        relocate(x,y);
        this.i = i;
        this.j = j;
        if(kolor%2==0)
        {
            setFill(Color.WHITE);
        }else {
            setFill(Color.BLACK);
        }



    }
    public void setPiece(int color, int x, int y, Tile[][] tiles, int i, int j) //tworzy piona na sobie
    {
        piece = new Piece(color,x,y,tiles,i, j);
    }



}



class Board {
    Tile[][] tiles = new Tile[8][8]; //wszystkie pola



    Board()
    {
        int color=0; //odpowiada za kolor pól
        int size = 80;//rozmiar pola
        int y = 35; //poczatkowy y
        for(int i = 0; i<8; i++, y += size) {
            int x = 320;//poczatkowy x
            for (int j = 0; j < 8; j++, x += size) {
                Tile tile= new Tile(color, x, y, size,i,j);
                tiles[i][j] = tile;
                if((i%2==0&&j%2!=0)||(i%2!=0&&j%2==0)) //czy pole jest czarne
                {
                    if(i<3) //czy gora dla czarnych
                        tile.setPiece(1, x +40, y +40,tiles,i,j);
                    if(i>4) // czy dol dla bialych
                        tile.setPiece(2, x +40, y +40,tiles,i,j);
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

    public void start(Stage primaryStage) {

        Scene content = new Scene(tileMap, 1280, 720);
        primaryStage.setScene(content);
        info.clicked=false;

        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                tileMap.getChildren().add(board.tiles[i][j]);
                if((i%2==0&&j%2!=0)||(i%2!=0&&j%2==0)) //dodaje odpowiednie pola jako dzieci
                {
                    if(i<3)
                        tileMap.getChildren().add(board.tiles[i][j].piece);
                    if(i>4)
                        tileMap.getChildren().add(board.tiles[i][j].piece);
                }

            }

        }
        primaryStage.show();

    }
    public static void movePieceFromOneTileToAnother(int old_i, int old_j, int i, int j) {
        int size = 80;//rozmiar pola
        int y = 35+(i*size)+40; //poczatkowy y
        int x = 320+(j*size)+40;//poczatkowy x
        Piece newPiece = board.tiles[old_i][old_j].piece;
        board.tiles[i][j].setPiece(newPiece.color, x, y, board.tiles, i,j);
        tileMap.getChildren().add(board.tiles[i][j].piece);
        tileMap.getChildren().remove(board.tiles[old_i][old_j].piece);
        board.tiles[old_i][old_j].deletePiece();
        if(info.isBeat){
            tileMap.getChildren().remove(board.tiles[(i+old_i)/2][(j+old_j)/2].piece);
            board.tiles[(i+old_i)/2][(j+old_j)/2].deletePiece();
            info.isBeat=false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

