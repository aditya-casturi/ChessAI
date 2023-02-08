import { React, useState } from 'react'
import Piece from './Piece'
import { useDrop } from 'react-dnd';

export default function Square(props) {
    const PIECE = []

    const [basket, setBasket] = useState([])
    const [{ isOver }, dropRef] = useDrop({
        accept: 'piece',
        drop: (item) => setBasket((basket) =>
            !basket.includes(item) ? [...basket, item] : basket),
        collect: (monitor) => ({
            isOver: monitor.isOver()
        })
    })

    return <div style={{ boxSizing: 'border-box', position: 'relative', backgroundColor: props.legalSquares.includes(props.squareIndex) && props.pieceInfo != null ? '#CB6569' : '', cursor: props.pieceInfo == null ? '' : 'grab'}}
        className={`${props.squareColor} ${props.selected ? 'selected-square' : ''}`}
        onClick={() => props.handleSquareClick(props.squareIndex)}>
        <div className='pets'>
            {PIECE.map(piece => <Piece draggable pieceInfo={piece.props.pieceInfo} />)}
        </div>
        <div className='basket' ref={dropRef}>
            {basket.map(piece => <Piece draggable pieceInfo={piece.props.pieceInfo} />)}
        </div>
        <div className='dot' style={{ visibility: props.legalSquares.includes(props.squareIndex) && props.pieceInfo == null ? 'visible' : 'hidden' }}>●</div>
        <Piece pieceInfo={props.pieceInfo} className='piece' />
    </div>
}
