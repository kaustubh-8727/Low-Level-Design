# Snake and Ladder Game

Welcome to the Snake and Ladder Game! This is a classic board game implemented in Java, where players roll dice to navigate the board, encountering snakes and ladders along the way.

## Introduction

This Java-based implementation of Snake and Ladder features a simple and intuitive design. The game consists of a board, snakes, ladders, dice, and players. Players take turns to roll the dice and move forward on the board. If they land on a snake's head, they slide down to the tail; if they land at the bottom of a ladder, they climb up to the top. The first player to reach or exceed the final cell on the board wins the game.

## Game Rules

1. Players start at position 0 on the board.
2. Each player rolls the dice in their turn to determine how many cells they move forward.
3. If a player lands on a cell with the head of a snake, they slide down to the tail of the snake.
4. If a player lands on a cell with the bottom of a ladder, they climb up to the top of the ladder.
5. The game continues until a player reaches or exceeds the last cell on the board.
6. The first player to do so is declared the winner.

## Classes and Design

### `Dice`
- Handles the rolling of dice.
- `rollDice()` method returns the sum of the rolled values.

### `Jump`
- Represents a snake or a ladder.
- `start` is the starting position (head of snake/bottom of ladder).
- `end` is the ending position (tail of snake/top of ladder).

### `Cell`
- Represents a cell on the board.
- Contains an optional `Jump` object (snake or ladder).

### `Player`
- Represents a player in the game.
- Tracks the player's name, age, and current position on the board.

### `Board`
- Represents the game board.
- Manages the cells, snakes, and ladders.
- Provides methods for setting up snakes, ladders, and moving players.

### `Game`
- Controls the game flow.
- Initializes the board, players, and dice.
- Handles the game loop, checking for winners, and player turns.

## How to Run

1. Ensure you have a Java development environment set up.
2. Copy the provided code into a Java file (e.g., `SnakeAndLadderGame.java`).
3. Compile the Java file:
    ```bash
    javac SnakeAndLadderGame.java
    ```
4. Run the compiled Java program:
    ```bash
    java SnakeAndLadderGame
    ```

## Example Output

```plaintext
welcome to snakes and ladders game

current player : mike
player position : 17

current player : willy
player position : 17

current player : mike
player position : 31

current player : willy
player position : 34

current player : mike
player position : 44

current player : willy
player position : 53

Ladder found
current player : mike
player position : 66

Snake found
current player : willy
player position : 20

current player : mike
player position : 82

current player : willy
player position : 42

current player : mike
player position : 94

current player : willy
player position : 59

current player : mike
player position : 115

winner of snake ladder game is mike
