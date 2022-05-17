package com.example.gameforpio;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import static java.lang.Math.abs;
import static javafx.scene.input.MouseEvent.*;

class Szachownica {
    Rectangle[][] tiles = new Rectangle[8][8];
    private   double size = 80;
    private int y=35;
    private int x=320;
    Szachownica()
    {
        int kolor=0;
        for(int i=0;i<8;i++,y+=size) {
            x=320;
            for (int j = 0; j < 8; j++,x+=size) {
                Rectangle pole = new Rectangle(x, y, size, size);
                pole.setStroke(Color.GRAY);
                if(kolor%2==0)
                {
                    pole.setFill(Color.WHITE);
                }else
                {
                    pole.setFill(Color.BLACK);
                }
                EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        System.out.println("Hello World");
                        pole.setStrokeWidth(5);
                        pole.setStroke(Color.LIGHTBLUE);
                    }
                };
                EventHandler<MouseEvent> eventHandler2 = new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        System.out.println("Hello World");
                        pole.setStrokeWidth(1);
                        pole.setStroke(Paint.valueOf("#98b8aa"));
                    }
                };
                pole.addEventHandler(MOUSE_ENTERED, eventHandler);
                pole.addEventHandler(MOUSE_EXITED, eventHandler2);

                tiles[i][j] = pole;
                kolor++;
            }
            kolor++;
        }
    }
}
class Pion{
    Circle pion;
    int x;
    int y;
    int kolor;
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
    Pion(){
        pion=new Circle(0,0,1);
    };
    void set(int kolor,int x,int y)
    {
        pion.setCenterX(x);
        pion.setCenterY(y);
        pion.setRadius(30);
        if(kolor==1)
        {
            pion.setFill(Color.WHITE);
        }
        else
        {
            pion.setFill(Color.RED);
        }
        EventHandler<MouseEvent> circleOnMousePressedEventHandler =
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent t) {
                        orgSceneX = (t.getSceneX()-t.getSceneX()%80);
                        orgSceneY = (t.getSceneY()-t.getSceneY()%80);
                        orgTranslateX = pion.getTranslateX();
                        orgTranslateY = pion.getTranslateY();
                    }
                };
        EventHandler<MouseEvent> circleOnMouseDraggedEventHandler =
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent t) {
                        double offsetX = ((t.getSceneX()-t.getSceneX()%80) - orgSceneX);
                        double offsetY = ((t.getSceneY()-t.getSceneY()%80) - orgSceneY);
                        if(abs(offsetY)==abs(offsetX))
                        {
                            double newTranslateX = orgTranslateX + offsetX;
                            double newTranslateY = orgTranslateY + offsetY;
                            pion.setTranslateX(newTranslateX);
                            pion.setTranslateY(newTranslateY);
                        }

                    }
                };
        pion.setOnMousePressed(circleOnMousePressedEventHandler);
        pion.setOnMouseDragged(circleOnMouseDraggedEventHandler);
    }
}

public class HelloApplication extends Application {

    public void start(Stage primaryStage) {

        Group root = new Group();

        int height = 1080;
        int width = 1920;
        AnchorPane tileMap = new AnchorPane();
        Scene content = new Scene(tileMap, 1280, 720);
        primaryStage.setScene(content);
        Szachownica szachownica = new Szachownica();
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                tileMap.getChildren().add(szachownica.tiles[i][j]);
            }
        }
        Pion [][] biale = new Pion[3][4];
        Pion [][] czarne = new Pion[3][4];
        int x=402;
        int y=35;
        int offset=-80;
        for(int i=0;i<3;i++)
        {
            if(i==1)
            {
                x=320;
                offset=80;
            }
            else
            {
                x=400;
                offset=-80;
            }
            for(int j=0;j<4;j++)
            {
                Pion pionb = new Pion();
                biale[i][j]=pionb;
                biale[i][j].set(1,x+40,y+40);
                tileMap.getChildren().add(biale[i][j].pion);
                Pion pionc = new Pion();
                czarne[i][j]=pionc;
                czarne[i][j].set(2,x+40+offset,y+40+400);
                tileMap.getChildren().add(czarne[i][j].pion);
                x+=80*2;
            }
            y+=80;
        }
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}