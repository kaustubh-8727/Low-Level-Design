import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Deque;
import java.util.ArrayList;

enum Piece {
    X,
    O
}

class playerPiece {
    
    Piece piece;
    
    public playerPiece(Piece piece) {
        this.piece = piece;
    }
}

class playerPieceX extends playerPiece {
    
    public playerPieceX() {
        super(Piece.X);
    }
}

class playerPieceO extends playerPiece {
    
    public playerPieceO() {
        super(Piece.O);
    }
}

class player {
    String playerName;
    playerPiece playerPieceType;
    
    public player(String playerName, playerPiece playerPieceType) {
        this.playerName = playerName;
        this.playerPieceType = playerPieceType;
    }
}

class Board {
    int size;
    Piece [][]board;
    
    public Board(int size) {
        this.size = size;
        board = new Piece[size][size];
    }
    
    public boolean isSpaceLeft() {
        int freeSpaceAvailable = 0;
        for(int row = 0 ; row < size ; row++) {
            for(int col = 0 ; col < size ; col++) {
                if(board[row][col] == null) freeSpaceAvailable++;
            }
        }
        if(freeSpaceAvailable > 0) {
            return true;
        }
        return false;
    }
    
    public boolean addPiece(int row, int col, Piece playingPiece) {
        if(board[row][col] == null) {
            board[row][col] = playingPiece;
            return true;
        }
        return false;
    }
    
    public boolean validateMove(int row, int column) {
        if(row >= size || row < 0 || column >= size || column < 0 || board[row][column] != null) {
            return false;
        }
        return true;
    }
    
    public void showBoard() {
        System.out.print("\n");
        for(int row = 0 ; row < size ; row++) {
            for(int column = 0 ; column < size ; column++) {
                
                String piece;
                
                if(board[row][column] == null) {
                    piece = "--";
                } else {
                    piece = board[row][column] == Piece.X ? "X" : "O";
                }
                System.out.print(piece + "  ");
            }
            System.out.print("\n");
        }
    }
    
    public boolean isWinner(int row, int column, Piece piece) {
        boolean rowMatch = true;
        boolean columnMatch = true;
        boolean diagonalMatch = true;
        boolean antiDiagonalMatch = true;

        //need to check in row
        for(int index = 0 ; index < size ; index++) {

            if(board[row][index] == null || board[row][index] != piece) {
                rowMatch = false;
                break;
            }
        }

        //need to check in column
        for(int index = 0 ; index < size ; index++) {

            if(board[index][column] == null || board[index][column] != piece) {
                columnMatch = false;
                break;
            }
        }

        //need to check diagonals
        for(int i=0, j=0; i<size;i++,j++) {
            if (board[i][j] == null || board[i][j] != piece) {
                diagonalMatch = false;
                break;
            }
        }

        //need to check anti-diagonals
        for(int i=0, j=size-1; i<size;i++,j--) {
            if (board[i][j] == null || board[i][j] != piece) {
                antiDiagonalMatch = false;
                break;
            }
        }

        return rowMatch || columnMatch || diagonalMatch || antiDiagonalMatch;
    }
    
}

class PlayTicTacToeGame {
    
    Deque<player> players = new LinkedList<>();
    Board board;
    
    public PlayTicTacToeGame(int boardSize, List<player> playersList) {
        preMatchSetup(boardSize, playersList);
    }
    
    public void preMatchSetup(int boardSize, List<player> playersList) {
        
        for(player currentPlayer : playersList) {
            players.add(currentPlayer);
        }

        board = new Board(boardSize);
    }
    
    public void playGame() {
        
        boolean isGameRunning = true;
        
        while(isGameRunning) {
            player playerTurn = players.removeFirst();
            
            if(!board.isSpaceLeft()) {
                isGameRunning = false;
                continue;
            }
            
            board.showBoard();
            
            // read player value
            String currentPlayerPiece = playerTurn.playerPieceType.piece == Piece.X ? "X" : "O";
            System.out.println("Player : " + playerTurn.playerName + " (" + currentPlayerPiece + ")" + " enter the row and column\n");
            Scanner inputScanner = new Scanner(System.in);
            String s = inputScanner.nextLine();
            String[] values = s.split(",");
            int inputRow = Integer.valueOf(values[0]);
            int inputColumn = Integer.valueOf(values[1]);
            
            //check if the move is valid
            boolean isValidMove = board.validateMove(inputRow, inputColumn);
            if(!isValidMove) {
                //player can not insert the piece into this cell, player has to choose another cell
                System.out.println("Incorret position chosen, try again");
                players.addFirst(playerTurn);
                continue;
            }
            
            // add the piece
            boolean pieceAddedSuccessfully = board.addPiece(inputRow, inputColumn, playerTurn.playerPieceType.piece);
            
            players.addLast(playerTurn);
    
            boolean winner = board.isWinner(inputRow, inputColumn, playerTurn.playerPieceType.piece);
            if(winner) {
                System.out.println("winner is " + playerTurn.playerName);
                isGameRunning = false;
                return;
            }
        }
        System.out.println("Match tie");
    }
}

public class TicTacToeGame {
	public static void main(String[] args) {
	    
	    // create players
	    player player1 = new player("jimmy", new playerPieceO());
	    player player2 = new player("jack", new playerPieceX());
	    
	    List<player> playerList = new ArrayList<>();
	    playerList.add(player1);
	    playerList.add(player2);
	    
	    // intialize boardSize
	    int boardSize = 3;
	    
	    System.out.println("start the TicTacToe game");
	    System.out.println("enter the row and column if A,B format only");
	    
		PlayTicTacToeGame playTicTacToeGame = new PlayTicTacToeGame(boardSize, playerList);
		playTicTacToeGame.playGame();
	}
}
