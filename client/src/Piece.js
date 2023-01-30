import React, { useState } from 'react'
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { library } from "@fortawesome/fontawesome-svg-core";
import { faChessKnight, faChessBishop, faChessPawn, faChessRook, faChessKing, faChessQueen } from "@fortawesome/free-solid-svg-icons";
import './Piece.css';

library.add(faChessKnight, faChessBishop, faChessPawn, faChessRook, faChessKing, faChessQueen);

export default function Piece(props) {
    if (props.mode == 'setup') {
        let color = props.rank == 8 ? "black" : "white"
        if (props.rank == 8 || props.rank == 1) {
            if (props.file == 'A' || props.file == 'H')
                return <FontAwesomeIcon icon="fa-solid fa-chess-rook" size='2x' color={color} />
            else if (props.file == 'B' || props.file == 'G')
                return <FontAwesomeIcon icon="fa-solid fa-chess-knight" size='2x' color={color} />
            else if (props.file == 'C' || props.file == 'F')
                return <FontAwesomeIcon icon="fa-solid fa-chess-bishop" size='2x' color={color} />
            else if (props.file == 'D')
                return <FontAwesomeIcon icon="fa-solid fa-chess-queen" size='2x' color={color} />
            else if (props.file == 'E')
                return <FontAwesomeIcon icon="fa-solid fa-chess-king" size='2x' color={color} />
        }
        else if (props.rank == 7 || props.rank == 2) {
            color = props.rank == 7 ? "black" : "white"
            return <FontAwesomeIcon icon="fa-solid fa-chess-pawn" size='2x' color={color} />
        }
    } else {
        return <FontAwesomeIcon icon="fa-solid fa-chess-pawn" size='2x' color='black' /> 
    } 
}
