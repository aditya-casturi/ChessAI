import { React, useState } from 'react'
import './Square.css'

export default function Square(props) {
    const [pieceOver, setPieceOver] = useState(false)

    const handleDragStart = (event, props) => {
        event.dataTransfer.setData("props", JSON.stringify(props));
    };

    const handleDragOver = (event) => {
        event.preventDefault();
        setPieceOver(true);
      };

    const handleDrop = (event) => {
        event.preventDefault();
        let data = JSON.parse(event.dataTransfer.getData("props"));
        props.handlePieceDrop(event, data)
      };

    return (
        <div draggable={false} className={`
            ${props.squareColor} 
            ${props.selected ? 'selected-square' : ''}
        `} onClick={() => props.handleSquareClick(props.squareIndex)}>
            <div 
                draggable 
                id={props.piece + props.squareIndex}
                onMouseDown={() => props.handleSquareClick(props.squareIndex)}
                onDragStart={(event) => handleDragStart(event, props)}
                onDragOver={handleDragOver}
                onDrop={handleDrop}
                style={{zIndex: '1', position: 'relative'}} className={`piece ${props.piece}`}>

                <div id={props.squareIndex} className='dot' style={{visibility: props.legalSquares.includes(props.squareIndex) ? 'visible' : 'hidden' }}>●</div>
            </div>
        </div>
    );
}

