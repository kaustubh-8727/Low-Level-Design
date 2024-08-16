# Tic-Tac-Toe Game

This is a simple Tic-Tac-Toe game implemented in Java. The game allows two players to play against each other on a customizable board size. The players take turns marking the cells in a grid, and the player who succeeds in placing three of their marks in a horizontal, vertical, or diagonal row wins the game.

## How It Works

### Classes Overview

1. **Piece**: An enum representing the two types of pieces (X and O) that players can use.

2. **playerPiece**: An abstract class representing the piece a player uses. It has two derived classes:
   - `playerPieceX`: Represents the X piece.
   - `playerPieceO`: Represents the O piece.

3. **player**: A class representing a player in the game. Each player has a name and a piece (`playerPiece`).

4. **Board**: This class represents the game board. It includes methods for checking if there's space left, adding a piece to the board, validating a move, displaying the board, and checking for a winner.

5. **PlayTicTacToeGame**: This class handles the game flow. It sets up the board and players, manages the turns, and checks for a winner or a tie.

6. **TicTacToeGame**: The main class to start the game.

### Game Flow

1. Players are initialized with their names and pieces (X or O).
2. The board is created with a specified size.
3. The game starts, and players take turns entering the row and column where they want to place their piece.
4. After each move, the board is checked for a winning condition or if there are no spaces left (resulting in a tie).
5. The game ends when a player wins or the board is full.

### Input Format

- Players enter the row and column as a comma-separated string (e.g., `1,2`).

## How to Run

1. Ensure you have Java installed on your system.
2. Compile the `TicTacToeGame` class.
   ```sh
   javac TicTacToeGame.java
   java TicTacToeGame
