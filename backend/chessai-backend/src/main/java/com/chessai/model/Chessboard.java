package com.chessai.model;

import org.checkerframework.checker.units.qual.C;

import static com.chessai.utils.BitboardUtil.*;

public class Chessboard {
    public long[] pieceBitboards;

    public enum EnumPiece {
        pawn, knight, bishop, rook, queen, king, white, black
    }

    public Chessboard() {
        this.pieceBitboards = new long[8];
        this.pieceBitboards[EnumPiece.pawn.ordinal()] = 0b0000000011111111000000000000000000000000000000001111111100000000L;
        this.pieceBitboards[EnumPiece.knight.ordinal()] = 0b0100001000000000000000000000000000000000000000000000000001000010L;
        this.pieceBitboards[EnumPiece.bishop.ordinal()] = 0b0010010000000000000000000000000000000000000000000000000010001000L;
        this.pieceBitboards[EnumPiece.rook.ordinal()] = 0b1000000100000000000000000000000000000000000000000000000010000001L;
        this.pieceBitboards[EnumPiece.queen.ordinal()] = 0b0001000000000000000000000000000000000000000000000000000010000000L;
        this.pieceBitboards[EnumPiece.king.ordinal()] = 0b0000100000000000000000000000000000000000000000000000000001000010L;
        this.pieceBitboards[EnumPiece.white.ordinal()] = 0b1111111111111111000000000000000000000000000000000000000000000000L;
        this.pieceBitboards[EnumPiece.black.ordinal()] = 0b0000000000000000000000000000000000000000000000001111111111111111L;
    }

    public static void main(String[] args) {
        Chessboard board = new Chessboard();
        for (long l : board.pieceBitboards) {
            System.out.println(String.format("%64s", Long.toBinaryString(l)).replace(' ', '0'));
        }
        long test = 0b1111111111111111111111111111111111111111111111111111111111111111L;
        System.out.println(bitboardIsFull(test));
    }
}
