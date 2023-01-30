import React, { useState, useEffect } from 'react'
import { useDrag, useDrop } from 'react-dnd';
import Chessboard from './Chessboard';
import './App.css'

function App() {
  return (
    <Chessboard />
  )
}

export default App