import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Deque;
import java.util.ArrayList;

enum PieceType {
    KING,
    QUEEN,
    PAWN,
    KNIGHT,
    BISHOP,
    ROOK
}

enum Color {
    WHITE,
    BLACK
}

abstract class Piece {
    protected Position position;
    protected PieceType pieceType;
    protected boolean isAlive;
    protected Color color;
    
    public Piece(Position position, boolean isAlive, Color color) {
        this.position = position;
        this.isAlive = isAlive;
        this.color = color;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    public Color getColor() {
        return color;
    }

    public abstract boolean isPossibleMove(Position newPosition, ChessBoard board);
}

class Pawn extends Piece {

    public Pawn(Position position, boolean isAlive, Color color) {
        super(position, isAlive, color);
        this.pieceType = PieceType.PAWN;
    }

    public boolean isPossibleMove(Position newPosition, ChessBoard board) {
        if (color == Color.WHITE) {
            return isPossibleWhiteMove(newPosition, board);
        } else {
            return isPossibleBlackMove(newPosition, board);
        }
    }

    private boolean isPossibleWhiteMove(Position newPosition, ChessBoard board) {

        Cell newCell = board.getBoardCell(newPosition);

        if (isForwardMove(newPosition, -1)) {
            return true;
        }
        if (isCaptureMove(newPosition, -1, newCell, Color.BLACK)) {
            return true;
        }

        return false;
    }

    private boolean isPossibleBlackMove(Position newPosition, ChessBoard board) {

        Cell newCell = board.getBoardCell(newPosition);

        if (isForwardMove(newPosition, 1)) {
            return true;
        }
        if (isCaptureMove(newPosition, 1, newCell, Color.WHITE)) {
            return true;
        }

        return false;
    }

    private boolean isForwardMove(Position newPosition, int direction) {
        return newPosition.row == position.row + direction && newPosition.column == position.column;
    }

    private boolean isCaptureMove(Position newPosition, int direction, Cell newCell, Color opponentColor) {
        return newPosition.row == position.row + direction &&
               (newPosition.column == position.column - 1 || newPosition.column == position.column + 1) &&
               newCell.getPiece() != null &&
               newCell.getPiece().getColor() == opponentColor;
    }
}

class Rook extends Piece {

    public Rook(Position position, boolean isAlive, Color color) {
        super(position, isAlive, color);
        this.pieceType = PieceType.ROOK;
    }

    public boolean isPossibleMove(Position newPosition, ChessBoard board) {
        return true;
    }
}

class Queen extends Piece {

    public Queen(Position position, boolean isAlive, Color color) {
        super(position, isAlive, color);
        this.pieceType = PieceType.QUEEN;
    }

    public boolean isPossibleMove(Position newPosition, ChessBoard board) {
        return true;
    }
}

class King extends Piece {

    public King(Position position, boolean isAlive, Color color) {
        super(position, isAlive, color);
        this.pieceType = PieceType.KING;
    }

    public boolean isPossibleMove(Position newPosition, ChessBoard board) {
        return true;
    }
}

class Bishop extends Piece {

    public Bishop(Position position, boolean isAlive, Color color) {
        super(position, isAlive, color);
        this.pieceType = PieceType.BISHOP;
    }

    public boolean isPossibleMove(Position newPosition, ChessBoard board) {
        return true;
    }
}

class Knight extends Piece {

    public Knight(Position position, boolean isAlive, Color color) {
        super(position, isAlive, color);
        this.pieceType = PieceType.KNIGHT;
    }

    public boolean isPossibleMove(Position newPosition, ChessBoard board) {
        return true;
    }
}

class Position {
    int row;
    int column;
    
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }
}

class Cell {
    Position position;
    Piece piece;
    
    public Cell(Position position) {
        this.position = position;
        piece = null;
    }
    
    public void setPiece(Piece piece) {
        this.piece = piece;
    }
    
    public void removePiece() {
        piece = null;
    }
    
    public Piece getPiece() {
        return piece;
    }
    
    public boolean isEmpty() {
        return piece == null;
    }
}

class ChessBoard {
    
    Cell[][] board;
    int boardSize;
    
    public ChessBoard(int boardSize) {
        this.boardSize = boardSize;
        board = new Cell[boardSize][boardSize];
    }
    
