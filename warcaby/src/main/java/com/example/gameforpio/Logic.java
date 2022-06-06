package com.example.gameforpio;

class Logic {
    static boolean clicked;//czy pion jest klikniety
    static boolean isBeatPiece = false;//czy jest bicie zwykly pion
    static Colors colorCanMove = Colors.LIGHT; // Kolor, ktory moze sie poruszac 2-jasne 1-ciemne
    static int actualPieceX;
    static int actualPieceY;
    static int pieceToRemoveX;
    static int pieceToRemoveY;
    final static int LEFT = -1;
    final static int RIGHT = 1;
    final static int  DOWN = -1;
    final static int  UP = 1;
    final static int LEFT_CAPTURE = -2;
    final static int RIGHT_CAPTURE = 2;
    final static int DOWN_CAPTURE = -2;
    final static int UP_CAPTURE = 2;
}
