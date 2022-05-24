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
    static boolean whiteTurn=true; //Czy jest tura białych
    static int colorCanMove=2; // Kolor, ktory moze sie poruszac 2-jasne 1-ciemne
}

class Piece extends Circle{
    int color; //kolor piona
    int x_current;//ktory rzad szachownicy
    int y_current;//ktora kolumna szachownicy
    boolean isQueen = false; //Sprawdza, czy pion jest damką
    Tile[][] tiles; //tablicy wszystkich pol
    //podswietla mozliwe ruchy (bez bicia)

    //podswietlenia po najechaniu myszka
    EventHandler<MouseEvent> eventHandler = (MouseEvent e) -> {
        if(color==info.colorCanMove){
        setStrokeWidth(3);
        setStroke(Color.LIGHTBLUE);
        }
    };
    public void unmove(){
        if (y_current < 7) {
            if (x_current < 7)
                if (tiles[y_current + 1][x_current + 1].hasNoPiece()) {
                    tiles[y_current + 1][x_current + 1].removeEventHandler(MOUSE_CLICKED, move);
                }
                else if(y_current<6 && x_current<6){
                    if (canIBeat(y_current+2,x_current+2) &&color==info.colorCanMove) {
                        tiles[y_current + 2][x_current + 2].removeEventHandler(MOUSE_CLICKED, move);
                    }
                }
            if (x_current > 0)
                if (tiles[y_current + 1][x_current - 1].hasNoPiece()) {
                    tiles[y_current + 1][x_current - 1].removeEventHandler(MOUSE_CLICKED, move);
                }
                else if(y_current<6 && x_current>1){
                    if (canIBeat(y_current+2,x_current-2)&&color==info.colorCanMove) {
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
                    if (canIBeat(y_current-2,x_current+2)&&color==info.colorCanMove) {
                        tiles[y_current - 2][x_current + 2].removeEventHandler(MOUSE_CLICKED, move);
                    }
                }
            if (x_current > 0)
                if (tiles[y_current - 1][x_current - 1].hasNoPiece()) {
                    tiles[y_current - 1][x_current - 1].removeEventHandler(MOUSE_CLICKED, move);
                }
                else if(y_current>1 && x_current>1){
                    if (canIBeat(y_current-2,x_current-2)&&color==info.colorCanMove) {
                        tiles[y_current - 2][x_current - 2].removeEventHandler(MOUSE_CLICKED, move);
                    }
                }
        }
    }
    //zgasza po wyjechaniu myszka
    public boolean canIBeat(int new_y,int new_x){
        if(tiles[(y_current + new_y)/2][(x_current + new_x)/2].piece==null ||tiles[y_current][x_current].piece ==null)
            return false;
        return (tiles[(y_current + new_y)/2][(x_current + new_x)/2].piece.color!= tiles[y_current][x_current].piece.color)&&tiles[new_y][new_x].hasNoPiece()&&!tiles[(y_current + new_y)/2][(x_current + new_x)/2].hasNoPiece();

    }
    EventHandler<MouseEvent> eventHandler2 = e -> setStrokeWidth(0);
    //po kliknieciu na podswietlony Tile przemieszcza pionek na odpowiedni Tile
    EventHandler<MouseEvent> move = ev -> {
        if (info.clicked) {
            clear();
            unmove();
        }
        Tile tile = (Tile) ev.getSource();
        //System.out.println(y_current+x_current+tile.i+ tile.j );
        System.out.println("y: " + y_current + "x: "+x_current);
        System.out.println("c: " +color);
        HelloApplication.movePieceFromOneTileToAnother(y_current, x_current, tile.i, tile.j);
        //Przemiana w Damke
        if(tile.i==0 &&color ==2)
        {
            System.out.println("Warunek do przemiany w damke spelniony");
            tiles[tile.i][tile.j].piece.isQueen=true;
            tiles[tile.i][tile.j].piece.setFill(new ImagePattern(new Image("/Pionek_Jasny_Damka.png")));
        }
        if(tile.i==7 &&color ==1)
        {
            System.out.println("Warunek do przemiany w damke ciemna spelniony");
            tiles[tile.i][tile.j].piece.isQueen=true;
            tiles[tile.i][tile.j].piece.setFill(new ImagePattern(new Image("/Pionek_Ciemny_Damka.png")));
        }

        info.clicked = true;
        //Zamiana tur
        info.whiteTurn= !info.whiteTurn; // <-- To jest w sumie niepotrzebne, siedzi tu dla picu
        if(info.colorCanMove==1)info.colorCanMove=2;
        else if(info.colorCanMove==2) {info.colorCanMove=1;}
        unmove();
    };

    SimpleObjectProperty<EventHandler<MouseEvent>> path = new SimpleObjectProperty<>(this, "path", new EventHandler<>() //podswietla mozliwe ruchy (bez bicia)
    { //Poruszanie się i podświetlanie bloków, na które możemy się ruszyć.
        @Override
        public void handle(MouseEvent e) {

            if (info.clicked) {
                clear();
                unmove(); //Trochę nie wiem po co to, bez tego działa
                info.isBeat=false;
            }

            if (y_current < 7) {
                if (x_current < 7)
                    if (tiles[y_current + 1][x_current + 1].hasNoPiece()&&color==info.colorCanMove) {
                        tiles[y_current + 1][x_current + 1].addEventHandler(MOUSE_CLICKED, move);
                        tiles[y_current + 1][x_current + 1].setStroke(Color.BLUE);
                        tiles[y_current + 1][x_current + 1].setStrokeWidth(5);
                    }
                    else if(y_current<6 && x_current<6){
                        if (canIBeat(y_current+2,x_current+2) &&color==info.colorCanMove) {
                            tiles[y_current + 2][x_current + 2].addEventHandler(MOUSE_CLICKED, move);
                            info.isBeat=true;
                            tiles[y_current + 2][x_current + 2].setStroke(Color.BLUE);
                            tiles[y_current + 2][x_current + 2].setStrokeWidth(5);
                        }
                    }
                if (x_current > 0)
                    if (tiles[y_current + 1][x_current - 1].hasNoPiece()&&color==info.colorCanMove) {
                        tiles[y_current + 1][x_current - 1].addEventHandler(MOUSE_CLICKED, move);
                        tiles[y_current + 1][x_current - 1].setStroke(Color.BLUE);
                        tiles[y_current + 1][x_current - 1].setStrokeWidth(5);
                    }
                    else if(y_current<6 && x_current>1){
                        if (canIBeat(y_current+2,x_current-2)&&color==info.colorCanMove) {
                            tiles[y_current + 2][x_current - 2].addEventHandler(MOUSE_CLICKED, move);
                            info.isBeat=true;
                            tiles[y_current + 2][x_current - 2].setStroke(Color.BLUE);
                            tiles[y_current + 2][x_current - 2].setStrokeWidth(5);
                        }
                    }
            }
            if (y_current > 0) {
                if (x_current < 7)
                    if (tiles[y_current - 1][x_current + 1].hasNoPiece()&&color==info.colorCanMove) {
                        tiles[y_current - 1][x_current + 1].addEventHandler(MOUSE_CLICKED, move);
                        tiles[y_current - 1][x_current + 1].setStroke(Color.BLUE);
                        tiles[y_current - 1][x_current + 1].setStrokeWidth(5);
                    }
                    else if(y_current>1 && x_current<6){
                        if (canIBeat(y_current-2,x_current+2)&&color==info.colorCanMove) {
                            tiles[y_current - 2][x_current + 2].addEventHandler(MOUSE_CLICKED, move);
                            info.isBeat=true;
                            tiles[y_current - 2][x_current + 2].setStroke(Color.BLUE);
                            tiles[y_current - 2][x_current + 2].setStrokeWidth(5);
                        }
                    }
                if (x_current > 0)
                    if (tiles[y_current - 1][x_current - 1].hasNoPiece()&&color==info.colorCanMove) {
                        tiles[y_current - 1][x_current - 1].addEventHandler(MOUSE_CLICKED, move);
                        tiles[y_current - 1][x_current - 1].setStroke(Color.BLUE);
                        tiles[y_current - 1][x_current - 1].setStrokeWidth(5);
                    }
                    else if(y_current>1 && x_current>1){
                        if (canIBeat(y_current-2,x_current-2)&&color==info.colorCanMove) {
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
    public Piece(int color, int x, int y,Tile[][] tiles,int i,int j,boolean queen) //przyjmuje color(1,0), wsporzedne, tablice pol, rzad,kolumne
    {
        setCenterX(x);
        setCenterY(y);
        setRadius(25);
        this.color=color;
        this.y_current =i;
        this.x_current =j;
        this.tiles=tiles;
        if(color==1&&!isQueen)
        {
            setFill(new ImagePattern(new Image("/Pionek_Ciemny.png")));
        }
        else if (color==2&&!isQueen) {
            setFill(new ImagePattern(new Image("/Pionek_Jasny.png")));
        }
        if(isQueen)
        {
            if(color==1)
            {
                setFill(new ImagePattern(new Image("/Pionek_Ciemny_Damka.png")));
            }
            if(color==2)
            {
                setFill(new ImagePattern(new Image("/Pionek_Jasny_Damka.png")));
            }
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
    public void setPiece(int color, int x, int y, Tile[][] tiles, int i, int j,boolean queen) //tworzy piona na sobie
    {
        piece = new Piece(color,x,y,tiles,i, j,queen);
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
                        tile.setPiece(1, x +40, y +40,tiles,i,j,false);
                    if(i>4) // czy dol dla bialych
                        tile.setPiece(2, x +40, y +40,tiles,i,j,false);
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
        System.out.println("Old piece queen: "+ board.tiles[old_i][old_j].piece.isQueen);
        System.out.println("New piece queen: "+ newPiece.isQueen);
        board.tiles[i][j].setPiece(newPiece.color, x, y, board.tiles, i,j, board.tiles[old_i][old_j].piece.isQueen);
        //Zastanawiam sie, czy da sie to ustawic w jakis inny prostszy sposob albo w innym miejscu ale no
        //Tutaj to musialm polozyc bo inaczej przemiana w damke nie dzialala, po wyjsciu z pola zmieniala sie
        //w zwykły pion
        if(newPiece.isQueen)
        {
            board.tiles[i][j].piece.isQueen=true;
            if( board.tiles[i][j].piece.color==1)
            {
                board.tiles[i][j].piece.setFill(new ImagePattern(new Image("/Pionek_Ciemny_Damka.png")));
            }
            if( board.tiles[i][j].piece.color==2)
            {
                board.tiles[i][j].piece.setFill(new ImagePattern(new Image("/Pionek_Jasny_Damka.png")));
            }
        }
        //
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

