package com.example.gameforpio;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.*;
import javafx.stage.Stage;

import static java.lang.Math.abs;

public class HelloApplication extends Application {

    static AnchorPane tileMap = new AnchorPane();
    static Board board = new Board();

    public static void removePieceFromTile( int y, int x){
        tileMap.getChildren().remove(board.tiles[y][x].piece);
        board.tiles[y][x].deletePiece();

    }
    public static GameResult isEndGame(){
        int whitePieces=0;
        int darkPieces=0;
        int numOfPossibleMoves=0;
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(!board.tiles[i][j].hasNoPiece()){
                    if(board.tiles[i][j].piece.color==Colors.LIGHT){
                        whitePieces++;
                    }
                    else
                        darkPieces++;
                    if(board.tiles[i][j].piece.isQueen){
                        numOfPossibleMoves+=board.tiles[i][j].piece.possibleMovesQueen();
                    }
                    else
                        numOfPossibleMoves+=board.tiles[i][j].piece.possibleMovesPiece();
                    numOfPossibleMoves+=board.tiles[i][j].piece.possibleCapture(i,j);
                    board.tiles[i][j].piece.removeHandlerMove();
                    board.tiles[i][j].piece.clear();
                    }
            }
        }
        if(whitePieces==0 && darkPieces!=0)
            return GameResult.DARKWINS;
        else if(darkPieces==0 && whitePieces!=0)
            return GameResult.WHITEWINS;
        else if(numOfPossibleMoves==0)
            return GameResult.DRAW;
        return GameResult.CONTINUE;
    }
    public static void handleEndGame(){
        GameResult result = isEndGame();
        if(result==GameResult.DARKWINS){
            Text resultText = new Text();
            resultText.setText("DARK WINS");
            resultText.setX(20.0f);
            resultText.setY(65.0f);
            resultText.setFill(Color.RED);
            resultText.setTextAlignment(TextAlignment.CENTER);
            resultText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 40));
            System.out.println("Darkwin\n");
            tileMap.getChildren().add(resultText);
        }
        else if(result==GameResult.WHITEWINS){
            Text resultText = new Text();
            resultText.setText("WHITE WINS");
            resultText.setX(20.0f);
            resultText.setY(65.0f);
            resultText.setFill(Color.RED);
            resultText.setTextAlignment(TextAlignment.CENTER);
            resultText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 40));
            System.out.println("Whitewin\n");
            tileMap.getChildren().add(resultText);
        }
        else if(result==GameResult.DRAW){
            Text resultText = new Text();
            resultText.setText("DRAW");
            resultText.setX(20.0f);
            resultText.setY(65.0f);
            resultText.setFill(Color.RED);
            resultText.setTextAlignment(TextAlignment.CENTER);
            resultText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 40));
            System.out.println("Draw\n");
            tileMap.getChildren().add(resultText);
        }
    }
    public static void removePieceFromTileQueen(int old_y, int old_x, int y, int x){
        if(old_x<x && old_y<y){
            while(old_x!=x && old_y!=y){
                if(!board.tiles[y][x].hasNoPiece()){
                    tileMap.getChildren().remove(board.tiles[y][x].piece);
                    board.tiles[y][x].deletePiece();
                    break;
                }
                x--;
                y--;
            }
        }
        if(old_x>x && old_y<y){
            while(old_x!=x && old_y!=y){
                if(!board.tiles[y][x].hasNoPiece()){
                    tileMap.getChildren().remove(board.tiles[y][x].piece);
                    board.tiles[y][x].deletePiece();
                    break;
                }
                x++;
                y--;
            }
        }
        if(old_x<x && old_y>y){
            while(old_x!=x && old_y!=y){
                if(!board.tiles[y][x].hasNoPiece()){
                    tileMap.getChildren().remove(board.tiles[y][x].piece);
                    board.tiles[y][x].deletePiece();
                    break;
                }
                x--;
                y++;
            }
        }
        if(old_x<x && old_y<y){
            while(old_x!=x && old_y!=y){
                if(!board.tiles[y][x].hasNoPiece()){
                    tileMap.getChildren().remove(board.tiles[y][x].piece);
                    board.tiles[y][x].deletePiece();
                    break;
                }
                x--;
                y--;
            }
        }

    }
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
        removePieceFromTile(old_i, old_j);
        if (Logic.isBeatPiece)removePieceFromTile((i + old_i) / 2,(j + old_j) / 2);
        if(Logic.canBeatQueen)removePieceFromTile(Logic.piecetodiey,Logic.piecetodiex);
        handleEndGame();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {

        Scene content = new Scene(tileMap, 1280, 720);
        primaryStage.setScene(content);
        Logic.clicked = false;

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

