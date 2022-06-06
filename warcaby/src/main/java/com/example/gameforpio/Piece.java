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
        if (check(xCurrent, yCurrent, horizontal, vertical) && tiles[yCurrent + vertical][ xCurrent + horizontal].hasNoPiece() && color == Logic.colorCanMove){
            makeHighlighted(yCurrent + vertical, xCurrent + horizontal);
            movesCounter++;

        }
        return  movesCounter;
    }

    int possibleMovesPiece() {
        int moveCounter = 0;
        final int LEFT = -1;
        final int RIGHT = 1;
        final int DOWN = -1;
        final int UP = 1;

        if (color == Colors.DARK) {
            moveCounter+=movesPiece(RIGHT, UP);
            moveCounter+=movesPiece(LEFT, UP);
        }
        if (color == Colors.LIGHT){
            moveCounter+=movesPiece(RIGHT, DOWN);
            moveCounter+=movesPiece(LEFT, DOWN);
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
        final int LEFT = -1;
        final int RIGHT = 1;
        final int DOWN = -1;
        final int UP = 1;
        if(color==Logic.colorCanMove) {
            movesCounter+=movesQueen(RIGHT, UP);
            movesCounter+=movesQueen(RIGHT, DOWN);
            movesCounter+=movesQueen(LEFT, UP);
            movesCounter+=movesQueen(LEFT, DOWN);

        }
        return movesCounter;
    }

    int possibleCapture(int y, int x) {
        int possibleBeatsCounter = 0;
        final int LEFT_CAPTURE = -2;
        final int RIGHT_CAPTURE = 2;
        final int DOWN_CAPTURE = -2;
        final int UP_CAPTURE = 2;

        if(color == Logic.colorCanMove){
            if (checkCapture(x, y, LEFT_CAPTURE, DOWN_CAPTURE)) {
                makeHighlighted(y + DOWN_CAPTURE, x + LEFT_CAPTURE);
                possibleBeatsCounter++;
            }
            if (checkCapture(x, y, LEFT_CAPTURE, UP_CAPTURE)) {
                makeHighlighted(y + UP_CAPTURE, x + LEFT_CAPTURE);
                possibleBeatsCounter++;
            }
            if (checkCapture(x, y, RIGHT_CAPTURE, DOWN_CAPTURE)) {
                makeHighlighted(y + DOWN_CAPTURE, x + RIGHT_CAPTURE);
                possibleBeatsCounter++;
            }
            if (checkCapture(x, y, RIGHT_CAPTURE, UP_CAPTURE)) {
                makeHighlighted(y + UP_CAPTURE, x + RIGHT_CAPTURE);
                possibleBeatsCounter++;
            }
        }
        return possibleBeatsCounter;
    }

    int possibleCapturesQueen(int yposition,int xposition) {
        int captureCounter = 0;
        if(color==Logic.colorCanMove) {
            int y = yposition;
            boolean anotherColorDetector = false;
            for (int x = xposition; x < Checkers.board.BOARD_WIDTH - 1 && y < Checkers.board.BOARD_HEIGHT - 1; x++, y++) {
                if (tiles[y + 1][x + 1].hasNoPiece()) {
                    if(anotherColorDetector)
                    {
                        makeHighlighted(y+1,x+1);
                    }
                }
                else {
                    if (tiles[y + 1][x + 1].piece.color ==Logic.colorCanMove) {
                        break;
                    } else {
                        y++;
                        x++;
                        if (!anotherColorDetector && x < Checkers.board.BOARD_WIDTH - 1 && y < Checkers.board.BOARD_HEIGHT - 1 && tiles[y + 1][x + 1].hasNoPiece()) {
                            anotherColorDetector = true;
                            makeHighlighted(y + 1, x + 1);
                            captureCounter++;
                        } else {
                            break;
                        }
                    }
                }

            }
            anotherColorDetector = false;
            y = yposition;
            for (int x = xposition; x > 0 && y < Checkers.board.BOARD_HEIGHT - 1; x--, y++) {
                if (tiles[y + 1][x - 1].hasNoPiece()){
                    if(anotherColorDetector)
                        makeHighlighted(y + 1, x - 1);
                }
                else {
                    if (tiles[y + 1][x - 1].piece.color == Logic.colorCanMove) {
                        break;
                    } else {
                        y++;
                        x--;
                        if (!anotherColorDetector && y < Checkers.board.BOARD_HEIGHT - 1 && x > 0 && tiles[y + 1][x - 1].hasNoPiece()) {
                            anotherColorDetector = true;
                            makeHighlighted(y + 1, x - 1);
                            captureCounter++;
                        } else {
                            break;
                        }
                    }
                }
            }
            anotherColorDetector = false;
            y = yposition;
            for (int x = xposition; x < Checkers.board.BOARD_WIDTH - 1 && y > 0; x++, y--) {
                if (tiles[y - 1][x + 1].hasNoPiece()){
                    if(anotherColorDetector)
                    {
                        makeHighlighted(y - 1, x + 1);
                    }
                }
                else {
                    if (tiles[y - 1][x + 1].piece.color == Logic.colorCanMove) {
                        break;
                    } else {
                        y--;
                        x++;
                        if (!anotherColorDetector && y > 0 && x < Checkers.board.BOARD_WIDTH - 1 && tiles[y - 1][x + 1].hasNoPiece()) {
                            anotherColorDetector = true;
                            makeHighlighted(y - 1, x + 1);
                            captureCounter++;
                        } else {
                            break;
                        }
                    }
                }
            }
            anotherColorDetector = false;
            y = yposition;
            for (int x = xposition; x > 0 && y > 0; x--, y--) {
                if (tiles[y - 1][x - 1].hasNoPiece()) {
                    if(anotherColorDetector)
                        makeHighlighted(y - 1, x - 1);
                }
                else {
                    if (tiles[y - 1][x - 1].piece.color == Logic.colorCanMove) {
                        break;
                    } else {
                        y--;
                        x--;
                        if (!anotherColorDetector && y > 0 && x > 0 && tiles[y - 1][x - 1].hasNoPiece()) {
                            anotherColorDetector = true;
                            makeHighlighted(y - 1, x - 1);
                            captureCounter++;
                        } else {
                            break;
                        }
                    }
                }
            }
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
    EventHandler<MouseEvent> path = new EventHandler<MouseEvent>() {
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
