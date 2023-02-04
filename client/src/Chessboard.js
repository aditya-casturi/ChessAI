import { React, useState } from 'react'
import './Chessboard.css'
import { Knight, Bishop, Rook, King, Queen, Pawn } from './PieceIcons'

const board = {};
const files = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'];
const pieces = ['rook', 'knight', 'bishop', 'queen', 'king', 'bishop', 'knight', 'rook'];
const pawns = ['pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn'];

for (let rank = 1; rank < 9; rank++) {
    for (let file = 0; file < 8; file++) {
        board[files[file] + rank] = (rank < 2 || rank > 7) ? pieces[file] : (rank === 2 || rank === 7) ? 'pawn' : null;
    }
}

export default function Chessboard() {
    const [squares, setSquares] = useState([]);
    const [selectedSquare, setSelectedSquare] = useState(-1);

    return (
        <div className="container">
            {[...Array(64)].map((e, i) => {
                const file = String.fromCharCode(97 + i % 8).toUpperCase();
                const rank = 8 - Math.floor(i / 8);
                const piece = board[file + rank]
                return (
                    <Square index={i} mode={'setup'} file={file} rank={rank} onClick={() => handleSquareClick(i, file, rank, piece)}
                        selected={selectedSquare === i} key={file + rank} piece={piece} />
                );
            })}
        </div>
    );

    function handleSquareClick(squarePressed, file, rank, piece) {
        setSelectedSquare(squarePressed);
    }
}

export function Square(props) {
    let squareColor =
        `{chess-square ${Math.floor(props.index / 8) % 2 === 0 ?
            (props.index % 2 === 0 ?
                'black-square' : 'white-square') : (props.index % 2 === 0 ?
                    'white-square' : 'black-square')}`

    if (props.mode == 'setup') {
        return <div className={`${squareColor} ${props.selected ? 'selected-square' : ''}`} onClick={props.onClick} data-square={props.file + props.rank}>
            <Piece rank={props.rank} file={props.file} squareColor={squareColor} mode={'setup'} />
        </div>
    }
}

export function Piece(props) {
    let piece = null
    if (props.mode == 'setup') {
        let color = props.rank == 8 ? "black" : "white"

        if (props.rank == 8 || props.rank == 1) {
            switch (props.file) {
                case 'A': case 'H':
                    piece = <Rook color={color} />;
                    break;
                case 'B': case 'G':
                    piece = <Knight color={color} />;
                    break;
                case 'C': case 'F':
                    piece = <Bishop color={color} />;
                    break;
                case 'D':
                    piece = <Queen color={color} />;
                    break;
                case 'E':
                    piece = <King color={color} />;
                    break;
            }
        } else if (props.rank == 7 || props.rank == 2) {
            color = props.rank == 7 ? "black" : "white";
            piece = <Pawn color={color} />;
        }
    }

    return (<div>{piece}</div>);

}
