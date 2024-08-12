import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

class Dice {
    
    int numberOfDice;
    int maxNum = 6;
    int minNum = 1;
    
    public Dice(int numberOfDice) {
        this.numberOfDice = numberOfDice;
    }
    
    public int rollDice() {
        int count = 1;
        int totalSum = 0;
        
        while(count <= numberOfDice) {
            int randomNumber = (int)(Math.random() * maxNum) + minNum;
            totalSum += randomNumber;
            count++;
        }
        
        return totalSum;
    }
}

class Jump {
    int start;
    int end;
    
    public Jump(int start, int end) {
        this.start = start;
        this.end = end;
    }
}

class Cell {
    Jump jump;
    
    public Cell() {
        
    }
    
    public Cell(Jump jump) {
        this.jump = jump;
    }
    
    public Jump getJump() {
        return jump;
    }
}

class Player {
    String name;
    int age;
    int currentPosition;
    
    public Player(String name, int age, int currentPosition) {
        this.name = name;
        this.age = age;
        this.currentPosition = currentPosition;
    }
    
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
    
    public String getName() {
        return name;
    }
    
    public int getCurrentPosition() {
        return currentPosition;
    }
}

class Board {
    
    int boardSize;
    Cell [][] board;
    
    public Board(int boardSize, int numberOfSnakes, int numberOfLadders) {
        
        this.boardSize = boardSize;
        board = new Cell[boardSize][boardSize];
        
        boardIntializeSetup();
        setSnakes(numberOfSnakes);
        setLadders(numberOfLadders);
    }
    
    public void boardIntializeSetup() {
        for(int row = 0 ; row < boardSize ; row++) {
            for(int column = 0 ; column < boardSize ; column++) {
                board[row][column] = new Cell();
            }
        }
    }
    
    private void setSnakes(int numberOfSnakes) {
        while(numberOfSnakes > 0) {
           int snakeHead = ThreadLocalRandom.current().nextInt(1, boardSize * boardSize - 1);
           int snakeTail = ThreadLocalRandom.current().nextInt(1, boardSize * boardSize - 1);
           if(snakeTail >= snakeHead) {
               continue;
           }

           Jump snakeObj = new Jump(snakeHead, snakeTail);

           Cell cell = getCell(snakeHead);
           cell.jump = snakeObj;

            numberOfSnakes--;
        }

    }
    
    private void setLadders(int numberOfLadders) {
        while(numberOfLadders > 0) {
           int ladderHead = ThreadLocalRandom.current().nextInt(1, boardSize * boardSize - 1);
           int ladderTail = ThreadLocalRandom.current().nextInt(1, boardSize * boardSize - 1);
           if(ladderTail <= ladderHead) {
               continue;
           }

           Jump ladderObj = new Jump(ladderHead, ladderTail);

           Cell cell = getCell(ladderHead);
           cell.jump = ladderObj;

            numberOfLadders--;
        }

    }
    
    public int move(int currentPosition, int diceCount) {
        currentPosition = currentPosition + diceCount;
        
        if(currentPosition >= boardSize * boardSize) {
            return currentPosition;
        }
        
        Cell cell = getCell(currentPosition);
        Jump jump = cell.getJump();
        
        if(jump != null) {
            currentPosition = jump.end;
            String SnakeOrLadder = jump.end > jump.start ? "Ladder" : "Snake";
            System.out.println(SnakeOrLadder + " found");
        }
        
        return currentPosition;
    }
    
    private Cell getCell(int playerPosition) {
        int boardRow = playerPosition / boardSize;
        int boardColumn = (playerPosition % boardSize);
        return board[boardRow][boardColumn];
    }
    
    public int getSize() {
        return boardSize;
    }
}

class Game {
    
    Board board;
    Dice dice;
    Deque<Player> playersList = new LinkedList<>();
    Player winner;

    public Game(int diceCount, int boardSize, int numberOfLadders, int numberOfSnakes, List<Player> playerList) {
        gameIntialSetup(diceCount, boardSize, numberOfSnakes, numberOfLadders, playerList);
    }
    
    public void gameIntialSetup(int diceCount, int boardSize, int numberOfSnakes, int numberOfLadders, List<Player> playerList) {
        dice = new Dice(diceCount);
        board = new Board(boardSize, numberOfSnakes, numberOfLadders);
        winner = null;
        
        for(Player player : playerList) {
            playersList.add(player);
        }
    }
    
    public void startGame() {
        while(winner == null) {
            
            Player currentPlayer = playersList.removeFirst();
            playersList.addLast(currentPlayer);
            
            int diceNumber = dice.rollDice();
            
            int playerNewPosition = getNewPosition(currentPlayer.currentPosition, diceNumber);
            currentPlayer.setCurrentPosition(playerNewPosition);
            
            System.out.println("current player : " + currentPlayer.getName());
            System.out.println("player position : " + currentPlayer.getCurrentPosition() + "\n");
            
            boolean isWinner = isGameWinner(playerNewPosition);
            
            if(isWinner) {
                winner = currentPlayer;
            }
        }
        
        System.out.println("winner of snake ladder game is " + winner.getName());
    }
    
    public int getNewPosition(int currentPosition, int diceNumber) {
        return board.move(currentPosition, diceNumber);
    }
    
    public boolean isGameWinner(int position) {
        if(position >= board.getSize() * board.getSize()) {
            return true;
        }
        return false;
    }
}

public class SnakeAndLadderGame {
	public static void main(String[] args) {
		System.out.println("welcome to snakes and ladders game\n");
		
		// game intial setup
		int numberOfDice = 5;
		int numberOfSnakes = 5;
		int numberOfLadders = 5;
		int boardSize = 10;
		
		//create players
		Player player1 = new Player("mike", 21, 0);
		Player player2 = new Player("willy", 24, 0);
		List<Player> playersList = new ArrayList<>();
		playersList.add(player1);
		playersList.add(player2);
		
		Game snakeLadder = new Game(numberOfDice, boardSize, numberOfLadders, numberOfSnakes, playersList);
		
		snakeLadder.startGame();
		
	}
}
