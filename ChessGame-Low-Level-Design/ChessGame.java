/*

The low-level design for the chess game is still in progress and is not yet completed.

*/



enum PieceType {
    KING,
    QUEEN,
    PAWN,
    KNIGHT,
    BISHOP,
    ROOK
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

    public abstract boolean isPossibleMove(Position newPosition, Board board);
}

class Pawn extends Piece {

    public Pawn(Position position, boolean isAlive, Color color) {
        super(position, isAlive, color);
        this.pieceType = PieceType.PAWN;
    }

    public boolean isPossibleMove(Position newPosition, Board board) {
        if (color == Color.WHITE) {
            return isPossibleWhiteMove(newPosition, board);
        } else {
            return isPossibleBlackMove(newPosition, board);
        }
    }

    private boolean isPossibleWhiteMove(Position newPosition, Board board) {
        if (!isWithinBoardBounds(newPosition, board)) return false;

        Cell newCell = board.getBoardCell(newPosition.row, newPosition.column);

        if (isForwardMove(newPosition, 1) && newCell.isEmpty()) {
            return true;
        }
        if (isCaptureMove(newPosition, 1, newCell, Color.BLACK)) {
            return true;
        }

        return false;
    }

    private boolean isPossibleBlackMove(Position newPosition, Board board) {
        if (!isWithinBoardBounds(newPosition, board)) return false;

        Cell newCell = board.getBoardCell(newPosition.row, newPosition.column);

        if (isForwardMove(newPosition, -1) && newCell.isEmpty()) {
            return true;
        }
        if (isCaptureMove(newPosition, -1, newCell, Color.WHITE)) {
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

    private boolean isWithinBoardBounds(Position newPosition, Board board) {
        int boardSize = board.getBoardSize();
        return newPosition.row >= 0 && newPosition.row < boardSize &&
               newPosition.column >= 0 && newPosition.column < boardSize;
    }
}

class Rook extends Piece {

    public Rook(Position position, boolean isAlive, Color color) {
        super(position, isAlive, color);
        this.pieceType = PieceType.ROOK;
    }

    // rest of the logic will be added here
}

class Queen extends Piece {

    public Queen(Position position, boolean isAlive, Color color) {
        super(position, isAlive, color);
        this.pieceType = PieceType.QUEEN;
    }

    // rest of the logic will be added here
}

class King extends Piece {

    public King(Position position, boolean isAlive, Color color) {
        super(position, isAlive, color);
        this.pieceType = PieceType.KING;
    }

    // rest of the logic will be added here
}

class Bishop extends Piece {

    public Bishop(Position position, boolean isAlive, Color color) {
        super(position, isAlive, color);
        this.pieceType = PieceType.BISHOP;
    }

    // rest of the logic will be added here
}

class Knight extends Piece {

    public Knight(Position position, boolean isAlive, Color color) {
        super(position, isAlive, color);
        this.pieceType = PieceType.KNIGHT;
    }

    // rest of the logic will be added here
}


public class Main {
	public static void main(String[] args) {
        
		System.out.println("The low-level design for the chess game is still in progress and is not yet completed.");
	}
}