    public void intializeChessBoard() {
        
        for(int row = 0 ; row < boardSize ; row++) {
            for(int column = 0 ; column < boardSize ; column++) {
                board[row][column] = new Cell(new Position(row, column));
            }
        }
        
        setupChessBoard();
    }
    
    public void setupChessBoard() {
        // Set up black pieces
        board[0][0].setPiece(new Rook(new Position(0, 0), true, Color.BLACK));
        board[0][1].setPiece(new Knight(new Position(0, 1), true, Color.BLACK));
        board[0][2].setPiece(new Bishop(new Position(0, 2), true, Color.BLACK));
        board[0][3].setPiece(new Queen(new Position(0, 3), true, Color.BLACK));
        board[0][4].setPiece(new King(new Position(0, 4), true, Color.BLACK));
        board[0][5].setPiece(new Bishop(new Position(0, 5), true, Color.BLACK));
        board[0][6].setPiece(new Knight(new Position(0, 6), true, Color.BLACK));
        board[0][7].setPiece(new Rook(new Position(0, 7), true, Color.BLACK));
    
        // Set up black pawns
        for (int i = 0; i < boardSize; i++) {
            board[1][i].setPiece(new Pawn(new Position(1, i), true, Color.BLACK));
        }
    
        // Set up white pieces
        board[7][0].setPiece(new Rook(new Position(7, 0), true, Color.WHITE));
        board[7][1].setPiece(new Knight(new Position(7, 1), true, Color.WHITE));
        board[7][2].setPiece(new Bishop(new Position(7, 2), true, Color.WHITE));
        board[7][3].setPiece(new Queen(new Position(7, 3), true, Color.WHITE));
        board[7][4].setPiece(new King(new Position(7, 4), true, Color.WHITE));
        board[7][5].setPiece(new Bishop(new Position(7, 5), true, Color.WHITE));
        board[7][6].setPiece(new Knight(new Position(7, 6), true, Color.WHITE));
        board[7][7].setPiece(new Rook(new Position(7, 7), true, Color.WHITE));
    
        // Set up white pawns
        for (int i = 0; i < boardSize; i++) {
            board[6][i].setPiece(new Pawn(new Position(6, i), true, Color.WHITE));
        }
    }

    
    public void move(Position currentPosition, Position nextPosition) {
        
        Cell currentCell = board[currentPosition.row][currentPosition.column];
        Cell nextCell = board[nextPosition.row][nextPosition.column];
        
        Piece piece = currentCell.getPiece();
        Piece nextCellPiece = nextCell.getPiece();
        
        nextCell.setPiece(piece);
        currentCell.removePiece();
        piece.setPosition(nextPosition);
        
    }

    public void printChessBoard() {
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                Piece piece = board[row][column].getPiece();
                if (piece == null) {
                    System.out.print("- ");
                } else {
                    switch (piece.pieceType) {
                        case KING:
                            System.out.print((piece.getColor() == Color.WHITE ? "K " : "k "));
                            break;
                        case QUEEN:
                            System.out.print((piece.getColor() == Color.WHITE ? "Q " : "q "));
                            break;
                        case ROOK:
                            System.out.print((piece.getColor() == Color.WHITE ? "R " : "r "));
                            break;
                        case BISHOP:
                            System.out.print((piece.getColor() == Color.WHITE ? "B " : "b "));
                            break;
                        case KNIGHT:
                            System.out.print((piece.getColor() == Color.WHITE ? "N " : "n "));
                            break;
                        case PAWN:
                            System.out.print((piece.getColor() == Color.WHITE ? "P " : "p "));
                            break;
                    }
                }
            }
            System.out.println();
        }
    }

    
    public Cell getBoardCell(Position position) {
        return board[position.row][position.column];
    }
    
    public int getBoardSize() {
        return boardSize;
    }
    
}

class Player {
    
    String playerName;
    int playerRank;
    Color playerColor;
    
    public Player(String playerName, int playerRank, Color playerColor) {
        this.playerName = playerName;
        this.playerRank = playerRank;
        this.playerColor = playerColor;
    }
    
    public Color getPlayerColor() {
        return playerColor;
    }
    
    public String getName() {
        return playerName;
    }
}

class PlayChessGame {
    
