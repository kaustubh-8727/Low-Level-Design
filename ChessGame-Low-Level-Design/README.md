# Chess Game Low-Level Design

This repository contains the low-level design of a chess game implemented in Java. The design models a chess game with a focus on object-oriented principles, where each piece has its own movement logic, and the game board manages the state of the game.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Classes and Structure](#classes-and-structure)
- [Game Instructions](#game-instructions)
- [How to Run](#how-to-run)
- [Future Enhancements](#future-enhancements)

## Overview

This chess game implementation provides a basic framework to simulate a standard chess game. It covers essential features such as board setup, piece movement, and player turns. The code is structured to separate concerns between different classes, making it easy to extend and modify.

## Features

- **Chess Board Setup:** The game initializes an 8x8 chessboard with all the pieces placed in their starting positions.
- **Piece Movement:** Each piece type (Pawn, Rook, Knight, Bishop, Queen, King) has its own movement logic. For example, Pawns can move forward or capture diagonally.
- **Move Validation:** The game validates player moves based on piece rules and ensures that moves are within the boundaries of the board.
- **Player Turns:** The game alternates turns between two players and validates their inputs.
- **Game Termination:** Players can end the game at any point, and the winner is declared based on the game state.

## Classes and Structure

### 1. `Piece`
- Abstract base class representing a generic chess piece.
- Derived classes: `Pawn`, `Rook`, `Knight`, `Bishop`, `Queen`, `King`.
- Each derived class implements the `isPossibleMove()` method to define valid moves.

### 2. `Position`
- Represents the position of a piece on the chessboard with `row` and `column` attributes.

### 3. `Cell`
- Represents a single cell on the chessboard.
- Manages the piece occupying the cell and provides methods to set or remove a piece.

### 4. `ChessBoard`
- Manages the chessboard as a 2D array of `Cell` objects.
- Responsible for initializing the board, placing pieces, and handling piece movements.

### 5. `Player`
- Represents a player in the game with attributes like `playerName`, `playerRank`, and `playerColor`.

### 6. `PlayChessGame`
- Coordinates the game flow, including player turns, move validation, and checking for game termination.

### 7. `ChessGame`
- Main class to start and run the chess game.

## Game Instructions

1. **Move Format:** To make a move, enter the starting position and the target position in the format `x1,y1 x2,y2`. For example, `1,0 2,0` moves the piece from row 1, column 0 to row 2, column 0.
2. **End Game:** If you want to end the game, type `"end game"`.
3. **Chess Board Orientation:**
   - White pieces are at the top of the board, represented by lowercase letters.
   - Black pieces are at the bottom of the board, represented by uppercase letters.

## Future Enhancements

- `Add More Piece Movement Logic:` Implement detailed movement rules for all pieces (e.g., castling for the King, en passant for Pawns).
- `Game Status Checks:` Implement checks for check, checkmate, and stalemate.
- `Graphical User Interface (GUI):` Develop a graphical interface to enhance user experience.
- `AI Opponent:` Implement a basic AI to play against the computer.

