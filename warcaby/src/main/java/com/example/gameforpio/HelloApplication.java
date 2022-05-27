package com.example.gameforpio;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import static java.lang.Math.abs;

public class HelloApplication extends Application {

    static AnchorPane tileMap = new AnchorPane();
    static Board board = new Board();

    public static void removePieceFromTile( int y, int x){
        tileMap.getChildren().remove(board.tiles[y][x].piece);
        board.tiles[y][x].deletePiece();

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

