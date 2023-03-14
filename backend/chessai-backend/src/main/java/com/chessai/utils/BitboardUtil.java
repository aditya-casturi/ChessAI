package com.chessai.utils;

import com.chessai.model.Chessboard;
import com.chessai.model.Move;
import com.chessai.model.Chessboard.EnumPiece;

import java.util.List;

public class BitboardUtil {
    private static final long universe = 0b1111111111111111111111111111111111111111111111111111111111111111L;
    private static final long empty = 0b0000000000000000000000000000000000000000000000000000000000000000L;
    private static final long base = 0b0000000000000000000000000000000000000000000000000000000000000001L;

    // 10 | 01 = 11
    public static long union(long a, long b) {
        return a | b;
    }

    // 11 & 01 = 01
    public static long subset(long a, long b) {
        return a & b;
    }

    // 00 == 00
    public static boolean bitboardIsEmpty(long b) {
        return b == 0;
    }

    // 11 == 11
    public static boolean bitboardIsFull(long b) {
        return b == universe;
    }

    public static long intToBinary64(int num) {
        return base << num;
    }

    public static void pawnMoves(String turn, Chessboard board) {
        long pawns;
        if (turn.equals("w")) {
            pawns = subset(board.bitboards.get(EnumPiece.pawn), board.bitboards.get(EnumPiece.white));
        } else {
            pawns = subset(board.bitboards.get(EnumPiece.pawn), board.bitboards.get(EnumPiece.black));
        }


    }

    public static long northOne(long bitboard) {
        return bitboard << 8;
    }

    public static long southOne(long bitboard) {
        return bitboard >> 8;
    }

    public static void printBinary64(long b) {
        System.out.println(String.format("%64s", Long.toBinaryString(b).replace(' ', '0')));
    }
}
