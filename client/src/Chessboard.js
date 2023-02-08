import { React, useState } from 'react'
import './Chessboard.css'
import Square from './Square'

const chessboard = new Map()
const files = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'];
const pieces = ['Rook', 'Knight', 'Bishop', 'Queen', 'King', 'Bishop', 'Knight', 'Rook'];

for (let rank = 1; rank < 9; rank++) {
    for (let file = 0; file < 8; file++) {
        chessboard[files[file] + rank] = (rank < 2 || rank > 7) ? pieces[file] : (rank === 2 || rank === 7) ? 'Pawn' : null;
        if (rank == 1 || rank == 2) {
            chessboard[files[file] + rank] = 'white:' + chessboard[files[file] + rank]
        } else if (rank == 7 || rank == 8) {
            chessboard[files[file] + rank] = 'black:' + chessboard[files[file] + rank]
        }
    }
}

let turn = "white";
let selectedPiece = "";
let whiteKingHasMoved = false;
let blackKingHasMoved = false;

export default function Chessboard() {
    const [board, setBoard] = useState(chessboard);
    const [selectedSquareIndex, setSelectedSquareIndex] = useState(-1)
    const [legalSquares, setLegalSquares] = useState([]);

    return (
        <>
            <div className="container">
                {[...Array(64)].map((e, i) => {
                    const squareColor = indexToSquareColor(i)
                    return (
                        <Square
                            className={`${legalSquares.includes(i) ? 'targeted-square' : ''}`} 
                            pieceInfo={board[indexToSquare(i)]}
                            squareColor={squareColor}
                            key={indexToSquare(i)}
                            squareIndex = {i}
                            selected={selectedSquareIndex == i}
                            handleSquareClick={handleSquareClick}
                            legalSquares={legalSquares} />
                    );
                })}
            </div>
            <h1>{turn}'s Turn</h1>
        </>
    );


    function indexToSquare(index) {
        const file = String.fromCharCode(97 + index % 8).toUpperCase();
        const rank = 8 - Math.floor(index / 8);
        return file + rank;
    }

    function indexToSquareColor(index) {
        return `chess-square ${Math.floor(index / 8) % 2 === 0 ?
                    (index % 2 === 0 ?
                        'black-square' : 'white-square') : (index % 2 === 0 ?
                            'white-square' : 'black-square')}`
    }

    function handleSquareClick(squareIndex) {
        var newBoard = {};
        for (var i in board) {
            newBoard[i] = board[i];
        }

        let selectedPieceColor = selectedPiece == '' ? '' : selectedPiece.split(':')[0];
        let pieceToTakeColor = board[indexToSquare(squareIndex)] == null ? '' : board[indexToSquare(squareIndex)].split(':')[0];

        if ((selectedPiece != '' || board[indexToSquare(squareIndex)] != null) && squareIndex != selectedSquareIndex) {
            if (board[indexToSquare(squareIndex)] != null && turn == pieceToTakeColor) {
                selectedPiece = board[indexToSquare(squareIndex)];
                setSelectedSquareIndex(squareIndex);
                getLegalMoves(squareIndex);
            } else if ((board[indexToSquare(squareIndex)] == null || selectedPiece != '') && selectedPieceColor != pieceToTakeColor && selectedPieceColor == turn) {
                if (legalSquares.includes(squareIndex))
                    renderMove(squareIndex, board)
            }

            setSelectedSquareIndex(squareIndex);
        }
    }

    function getLegalMoves(squareIndex) {
        let legalSquares = [];
        let squareToMoveIsEmpty;
        let toCheck;

        if (selectedPiece.includes('Pawn')) {
            turn == 'white' ? toCheck = [-16, -8, -9, -7] : toCheck = [16, 8, 9, 7]

            for (let i = 0; i < toCheck.length; i++) {
                squareToMoveIsEmpty = board[indexToSquare(squareIndex + toCheck[i])] == null;
                if ((i == 0 && ((squareIndex > 47 && squareIndex < 56) || (squareIndex > 7 && squareIndex < 16))) || i == 1) {
                    if (squareToMoveIsEmpty) legalSquares.push(squareIndex + toCheck[i])
                } else {
                    let pieceToTakeColor = String(board[indexToSquare(squareIndex + toCheck[i])]).split(':')[0]
                    if (!squareToMoveIsEmpty && pieceToTakeColor != turn) legalSquares.push(squareIndex + toCheck[i])
                }
            }
        } else if (selectedPiece.includes('Knight')) {
            toCheck = [10, 17, 15, -6, -10, -15, -17, 6];
            
            for (let i = 0; i < toCheck.length; i++) {
                let pieceToTakeColor = String(board[indexToSquare(squareIndex + toCheck[i])]).split(':')[0]
                squareToMoveIsEmpty = board[indexToSquare(squareIndex + toCheck[i])] == null ? true : false
                let moveIsToValidFile = Math.abs((squareIndex % 8) - ((squareIndex + toCheck[i]) % 8)) <= 2;
                if (((!squareToMoveIsEmpty && pieceToTakeColor != turn) || squareToMoveIsEmpty) && moveIsToValidFile) {
                    legalSquares.push(squareIndex + toCheck[i])
                }
            }
        } else if (selectedPiece.includes('Bishop')) {
            legalSquares = legalBishopMoves(squareIndex)
        } else if (selectedPiece.includes('Rook')) {
            legalSquares = legalRookMoves(squareIndex)
        } else if (selectedPiece.includes('Queen')) {
            legalSquares = legalRookMoves(squareIndex);
            legalSquares = legalSquares.concat(legalBishopMoves(squareIndex));
        } else {
            toCheck = [-9, -8, -7, -1, 1, 7, 8, 9];

            for (let i = 0; i < toCheck.length; i++) {
                let pieceToTakeColor = String(board[indexToSquare(squareIndex + toCheck[i])]).split(':')[0]
                squareToMoveIsEmpty = board[indexToSquare(squareIndex + toCheck[i])] == null ? true : false
                let moveIsToValidFile = Math.abs((squareIndex % 8) - ((squareIndex + toCheck[i]) % 8)) <= 1;
                if (((!squareToMoveIsEmpty && pieceToTakeColor != turn) || squareToMoveIsEmpty) && moveIsToValidFile) {
                    legalSquares.push(squareIndex + toCheck[i])
                }
            }

            console.log(squareIndex)
            console.log(board)
            console.log(indexToSquare(61))
            console.log(indexToSquare(62))
            console.log(indexToSquare(63))
            console.log(board[indexToSquare(61)])
            console.log(board[indexToSquare(62)])
            console.log(board[indexToSquare(63)])

            if (squareIndex == 60 && board[indexToSquare(61)] == null && board[indexToSquare(62)] == null 
                && String(board[indexToSquare(63)].includes('Rook')) && !whiteKingHasMoved) {
                    legalSquares.push(62);
            }
        }

        setLegalSquares(legalSquares);
    }

    function legalRookMoves(position) {
        let moves = [];
        let x = Math.floor(position / 8);
        let y = position % 8;

        // Check moves in the up direction
        let i = x - 1;
        while (i >= 0) {
            let target = i * 8 + y;
            let pieceToTakeColor = String(board[indexToSquare(target)]).split(':')[0];
            if (board[indexToSquare(target)] === null) {
                moves.push(target);
            } else if (pieceToTakeColor != turn) {
                moves.push(target);
                break;
            } else {
                break;
            }
            i--;
        }
        // Check moves in the down direction
        i = x + 1;
        while (i < 8) {
            let target = i * 8 + y;
            let pieceToTakeColor = String(board[indexToSquare(target)]).split(':')[0];
            if (board[indexToSquare(target)] === null) {
                moves.push(target);
            } else if (pieceToTakeColor != turn) {
                moves.push(target);
                break;
            } else {
                break;
            }
            i++;
        }
        // Check moves in the right direction
        let j = y + 1;
        while (j < 8) {
            let target = x * 8 + j;
            let pieceToTakeColor = String(board[indexToSquare(target)]).split(':')[0];
            if (board[indexToSquare(target)] === null) {
                moves.push(target);
            } else if (pieceToTakeColor != turn) {
                moves.push(target);
                break;
            } else {
                break;
            }
            j++;
        }
        
        // Check moves in the left direction
        j = y - 1;
        while (j >= 0) {
            let target = x * 8 + j;
            let pieceToTakeColor = String(board[indexToSquare(target)]).split(':')[0];
            if (board[indexToSquare(target)] === null) {
                moves.push(target);
            } else if (pieceToTakeColor != turn) {
                moves.push(target);
                break;
            } else {
                break;
            }
            j--;
        }
        return moves;
    }


    function legalBishopMoves(position) {
        let moves = [];
        let x = Math.floor(position / 8);
        let y = position % 8;

        // Check moves in the up-right direction
        let i = x - 1, j = y + 1;
        while (i >= 0 && j < 8) {
            let target = i * 8 + j;
            let pieceToTakeColor = String(board[indexToSquare(target)]).split(':')[0];
            if (board[indexToSquare(target)] === null) {
                moves.push(target);
            } else if (pieceToTakeColor != turn) {
                moves.push(target);
                break;
            } else {
                break;
            }
            i--;
            j++;
        }
        // Check moves in the up-left direction
        i = x - 1;
        j = y - 1;
        while (i >= 0 && j >= 0) {
            let target = i * 8 + j;
            let pieceToTakeColor = String(board[indexToSquare(target)]).split(':')[0];
            if (board[indexToSquare(target)] === null) {
                moves.push(target);
            } else if (pieceToTakeColor != turn) {
                moves.push(target);
                break;
            } else {
                break;
            }
            i--;
            j--;
        }
        // Check moves in the down-right direction
        i = x + 1;
        j = y + 1;
        while (i < 8 && j < 8) {
            let target = i * 8 + j;
            let pieceToTakeColor = String(board[indexToSquare(target)]).split(':')[0];
            if (board[indexToSquare(target)] === null) {
                moves.push(target);
            } else if (pieceToTakeColor != turn) {
                moves.push(target);
                break;
            } else {
                break;
            }
            i++;
            j++;
        }
        // Check moves in the down-left direction
        i = x + 1;
        j = y - 1;
        while (i < 8 && j >= 0) {
            let target = i * 8 + j;
            let pieceToTakeColor = String(board[indexToSquare(target)]).split(':')[0];
            if (board[indexToSquare(target)] === null) {
                moves.push(target);
            } else if (pieceToTakeColor != turn) {
                moves.push(target);
                break;
            } else {
                break;
            }
            i++;
            j--;
        }
        return moves;
    }

    function renderMove(newSquareIndex, newBoard) {
        if (selectedPiece.includes("King") && newSquareIndex != 62) {
            whiteKingHasMoved = true;
        } else if (selectedPiece.includes("King") && newSquareIndex == 62) {
            newBoard[indexToSquare(63)] = null;
            newBoard[indexToSquare(62)] = selectedPiece;
            newBoard[indexToSquare(61)] = turn + ':Rook';
            newBoard[indexToSquare(60)] = null;
        } else {
            newBoard[indexToSquare(newSquareIndex)] = board[indexToSquare(selectedSquareIndex)];
            newBoard[indexToSquare(selectedSquareIndex)] = null;
        }

        setBoard(newBoard);
        turn = turn == 'white' ? 'black' : 'white';



        selectedPiece = "";
        setLegalSquares([]);
    }
}
