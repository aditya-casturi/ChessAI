import { React, useState } from 'react'
import './Chessboard.css'
import Square from './Square'

let turn = "w";
let selectedPiece = "";
let whiteKingHasMoved = false;
let isWhiteKingInCheck = false;
let isBlackKingInCheck = false;
let blackKingSquareIndex = 4;
let whiteKingSquareIndex = 60;

export default function Chessboard() {
    const [board, setBoard] = useState(boardInit());
    const [selectedSquareIndex, setSelectedSquareIndex] = useState(-1)
    const [legalSquares, setLegalSquares] = useState([]);

    return (
        <>
            <div className="container">
                {[...Array(64)].map((e, i) => {
                    const squareColor = indexToSquareColor(i)
                    return (
                        <Square
                            piece={board[indexToSquare(i)]}
                            squareColor={squareColor}
                            key={indexToSquare(i)}
                            squareIndex = {i}
                            selected={selectedSquareIndex == i}
                            handleSquareClick={handleSquareClick}
                            handlePieceDrop={handlePieceDrop}
                            legalSquares={legalSquares} />
                    );
                })}
            </div>
            <h1>{turn}'s Turn</h1>
        </>
    );

    function boardInit() {
        const chessboard = new Map()
        const files = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'];
        const pieces = ['R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'];


        for (let rank = 1; rank < 9; rank++) {
            for (let file = 0; file < 8; file++) {
                let square = files[file] + rank;
                let piece = (rank < 2 || rank > 7) ? pieces[file] : (rank === 2 || rank === 7) ? 'P' : '';

                if (rank == 1 || rank == 2) {
                    chessboard[square] = 'w' + piece;
                } else if (rank == 7 || rank == 8) {
                    chessboard[square] = 'b' + piece;
                } else {
                    chessboard[square] = '';
                }
            }
        }
        return chessboard;
    }

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

    function handleSquareClick(clickedSquareIndex) {
        var newBoard = {};
        for (var i in board) {
            newBoard[i] = board[i];
        }

        const selectedPieceColor = selectedPiece ? selectedPiece[0] : '';
        const squareContents = board[indexToSquare(clickedSquareIndex)];
        const squareColor = squareContents ? squareContents[0] : '';

        const isPieceSelected = selectedPiece != '';
        const isSquareOccupied = squareContents != '';
        const isPieceOfCorrectColor = selectedPieceColor == turn;
        const isSquareNotSelected = clickedSquareIndex != selectedSquareIndex;
        const isPieceInSquareOfCorrectColor = squareColor == turn;
        const isLegalMove = legalSquares.includes(clickedSquareIndex);

        if (isSquareNotSelected && (isPieceSelected || (isSquareOccupied && isPieceInSquareOfCorrectColor))) {
            if (isSquareOccupied && isPieceInSquareOfCorrectColor) {
                selectedPiece = squareContents;
                setSelectedSquareIndex(clickedSquareIndex);
                getLegalMoves(clickedSquareIndex);
            } else if (isPieceOfCorrectColor && isLegalMove) {
                renderMove(clickedSquareIndex, board);
                setSelectedSquareIndex(clickedSquareIndex);
            }
        }
    }

    function handlePieceDrop(event, data) {
        const targetSquareIndex = parseInt(event.target.id);

        if (!legalSquares.includes(targetSquareIndex)) {
            return;
        }

        renderMove(targetSquareIndex, { ...board })
        setSelectedSquareIndex(event.target.id);
    }

    function getLegalMoves(pieceSquareIndex) {
        let legalMoves = [];

        if (selectedPiece.includes('P')) {
            legalMoves = getLegalPawnMoves(pieceSquareIndex)
        } else if (selectedPiece.includes('N')) {
            legalMoves = getLegalKnightMoves(pieceSquareIndex)
        } else if (selectedPiece.includes('B')) {
            legalMoves = getLegalBishopMoves(pieceSquareIndex)
        } else if (selectedPiece.includes('R')) {
            legalMoves = getLegalRookMoves(pieceSquareIndex)
        } else if (selectedPiece.includes('Q')) {
            legalMoves = getLegalRookMoves(pieceSquareIndex).concat(getLegalBishopMoves(pieceSquareIndex));
        } else {
            legalMoves = getLegalKingMoves(pieceSquareIndex);
        }

        setLegalSquares(legalMoves);
    }

    function getLegalRookMoves(pieceSquareIndex) {
        let moves = [];
        let x = Math.floor(pieceSquareIndex / 8);
        let y = pieceSquareIndex % 8;

        let directions = [[-1, 0], [1, 0], [0, 1], [0, -1]];
        for (let i = 0; i < directions.length; i++) {
            let dx = directions[i][0];
            let dy = directions[i][1];
            let targetX = x + dx;
            let targetY = y + dy;
            while (targetX >= 0 && targetX < 8 && targetY >= 0 && targetY < 8) {
                let target = targetX * 8 + targetY;
                let pieceToTakeColor = String(board[indexToSquare(target)]).charAt(0);
                if (board[indexToSquare(target)] === '') {
                    moves.push(target);
                } else if (pieceToTakeColor != turn) {
                    moves.push(target);
                    break;
                } else {
                    break;
                }
                targetX += dx;
                targetY += dy;
            }
        }
        return moves;
    }

    function getLegalBishopMoves(pieceSquareIndex) {
        let moves = [];
        let x = Math.floor(pieceSquareIndex / 8);
        let y = pieceSquareIndex % 8;

        const directions = [
            [-1, 1],
            [-1, -1],
            [1, 1],
            [1, -1]
        ];

        for (const [dx, dy] of directions) {
            let i = x + dx, j = y + dy;
            while (i >= 0 && i < 8 && j >= 0 && j < 8) {
                let target = i * 8 + j;
                let pieceToTakeColor = String(board[indexToSquare(target)]).charAt(0);
                if (board[indexToSquare(target)] === '') {
                    moves.push(target);
                } else if (pieceToTakeColor !== turn) {
                    moves.push(target);
                    break;
                } else {
                    break;
                }
                i += dx;
                j += dy;
            }
        }
        return moves;
    }

    function getLegalPawnMoves(pieceSquareIndex) {
        let squaresToCheck = turn == 'w' ? [-8, -9, -7] : [8, 9, 7];
        let legalSquares = [];

        squaresToCheck.forEach(offset => {
            let targetSquare = pieceSquareIndex + offset;
            let squareContents = board[indexToSquare(targetSquare)];
            let squareColor = squareContents.charAt(0);

            if (offset === -8 || offset === 8) {
                if (squareContents === '') {
                    legalSquares.push(targetSquare);
                }
            } else if (squareContents !== '' && squareColor !== turn) {
                legalSquares.push(targetSquare);
            }

            targetSquare = (turn === 'w') ? pieceSquareIndex - 16 : pieceSquareIndex + 16;

            if ((turn === 'w' && pieceSquareIndex > 47 && pieceSquareIndex < 56) ||
                (turn === 'b' && pieceSquareIndex > 7 && pieceSquareIndex < 16)) {
                let squareContents = board[indexToSquare(targetSquare)];
                if (squareContents === '') {
                    if (turn == 'w' && board[indexToSquare(pieceSquareIndex - 8)] == '' ||
                        turn == 'b' && board[indexToSquare(pieceSquareIndex + 8)] == '')
                        legalSquares.push(targetSquare);
                }
            }
        });

        return legalSquares;
    }

    function getLegalKnightMoves(pieceSquareIndex) {
        let squaresToCheck = [10, 17, 15, -6, -10, -15, -17, 6];
        let legalSquares = [];

        for (let i = 0; i < squaresToCheck.length; i++) {
            let targetPieceColor = String(board[indexToSquare(pieceSquareIndex + squaresToCheck[i])]).charAt(0)
            let isTargetSquareEmpty = board[indexToSquare(pieceSquareIndex + squaresToCheck[i])] == '' ? true : false
            let isMoveToValidFile = Math.abs((pieceSquareIndex % 8) - ((pieceSquareIndex + squaresToCheck[i]) % 8)) <= 2;

            if (((!isTargetSquareEmpty && targetPieceColor != turn) || isTargetSquareEmpty) && isMoveToValidFile) {
                legalSquares.push(pieceSquareIndex + squaresToCheck[i])
            }
        }

        return legalSquares;
    }

    function getLegalKingMoves(pieceSquareIndex) {
        let squaresToCheck = [-9, -8, -7, -1, 1, 7, 8, 9];
        let legalMoves = []

        for (let i = 0; i < squaresToCheck.length; i++) {
            let pieceToTakeColor = String(board[indexToSquare(pieceSquareIndex + squaresToCheck[i])]).charAt(0);
            let squareToMoveIsEmpty = board[indexToSquare(pieceSquareIndex + squaresToCheck[i])] == '' ? true : false
            let moveIsToValidFile = Math.abs((pieceSquareIndex % 8) - ((pieceSquareIndex + squaresToCheck[i]) % 8)) <= 1;

            if (((!squareToMoveIsEmpty && pieceToTakeColor != turn) || squareToMoveIsEmpty) && moveIsToValidFile) {
                legalMoves.push(pieceSquareIndex + squaresToCheck[i])
            }
        }

        return legalMoves;
    }

    function renderMove(targetSquareIndex, board) {
        if (selectedPiece == 'bK') {
            blackKingSquareIndex = targetSquareIndex;
        } else if (selectedPiece == 'wK') {
            whiteKingSquareIndex = targetSquareIndex;
        }

        console.log(isMoveACheck(targetSquareIndex, board))
        board[indexToSquare(targetSquareIndex)] = board[indexToSquare(selectedSquareIndex)];
        board[indexToSquare(selectedSquareIndex)] = '';

        setBoard(board);
        turn = turn == 'w' ? 'b' : 'w';

        selectedPiece = "";
        setLegalSquares([]);
    }

    function isMoveACheck(squareIndexOfMove) {
        const opponentKingSquareIndex = turn == 'w' ? blackKingSquareIndex : whiteKingSquareIndex;

        if (selectedPiece.includes('P')) {
            return getLegalPawnMoves(squareIndexOfMove).includes(opponentKingSquareIndex);
        } else if (selectedPiece.includes('N')) {
            return getLegalKnightMoves(squareIndexOfMove).includes(opponentKingSquareIndex);
        } else if (selectedPiece.includes('R')) {
            return getLegalRookMoves(squareIndexOfMove).includes(opponentKingSquareIndex);
        } else if (selectedPiece.includes('B')) {
            return getLegalBishopMoves(squareIndexOfMove).includes(opponentKingSquareIndex);
        } else if (selectedPiece.includes('Q')) {
            return getLegalRookMoves(squareIndexOfMove).includes(opponentKingSquareIndex) ||
                getLegalBishopMoves(squareIndexOfMove).includes(opponentKingSquareIndex);
        }
    }
}
