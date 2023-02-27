package com.chessai.model;

public class Move {
    private int fromSquare;
    private int toSquare;

    public Move(int fromSquare, int toSquare) {
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
    }

    public int getFromSquare() {
        return fromSquare;
    }

    public void setFromSquare(int fromSquare) {
        this.fromSquare = fromSquare;
    }

    public int getToSquare() {
        return toSquare;
    }

    public void setToSquare(int toSquare) {
        this.toSquare = toSquare;
    }
}
