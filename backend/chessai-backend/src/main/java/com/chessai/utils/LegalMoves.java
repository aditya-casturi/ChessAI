package com.chessai.utils;

import com.chessai.model.Move;
import com.chessai.model.Position;

import java.util.*;

public class LegalMoves {

    public static List<Move> getLegalMoves(Position position) {
        String[] board = position.board;
        String turn = position.getTurn();
        List<Move> legalMoves = getAllMoves(turn, board);

        legalMoves = pruneIllegalMoves(legalMoves, turn, board);

        int kingSquareIndex = -1;
        String opponentColor = turn == "w" ? "b" : "w";
        for (int i = 0; i < board.length; i++) {
            if (board[i].equals(opponentColor + "K")) {
                kingSquareIndex = i;
                break;
            }
        }
        legalMoves.removeAll(Collections.singleton(kingSquareIndex));

        return legalMoves;
    }

    private static List<Move> getLegalPawnMoves(String turn, String[] board) {
        List<Move> legalMoves = new ArrayList<>();
        int[] squaresToCheck = Objects.equals(turn, "w") ? new int[]{-8, -9, -7, -16} : new int[]{8, 9, 7, 16};

        for (int pawnSquare = 0; pawnSquare < board.length; pawnSquare++) {
            if (board[pawnSquare].equals(turn + "P")) {
                for (int squareToCheck : squaresToCheck) {
                    int targetSquare = pawnSquare + squareToCheck;
                    if (targetSquare > -1 && targetSquare < 64) {
                        if (squareToCheck == -8 || squareToCheck == 8) {
                            // check if square is empty
                            if (board[targetSquare].isEmpty()) {
                                legalMoves.add(new Move(pawnSquare, targetSquare));
                            }
                        } else if (squareToCheck == 16 || squareToCheck == -16) {
                            // check if the pawn move is 2 squares forward
                            if (board[targetSquare].equals("")) {
                                if (((pawnSquare >= 48 && pawnSquare <= 55) || (pawnSquare >= 8 && pawnSquare <= 15)) &&
                                        board[targetSquare - 8 * (squareToCheck/16)].equals("")) {
                                    legalMoves.add(new Move(pawnSquare, targetSquare));
                                }
                            }
                        } else {
                            // check if square is occupied by opponent
                            int pawnFile = (pawnSquare % 8) + 1;
                            int targetFile = (targetSquare % 8) + 1;

                            if (Math.abs(pawnFile - targetFile) == 1) {
                                String opponent = board[targetSquare];
                                if (!Objects.equals(opponent, "") && !String.valueOf(opponent.charAt(0)).equals(turn)) {
                                    legalMoves.add(new Move(pawnSquare, targetSquare));
                                }
                            }
                        }
                    }
                }
            }
        }

        return legalMoves;

    }

    private static List<Move> getLegalKnightMoves(String turn, String[] board) {
        List<Move> legalMoves = new ArrayList<>();

        int[] squaresToCheck = new int[]{10, 17, 15, -6, -10, -15, -17, 6};

        for (int knightSquare = 0; knightSquare < board.length; knightSquare++) {
            if (board[knightSquare].equals(turn + "N")) {
                for (int squareToCheck : squaresToCheck) {
                    if (knightSquare + squareToCheck < 64 && knightSquare + squareToCheck > -1) {
                        boolean squareToMoveIsEmpty = board[knightSquare + squareToCheck].equals("");
                        boolean moveIsToValidFile = Math.abs((knightSquare % 8) - ((knightSquare + squareToCheck) % 8)) <= 2;
                        char pieceToTakeColor = ' ';
                        if (!squareToMoveIsEmpty) pieceToTakeColor = board[knightSquare + squareToCheck].charAt(0);

                        if ((squareToMoveIsEmpty || !String.valueOf(pieceToTakeColor).equals(turn)) && moveIsToValidFile) {
                            legalMoves.add(new Move(knightSquare, knightSquare + squareToCheck));
                        }
                    }
                }
            }
        }

        return legalMoves;
    }

    private static List<Move> getLegalBishopMoves(String turn, String[] board) {
        List<Move> legalMoves = new ArrayList<>();

        for (int bishopSquare = 0; bishopSquare < board.length; bishopSquare++) {
            if (board[bishopSquare].equals(turn + "B")) {
                legalMoves.addAll(getMovesOnDiagonals(turn, board, bishopSquare));
            }
        }

        return legalMoves;
    }

    private static List<Move> getLegalRookMoves(String turn, String[] board) {
        List<Move> legalMoves = new ArrayList<>();

        for (int rookSquare = 0; rookSquare < board.length; rookSquare++) {
            if (board[rookSquare].equals(turn + "R")) {
                legalMoves.addAll(getMovesOnFileAndRank(turn, board, rookSquare));
            }
        }

        return legalMoves;
    }

    private static List<Move> getLegalQueenMoves(String turn, String[] board) {
        List<Move> legalMoves = new ArrayList<>();
        int queenSquare = -1;
        for (int i = 0; i < 64; i++) {
            if (board[i].equals(turn + "Q")) {
                queenSquare = i;
            }
        }
        if (queenSquare == -1) {
            return legalMoves;
        }

        legalMoves.addAll(getMovesOnFileAndRank(turn, board, queenSquare));
        legalMoves.addAll(getMovesOnDiagonals(turn, board, queenSquare));

        return legalMoves;
    }

