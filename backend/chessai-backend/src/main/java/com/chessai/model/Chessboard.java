package com.chessai.model;

import java.util.HashMap;
import java.util.Map;

import static com.chessai.utils.BitboardUtil.*;

public class Chessboard {
    public Map<EnumPiece, Long> bitboards;

    public enum EnumPiece {
        pawn, knight, bishop, rook, queen, king, white, black
    }

    public Chessboard() {
        this.bitboards = new HashMap<EnumPiece, Long>();
        this.bitboards.put(EnumPiece.pawn, 0b0000000011111111000000000000000000000000000000001111111100000000L);
        this.bitboards.put(EnumPiece.knight, 0b0100001000000000000000000000000000000000000000000000000001000010L);
        this.bitboards.put(EnumPiece.bishop, 0b0010010000000000000000000000000000000000000000000000000010001000L);
        this.bitboards.put(EnumPiece.rook, 0b1000000100000000000000000000000000000000000000000000000010000001L);
        this.bitboards.put(EnumPiece.queen, 0b0001000000000000000000000000000000000000000000000000000000010000L);
        this.bitboards.put(EnumPiece.king, 0b0000100000000000000000000000000000000000000000000000000000010000L);
        this.bitboards.put(EnumPiece.white, 0b0000000000000000000000000000000000000000000000001111111111111111L);
        this.bitboards.put(EnumPiece.black, 0b1111111111111111000000000000000000000000000000000000000000000000L);
    }

    public static void main(String[] args) {
        Chessboard board = new Chessboard();
        long test = 0b0000000000000000000000000000000000000000000000000000000000000001L;
        long result = make(test, 0, 63);
    }

    private static long make(long bitboard, int fromIndex, int toIndex) {
        long result = bitboard;
        result = result ^ intToBinary64(fromIndex);
        result = result ^ intToBinary64(toIndex);
        return result;
    }

    private static long capture(long bitboard, int fromIndex, int toIndex) {
        long result = bitboard;
        result = result ^ intToBinary64(fromIndex);
        return result;
    }
}
