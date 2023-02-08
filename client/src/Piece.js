import React from 'react'
import { Knight, Bishop, Rook, King, Queen, Pawn } from './PieceIcons'
import { useDrag } from 'react-dnd'

export default function Piece(props) {
    const [{ isDragging }, dragRef] = useDrag({
        type: 'piece',
        item: { props },
        collect: (monitor) => ({
            isDragging: monitor.isDragging()
        })
    })

    let piece = null;
    const pieceColor = String(props.pieceInfo).split(':')[0];
    const pieceType = String(props.pieceInfo).split(':')[1];
    switch (pieceType) {
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

    return <div ref={dragRef}>{piece}{isDragging}</div>
}
