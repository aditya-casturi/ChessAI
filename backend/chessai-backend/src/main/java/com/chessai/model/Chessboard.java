package com.chessai.model;

import java.util.HashMap;
import java.util.Map;

import static com.chessai.utils.BitboardUtil.*;

public class Chessboard {
    public Map<Bitboards, Long> bitboards;

    public enum Bitboards {
        pawn, knight, bishop, rook, queen, king, white, black, empty, universe
    }

    public enum Turn {
        BLACK, WHITE
    }

    public Chessboard() {
        this.bitboards = new HashMap<Bitboards, Long>();
        this.bitboards.put(Bitboards.pawn, 0b0000000011111111000000000000000000000000000000001111111100000000L);
        this.bitboards.put(Bitboards.knight, 0b0100001000000000000000000000000000000000000000000000000001000010L);
        this.bitboards.put(Bitboards.bishop, 0b0010010000000000000000000000000000000000000000000000000010001000L);
        this.bitboards.put(Bitboards.rook, 0b1000000100000000000000000000000000000000000000000000000010000001L);
        this.bitboards.put(Bitboards.queen, 0b0001000000000000000000000000000000000000000000000000000000010000L);
        this.bitboards.put(Bitboards.king, 0b0000100000000000000000000000000000000000000000000000000000010000L);
        this.bitboards.put(Bitboards.white, 0b0000000000000000000000000000000000000000000000001111111111111111L);
        this.bitboards.put(Bitboards.black, 0b1111111111111111000000000000000000000000000000000000000000000000L);
        this.bitboards.put(Bitboards.empty, 0b0000000000000000111111111111111111111111111111110000000000000000L);
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
