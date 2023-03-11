package com.chessai.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Position {
    private String turn;
    private int score;
    public Position parent;
    public List<Position> children;
    public Move lastMove;

    private boolean positionEvaluated;

    public String[] board;

    private String keyString;

    public Position(String turn, Position parent, String[] board, Move lastMove) {
        this.turn = turn;
        this.parent = parent;
        this.board = board;
        this.children = new ArrayList<>();
        this.lastMove = lastMove;
        this.keyString = String.join(" ", board);
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public int getScore() {
        return score;
    }

    public boolean isPositionEvaluated() {
        return positionEvaluated;
    }

    public void setPositionEvaluated(boolean positionEvaluated) {
        this.positionEvaluated = positionEvaluated;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Position)) return false;
        Position p = (Position) o;
        String thisBoardRepresentation = String.join(" ", board);
        String otherBoardRepresentation = String.join(" ", p.board);
        if (thisBoardRepresentation.equals(otherBoardRepresentation)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyString);
    }
}
