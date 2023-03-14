import React from 'react'
import Chessboard from './Chessboard'
import './App.css'

export default function App() {
  return (
    <div>
      <Chessboard />
      <html>
        <head></head>
        <body>
          <div class="topnav">
            <a href="home">Home</a>
            <a class="active" href="play">Play</a>
            <a href="contact">Contact</a>
            <a href="about">About</a>
          </div>
          <div class="topnav1">
            <a href="logout">Logout</a>
          </div>
        </body>
      </html>
    </div>  
  )
}
