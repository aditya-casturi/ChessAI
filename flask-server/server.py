from flask import Flask
from enum import Enum

app = Flask(__name__)


@app.route('/members')
def index():
    return {"members": ["Member1", "Member2", "Member3"]}


if __name__ == "__main__":
    app.run(debug=True)


class Color(Enum):
    WHITE = 1
    BLACK = 2


class Piece(Enum):
    PAWN = 1
    KNIGHT = 2
    BISHOP = 3
    ROOK = 4
    QUEEN = 5
    KING = 6


class Square():
    file = "A"
    rank = 1

    def __init__(self, file, rank):
        self.file = file
        self.rank = rank


class ChessPiece:
    color = Color.WHITE
    type = Piece.PAWN
    square = Square()

    def __init__(self, color, type, square):
        self.color = color
        self.type = type
        self.square = square


def add_piece(piece, file_one, file_two):
    pieces.add(ChessPiece(Color.WHITE, piece, Square(file_one, 1)))
    pieces.add(ChessPiece(Color.BLACK, piece, Square(file_one, 8)))
    if file_two != "":
        pieces.add(ChessPiece(Color.WHITE, piece, Square(file_two, 1)))
        pieces.add(ChessPiece(Color.BLACK, piece, Square(file_two, 8)))


def initialize_board():
    letters = ["A", "B", "C", "D", "E", "F", "G", "H"]

    for i in range(8):
        pieces.add(ChessPiece(Color.WHITE, Piece.PAWN, Square(letters[i], 2)))
        pieces.add(ChessPiece(Color.BLACK, Piece.PAWN, Square(letters[i], 7)))

    add_piece(Piece.KNIGHT, "B", "G")
    add_piece(Piece.BISHOP, "C", "F")
    add_piece(Piece.ROOK, "A", "H")
    add_piece(Piece.QUEEN, "D", "")
    add_piece(Piece.KING, "E", "")


pieces = []
initialize_board()
