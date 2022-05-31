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
    int xCurrent;//ktory rzad szachownicy
    int yCurrent;//ktora kolumna szachownicy
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
        this.yCurrent = i;
        this.xCurrent = j;
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

    public void removeHandlerMove() {
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


    public void makeHighlighted(int y, int x){
        tiles[y][x].addEventHandler(MOUSE_CLICKED, move);
        tiles[y][x].setStroke(Color.BLUE);
        tiles[y][x].setStrokeWidth(5);
    }
    boolean queenbeat(int i,int j)
    {
        int iincrement=0;
        int jincrement=0;
        if(Logic.actualpiecey>i)
        {
            iincrement=1;
        }else{
            iincrement=-1;
        }
        if(Logic.actualpiecex>j)
        {
            jincrement=1;
        }else{
            jincrement=-1;
        }
        System.out.println(i+" "+j+" "+iincrement+" "+jincrement);
        for(int ii=i;ii!=Logic.actualpiecey;ii=ii+iincrement)
        {
            for(int jj=j;jj!=Logic.actualpiecex;jj=jj+jincrement)
            {
                if(tiles[ii][jj].piece!=null)
                {
                    if(color!=tiles[ii][jj].piece.color) {
                        Logic.piecetodiex = jj;
                        Logic.piecetodiey = ii;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    EventHandler<MouseEvent> move = ev -> {
        if (Logic.clicked) {
            clear();
            removeHandlerMove();
        }
        Tile tile = (Tile) ev.getSource();
        System.out.println("y: " + yCurrent + "x: " + xCurrent);
        System.out.println("c: " + color);
        //trzeba zoabczyc czy isQueeen dziala
//przypisanie czy wystepuje bicie po przesunieciu w y
        if(!isQueen) Logic.isBeatPiece = abs(Logic.actualpiecey - tile.i) == 2;
        else Logic.canBeatQueen = queenbeat(tile.i,tile.j);
        System.out.println(Logic.canBeatQueen);
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
        System.out.println(Logic.canBeatQueen);
        Logic.clicked = false;
        //Zamiana tur
        int beats = 0;
        if (Logic.isBeatPiece && !tile.piece.isQueen){
            beats = possibleCapture(tile.i, tile.j);
        }
        if (beats == 0) {
            if (Logic.colorCanMove == Colors.DARK){
                Logic.colorCanMove = Colors.LIGHT;
            }
            else {
                Logic.colorCanMove = Colors.DARK;
            }
            HelloApplication.infoAboutWhoMoves();
            Logic.isBeatPiece = false;
            Logic.canBeatQueen=false;
        }
        removeHandlerMove();
    };

    int possibleMovesPiece() {
        int flag = 0;
        if (color == Colors.DARK) {
            if (yCurrent < 7) {
                if (xCurrent < 7 && tiles[yCurrent + 1][xCurrent + 1].hasNoPiece() && color == Logic.colorCanMove) {
                    makeHighlighted(yCurrent +1, xCurrent +1);
                    flag++;
                }

                if (xCurrent > 0 && tiles[yCurrent + 1][xCurrent - 1].hasNoPiece() && color == Logic.colorCanMove) {
                    makeHighlighted(yCurrent +1, xCurrent -1);
                    flag++;
                }
            }
        } else {
            if (xCurrent < 7 && tiles[yCurrent - 1][xCurrent + 1].hasNoPiece() && color == Logic.colorCanMove) {
                makeHighlighted(yCurrent -1, xCurrent +1);
                flag++;
            }
            if (xCurrent > 0 && tiles[yCurrent - 1][xCurrent - 1].hasNoPiece() && color == Logic.colorCanMove) {
                makeHighlighted(yCurrent -1, xCurrent -1);
                flag++;
            }
        }
        return flag;
    }

    int possibleMovesQueen() {
        int flag = 0;
        if(color==Logic.colorCanMove) {
            int y = yCurrent;
            boolean AnotherColorDetector = false;
            for (int x = xCurrent; x < 7 && y < 7; x++) {
                if (tiles[y + 1][x + 1].hasNoPiece()) makeHighlighted(y + 1, x + 1);
                else {
                    if (tiles[y + 1][x + 1].piece.color == tiles[yCurrent][xCurrent].piece.color) break;
                    else {
                        y++;
                        x++;
                        if (!AnotherColorDetector && y < 7 && x < 7 && tiles[y + 1][x + 1].hasNoPiece()) {
                            AnotherColorDetector = true;
                            makeHighlighted(y + 1, x + 1);
                        } else break;
                    }
                }
                flag++;
                y++;
            }
            AnotherColorDetector = false;
            y = yCurrent;
            for (int x = xCurrent; x > 0 && y < 7; x--) {
                if (tiles[y + 1][x - 1].hasNoPiece()) makeHighlighted(y + 1, x - 1);
                else {
                    if (tiles[y + 1][x - 1].piece.color == tiles[yCurrent][xCurrent].piece.color) break;
                    else {
                        y++;
                        x--;
                        if (!AnotherColorDetector && y < 7 && x > 0 && tiles[y + 1][x - 1].hasNoPiece()) {
                            AnotherColorDetector = true;
                            makeHighlighted(y + 1, x - 1);
                        } else break;
                    }
                }
                flag++;
                y++;
            }
            AnotherColorDetector = false;
            y = yCurrent;
            for (int x = xCurrent; x < 7 && y > 0; x++) {
                if (tiles[y - 1][x + 1].hasNoPiece()) makeHighlighted(y - 1, x + 1);
                else {
                    if (tiles[y - 1][x + 1].piece.color == tiles[yCurrent][xCurrent].piece.color) break;
                    else {
                        y--;
                        x++;
                        if (!AnotherColorDetector && y > 0 && x < 7 && tiles[y - 1][x + 1].hasNoPiece()) {
                            AnotherColorDetector = true;
                            makeHighlighted(y - 1, x + 1);
                        } else break;
                    }
                }
                flag++;
                y--;
            }
            AnotherColorDetector = false;
            y = yCurrent;
            for (int x = xCurrent; x > 0 && y > 0; x--) {
                if (tiles[y - 1][x - 1].hasNoPiece()) makeHighlighted(y - 1, x - 1);
                else {
                    if (tiles[y - 1][x - 1].piece.color == tiles[yCurrent][xCurrent].piece.color) break;
                    else {
                        y--;
                        x--;
                        if (!AnotherColorDetector && y > 0 && x > 0 && tiles[y - 1][x - 1].hasNoPiece()) {
                            AnotherColorDetector = true;
                            makeHighlighted(y - 1, x - 1);
                        }
                    }
                }
                flag++;
                y--;
            }
        }
        return flag;
    }

    int possibleCapture(int y, int x) {
        int flag = 0;
        if (y < 6 && x < 6) {
            if (canIBeat(y, x, y + 2, x + 2) && color == Logic.colorCanMove) {
                makeHighlighted(y+2, x+2);
                possibleCapture(y + 2, x + 2);
                flag++;
            }
        }
        if (y < 6 && x > 1) {
            if (canIBeat(y, x, y + 2, x - 2) && color == Logic.colorCanMove) {
                makeHighlighted(y+2, x-2);
                possibleCapture(y + 2, x - 2);
                flag++;
            }
        }
        if (y > 1 && x < 6) {
            if (canIBeat(y, x, y - 2, x + 2) && color == Logic.colorCanMove) {
                makeHighlighted(y-2, x+2);
                possibleCapture(y - 2, x + 2);
                flag++;
            }
        }
        if (y > 1 && x > 1) {
            if (canIBeat(y, x, y - 2, x - 2) && color == Logic.colorCanMove) {
                makeHighlighted(y-2, x-2);
                possibleCapture(y - 2, x - 2);
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
                removeHandlerMove();
            }
            if (!Logic.isBeatPiece){
                if(!isQueen)possibleMovesPiece();
            }
            if(!Logic.canBeatQueen)
            {
                if(isQueen)possibleMovesQueen();
            }
            if(!isQueen) possibleCapture(yCurrent, xCurrent);
            setStrokeWidth(0);
            Logic.actualpiecex = xCurrent;
            Logic.actualpiecey = yCurrent;
            Logic.clicked = true;
        }
    };

}