    ChessBoard chessBoard;
    Deque<Player> playersList = new LinkedList<>();
    Player winner;
    
    public PlayChessGame(int boardSize, List<Player> players) {
        intializeGame(boardSize, players);
    }
    
    public void intializeGame(int boardSize, List<Player> players) {
        chessBoard = new ChessBoard(boardSize);
        chessBoard.intializeChessBoard();
        
        for(Player player : players) {
            playersList.add(player);
        }
    }
    
    public boolean checkMoveValidation(Position currentPosition, Position nextPosition) {
        
        int boardSize = chessBoard.getBoardSize();
        boolean boardValidation = true;
        boolean cellValidation = true;
        boolean pieceValidation = true;
        
        if(currentPosition.row >= boardSize || currentPosition.column >= boardSize || nextPosition.row >= boardSize &&  nextPosition.column >= boardSize) {
                boardValidation = false;
        }
        
        Cell currentCell = chessBoard.getBoardCell(currentPosition);
        Cell nextCell = chessBoard.getBoardCell(nextPosition);
        
        if(currentCell == null) {
            cellValidation = false;
        }
        
        Piece piece = currentCell.getPiece();
        Piece nextCellPiece = nextCell.getPiece();
        
        if(piece == null || (nextCellPiece != null && nextCellPiece.color == piece.color)) {
            pieceValidation = false;
        }
        
        boolean moveValidation = piece.isPossibleMove(nextPosition, chessBoard);

        return boardValidation && cellValidation && pieceValidation && moveValidation;
    }
    
    public void startGame() {
        
        while(winner == null) {
            
            // get current player turn
            Player currentPlayer = playersList.removeFirst();
            
            // show board structure
            chessBoard.printChessBoard();
            
            System.out.println("current player : " + currentPlayer.getName());
            
            // read player value
            System.out.println("Player: " + currentPlayer.getName() + " enter your move\n");
            Scanner inputScanner = new Scanner(System.in);
            String moveInput = inputScanner.nextLine();
            
            if(moveInput.equals("end game")) {
                winner = playersList.removeFirst();
                continue;
            }
            
            // Split the input into two parts: "x1,y1" and "x2,y2"
            String[] positions = moveInput.split(" ");
            // Further split each part by the comma to get row and column values
            String[] fromPositionValues = positions[0].split(",");
            int fromRow = Integer.valueOf(fromPositionValues[0]);
            int fromColumn = Integer.valueOf(fromPositionValues[1]);
            Position currentPosition = new Position(fromRow, fromColumn);
            String[] toPositionValues = positions[1].split(",");
            int toRow = Integer.valueOf(toPositionValues[0]);
            int toColumn = Integer.valueOf(toPositionValues[1]);
            Position nextPosition = new Position(toRow, toColumn);
            
            // check if the player move is valid or not
            if(!checkMoveValidation(currentPosition, nextPosition)) {
                System.out.println("Player : " + currentPlayer.getName() + " invalid move try again\n");
                playersList.addFirst(currentPlayer);
                continue;
            }
            
            // make a move in chess
            chessBoard.move(currentPosition, nextPosition);
            
            // update player turn
            playersList.addLast(currentPlayer);
            
        }
        
        System.out.println("winner of the game is " + winner.getName());
        
    }
}

public class ChessGame {
	public static void main(String[] args) {
        
		System.out.println("low level design of chess game\n\n");
		System.out.println("game instructions\n");
		System.out.println("1. if you want to make a move enter from which position you want to move to which position");
		System.out.println("   enter the move in given format x1,y1 x2,y2");
		System.out.println("2. if lose the game enter 'end game'");
		System.out.println("3. in chess board white pieces is on top of board and black pieces are on bottom");
		System.out.println("4. white pieces are denoted in lowercase and black pieces are denoted in uppercase\n");
		
		// get the board size
		int boardSize = 8;
		
		// create players
		Player player1 = new Player("jimmy", 1, Color.WHITE);
		Player player2 = new Player("josh", 1, Color.BLACK);
		List<Player> players = new ArrayList<>();
		players.add(player1);
		players.add(player2);
		
		PlayChessGame playChessGame = new PlayChessGame(boardSize, players);
		
		playChessGame.startGame();
		
	}
}
