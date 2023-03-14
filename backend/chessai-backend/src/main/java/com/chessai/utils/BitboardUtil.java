package com.chessai.utils;

import com.chessai.model.Chessboard;
import com.chessai.model.Chessboard.Bitboards;
import com.chessai.model.Chessboard.Turn;
import com.chessai.model.Move;

import java.util.ArrayList;
import java.util.List;

public class BitboardUtil {
    private static final long universe = 0b1111111111111111111111111111111111111111111111111111111111111111L;
    private static final long empty = 0b0000000000000000000000000000000000000000000000000000000000000000L;
    private static final long base = 0b0000000000000000000000000000000000000000000000000000000000000001L;

    // 10 | 01 = 11


    // 11 & 01 = 01

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

//    private static long getLegalPawnMoves(Turn turn, Chessboard board) {
//        List<Move> moves = new ArrayList<Move>();
//
//        long whitePawns = board.bitboards.get(Bitboards.pawn) & board.bitboards.get(Bitboards.white);
//        List<Integer> whitePawnIndices = new ArrayList<Integer>();
//        int index = 0;
//        while (whitePawns != 0) {
//            if ((whitePawns & 1) == 1) {
//                whitePawnIndices.add(index);
//            }
//            whitePawns = whitePawns >> 1;
//            index++;
//        }
//
//        for (int fromIndex : whitePawnIndices) {
////            long pawnBitboard = generateBitboardFromIndex(fromIndex);
////            turn == Turn.WHITE ? wSinglePushTargets(board, pawnBitboard, moves, fromIndex) : bSinglePushTargets(board, pawnBitboard, moves, fromIndex);
////            turn == Turn.WHITE ? wDoublePushTargets(board, pawnBitboard, moves, fromIndex) : bDoublePushTargets(board, pawnBitboard, moves, fromIndex);
////            turn == Turn.WHITE ? wPawnCaptures(board, pawnBitboard, moves, fromIndex) : bPawnCaptures(board, pawnBitboard, moves, fromIndex);
//        }
//    }

    private static long generateBitboardFromIndex(int index) {
        long bitboard = 0;
        return bitboard | (1L << index);
    }

    private static long wPawnCaptures(Chessboard board) {
        long whitePawns = board.bitboards.get(Bitboards.pawn) & board.bitboards.get(Bitboards.white);
        long northEastTargets = northEastOne(whitePawns) & board.bitboards.get(Bitboards.black);
        long northWestTargets = northWestOne(whitePawns) & board.bitboards.get(Bitboards.black);
        return northEastTargets | northWestTargets;
    }

    private static long bPawnCaptures(Chessboard board) {
        long blackPawns = board.bitboards.get(Bitboards.pawn) & board.bitboards.get(Bitboards.black);
        long southEastTargets = southEastOne(blackPawns) & board.bitboards.get(Bitboards.white);
        long southWestTargets = southWestOne(blackPawns) & board.bitboards.get(Bitboards.white);
        return southEastTargets | southWestTargets;
    }

    private static long northEastOne(long bitboard) {
        return bitboard << 9;
    }

    private static long northWestOne(long bitboard) {
        return bitboard << 7;
    }

    private static long southEastOne(long bitboard) {
        return bitboard >> 7;
    }

    private static long southWestOne(long bitboard) {
        return bitboard >> 9;
    }

    private static long wSinglePushTargets(Chessboard board, long pawnBitboard, List<Move> moves, int fromIndex) {
        long singlePushTargets = northOne(pawnBitboard) & board.bitboards.get(Bitboards.empty);
        int index = 0;
        while (singlePushTargets != 0) {
            if ((singlePushTargets & 1) == 1) {
                moves.add(new Move(fromIndex, index));
            }
            singlePushTargets = singlePushTargets >> 1;
            index++;
        }
        return northOne(pawnBitboard) & board.bitboards.get(Bitboards.empty);
    }

    private static long bSinglePushTargets(Chessboard board) {
        long blackPawns = board.bitboards.get(Bitboards.pawn) & board.bitboards.get(Bitboards.black);
        return southOne(blackPawns) & board.bitboards.get(Bitboards.empty);
    }

    private static long wDoublePushTargets(Chessboard board) {
        long rank2 = 0b0000000000000000000000000000000000000000000000001111111100000000L;
        long whitePawns = board.bitboards.get(Bitboards.pawn) & board.bitboards.get(Bitboards.white) & rank2;
        long singlePushTargets = northOne(whitePawns) & board.bitboards.get(Bitboards.empty);
        return northOne(singlePushTargets) & board.bitboards.get(Bitboards.empty);
    }

    private static long bDoublePushTargets(Chessboard board) {
        long rank7 = 0b0000000011111111000000000000000000000000000000000000000000000000L;
        long blackPawns = board.bitboards.get(Bitboards.pawn) & board.bitboards.get(Bitboards.black) & rank7;
        long singlePushTargets = southOne(blackPawns) & board.bitboards.get(Bitboards.empty);
        return southOne(singlePushTargets) & board.bitboards.get(Bitboards.empty);
    }

    private static long northOne(long bitboard) {
        return bitboard << 8;
    }

    private static long southOne(long bitboard) {
        return bitboard >> 8;
    }

    private static long getLegalKnightMoves(Turn turn, Chessboard board) {
        long knights = turn == Turn.WHITE ? board.bitboards.get(Bitboards.white) & board.bitboards.get(Bitboards.knight) : board.bitboards.get(Bitboards.black) & board.bitboards.get(Bitboards.knight);
        long targets = turn == Turn.WHITE ? board.bitboards.get(Bitboards.black) : board.bitboards.get(Bitboards.white);
        targets |= board.bitboards.get(Bitboards.empty);
        printBinary64(knights);
        System.out.println();
//        long northEastTargets = knights  & targets;
//        long northWestTargets = northWestTwo(knights) & targets;
//        long southEastTargets = southEastTwo(knights) & targets;
//        long southWestTargets = southWestTwo(knights) & targets;
//        long eastNorthTargets = eastNorthTwo(knights) & targets;
//        long eastSouthTargets = eastSouthTwo(knights) & targets;
//        long westNorthTargets = westNorthTwo(knights) & targets;
//        long westSouthTargets = westSouthTwo(knights) & targets;

        return 0b0;
    }

    public static void printBinary64(long b) {
        String board = String.format("%64s", Long.toBinaryString(b)).replace(' ', '0');
        for (int i = 0; i < 8; i++) {
            System.out.println(board.substring(i * 8, (i + 1) * 8));
        }
    }

    public static void main(String[] args) {
//        Chessboard board = new Chessboard();
//        long pawnMoves = getLegalPawnMoves(Turn.WHITE, board);
//        printBinary64(pawnMoves);
//        printBinary64(getLegalKnightMoves(Turn.WHITE, board));
    }
}
