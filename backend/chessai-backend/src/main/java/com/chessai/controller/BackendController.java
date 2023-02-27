package com.chessai.controller;

import com.chessai.model.Move;
import com.chessai.model.Position;
import com.chessai.utils.Eval;
import com.chessai.utils.LegalMoves;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.chessai.utils.Eval.boardEval;
import static com.chessai.utils.LegalMoves.*;
@Controller
@RequestMapping("/api")
public class BackendController {
    @RequestMapping(value = "/getComputerMove", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:3000")
    public String getComputerMove(@RequestBody Map<String, String> requestBody) {
        Gson gson = new Gson();
        String json = requestBody.get("jsonPayload");
        String turn = requestBody.get("turn");
        String[] board = gson.fromJson(json, String[].class);
        Position rootPosition = new Position(turn, null, board, null);
        List<Position> leaves = new ArrayList<>();
        buildPositionsTree(rootPosition, 4, leaves);

        Map<Position, Integer> transpositionTable = new HashMap<>();

        // for computer move, alpha=MIN_VALUE, beta=MAX_VALUE, maximizingPlayer=true
        int bestScore = minimax(rootPosition, Integer.MIN_VALUE, Integer.MAX_VALUE, false, transpositionTable);
        Position bestChild = null;
        for (Position child : rootPosition.children) {
            if (child.getScore() == rootPosition.getScore()) {
                bestChild = child;
                break;
            }
        }

        Map<String, Object> returnData = new HashMap<>();
        returnData.put("updatedBoard", bestChild.board);
        returnData.put("fromSquare", bestChild.lastMove.getFromSquare());
        returnData.put("toSquare", bestChild.lastMove.getToSquare());

        return gson.toJson(returnData);
    }

    private static int minimax(Position position, int alpha, int beta, boolean maximizingPlayer, Map<Position, Integer> transpositionTable) {
        if (position.children.size() == 0) {
            if (transpositionTable.containsKey(position))
                return transpositionTable.get(position);
            return position.getScore();
        }

        int bestEval = maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (Position child : position.children) {
            int eval;
            if (transpositionTable.containsKey(child)) {
                eval = transpositionTable.get(child);
            } else {
                eval = minimax(child, alpha, beta, !maximizingPlayer, transpositionTable);
                transpositionTable.put(child, eval);
            }
            if (maximizingPlayer) {
                bestEval = Math.max(bestEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha)
                    break;
            } else {
                bestEval = Math.min(bestEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha)
                    break;
            }
        }
        position.setScore(bestEval);
        return bestEval;
    }



    private static void buildPositionsTree(Position position, int depth, List<Position> leaves) {
        if (depth == 0) {
            position.setScore(boardEval(position.board));
            leaves.add(position);
            return;
        }

        List<Move> legalMoves = getLegalMoves(position);
        String[] board = position.board;
        String turn = position.getTurn();

        turn = turn == "w" ? "b" : "w";
        for (Move legalMove : legalMoves) {
            String[] newBoard = board.clone();
            if (newBoard[legalMove.getToSquare()] == "")
                newBoard = move(newBoard, legalMove.getFromSquare(), legalMove.getToSquare());
            else {
                newBoard = capture(newBoard, legalMove.getFromSquare(), legalMove.getToSquare());
            }
            position.children.add(new Position(turn, position, newBoard, legalMove));
            buildPositionsTree(position.children.get(position.children.size() - 1), depth-1, leaves);
        }
    }

    private static void printBoard(String[] board) {
        System.out.println("\n");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print("+-----");
            }
            System.out.println("+");

            for (int j = 0; j < 8; j++) {
                int index = i * 8 + j;
                System.out.print("|");

                if (board[index].length() < 5) {
                    int spaces = (5 - board[index].length()) / 2;
                    for (int k = 0; k < spaces; k++) {
                        System.out.print(" ");
                    }
                    System.out.print(board[index]);
                    for (int k = 0; k < spaces; k++) {
                        System.out.print(" ");
                    }
                    if ((5 - board[index].length()) % 2 == 1) {
                        System.out.print(" ");
                    }
                } else {
                    System.out.print(" " + board[index].substring(0, 4) + ".");
                }
            }
            System.out.println("|");
        }
        for (int j = 0; j < 8; j++) {
            System.out.print("+-----");
        }
        System.out.println("+");
        System.out.println("\n");
    }
}
