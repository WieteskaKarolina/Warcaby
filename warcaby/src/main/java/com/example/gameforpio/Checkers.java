package com.example.gameforpio;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class Checkers extends Application {

    static AnchorPane tileMap = new AnchorPane();
    static Board board = new Board();
    static Text infoText = new Text();
    static Text statisticText = new Text();

    public static void removePieceFromTile(int y, int x) {
        tileMap.getChildren().remove(board.tiles[y][x].piece);
        board.tiles[y][x].deletePiece();

    }

    public static GameResult isEndGame() {
        int whitePieces = 0;
        int darkPieces = 0;
        int numOfPossibleMoves = 0;
        for (int y = 0; y < board.BOARD_HEIGHT; y++) {
            for (int x = 0; x < board.BOARD_WIDTH; x++) {
                if (!board.tiles[y][x].hasNoPiece()) {
                    if (board.tiles[y][x].piece.color == Colors.LIGHT) {
                        whitePieces++;
                    } else
                        darkPieces++;
                    if (board.tiles[y][x].piece.isQueen) {
                        numOfPossibleMoves += board.tiles[y][x].piece.possibleMovesQueen();
                    } else
                        numOfPossibleMoves += board.tiles[y][x].piece.possibleMovesPiece();
                    numOfPossibleMoves += board.tiles[y][x].piece.possibleCapture(y, x);
                    board.tiles[y][x].piece.removeHandlerMove();
                    board.tiles[y][x].piece.clearBoard();
                }
            }
        }
        Logic.NUMBER_OF_DARK_PIECES = darkPieces;
        Logic.NUMBER_OF_WHITE_PIECES = whitePieces;

        if (whitePieces == 0 && darkPieces != 0) {
            return GameResult.DARK_WINS;
        } else if (darkPieces == 0 && whitePieces != 0) {
            return GameResult.WHITE_WINS;
        } else if (numOfPossibleMoves == 0) {
            return GameResult.DRAW;
        }
        return GameResult.CONTINUE;
    }

    public static GameResult handleEndGame() {
        GameResult result = isEndGame();
        if (result == GameResult.DARK_WINS) {
            writeText("DARK WINS", Color.RED, Color.TRANSPARENT, 20.0f, 350.0f );
        } else if (result == GameResult.WHITE_WINS) {
            writeText("WHITE WINS", Color.RED, Color.TRANSPARENT, 20.0f, 350.0f );
        } else if (result == GameResult.DRAW) {
            writeText("DRAW", Color.RED, Color.TRANSPARENT, 20.0f, 350.0f );
        }
        return result;
    }

    private static void writeText(String text, Color color, Color stroke, float positionX, float positionY) {
        infoText.setText(text);
        infoText.setFill(color);
        infoText.setStroke(stroke);
        infoText.setX(positionX);
        infoText.setY(positionY);
        infoText.setTextAlignment(TextAlignment.CENTER);
        infoText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 40));
        tileMap.getChildren().add(infoText);
    }

    private static void writeStatistics(String text, Color color, Color stroke, float positionX, float positionY) {
        statisticText.setText(text);
        statisticText.setFill(color);
        statisticText.setStroke(stroke);
        statisticText.setX(positionX);
        statisticText.setY(positionY);
        statisticText.setTextAlignment(TextAlignment.CENTER);
        statisticText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
        tileMap.getChildren().add(statisticText);
    }

    public static void statistics(){
        tileMap.getChildren().remove(statisticText);
        writeStatistics("White left:"+ Logic.NUMBER_OF_WHITE_PIECES+ "\n" +
        "White captured:"+ (Logic.NUMBER_OF_PIECES-Logic.NUMBER_OF_WHITE_PIECES)+ "\n" +
        "Dark left:"+ Logic.NUMBER_OF_DARK_PIECES + "\n" +
        "Dark captured:"+ (Logic.NUMBER_OF_PIECES-Logic.NUMBER_OF_DARK_PIECES), Color.WHITE, Color.BLACK, 970.0f, 300.0f);

    }

    public static void infoAboutWhoMoves() {
        tileMap.getChildren().remove(infoText);
        if (handleEndGame() != GameResult.CONTINUE) {
            statistics();
            return;
        }
        if (Logic.colorCanMove == Colors.LIGHT) {
            writeText("LIGHT MOVE!", Color.WHITE, Color.BLACK, 20.0f, 350.0f );

        } else {
            writeText("DARK MOVE!", Color.BLACK, Color.WHITE, 20.0f, 350.0f );
        }
        statistics();
    }

    public static void movePieceFromOneTileToAnother(int oldY, int oldX, int y, int x) {
        int positionY = board.PRIMARY_POSITION_Y + (y * board.TILE_SIZE) + board.TILE_SIZE / 2; //poczatkowy y
        int positionX = board.PRIMARY_POSITION_X + (x * board.TILE_SIZE) + board.TILE_SIZE / 2;//poczatkowy x
        if (board.tiles[oldY][oldX].piece == null)
            return;
        board.tiles[y][x].setPiece(board.tiles[oldY][oldX].piece.color, positionX, positionY, board.tiles, y, x, board.tiles[oldY][oldX].piece.isQueen);

        if (board.tiles[oldY][oldX].piece.isQueen) {
            board.tiles[y][x].piece.isQueen = true;
            if (board.tiles[y][x].piece.color == Colors.DARK) {
                board.tiles[y][x].piece.setFill(new ImagePattern(new Image("/Pionek_Ciemny_Damka.png")));
            }
            if (board.tiles[y][x].piece.color == Colors.LIGHT) {
                board.tiles[y][x].piece.setFill(new ImagePattern(new Image("/Pionek_Jasny_Damka.png")));
            }
        }
        //
        tileMap.getChildren().add(board.tiles[y][x].piece);
        if (Logic.isBeatPiece) {
            if(board.tiles[oldY][oldX].piece.isQueen)
            {
                removePieceFromTile(Logic.pieceToRemoveY, Logic.pieceToRemoveX);
            }
            else{
                removePieceFromTile((y + oldY) / 2, (x + oldX) / 2);
            }
        }
        removePieceFromTile(oldY, oldX);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {

        Scene content = new Scene(tileMap, 1280, 720);
        content.setFill(new ImagePattern(new Image("/backgroundImage.jpg")));
        primaryStage.setScene(content);
        Logic.clicked = false;

        for (int y = 0; y < board.BOARD_HEIGHT; y++) {
            for (int x = 0; x < board.BOARD_WIDTH; x++) {
                tileMap.getChildren().add(board.tiles[y][x]);
                if (board.tiles[y][x].color == Colors.DARK) //dodaje odpowiednie pola jako dzieci
                {
                    if (y < 3)
                        tileMap.getChildren().add(board.tiles[y][x].piece);
                    if (y > 4)
                        tileMap.getChildren().add(board.tiles[y][x].piece);
                }

            }

        }
        infoAboutWhoMoves();
        primaryStage.show();

    }
}