    private static List<Move> getLegalKingMoves(String turn, String[] board) {
        List<Move> legalMoves = new ArrayList<>();
        int kingSquare = -1;
        for (int i = 0; i < 64; i++) {
            if (board[i].equals(turn + "K")) {
                kingSquare = i;
            }
        }
        if (kingSquare == -1) {
            return legalMoves;
        }
        int[] squaresToCheck = {-9, -8, -7, -1, 1, 7, 8, 9};

        for (int i = 0; i < squaresToCheck.length; i++) {
            int targetSquareIndex = kingSquare + squaresToCheck[i];
            if (targetSquareIndex >= 0 && targetSquareIndex < 64) {
                String targetSquareContent = board[targetSquareIndex];
                char pieceToTakeColor = ' ';
                if (!targetSquareContent.equals("")) {
                    pieceToTakeColor = targetSquareContent.charAt(0);
                }
                boolean squareToMoveIsEmpty = targetSquareContent.equals("");
                boolean moveIsToValidFile = Math.abs((kingSquare % 8) - (targetSquareIndex % 8)) <= 1;

                if (((!squareToMoveIsEmpty && !String.valueOf(pieceToTakeColor).equals(turn)) || squareToMoveIsEmpty) && moveIsToValidFile) {
                    legalMoves.add(new Move(kingSquare, targetSquareIndex));
                }
            }
        }

        return legalMoves;
    }

    private static List<Move> getMovesOnFileAndRank(String turn, String[] board, int pieceSquare) {
        List<Move> legalMoves = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};

        for (int[] direction : directions) {
            int dx = direction[0], dy = direction[1];
            int x = pieceSquare / 8, y = pieceSquare % 8;
            legalMoves.addAll(walkPath(turn, board, pieceSquare, dx, dy, x, y));
        }

        return legalMoves;
    }

    private static List<Move> getMovesOnDiagonals(String turn, String[] board, int pieceSquare) {
        List<Move> legalMoves = new ArrayList<>();
        int[][] directions = {{-1, 1}, {-1, -1}, {1, 1}, {1, -1}};

        for (int[] direction : directions) {
            int dx = direction[0], dy = direction[1];
            int x = (int) Math.floor((double) pieceSquare / 8), y = pieceSquare % 8;
            legalMoves.addAll(walkPath(turn, board, pieceSquare, dx, dy, x, y));
        }

        return legalMoves;
    }

    private static List<Move> walkPath(String turn, String[] board, int pieceSquare, int dx, int dy, int x, int y) {
        List<Move> legalMoves = new ArrayList<>();
        int targetX = x + dx, targetY = y + dy;
        while (targetX >= 0 && targetX < 8 && targetY >= 0 && targetY < 8) {
            int target = targetX * 8 + targetY;
            if (target > -1 && target < 64) {
                char pieceToTakeColor = ' ';
                if (!board[target].equals("")) {
                    pieceToTakeColor = board[target].charAt(0);
                }
                if (!String.valueOf(pieceToTakeColor).equals(turn) && pieceSquare > -1) {
                    legalMoves.add(new Move(pieceSquare, target));
                }
                if (!board[target].equals("")) {
                    break;
                }
            }
            targetX += dx;
            targetY += dy;
        }

        return legalMoves;
    }

    private static boolean isKingInCheck(String[] board, String turn) {
        int kingSquareIndex = -1;
        for (int i = 0; i < board.length; i++) {
            if (board[i].equals(turn + "K")) {
                kingSquareIndex = i;
            }
        }

        turn = turn == "w" ? "b" : "w";
        List<Move> legalMoves = getAllMoves(turn, board);

        boolean isKingInCheck = false;
        for (Move move : legalMoves) {
            if (move.getToSquare() == kingSquareIndex) {
                isKingInCheck = true;
                break;
            }
        }

        return isKingInCheck;
    }

    private static List<Move> getAllMoves(String turn, String[] board) {
        List<Move> legalMoves = new ArrayList<>();

        legalMoves.addAll(getLegalPawnMoves(turn, board));
        legalMoves.addAll(getLegalKnightMoves(turn, board));
        legalMoves.addAll(getLegalBishopMoves(turn, board));
        legalMoves.addAll(getLegalRookMoves(turn, board));
        legalMoves.addAll(getLegalQueenMoves(turn, board));
        legalMoves.addAll(getLegalKingMoves(turn, board));

        return legalMoves;
    }

    private static boolean doesMoveHandleCheck(Move move, String[] board, String turn) {
        String[] newBoard = board.clone();
        if (newBoard[move.getToSquare()] == "")
            newBoard = move(newBoard, move.getFromSquare(), move.getToSquare());
        else {
            newBoard = capture(newBoard, move.getFromSquare(), move.getToSquare());
        }

        return !isKingInCheck(newBoard, turn);
    }

    private static List<Move> pruneIllegalMoves(List<Move> moves, String turn, String[] board) {
        List<Move> updatedMoves = new ArrayList<>(moves);
        boolean isKingInCheck = isKingInCheck(board, turn);
        if (!isKingInCheck) {
            return updatedMoves;
        }

        Iterator<Move> iter = updatedMoves.iterator();

        while (iter.hasNext()) {
            Move move = iter.next();

            if (!doesMoveHandleCheck(move, board, turn))
                iter.remove();
        }

        return updatedMoves;
    }

    public static String[] move(String[] arr, int index1, int index2) {
        String temp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = temp;
        return arr;
    }

    public static String[] capture(String[] arr, int index1, int index2) {
        arr[index2] = arr[index1];
        arr[index1] = "";
        return arr;
    }
}