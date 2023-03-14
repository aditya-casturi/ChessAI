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

    private static void getLegalPawnMoves(Turn turn, Chessboard board, List<Move> moves) {
        long pawns = turn == Turn.WHITE ? board.bitboards.get(Bitboards.pawn) & board.bitboards.get(Bitboards.white) : board.bitboards.get(Bitboards.pawn) & board.bitboards.get(Bitboards.black);
        List<Integer> pawnIndices = new ArrayList<Integer>();
        int index = 0;
        while (pawns != 0) {
            if ((pawns & 1) == 1) {
                pawnIndices.add(index);
            }
            pawns = pawns >> 1;
            index++;
        }

        for (int fromIndex : pawnIndices) {
            long pawnBitboard = generateBitboardFromIndex(fromIndex);
            pawnSinglePushTargets(board, pawnBitboard, moves, fromIndex, turn);
            pawnDoublePushTargets(board, pawnBitboard, moves, fromIndex, turn);
            pawnCaptures(board, pawnBitboard, moves, fromIndex, turn);
        }
    }

    private static long generateBitboardFromIndex(int index) {
        long bitboard = 0;
        return bitboard | (1L << index);
    }

    private static void pawnSinglePushTargets(Chessboard board, long pawnBitboard, List<Move> moves, int fromIndex, Turn turn) {
        long singlePushTargets = turn == Turn.WHITE ? northOne(pawnBitboard) & board.bitboards.get(Bitboards.empty) :
                southOne(pawnBitboard) & board.bitboards.get(Bitboards.empty);

        addMovesFromBitboard(moves, singlePushTargets, fromIndex);
    }

    private static void pawnDoublePushTargets(Chessboard board, long pawnBitboard, List<Move> moves, int fromIndex, Turn turn) {
        long rank = turn == Turn.WHITE ? 0b0000000000000000000000000000000000000000000000001111111100000000L :
                0b0000000011111111000000000000000000000000000000000000000000000000L;
        long pawns = pawnBitboard & rank;
        long doublePushTargets = turn == Turn.WHITE ? northOne(pawnBitboard) & board.bitboards.get(Bitboards.empty) :
                southOne(pawnBitboard) & board.bitboards.get(Bitboards.empty);
        doublePushTargets = turn == Turn.WHITE ? northOne(doublePushTargets) & board.bitboards.get(Bitboards.empty) :
                southOne(doublePushTargets) & board.bitboards.get(Bitboards.empty);

        addMovesFromBitboard(moves, doublePushTargets, fromIndex);
    }
    private static void pawnCaptures(Chessboard board, long pawnBitboard, List<Move> moves, int fromIndex, Turn turn) {
        long enemyPawns = turn == Turn.WHITE ? board.bitboards.get(Bitboards.pawn) & board.bitboards.get(Bitboards.black) :
                board.bitboards.get(Bitboards.pawn) & board.bitboards.get(Bitboards.white);
        long eastTargets = turn == Turn.WHITE ? northEastOne(pawnBitboard) & enemyPawns :
                southEastOne(pawnBitboard) & enemyPawns;
        long westTargets = turn == Turn.WHITE ? northWestOne(pawnBitboard) & enemyPawns :
                southWestOne(pawnBitboard) & enemyPawns;

        long captureTargets = eastTargets | westTargets;

        addMovesFromBitboard(moves, captureTargets, fromIndex);
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

    private static void addMovesFromBitboard(List<Move> moves, long bitboard, int fromIndex) {
        int index = 0;
        while (bitboard != 0) {
            if ((bitboard & 1) == 1) {
                moves.add(new Move(fromIndex, index));
            }
            bitboard = bitboard >> 1;
            index++;
        }
    }

    private static long northOne(long bitboard) {
        return bitboard << 8;
    }

    private static long southOne(long bitboard) {
        return bitboard >> 8;
    }

    private static void getLegalKnightMoves(Turn turn, Chessboard board, List<Move> moves) {
        long knights = turn == Turn.WHITE ? board.bitboards.get(Bitboards.white) & board.bitboards.get(Bitboards.knight) : board.bitboards.get(Bitboards.black) & board.bitboards.get(Bitboards.knight);
        long targets = turn == Turn.WHITE ? board.bitboards.get(Bitboards.black) : board.bitboards.get(Bitboards.white);
        targets |= board.bitboards.get(Bitboards.empty);
        long notAFile = 0b1111111011111110111111101111111011111110111111101111111011111110L;
        long notHFile = 0b0111111101111111011111110111111101111111011111110111111101111111L;
        long notABFile = 0b1111110011111100111111001111110011111100111111001111110011111100L;
        long notGHFile = 0b0011111100111111001111110011111100111111001111110011111100111111L;

        List<Integer> knightIndices = new ArrayList<Integer>();
        int index = 0;
        while (knights != 0) {
            if ((knights & 1) == 1) {
                knightIndices.add(index);
            }
            knights = knights >> 1;
            index++;
        }

        for (int fromIndex : knightIndices) {
            long knightBitboard = generateBitboardFromIndex(fromIndex);
            long northEastTargets = (knightBitboard << 10) & targets & notABFile;
            long northWestTargets = (knightBitboard << 6) & targets & notGHFile;
            long southEastTargets = (knightBitboard >> 6) & targets & notABFile;
            long southWestTargets = (knightBitboard >> 10) & targets & notGHFile;
            long eastNorthTargets = (knightBitboard << 17) & targets & notAFile;
            long eastSouthTargets = (knightBitboard >> 15) & targets & notAFile;
            long westNorthTargets = (knightBitboard << 15) & targets & notHFile;
            long westSouthTargets = (knightBitboard >> 15) & targets & notAFile;

            long moveOrCaptureTargets = northEastTargets | northWestTargets | southEastTargets | southWestTargets |
                    eastNorthTargets | eastSouthTargets | westNorthTargets | westSouthTargets;

            addMovesFromBitboard(moves, moveOrCaptureTargets, fromIndex);
        }
    }

    private static void getLegalBishopMoves(Turn turn, Chessboard board, List<Move> moves) {
        long bishops = turn == Turn.WHITE ? board.bitboards.get(Bitboards.white) & board.bitboards.get(Bitboards.bishop) : board.bitboards.get(Bitboards.black) & board.bitboards.get(Bitboards.bishop);
        long targets = turn == Turn.WHITE ? board.bitboards.get(Bitboards.black) : board.bitboards.get(Bitboards.white);
        targets |= board.bitboards.get(Bitboards.empty);

        List<Integer> bishopIndices = new ArrayList<>();
        int index = 0;
        while (bishops != 0) {
            if ((bishops & 1) == 1) {
                bishopIndices.add(index);
            }
            bishops = bishops >> 1;
            index++;
        }

        for (int fromIndex : bishopIndices) {
            long bishopBitboard = generateBitboardFromIndex(fromIndex);

            long northeastTargets = generateTargetsInDirection(board, targets, bishopBitboard, Direction.NORTHEAST);
            long northwestTargets = generateTargetsInDirection(board, targets, bishopBitboard, Direction.NORTHWEST);
            long southeastTargets = generateTargetsInDirection(board, targets, bishopBitboard, Direction.SOUTHEAST);
            long southwestTargets = generateTargetsInDirection(board, targets, bishopBitboard, Direction.SOUTHWEST);

            long moveOrCaptureTargets = northeastTargets | northwestTargets | southeastTargets | southwestTargets;

            addMovesFromBitboard(moves, moveOrCaptureTargets, fromIndex);
        }
    }

    private static long generateTargetsInDirection(Chessboard board, long targets, long fromBitboard, Direction direction) {
        long targetsInDirection = 0L;
        long currentBitboard = fromBitboard;

        while (true) {
            currentBitboard = direction.shift(currentBitboard);
            if ((currentBitboard & targets) != 0) {
                targetsInDirection |= currentBitboard;
                break;
            } else if ((currentBitboard & board.bitboards.get(Bitboards.empty)) != 0) {
                targetsInDirection |= currentBitboard;
            } else {
                break;
            }
        }

        return targetsInDirection;
    }

    public enum Direction {
        NORTHEAST(9),
        NORTHWEST(7),
        SOUTHEAST(-7),
        SOUTHWEST(-9);

        private final int shiftAmount;

        Direction(int shiftAmount) {
            this.shiftAmount = shiftAmount;
        }

        public long shift(long bitboard) {
            return bitboard << shiftAmount;
        }
    }

    public static void printBinary64(long b) {
        String board = String.format("%64s", Long.toBinaryString(b)).replace(' ', '0');
        for (int i = 0; i < 8; i++) {
            System.out.println(board.substring(i * 8, (i + 1) * 8));
        }
    }

    public static void main(String[] args) {
        List<Move> moves = new ArrayList<Move>();
        Chessboard board = new Chessboard();
//        getLegalPawnMoves(Turn.WHITE, board, moves);
//        getLegalKnightMoves(Turn.WHITE, board, moves);
        getLegalBishopMoves(Turn.WHITE, board, moves);
        for (Move move : moves) {
            System.out.println("From: " + move.getFromSquare() + " To: " + move.getToSquare() + "");
        }
    }
}
