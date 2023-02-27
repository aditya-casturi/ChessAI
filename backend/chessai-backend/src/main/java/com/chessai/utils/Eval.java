package com.chessai.utils;

import java.util.Map;

import static java.util.Map.entry;

public class Eval {
    public static int boardEval(String[] board) {
        int score = 0;

        score += materialEval(board);

        return score;
    }

    private static int materialEval(String[] board) {
        int whiteMaterialValue = 0;
        int blackMaterialValue = 0;

        Map<Character, Integer> pieceValues = Map.ofEntries(
                entry('Q', 9),
                entry('R', 5),
                entry('B', 3),
                entry('N', 3),
                entry('P', 1),
                entry('K', 0)
        );

        for (int i = 0; i < board.length; i++) {
            if (!board[i].equals("")) {
                char color = board[i].charAt(0);
                char piece = board[i].charAt(1);
                if (color == 'w') {
                    whiteMaterialValue += pieceValues.get(piece);
                } else {
                    blackMaterialValue += pieceValues.get(piece);
                }
            }
        }

        return whiteMaterialValue - blackMaterialValue;
    }
}
