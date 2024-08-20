# Chess Game Low-Level Design

## Overview

This repository contains the low-level design of a Chess game, implemented in Java. The design includes various components necessary to simulate a standard Chess game, including classes for different pieces, the game board, players, and game logic. This design emphasizes object-oriented principles and design patterns to ensure modularity, scalability, and maintainability.

## Features

- **Object-Oriented Design**: Utilizes object-oriented principles to model the Chess game, with clear abstractions for pieces, moves, and game state.
- **Game Board**: A class to represent the chessboard with an 8x8 grid, managing the positions of the pieces.
- **Chess Pieces**: Separate classes for each type of piece (Pawn, Rook, Knight, Bishop, Queen, King), encapsulating their movement logic.
- **Game Logic**: Classes to handle the rules of Chess, including check, checkmate, and special moves like castling and en passant.
- **Players**: Models players with their respective pieces and the ability to make moves.
- **Move Validation**: Ensures that moves adhere to the rules of Chess, including legal and illegal moves.
- **Turn Management**: Handles alternating turns between the two players.

## Design Patterns Used

- **Factory Pattern**: Used for creating different chess pieces based on the type and color.
- **Strategy Pattern**: Implements the movement behavior of each piece, allowing flexibility in handling piece-specific rules.
- **Observer Pattern**: Notifies components of changes in the game state, such as when a move is made or when checkmate occurs.
- **Command Pattern**: Encapsulates a move as an object, allowing undo/redo functionality if implemented in the future.

## Classes

- **Game**: Manages the flow of the game, including starting, stopping, and tracking the state.
- **Board**: Represents the chessboard and manages piece positions.
- **Piece**: Abstract base class for all chess pieces.
- **Specific Piece Classes**: (Pawn, Rook, Knight, Bishop, Queen, King) - Handle the unique movement logic for each piece.
- **Player**: Represents a player in the game, managing their pieces and moves.
- **Move**: Represents a move on the board, including validation logic.
- **GameState**: Tracks the current state of the game, including whose turn it is, and whether the game is in check or checkmate.
