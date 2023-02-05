import { React, useState } from 'react'
import './Chessboard.css'
import { Knight, Bishop, Rook, King, Queen, Pawn } from './PieceIcons'

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

export default function Chessboard() {
    const [board, setBoard] = useState(chessboard);
    const [selectedSquareIndex, setSelectedSquareIndex] = useState(-1)
    const [selectedPiece, setSelectedPiece] = useState('');

    return (
        <>
            <div className="container">
                {[...Array(64)].map((e, i) => {
                    const squareColor = indexToSquareColor(i)
                    return (
                        <Square 
                            pieceInfo={board[indexToSquare(i)]}
                            squareColor={squareColor}
                            key={indexToSquare(i)}
                            squareIndex = {i}
                            selected={selectedSquareIndex == i} />
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
            if (selectedPiece == '' && board[indexToSquare(squareIndex)] != null && turn == pieceToTakeColor) {
                setSelectedPiece(board[indexToSquare(squareIndex)]);
                setSelectedSquareIndex(squareIndex);
            } else if ((board[indexToSquare(squareIndex)] == null || selectedPiece != '') && selectedPieceColor != pieceToTakeColor && selectedPieceColor == turn) {
                renderMove(squareIndex, newBoard)
            }

            setSelectedSquareIndex(squareIndex);
        }
    }

    function renderMove(newSquareIndex, newBoard) {
        if(checkMoveValidity(selectedSquareIndex, newSquareIndex)) {
            newBoard[indexToSquare(newSquareIndex)] = board[indexToSquare(selectedSquareIndex)];
            newBoard[indexToSquare(selectedSquareIndex)] = null;
    
            setBoard(newBoard);
            turn = turn == 'white' ? 'black' : 'white';
        }

        setSelectedPiece('');
    }

    function checkMoveValidity(fromIndex, toIndex) {
        let fromSquare = indexToSquare(fromIndex);
        let toSquare = indexToSquare(toIndex);

        let piece = board[fromSquare].split(':')[1];

        let squareToMoveIsEmpty = board[toSquare] == null ? true : false

        if (piece == 'Pawn') {
            if ((fromIndex > 47 && toIndex < 56 && fromIndex == toIndex + 16) || 
                (fromIndex > 7 && toIndex < 32 && fromIndex == toIndex - 16)) {
                return squareToMoveIsEmpty;
            } else if ((fromIndex == toIndex + 8 || fromIndex == toIndex - 8) && squareToMoveIsEmpty) {
                return true;
            } else if ((fromIndex == toIndex + 7 || fromIndex == toIndex - 7 || 
                        fromIndex == toIndex + 9 || fromIndex == toIndex - 9) && !squareToMoveIsEmpty) {
                return true;
            }
            return false;
        } else if (piece == 'Knight') {
            if (fromIndex == toIndex + 17 || fromIndex == toIndex + 15 || fromIndex == toIndex + 6 || fromIndex == toIndex - 10 ||
                fromIndex == toIndex -17 || fromIndex == toIndex - 15 || fromIndex == toIndex - 6 || fromIndex == toIndex + 10) {
                return true;
            }
            return false;
        } else if (piece == 'Rook') {
            return checkIfPathIsBlocked(fromIndex, toIndex, 'line')
        } else if (piece == 'Bishop') {
            return checkIfPathIsBlocked(fromIndex, toIndex, 'diagonal')
        } else if (piece == 'Queen') {
            let isDiagonalBlocked = checkIfPathIsBlocked(fromIndex, toIndex, 'diagonal');
            let isLineBlocked = checkIfPathIsBlocked(fromIndex, toIndex, 'line');
            return (!isDiagonalBlocked && isLineBlocked) || (!isLineBlocked && isDiagonalBlocked)
        } else {
            if ((toIndex >= fromIndex - 9 && toIndex <= fromIndex - 7) || (toIndex >= fromIndex + 7 && toIndex <= fromIndex + 9) ||
                fromIndex == toIndex + 1 || fromIndex == toIndex - 1) {
                    return true;
            }
            return false;
        }
    }

    function checkIfPathIsBlocked(start, end, pathType) {
        let squareIndexesToCheck;
        if (pathType == 'diagonal') {
            squareIndexesToCheck = getDiagonalSquares(start, end);
        } else if (pathType == 'line') {
            squareIndexesToCheck = getLineSquares(start, end);
        }

        if (squareIndexesToCheck == null) {
            return false;
        } else {
            for (let i = 0; i < squareIndexesToCheck.length; i++)  {
                if (board[indexToSquare(squareIndexesToCheck[i])] != null) return false;
            }
            return true;
        }
    }

    function getLineSquares(start, end) {
        const startRow = Math.floor(start / 8);
        const startCol = start % 8;
        const endRow = Math.floor(end / 8);
        const endCol = end % 8;
        
        if (startRow !== endRow && startCol !== endCol) {
          return null;
        }
        
        let squares = [];
        
        if (startRow === endRow) {
          let colStep = startCol < endCol ? 1 : -1;
          let currentCol = startCol + colStep;
          
          while (currentCol !== endCol) {
            squares.push(startRow * 8 + currentCol);
            currentCol += colStep;
          }
        } else {
          let rowStep = startRow < endRow ? 1 : -1;
          let currentRow = startRow + rowStep;
          
          while (currentRow !== endRow) {
            squares.push(currentRow * 8 + startCol);
            currentRow += rowStep;
          }
        }
        
        return squares;
      }
      

    function getDiagonalSquares(start, end) {
        const startRow = Math.floor(start / 8);
        const startCol = start % 8;
        const endRow = Math.floor(end / 8);
        const endCol = end % 8;
        
        if (Math.abs(startRow - endRow) !== Math.abs(startCol - endCol)) {
          return null;
        }
        
        const rowDiff = endRow - startRow;
        const colDiff = endCol - startCol;
        
        const rowStep = rowDiff > 0 ? 1 : -1;
        const colStep = colDiff > 0 ? 1 : -1;
        
        let currentRow = startRow + rowStep;
        let currentCol = startCol + colStep;
        let squares = [];
        
        while (currentRow !== endRow) {
          squares.push(currentRow * 8 + currentCol);
          
          currentRow += rowStep;
          currentCol += colStep;
        }
        
        return squares;
    }

    function Square(props) {
        return <div 
                className={`${props.squareColor} ${props.selected ? 'selected-square' : ''}`} 
                onClick={() => handleSquareClick(props.squareIndex)}>
                <Piece pieceInfo={props.pieceInfo}/>
            </div>
    }
    
    function Piece(props) {
        let piece = null;
        const pieceColor = String(props.pieceInfo).split(':')[0];
        const pieceType = String(props.pieceInfo).split(':')[1];
        switch(pieceType) {
            case 'Rook':
                piece = <Rook color={pieceColor} />
                break;
            case 'Knight':
                piece = <Knight color={pieceColor} />
                break;
            case 'Bishop':
                piece = <Bishop color={pieceColor} />
                break;
            case 'Queen':
                piece = <Queen color={pieceColor} />
                break;
            case 'King':
                piece = <King color={pieceColor} />
                break;
            case 'Pawn':
                piece = <Pawn color={pieceColor} />
                break;
        }
    
        return <>{piece}</>
    }
}
