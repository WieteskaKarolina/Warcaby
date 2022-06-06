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
    boolean isQueen; //Sprawdza, czy pion jest damka
    Tile[][] tiles; //tablicy wszystkich pol


    EventHandler<MouseEvent> enter = (MouseEvent e) -> //podswietlenia po najechaniu myszka
    {
        if (color == Logic.colorCanMove) {
            setStrokeWidth(3);
            setStroke(Color.LIGHTBLUE);
        }
    };
    EventHandler<MouseEvent> quit = e -> setStrokeWidth(0);

    public Piece(Colors color, int positionX, int positionY, Tile[][] tiles, int y, int x, boolean queen) //przyjmuje color, wsporzedne, tablice pol, rzad,kolumne
    {
        setCenterX(positionX);
        setCenterY(positionY);
        setRadius(25);
        this.color = color;
        this.yCurrent = y;
        this.xCurrent = x;
        this.tiles = tiles;
        this.isQueen = queen;
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
        for (int y = 0; y < Checkers.board.BOARD_HEIGHT; y++) {
            for (int x = 0; x < Checkers.board.BOARD_WIDTH; x++) {
                tiles[y][x].removeEventHandler(MOUSE_CLICKED, move);
            }
        }
    }

    public boolean canIBeat(int y, int x, int newY, int newX) {


        if (tiles[(y + newY) / 2][(x + newX) / 2].piece == null || tiles[y][x].piece == null)
            return false;
        return (tiles[(y + newY) / 2][(x + newX) / 2].piece.color != tiles[y][x].piece.color) && tiles[newY][newX].hasNoPiece() && !tiles[(y + newY) / 2][(x + newX) / 2].hasNoPiece();
    }


    public void makeHighlighted(int y, int x) {
        tiles[y][x].addEventHandler(MOUSE_CLICKED, move);
        tiles[y][x].setStroke(Color.BLUE);
        tiles[y][x].setStrokeWidth(5);
    }

    boolean queenBeat(int y, int x) {
        int yIncrement;
        int xIncrement;
        if (Logic.actualPieceY > y) {
            yIncrement = 1;
        } else {
            yIncrement = -1;
        }
        if (Logic.actualPieceX > x) {
            xIncrement = 1;
        } else {
            xIncrement = -1;
        }
        for (; y != Logic.actualPieceY && x != Logic.actualPieceX; y += yIncrement, x += xIncrement) {
            if (tiles[y][x].piece != null) {
                if (color != tiles[y][x].piece.color) {
                    Logic.pieceToRemoveX = x;
                    Logic.pieceToRemoveY = y;
                    return true;
                }
            }
        }
        return false;
    }

    boolean check(int x, int y, int horizontal, int vertical){
        if(horizontal<0){
            if(vertical<0) {
                return (x > 0 && y > 0);
            }
            else{
                return (y < Checkers.board.BOARD_HEIGHT - 1 && x > 0);
            }
        }
        if(horizontal>0){
            if(vertical<0) {
                return (x < Checkers.board.BOARD_WIDTH - 1 && y > 0);
            }
            else{
                return (y < Checkers.board.BOARD_HEIGHT - 1 && x < Checkers.board.BOARD_WIDTH - 1);
            }
        }
        return false;
    }

    boolean checkCapture(int x, int y, int horizontal, int vertical){
        if(horizontal<0){
            if(vertical<0) {
                return (x > 1 && y > 1) && canIBeat(y, x, y+vertical, x+horizontal);
            }
            else{
                return  (y < Checkers.board.BOARD_HEIGHT - 2 && x > 1) && canIBeat(y, x, y+vertical, x+horizontal);
            }
        }
        if(horizontal>0){
            if(vertical<0) {
                return (x < Checkers.board.BOARD_WIDTH - 2 && y > 1) && canIBeat(y, x, y+vertical, x+horizontal);
            }
            else{
                return (y < Checkers.board.BOARD_HEIGHT - 2 && x < Checkers.board.BOARD_WIDTH - 2) && canIBeat(y, x, y+vertical, x+horizontal);
            }
        }
        return false;
    }

    int movesPiece(int horizontal, int vertical){
        int movesCounter = 0;
        if (check(xCurrent, yCurrent, horizontal, vertical) && tiles[yCurrent + vertical][ xCurrent + horizontal].hasNoPiece()){
            makeHighlighted(yCurrent + vertical, xCurrent + horizontal);
            movesCounter++;

        }
        return  movesCounter;
    }

    int possibleMovesPiece() {
        int moveCounter = 0;
        if( color == Logic.colorCanMove){
            if (color == Colors.DARK) {
                moveCounter+=movesPiece(Logic.RIGHT, Logic.UP);
                moveCounter+=movesPiece(Logic.LEFT, Logic.UP);
            }
            if (color == Colors.LIGHT){
                moveCounter+=movesPiece(Logic.RIGHT, Logic.DOWN);
                moveCounter+=movesPiece(Logic.LEFT, Logic.DOWN);
            }
        }

        return moveCounter;

    }

    int movesQueen(int horizontal, int vertical){
        int movesCounter = 0;
        for (int x = xCurrent, y = yCurrent; check(x, y, horizontal, vertical) ; x+=horizontal, y+=vertical) {
            if (tiles[y + vertical][x + horizontal].hasNoPiece()) {
                makeHighlighted(y + vertical,x + horizontal);
            }
            else {
                break;
            }
            movesCounter++;
        }
        return  movesCounter;
    }
    int possibleMovesQueen() {
        int movesCounter = 0;

        if(color==Logic.colorCanMove) {
            movesCounter+=movesQueen(Logic.RIGHT, Logic.UP);
            movesCounter+=movesQueen(Logic.RIGHT, Logic.DOWN);
            movesCounter+=movesQueen(Logic.LEFT, Logic.UP);
            movesCounter+=movesQueen(Logic.LEFT, Logic.DOWN);

        }
        return movesCounter;
    }

    int possibleCapture(int y, int x) {
        int possibleBeatsCounter = 0;

        if(color == Logic.colorCanMove){
            if (checkCapture(x, y, Logic.LEFT_CAPTURE, Logic.DOWN_CAPTURE)) {
                makeHighlighted(y + Logic.DOWN_CAPTURE, x + Logic.LEFT_CAPTURE);
                possibleBeatsCounter++;
            }
            if (checkCapture(x, y, Logic.LEFT_CAPTURE, Logic.UP_CAPTURE)) {
                makeHighlighted(y + Logic.UP_CAPTURE, x + Logic.LEFT_CAPTURE);
                possibleBeatsCounter++;
            }
            if (checkCapture(x, y, Logic.RIGHT_CAPTURE, Logic.DOWN_CAPTURE)) {
                makeHighlighted(y + Logic.DOWN_CAPTURE, x + Logic.RIGHT_CAPTURE);
                possibleBeatsCounter++;
            }
            if (checkCapture(x, y, Logic.RIGHT_CAPTURE, Logic.UP_CAPTURE)) {
                makeHighlighted(y + Logic.UP_CAPTURE, x + Logic.RIGHT_CAPTURE);
                possibleBeatsCounter++;
            }
        }
        return possibleBeatsCounter;
    }

    int capturesQueen(int y, int x,int horizontal, int vertical){
        int captureCounter = 0;
        boolean anotherColorDetector=false;
        for (; check(x, y, horizontal, vertical) ; x+=horizontal, y+=vertical) {
            if (tiles[y + vertical][x + horizontal].hasNoPiece()){
                if(anotherColorDetector){
                    makeHighlighted(y + vertical, x + horizontal);
                }
            }
            else {
                if (tiles[y + vertical][x + horizontal].piece.color == Logic.colorCanMove) {
                    break;
                } else {
                    y+=vertical;
                    x+=horizontal;
                    if (!anotherColorDetector && check(x, y, horizontal, vertical)&& tiles[y + vertical][x + horizontal].hasNoPiece()) {
                        anotherColorDetector = true;
                        makeHighlighted(y + vertical, x + horizontal);
                        captureCounter++;
                    } else {
                        break;
                    }
                }
            }
        }
        return  captureCounter;
    }

    int possibleCapturesQueen(int yposition,int xposition) {
        int captureCounter = 0;

        if(color==Logic.colorCanMove) {
            captureCounter=captureCounter+capturesQueen(yposition,xposition,Logic.LEFT,Logic.UP);
            captureCounter=captureCounter+capturesQueen(yposition,xposition,Logic.LEFT,Logic.DOWN);
            captureCounter=captureCounter+capturesQueen(yposition,xposition,Logic.RIGHT,Logic.UP);
            captureCounter=captureCounter+capturesQueen(yposition,xposition,Logic.RIGHT,Logic.DOWN);
        }
        return captureCounter;
    }

    void clearBoard() {
        for (int y = 0; y < Checkers.board.BOARD_HEIGHT; y++) {
            for (int x = 0; x < Checkers.board.BOARD_WIDTH; x++) {
                tiles[y][x].setStroke(Color.CRIMSON);
                tiles[y][x].setStrokeWidth(2);
            }
        }
    }
    EventHandler<MouseEvent> move = ev -> {
        if (Logic.clicked) {
            clearBoard();
            removeHandlerMove();
        }
        Tile tile = (Tile) ev.getSource();

        if (!isQueen) Logic.isBeatPiece = abs(Logic.actualPieceY - tile.y) == 2;
        else Logic.isBeatPiece = queenBeat(tile.y, tile.x);

        Checkers.movePieceFromOneTileToAnother(Logic.actualPieceY, Logic.actualPieceX, tile.y, tile.x);
        int beats = 0;
        if (Logic.isBeatPiece && !tile.piece.isQueen) {
            beats = possibleCapture(tile.y, tile.x);
            Logic.actualPieceY=tile.y;
            Logic.actualPieceX=tile.x;
        }
        if(Logic.isBeatPiece && tile.piece.isQueen)
        {
            //  System.out.println(possibleCapturesQueen(Logic.actualPieceY, Logic.actualPieceX));
            beats = possibleCapturesQueen(tile.y, tile.x);
            Logic.actualPieceY=tile.y;
            Logic.actualPieceX=tile.x;
        }
        //Przemiana w Damke

        if (tile.y == 0 && color == Colors.LIGHT) {

            tiles[tile.y][tile.x].piece.isQueen = true;
            tiles[tile.y][tile.x].piece.setFill(new ImagePattern(new Image("/Pionek_Jasny_Damka.png")));
        }
        if (tile.y == Checkers.board.BOARD_HEIGHT - 1 && color == Colors.DARK) {
            tiles[tile.y][tile.x].piece.isQueen = true;
            tiles[tile.y][tile.x].piece.setFill(new ImagePattern(new Image("/Pionek_Ciemny_Damka.png")));
        }

        Logic.clicked = false;

        if (beats == 0) {
            if (Logic.colorCanMove == Colors.DARK) {
                Logic.colorCanMove = Colors.LIGHT;
            } else {
                Logic.colorCanMove = Colors.DARK;
            }
            Checkers.infoAboutWhoMoves();
            Logic.isBeatPiece = false;
        }
        removeHandlerMove();
    };
    EventHandler<MouseEvent> path = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {

            if (Logic.clicked) {
                clearBoard();
                removeHandlerMove();
            }
            if (isQueen) {
                if (possibleCapturesQueen(yCurrent, xCurrent) == 0 && !Logic.isBeatPiece) {
                    possibleMovesQueen();
                }

            } else {
                if (possibleCapture(yCurrent, xCurrent) == 0 && !Logic.isBeatPiece) {
                    possibleMovesPiece();
                }
            }

            Logic.actualPieceX = xCurrent;
            Logic.actualPieceY = yCurrent;
            Logic.clicked = true;
        }
    };

}
