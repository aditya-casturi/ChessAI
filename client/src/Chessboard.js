import Piece from './Piece';
import React, { useRef, useState } from 'react';

const Square = ({ file, rank, i, handleSquareClick, squares }) => {
  const squarePressed = useRef();

  return (
    <div ref={squarePressed} onClick={() => handleSquareClick(squarePressed)} className={`chess-square ${Math.floor(i / 8) % 2 === 0 ? (i % 2 === 0 ? 'black-square' : 'white-square') : (i % 2 === 0 ? 'white-square' : 'black-square')}`} data-square={file + rank}>
        <Piece file={file} rank={rank} mode={'setup'}/>
    </div>
  );
};

export default function Chessboard() {
    const [squares, setSquares]  = useState([]);

    function handleSquareClick (squarePressed) {
        const newSquares = [...squares];
        const index = newSquares.indexOf(squarePressed);
    
        if (index === -1) {
          newSquares.forEach((s) => {
            s.current.style.backgroundColor = '';
          });
          squarePressed.current.style.backgroundColor = '#706256';

          if (newSquares.length != 0) {
            newSquares[newSquares.indexOf(squarePressed)] = squarePressed;
            newSquares[squares.length - 1].current.innerHTML = '';
          }
          setSquares([squarePressed])
        } else {
          squarePressed.current.style.backgroundColor = '';
          newSquares.splice(index, 1);

          setSquares(newSquares);
        }


    }

    return (
        <div className="container">
        {[...Array(64)].map((e, i) => {
            const file = String.fromCharCode(97 + i % 8).toUpperCase();
            const rank = 8 - Math.floor(i / 8);

            return (
                <Square file={file} rank={rank} squares={squares} i={i} handleSquareClick={handleSquareClick} key={`${file}${rank}`} />
            );
        })}
        </div>
    );
}