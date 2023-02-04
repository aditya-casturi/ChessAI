from flask import Flask, request
from enum import Enum

app = Flask(__name__)

board = {}

pieces = {
    "Rook": ['A8', 'H8', 'A1', 'H1'],
    "Knight": ['B8', 'G8', 'B1', 'G1'],
    "Bishop": ['C8', 'F8', 'C1', 'F1'],
    "Queen": ['D8', 'D1'],
    "King": ['E8', 'E1'],
    "Pawn": ['A7', 'B7', 'C7', 'D7', 'E7', 'F7', 'G7', 'H7', 'A2', 'B2', 'C2', 'D2', 'E2', 'F2', 'G2', 'H2']
}

for piece_type, positions in pieces.items():
    for pos in positions:
        board[pos] = piece_type

for file in "ABCDEFGH":
    for rank in "12345678":
        square = file + rank
        if square not in board:
            board[square] = None


@app.route('/move')
def index():
    rank = request.args.get('rank')
    file = request.args.get('file')
    if board[file+rank] != None:
        return 'valid'
    return 'invalid'


if __name__ == "__main__":
    app.run(debug=True)
